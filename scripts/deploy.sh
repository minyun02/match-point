#!/bin/bash
APP_NAME="match-point"
DEPLOY_PATH="/usr/local/match-point"
JAR_PATH="$DEPLOY_PATH/$APP_NAME.jar"
LOG_PATH="/var/log/match-point"
ENV_PATH="$DEPLOY_PATH/.env"

# 현재 시간 기록
echo "[1/8] Starting deployment process..."
DEPLOY_TIME=$(date +%Y%m%d_%H%M%S)

# 현재 포트 확인
echo "[2/8] Checking current port..."
CURRENT_PORT=$(sudo netstat -tnlp | grep -E ':8080|:8081' | grep java | awk '{print substr($4,length($4)-3,4)}')
if [ ${CURRENT_PORT} -eq 8080 ]; then
  TARGET_PORT=8081
else
  TARGET_PORT=8080
fi

# 환경변수 파일 존재 확인 및 로드
echo "[3/8] Loading environment variables..."
if [ ! -f $ENV_PATH ]; then
    echo "❌ Environment file not found at $ENV_PATH"
    exit 1
fi
source $ENV_PATH

# 이전 JAR 백업
echo "[4/8] Backing up previous version..."
if [ -f $JAR_PATH ]; then
    mv $JAR_PATH $JAR_PATH.$DEPLOY_TIME
fi

# 기존 프로세스 종료
echo "[5/8] Stopping current application..."
TARGET_PID=$(pgrep -f ${APP_NAME}.*.jar.*${TARGET_PORT})
if [ -n "$TARGET_PID" ]; then
    kill -15 $TARGET_PID
    sleep 5
fi

# 새 버전 배포
echo "[6/8] Deploying new version..."
cp $DEPLOY_PATH/build/libs/matchpoint-0.0.1-SNAPSHOT.jar $JAR_PATH
nohup java \
    -Dspring.config.location=classpath:/application.yml \
    -Dserver.port=${TARGET_PORT} \
    -DJWT_SECRET_KEY="${JWT_SECRET_KEY}" \
    -DDB_URL="${DB_URL}" \
    -DDB_USERNAME="${DB_USERNAME}" \
    -DDB_PASSWORD="${DB_PASSWORD}" \
    -DFIREBASE_CREDENTIALS_PATH="${FIREBASE_CREDENTIALS_PATH}" \
    -DUPLOAD_PATH="${UPLOAD_PATH}" \
    -jar $JAR_PATH > $LOG_PATH/applications-${TARGET_PORT}.log 2>&1 &

echo "[7/8] Health checking..."
for RETRY in {1..10}; do
  sleep 10
  RESPONSE=$(curl -s http://localhost:${TARGET_PORT}/actuator/health)
  if [[ $RESPONSE == *"UP"* ]]; then
    break
  fi
  if [ $RETRY -eq 10 ]; then
    echo "❌ Health check failed"
    exit 1
  fi
done

echo "[8/8] Updating nginx configuration..."
echo "set \$service_url http://127.0.0.1:${TARGET_PORT};" | sudo tee /etc/nginx/conf.d/service-url.inc
sudo service nginx reload

# 배포 결과 확인
echo "Verifying deployment..."
sleep 5
if pgrep -f $APP_NAME > /dev/null; then
  echo "✅ Deploy success"
else
  echo "❌ Deploy failed"
  exit 1
fi