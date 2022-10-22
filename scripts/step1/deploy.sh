#!/bin/bash

REPOSITORY=/home/ec2-user/app/step1
PROJECT_NAME=SpringBoot-Study

cd $REPOSITORY/$PROJECT_NAME/

echo "> Git Pull"
git pull

echo "> 프로젝트 Build 시작"
./gradlew build

echo "> step1 디렉토리로 이동"
cd $REPOSITORY

echo "> Build 파일 복사"
cp $REPOSITORY/$PROJECT_NAME/build/libs/*.jar $REPOSITORY/

echo "> 현재 구동중인 애플리케이션 pid 확인"
# pgrep은 process id만 추출하는 명령어이다. -f는 프로세스 이름으로 찾겠다는 의미다.
CURRENT_PID=$(pgrep -f ${PROJECT_NAME}.*.jar)
echo "현재 구동중인 애플리케이션 pid: $CURRENT_PID"

if [ -z "$CURRENT_PID" ]; then
  echo "> 현재 구동 중인 애플리케이션이 없으므로 종료하지 않습니다."
else
  echo "> kill -15 $CURRENT_PID"
  kill -15 $CURRENT_PID
  sleep 5
fi

echo "> 새 애플리케이션 배포"
# 새로 실행할 jar 파일을 찾는다. 여러 파일이 생기기 때문에 tail -n으로 가장 최신 jar를 변수에 저장한다.1
JAR_NAME=$(ls -tr $REPOSITORY/ | grep jar | tail -n 1)

echo "> Jar Name: $JAR_NAME"

# nohup으로 jar을 실행한다. 터미널 접속을 끊어도 애플리케이션이 종료되지 않는다.
# SpringBoot의 장점으로 특별히 외장톰캣 설치할 필요가 없다.
# application-oauth.properties를 사용하도록 수정하였다.
# Dspring.config.location : 스프링 설정파일 위치를 지정한다.
# classpath가 붙으면 jar 안의 resource 디렉토리를 기준으로 경로가 생성된다.
# application-oauth.properties는 여기서 외부에 있으니, 절대경로를 사용한다.
nohup java -jar \
  -Dspring.config.location=
    classpath:/application.properties,
    /home/ec2-user/app/application-oauth.properties,
    /home/ec2-user/app/application-real-db.properties,
    classpath:/application-real.properties \
  -Dspring.profiles.active=real \
  $REPOSITORY/$JAR_NAME 2>&1 &
#  -Dspring.profiles.active=real 는 application-real.properties를 활성화시킨다.
# 해당 파일 내부에 oauth, real-db도 사용하기 때문에, real-db 역시 활성화대상에 포함된다.
