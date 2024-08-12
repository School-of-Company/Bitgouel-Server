REPOSITORY=/home/ec2-user
PROJECT_NAME=Bitgouel-Server
JAR_PATH=$REPOSITORY/$PROJECT_NAME/bitgouel-api/build/libs/bitgouel-api-0.0.1-SNAPSHOT.jar

cd $REPOSITORY/$PROJECT_NAME/

echo "> Git Pull"
git pull origin master

echo "> Project Build"
./gradlew clean bitgouel-api:build -x test

CURRENT_PID=$(lsof -i tcp:8080)
if [ -z "$CURRENT_PID" ]; then
    echo "> 현재 구동중인 애플리케이션이 없으므로 종료하지 않습니다."
else
    echo "> kill -9 $CURRENT_PID"
    kill -9 $CURRENT_PID
    sleep 5
fi

echo "> 애플리케이션 배포"
JAR_NAME=$(ls -tr $JAR_PATH | tail -n 1)
echo "> JAR NAME: $JAR_NAME"
TZ='Asia/Seoul' nohup java -jar $JAR_NAME --spring.profiles.active=prod >> /home/ec2-user/deploy.log 2>/home/ec2-user/deploy-err.log &