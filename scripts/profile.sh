#!/usr/bin/env bash

# 쉬고있는 profile 찾기: real1이 사용중이면 real2가 쉬고있다.

function find_idle_profile(){
  # 현재 Nginx가 바라보고 있는 Boot가 정상 수행중인지 HttpStatus로 확인.
  RESPONSE_CODE=$(curl -s -o /dev/null -w "%{http_code}" http://localhost/profile)

  if [ ${RESPONSE_CODE} -ge 400 ] # 400보다크면. 즉, 에러모두 포함
  then
    CURRENT_PROFILE=real2
  else
    CURRENT_PROFILE=real1
  fi

  if [ ${CURRENT_PROFILE} == real1 ]
  then
    IDLE_PROFILE=real2
  else
    IDLE_PROFILE=real1
  fi

  echo "${IDLE_PROFILE}"
}

# 쉬고 있는 profile의 port 찾기
function find_idle_port() {
    IDLE_PROFILE=$(find_idle_profile)

    if [ ${IDLE_PROFILE} == real1 ]
    then
      echo "8081"
    else
      echo "8082"
    fi
}

# bash script는 값을 반환하는 기능이 없기에, echo로 출력 후 그 값을 클라이언트가 잡아서 사용한다.