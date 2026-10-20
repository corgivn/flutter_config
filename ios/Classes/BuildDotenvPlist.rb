#!/usr/bin/env ruby
# frozen_string_literal: true

require 'fileutils'
require_relative 'ReadDotEnv'

envs_root = ARGV[0]
plist_output = ARGV[1]
puts "reading env file from #{envs_root} and writing .plist to #{plist_output}"

def xml_escape(value)
  value.gsub('&', '&amp;').gsub('<', '&lt;').gsub('>', '&gt;')
end

dotenv, _custom_env = read_dot_env(envs_root)

entries = dotenv.map do |key, value|
  "\t<key>#{xml_escape(key)}</key>\n\t<string>#{xml_escape(value.chomp)}</string>"
end

FileUtils.mkdir_p(File.dirname(plist_output))
File.write(plist_output, <<~PLIST)
  <?xml version="1.0" encoding="UTF-8"?>
  <!DOCTYPE plist PUBLIC "-//Apple//DTD PLIST 1.0//EN" "http://www.apple.com/DTDs/PropertyList-1.0.dtd">
  <plist version="1.0">
  <dict>
  #{entries.join("\n")}
  </dict>
  </plist>
PLIST

puts "Wrote #{dotenv.size} values to #{plist_output}"
