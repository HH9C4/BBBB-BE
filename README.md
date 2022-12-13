
<div align=center>

# 👀 welcome to ![image](https://user-images.githubusercontent.com/99253403/203619462-fcef5c78-16ad-4dc7-8a57-46dade14ab80.png) 👀

## "서울시 도시 데이터 공공 API 기반 정보제공형 커뮤니티"
</div>

<br>

서울시 구별 실시간 정보를 확인하고, 다양한 소식을 공유해보세요!

서울시에 얼마나 많은 사람이 모였을까요? 🤔

방문 전 지역 별 실시간 인구 혼잡도를 확인하고, 구 별 커뮤니티를 통해 소식을 확인해보세요!

처음 방문하는 지역에 어떤 곳이 🔥HOT🔥한지 궁금하신가요?

크리스마스🎄 에 내가 갈 지역에 사람이 얼마나 모였는지 확인하고 싶으신가요?

출퇴근 시간에 행사가 있다던데..🤦 상황이 궁금하신가요?

🥳 붐비는 곳을 찾는 파티피플들도 😐 피하고 싶은 사람들도 모두 붐비붐비에서 만나요!

<br><br>

## 👨‍👩‍👧‍👦 Our Team

|정성우|장윤서|유도원|오기쁨|임효진|이지혜|
|:---:|:---:|:---:|:---:|:---:|:---:|
|[@anfrosus](https://github.com/anfrosus)|[@Younddo](https://github.com/Younddo)|[@dwon5001](https://github.com/dwon5001)|[@joyfive](https://github.com/joyfive)|[@BLAKE198492](https://github.com/BLAKE198492)|aksjdffg@naver.com|
|VL💛BE|BE💛|BE💛|TL💚FE|FE💚|DS💙|

<br>

### [👊 프로젝트 노션 바로가기](https://joyfive.notion.site/C-4-SA-9407bb7a0897420782b957a25036b092)
### [👊 원페이지 노션 바로가기](https://joyfive.notion.site/ee0519f495c74049ac3a7c4a7691d8d3)

<br><br>



## ✨ 프로젝트 설명

### 💫 프로젝트 기능

### :one: OAuth2 소셜로그인 (kakao, naver)
<details>
<summary>미리보기</summary>
<div markdown="1">

![로그인1](https://user-images.githubusercontent.com/99253403/207301154-bf2cfd83-49d1-474d-9980-69d1d41a64f8.gif)

 <br>
</div>
</details>

- Kakao와 Naver계정을 통한 간편 로그인이 가능합니다.
 
- Kakao Email과 Naver Email 이 동일한 경우 하나의 계정으로 통합하여 사용이 가능합니다.
 
- 서비스 탈퇴기능
 
  - 하나의 계정에 Kakao 와 Naver 모두 연동되어있는 경우 두 곳 모두에서 연동이 해제되며 서비스에서 탈퇴처리 됩니다.
   
  - 작성한 게시글, 댓글 등은 사라지지 않으며 탈퇴한계정으로 표시됩니다(회원 정보는 삭제됨).

 <br>

### :two: 서울시 open api를 활용한 실시간 정보제공 기능
<details>
<summary>미리보기</summary>
<div markdown="1">

![데이터 보여주기](https://user-images.githubusercontent.com/99253403/207301175-8c1b24aa-4b91-4de5-9e08-29afdbedf8ff.gif)

 <br>
</div>
</details>
 
 - 스케쥴러를 활용하여 5분마다 데이터를 수집합니다.
 
 - 2-1. 구 별 코로나 정보, spot 별 날씨 정보 제공 : 저장되어 있는 데이터를 실시간으로 제공합니다.
 
 - 2-2. 전체 데이터의 누적 통계를 활용한 정보제공 기능 : 수집한 데이터를 기반으로 혼잡도 점수를 산정 하여 순위 통계를 제공합니다.
 
 - 2-3. spot 별 누적 + 실시간 정보제공 기능 : 지난주 같은 요일의 혼잡도, 인구수를 비교하여 실시간 인구 추이를 제공합니다.
 
 <br>

### :three: 구 별 커뮤니티 (CRUD)

<details>
<summary>미리보기</summary>
<div markdown="1">

![글쓰기1](https://user-images.githubusercontent.com/99253403/207301193-a4957b80-66b4-49ef-ab55-0f180fa0b806.gif)
 
![글쓰기2](https://user-images.githubusercontent.com/99253403/207301224-b89ad772-209f-4557-ae20-d6064610d649.gif)

 <br>
</div>
</details>
 
 - 서울시 25개 구 별 커뮤니티를 제공합니다.
 
 - 1.게시글, 댓글 작성/수정/삭제/조회 : 다중 이미지 업로드가 가능하며 카테고리 선택과 태그추가 기능을 지원합니다.
 
 - 2.좋아요 : 게시글, 댓글을 좋아요 할 수 있으며 이에대한 알림기능도 지원합니다.
 
 - 3.북마크 : 내가 자주 사용하는 구를 북마크 할 수 있습니다. 북마크한 지역의 게시글이 추가되면 실시간 알림을 제공합니다.

 <br>

### :four: 검색 기능(내용+태그, 태그 검색기능)

<details>
<summary>미리보기</summary>
<div markdown="1">

![검색기능](https://user-images.githubusercontent.com/99253403/207301257-6c59f8b5-ad66-4cd9-927c-4e0f11d30663.gif)

 <br>
</div>
</details>

 - QueryDSL을 활용하여 동적 쿼리작성이 가능하도록 구현하였습니다.
 
 - 게시글의 내용을 검색하거나 태그로 검색이 가능합니다. 작성자 검색도 가능합니다.

 <br>

### :five: 마이페이지 기능 (내가 작성한 글, 내가 좋아요한 글, 내 게시글에 달린 댓글, 내 정보 수정)

<details>
<summary>미리보기</summary>
<div markdown="1">

 <br>
</div>
</details>

(댓글 알림 클릭 시 알림 아이콘이 없어지는것 보여주면 될듯, 수정도 가능하다정도만)
 
 - 마이페이지에서 내가 작성한글, 내가 좋아요한 글, 내 게시글에 달린 댓글을 확인할 수 있으며 프로필사진과 닉네임을 수정할 수 있습니다.
 
 - 내 게시글에 새로운 댓글이 달리면 새로운 알림이 등록됩니다.

<br>

### :six: 신고 기능 (사용자, 닉네임, 게시글, 댓글)

<details>
<summary>미리보기</summary>
<div markdown="1">

 <br>
</div>
</details>

(댓글 알림 클릭 시 알림 아이콘이 없어지는것 보여주면 될듯, 수정도 가능하다정도만)
 
 - 악성 사용자, 불건전한 닉네임, 게시글, 댓글을 내용과 함께 신고할 수 있습니다.
 
 - 본인은 본인을 신고할 수 없으며 같은 건의 신고에 대해서는 계정 하나당 1회로 제한됩니다.
   
 - 항목별 일정 횟수가 지나게 되면 강제로 닉네임을 변경하거나, 내용을 비공개 처리하여 보여주게 됩니다.

 <br>

### :seven: 무한스크롤

<details>
<summary>미리보기</summary>
<div markdown="1">

 <br>
</div>
</details>

 <br>

### :eight: WebSocket을 활용한 실시간 채팅

<details>
<summary>미리보기</summary>
<div markdown="1">

 <br>
</div>
</details>

(메인에서 알림떠서 채팅방 이동해서 소통하는 그림)
(나갔다 들어오는것도 하면좋을듯, 둘다 나가면 채팅방이 새로 시작되는 것도)

 - 실시간 채팅이 가능합니다.
 
 - 최근에 대화가 이루어진 순서대로 채팅방이 보여집니다.
 
 - 상대방이 나간 후에 새로운 메세지가 등록되면 기존의 채팅이 이어집니다.
 
 - 채팅방에서 모두 나가게 되면 채팅 내역은 삭제되며 다시 대화를 시작하면 새로운 채팅방이 생성됩니다.
 
 <br>

### :nine: SSE를 활용한 실시간 알림

<details>
<summary>미리보기</summary>
<div markdown="1">

 <br>
</div>
</details>


(메인에서 알림떠서 게시글로 이동하는 그림)
 
 - 북마크한 게시판에 새로운 글이 등록되면 실시간 알림을 제공하며 알림을 클릭하면 해당 게시글로 이동합니다.
 
 - 내가 작성한 게시글에 좋아요 및 댓글이 달리면 실시간 알림을 제공하며 알림을 클릭하면 해당 게시글로 이동합니다.
 
 - 실시간 채팅이 오면 실시간 알림을 제공하며 알림을 클릭하면 해당 채팅방으로 이동합니다.
 
<br>

---

### 💫 적용 기술

### :one: QueryDSL

 - 정렬, 검색어 등에 따른 동적 쿼리 작성을 위하여 QueryDSL 도입하여 활용했습니다.


### :two: Swagger

 - 프론트엔드와 정확하고 원활한 소통을 위하여 스웨거를 도입하여 적용하였습니다.
 - [BoombiBoombi Swagger](https://boombiboombi.o-r.kr/swagger-ui/index.html#/)


### :three: Sentry를 통한 에러 로그 확인 및 공유

 - Sentry를 활용하여 에러로그를 쉽게 확인/공유 할 수 있었습니다.
 - [Sentry Image](https://github.com/HH9C4/BBBB-BE/wiki/%5BTech%5D-Sentry)


### :four: Github Actions & Code Deploy (CI/CD)

 - 자동 빌드/배포를 위하여 깃허브 액션과 코드디플로이를 활용하여 CI/CD 를 구축했습니다.
 - [AWS CodeDeploy](https://github.com/HH9C4/BBBB-BE/wiki/%5BTect%5D-AWS-CodeDeploy)


### :five: Scheduler를 통한 open api 호출
 
 - 5분마다 변동되는 데이터를 수집/제공/관리 하기 위하여 스케쥴러를 활용하였습니다.


### :six: Redis

 - 연속된 요청으로 인한 DB병목을 해소하고 RefreshToken 등 소멸기간이 존재하는 데이터의 TimeToLive 관리를 용이하게 할 수 있도록 Redis를 도입하였습니다.

<br><br>

---

## [📋 ERD Diagram](https://github.com/HH9C4/BBBB-BE/wiki/%5BTech%5D-ERD-Diagram)




<br>

## 🚨 Trouble Shooting

#### Join Fetch 순서 보장 문제 [WIKI보기](https://github.com/HH9C4/BBBB-BE/wiki/%5BTrouble-Shooting%5D-Fetch-Join)

#### NGINX ReverseProxy를 활용한 무중단배포 시 Cors, SSE 설정 [WIKI보기](https://github.com/HH9C4/BBBB-BE/wiki/%5BTrouble-Shooting%5D-NGINX-ReverseProxy%EB%A5%BC-%ED%99%9C%EC%9A%A9%ED%95%9C-%EB%AC%B4%EC%A4%91%EB%8B%A8%EB%B0%B0%ED%8F%AC-%EC%8B%9C-Cors,-SSE-%EC%84%A4%EC%A0%95)

#### JPA N+1 문제 [WIKI보기](https://github.com/HH9C4/BBBB-BE/wiki/%5BTrouble-Shooting%5D-N%E2%9E%951)

#### Open Api data [WIKI보기](https://github.com/HH9C4/BBBB-BE/wiki/%5BTrouble-Shooting%5D-Open-Api-Data)

#### Token Reissue [WIKI보기](https://github.com/HH9C4/BBBB-BE/wiki/%5BTrouble-Shooting%5D-Token-Reissue)


<br>

## :raising_hand::thought_balloon: Concern

#### Access Token and Refresh Token Reissue Process [WIKI보기](https://github.com/HH9C4/BBBB-BE/wiki/%5BConcern%5D-Access-Token-and-Refresh-Token-Reissue-Process)

#### Comment & Like Table Structure [WIKI보기](https://github.com/HH9C4/BBBB-BE/wiki/%5BConcern%5D-Comment-&-Like-Table-Structure)

#### S3 Image Upload Control [WIKI보기](https://github.com/HH9C4/BBBB-BE/wiki/%5BConcern%5D-S3-Image-Upload-Control)

#### Validation Logic [WIKI보기](https://github.com/HH9C4/BBBB-BE/wiki/%5BConcern%5D-Validation-Logic)

<br>

## 📝 Technologies & Tools (BE) 📝

<br>
 
<img src="https://img.shields.io/badge/Spring-6DB33F?style=for-the-badge&logo=spring&logoColor=white"/>  <img src="https://img.shields.io/badge/SpringSecurity-6DB33F?style=for-the-badge&logo=SpringSecurity&logoColor=white"/>  <img src="https://img.shields.io/badge/SpringBoot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white"/>   <img src="https://img.shields.io/badge/github-181717?style=for-the-badge&logo=github&logoColor=white"/>  <img src="https://img.shields.io/badge/CODEDEPLOY-181717?style=for-the-badge"/>  

<img src="https://img.shields.io/badge/git-F05032?style=for-the-badge&logo=git&logoColor=white"/> <img src="https://img.shields.io/badge/GithubActions-2088FF?style=for-the-badge&logo=githubactions&logoColor=white"/>  <img src="https://img.shields.io/badge/java-007396?style=for-the-badge&logo=java&logoColor=white">  <img src="https://img.shields.io/badge/JSONWebToken-000000?style=for-the-badge&logo=JSONWebTokens&logoColor=white"/>  <img src="https://img.shields.io/badge/Gradle-02303A?style=for-the-badge&logo=Gradle&logoColor=white"/>  <img src="https://img.shields.io/badge/IntelliJIDEA-000000?style=for-the-badge&logo=IntelliJIDEA&logoColor=white"/>  <img src="https://img.shields.io/badge/Postman-FF6C37?style=for-the-badge&logo=Postman&logoColor=white"/>  <img src="https://img.shields.io/badge/Notion-000000?style=for-the-badge&logo=Notion&logoColor=white"/>

<img src="https://img.shields.io/badge/AmazonS3-569A31?style=for-the-badge&logo=AmazonS3&logoColor=white"/>  <img src="https://img.shields.io/badge/AmazonEC2-FF9900?style=for-the-badge&logo=AmazonEC2&logoColor=white"/>  <img src="https://img.shields.io/badge/AmazonRDS-527FFF?style=for-the-badge&logo=AmazonRDS&logoColor=white"/>  <img src="https://img.shields.io/badge/MySQL-4479A1?style=for-the-badge&logo=MySQL&logoColor=white"/>  <img src="https://img.shields.io/badge/Ubuntu-E95420?style=for-the-badge&logo=Ubuntu&logoColor=white"/>

<img src="https://img.shields.io/badge/Swagger-85EA2D?style=for-the-badge&logo=swagger&logoColor=black"/>  <img src="https://img.shields.io/badge/Docker-2496ED?style=for-the-badge&logo=docker&logoColor=white"/>  <img src="https://img.shields.io/badge/Sentry-362D59?style=for-the-badge&logo=sentry&logoColor=white"/>  <img src="https://img.shields.io/badge/JiraSoftware-0052CC?style=for-the-badge&logo=jirasoftware&logoColor=white"/>  <img src="https://img.shields.io/badge/Slack-4A154B?style=for-the-badge&logo=slack&logoColor=white"/>  <img src="https://img.shields.io/badge/NGINX-009639?style=for-the-badge&logo=nginx&logoColor=white"/>  <img src="https://img.shields.io/badge/LINUX-FCC624?style=for-the-badge&logo=linux&logoColor=black"/>  <img src="https://img.shields.io/badge/Figma-F24E1E?style=for-the-badge&logo=figma&logoColor=white"/>


<div align=center>

![image](https://user-images.githubusercontent.com/99253403/203619879-c68ee5ad-b7c6-496c-a0ca-d72fd19fafd2.png)


</div>
