#!/usr/bin/env bash

ABSPATH=$(readlink -f $0)
ABSDIR=$(dirname $ABSPATH)
source ${ABSDIR}/profile.sh

function switch_proxy() {
    IDLE_PORT=$(find_idle_port)

    echo "> 전환할 Port: $IDLE_PORT"
    echo "> Port 전환"
    # 하나의 문장을 만들어 파이프라인으로 넘겨주기 위해 echo를 사용했다. 즉, 프록시 설정파일에 저장 될 내용이다.
    # "" 를 사용하지 않으면 $service_url 변수를 찾게 되므로 꼭 ""를 사용해야 한다.
    # tee는 덮어쓰는 명령어인듯
    echo "set \$service_url http://127.0.0.1:${IDLE_PORT};" | sudo tee /etc/nginx/conf.d/service-url.inc

    echo "> 엔진엑스 Reload"
    sudo service nginx reload
    # restart와는 다른데, 설정을 다시불러온다. restart는 끊기지만 reload는 끊김 없이 다시 불러온다.
    # 다만 중요한 설정들은 반영되지 않으므로 restart를 해야 한다.
    # 여기서는 외부설정파일만 다시불러오는거라 reload로 가능하다.
}