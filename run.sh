#!/bin/bash

# Setup vars
server_path="$HOME/Documents/Servers/Server"
plugin_name="BetterCommands"
shaded_jar="BetterCommands.jar"

# Init colors
red=`tput setaf 1`
green=`tput setaf 2`
yellow=`tput setaf 3`
cyan=`tput setaf 6`
reset=`tput sgr0`

# Building
echo "${cyan}Building $plugin_name with Maven..."
if mvn install
then
	echo "${green}Build success."
else
	echo "${red}[ERROR] Failed build this plugin :("
	exit 1
fi

# Deploying
echo "${cyan}Copying compiled jar into test server directory as $plugin_name.jar..."
if cp target/$shaded_jar $server_path/plugins/$plugin_name.jar 
then
	echo "${green}Successfully copied, reload plugin ;)"
else
	echo "${red}[ERROR] Failed copy compiled plugin jar into server plugins directory."
	exit 2
fi