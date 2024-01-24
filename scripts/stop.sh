#!/bin/bash

ROOT_PATH="/home/ec2-user/jpaboard"
JAR="$ROOT_PATH/jpaboardpractice-0.0.1-SNAPSHOT.jar"
STOP_LOG="$ROOT_PATH/stop.log"
SERVICE_PID=$(pgrep -f $JAR)

if [ -z "$SERVICE_PID" ]; then
  echo "Service NotFound" >> $STOP_LOG
else
  echo "Service Exit " >> $STOP_LOG
  kill "$SERVICE_PID"
fi