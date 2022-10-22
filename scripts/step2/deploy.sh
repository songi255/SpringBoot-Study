#!/bin/bash

REPOSITORY=/home/ec2-user/app/step2
PROJECT_NAME=SpringBoot-Study

echo "> Build 파일 복사"
cp $REPOSITORY/zip/*.jar $REPOSITORY/

echo "> 현재 구동중인 애플리케이션 pid 확인"
CURRENT_PID=$(pgrep -fl ${PROJECT_NAME}  | grep jar | awk '{print $1}')
# 프로젝트 이름의 다른 프로그램들이 있을 수 있어서, jar 프로세스를 찾은 뒤 ID를 찾는다.
echo "현재 구동중인 애플리케이션 pid: $CURRENT_PID"

if [ -z "$CURRENT_PID" ]; then
  echo "> 현재 구동 중인 애플리케이션이 없으므로 종료하지 않습니다."
else
  echo "> kill -15 $CURRENT_PID"
  kill -15 $CURRENT_PID
  sleep 5
fi

echo "> 새 애플리케이션 배포"
JAR_NAME=$(ls -tr $REPOSITORY/*.jar | tail -n 1)

echo "> Jar Name: $JAR_NAME"
echo "> $JAR_NAME 에 실행권한 추가"
chmod +x $JAR_NAME

echo "> $JAR_NAME 실행"
nohup java -jar \
  -Dspring.config.location=
    classpath:/application.properties,
    /home/ec2-user/app/application-oauth.properties,
    /home/ec2-user/app/application-real-db.properties,
    classpath:/application-real.properties \
  -Dspring.profiles.active=real \
  JAR_NAME > $REPOSITORY/nohup.out 2>&1 &
# nohup 실행 시 CodeDeploy는 무한대기한다. 이 이슈를 해결하기 위해 nohup.out 파일을 표준 입출력용으로 별도로 사용한다.
# 이렇게 하지 않으면 nohup.out이 생기지 않고, CodeDeploy 로그에 표준 입출력이 출력된다.
# nohup이 끝나기 전까지 CodeDeploy도 끝나지 않으니 꼭 이렇게 해야만 한다.

# BashSupport 플러그인을 설치하면 .sh 편집 시 도움을 받을 수 있다. 근데 난 유료버전이라 괜찮을듯?

# step1 의 deploy와 비교해보면, 직접 git에서 pull 해서 빌드하던 부분이 없어졌다. 그리고 Jar 실행단계에서 몇가지 코드가 추가되었다.
