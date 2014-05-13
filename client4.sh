#!/bin/bash
CLIENT=$PWD/scripts/new4PersonGame.sh

xterm -geometry 100x15-0+430 -rv -e $CLIENT localhost:5656 localhost:5678 localhost:5600 localhost:5700 &