#!/bin/bash
CLIENT=$PWD/new2PersonGame.sh

xterm -geometry 100x15-0+430 -rv -e $CLIENT localhost:5656 localhost:5678 &