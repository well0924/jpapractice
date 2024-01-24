#!/bin/bash

ROOT_PATH="/home/ec2-user/jpaboard/build/libs"
JAR="$ROOT_PATH/jpaboardpractice-0.0.1-SNAPSHOT.jar"

APP_LOG="$ROOT_PATH/application.log"
ERROR_LOG="$ROOT_PATH/error.log"
START_LOG="$ROOT_PATH/start.log"

NOW=$(date +%c)

echo "[$NOW] > $JAR 실행" >> $START_LOG
nohup java -jar $JAR > $APP_LOG 2> $ERROR_LOG &

SERVICE_PID=$(pgrep -f $JAR)
echo "[$NOW] > Serivce PID: $SERVICE_PID" >> $START_LOG