## switch.sh
#
#
#
## service_url.inc 에서 현재 서비스를 하고 있는 WAS의 포트 번호 가져오기
#CURRENT_PORT=$(cat /home/ec2-user/service_url.inc  | grep -Po '[0-9]+' | tail -1)
#TARGET_PORT=0
#
#echo "> Nginx currently proxies to ${CURRENT_PORT}."
#
#if [ ${CURRENT_PORT} -eq 8081 ]; then
#    TARGET_PORT=8082
#elif [ ${CURRENT_PORT} -eq 8082 ]; then
#    TARGET_PORT=8081
#else
#    echo "> No WAS is connected to nginx"
#    exit 1
#fi
#
## 위 커맨드들을 통해 현재 타겟포트 가져오기
#
## $ service_url.inc 파일을 현재 바뀐 서버의 포트로 변경
#echo "set \$service_url http://127.0.0.1:${TARGET_PORT};" | tee /home/ec2-user/service_url.inc
#
#echo "> Now Nginx proxies to ${TARGET_PORT}."
#
## nginx를 reload 해준다.
#sudo service nginx reload
#
#echo "> Nginx reloaded."
#
#
##=============================================================
#
#
#
### switch.sh
##
###!/bin/bash
##
### Crawl current connected port of WAS
##CURRENT_PORT=$(cat /home/ec2-user/service_url.inc  | grep -Po '[0-9]+' | tail -1)
##TARGET_PORT=0
##
##echo "> Nginx currently proxies to ${CURRENT_PORT}."
##
### Toggle port number
##if [ ${CURRENT_PORT} -eq 8081 ]; then
##    TARGET_PORT=8082
##elif [ ${CURRENT_PORT} -eq 8082 ]; then
##    TARGET_PORT=8081
##else
##    echo "> No WAS is connected to nginx"
##    exit 1
##fi
##
### Change proxying port into target port
##echo "set \$service_url http://127.0.0.1:${TARGET_PORT};" | tee /home/ec2-user/service_url.inc
##
##echo "> Now Nginx proxies to ${TARGET_PORT}."
##
### Reload nginx
##sudo service nginx reload
##
##echo "> Nginx reloaded."