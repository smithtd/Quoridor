#!/bin/bash
SERVER=$PWD/newServer.sh

xterm -geometry 100x15-0+0 -e $SERVER 5656 &
xterm -geometry 100x15-0+220 -e $SERVER 5678 &
