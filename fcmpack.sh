#!
@echo off
export JWT_CKEY=$(cat .\testkey.txt)
.\mvnw package