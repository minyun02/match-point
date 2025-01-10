#!/bin/bash
APP_NAME="match-point"
DEPLOY_PATH="/usr/local/match-point"
JAR_PATH="$DEPLOY_PATH/$APP_NAME.jar"
LOG_PATH="/var/log/match-point"

# 현재 시간 기록
echo "[1/5] Starting deployment process..."
DEPLOY_TIME=$(date +%Y%m%d_%H%M%S)

# 이전 JAR 백업
echo "[2/5] Backing up previous version..."
if [ -f $JAR_PATH ]; then
    mv $JAR_PATH $JAR_PATH.$DEPLOY_TIME
fi

# 기존 프로세스 종료
echo "[3/5] Stopping current application..."
pid=$(pgrep -f $APP_NAME)
if [ -n "$pid" ]; then
    kill -15 $pid
    sleep 5
fi

# 새 버전 배포
echo "[4/5] Deploying new version..."
cp $DEPLOY_PATH/*.jar $JAR_PATH
nohup java -jar $JAR_PATH > $LOG_PATH/applications.log 2>&1 &

# 배포 결과 확인
echo "[5/5] Verifying deployment..."
sleep 5
if pgrep -f $APP_NAME > /dev/null; then
  echo "✅ Deploy success"
else
  echo "❌ Deploy failed"
  exit 1
fi