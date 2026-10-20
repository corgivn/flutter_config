import com.android.build.api.variant.ApplicationAndroidComponentsExtension
import com.android.build.api.variant.BuildConfigField
import com.android.build.api.variant.ResValue
import java.io.File

val envLinePattern = Regex("""^\s*(?:export\s+)?([\w.\-]+)\s*=\s*['"]?(.*?)['"]?\s*$""")

fun resolveEnvFileName(variantName: String): String {
    System.getenv("ENVFILE")?.takeIf { it.isNotEmpty() }?.let { return it }
    System.getProperty("ENVFILE")?.takeIf { it.isNotEmpty() }?.let { return it }

    if (project.hasProperty("envConfigFiles")) {
        val configs = project.property("envConfigFiles") as Map<*, *>
        val name = variantName.lowercase()
        configs.entries
            .firstOrNull { name.startsWith(it.key.toString().lowercase()) }
            ?.let { return it.value.toString() }
    }

    if (project.hasProperty("defaultEnvFile")) {
        return project.property("defaultEnvFile").toString()
    }

    return ".env"
}

fun loadDotEnv(fileName: String): Map<String, String> {
    val file = File(rootDir, "../$fileName").takeIf { it.exists() } ?: File(fileName)
    if (!file.exists()) {
        logger.warn("*** Missing .env file: $fileName ***")
        return emptyMap()
    }

    logger.lifecycle("Reading env from: $fileName")
    return file.readLines()
        .mapNotNull { line ->
            envLinePattern.matchEntire(line)?.let { match ->
                match.groupValues[1] to match.groupValues[2].replace("\"", "\\\"")
            }
        }
        .toMap()
}

// Best-effort guess from the requested tasks (e.g. assembleDevelopDebug -> developdebug) so that
// `project.env` is readable while the build script is still being evaluated.
fun currentFlavorFromTasks(): String {
    val pattern = Regex("""(?:.*:)*[a-z]+([A-Z][A-Za-z0-9]+)""")
    return gradle.startParameter.taskNames
        .firstNotNullOfOrNull { pattern.find(it)?.groupValues?.get(1)?.lowercase() }
        ?: ""
}

extra["env"] = loadDotEnv(resolveEnvFileName(currentFlavorFromTasks()))

val androidComponents = extensions.getByType(ApplicationAndroidComponentsExtension::class.java)

androidComponents.finalizeDsl { android ->
    android.buildFeatures.buildConfig = true
    android.buildFeatures.resValues = true
}

androidComponents.onVariants { variant ->
    loadDotEnv(resolveEnvFileName(variant.name)).forEach { (key, value) ->
        variant.buildConfigFields?.put(
            key,
            BuildConfigField("String", "\"$value\"", null)
        )
        variant.resValues.put(
            variant.makeResValueKey("string", key),
            ResValue(value.replace("%", "\\u0025"), null)
        )
    }
}
