#!/bin/bash
SERVER=/Users/emilydonahue/Documents/SoftwareEngineering/team-511Tactical/startServer.sh
CLIENT=/Users/emilydonahue/Documents/SoftwareEngineering/team-511Tactical/start2PersonGame.sh

xterm -e $SERVER 5656 &
xterm -e $SERVER 5678 &
sleep 5
xterm -e $CLIENT localhost:5656 localhost:5678 &

#open -a Terminal -e "$QUORIDOR_HOME" && sh startServer.sh 5656
