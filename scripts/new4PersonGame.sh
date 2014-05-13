#!/bin/bash 
cd $PWD/src/
javac network/GameClient.java
java network/GameClient $1 $2 $3 $4
$SHELL