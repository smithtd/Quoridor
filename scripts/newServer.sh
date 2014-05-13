#!/bin/bash 
cd $PWD/src/
javac network/MoveServer.java
java network/MoveServer $1
$SHELL