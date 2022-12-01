# ae-Server
ae 프로젝트의 종합적인 서버 프로젝트 저장소로, 서비스를 제공하는  ae_SpringServer, ae_FlaskServer, ae_communityServer와 eureka_server, gateway_server를 포함하고 있습니다. 

# [채삐] 채식인들의 식단 고민을 해결해주는 채식 비서 - SERVER 

#### 🌱 서비스 이름
> 채삐 (Chaebbi)
#### 🌱 진행 기간
> 2021.12  ~ 2022.11.28
#### 🌱 소개
> 지극히 평범한 일상에서도 채식인들은 대중적이지 않은 식단으로 인해 선택에 어려움을 겪습니다.  
> 해당 서비스는 채식인들을 주 타겟층으로 하며, 그들의 라이프스타일을 반영하여 식생활 전반을 관리해줍니다.

1. 냉장고에 있는 재료만으로 한 끼를 만들어 먹고 싶을 때! 
2. 약속이나 여행 등의 일정으로 외식을 해야 할 때!
3. 집 근처에서 간편식으로 간단히 한 끼를 해결하고 싶을 때!

🙋🏻 app store에서 채식 비서 [채삐](https://apps.apple.com/kr/app/%EC%B1%84%EC%82%90/id1643485964)를 사용해보세요!
  

<br>

## 🛠 사용 기술
### Application
⌨️ Java 11 <br>
⌨️ SpringBoot 2.7.0 <br>
⌨️ Spring Data JPA, Spring Security, Spring REST Docs <br>
⌨️ Spring cloud gateway, netflix eureka

### Infrastructure
⌨️ MySQL <br>
⌨️ AWS EC2, RDS, S3
⌨️ AWS codeDeploy

<br>

## 전체 아키텍처 
![readme 위한 아키텍처](https://user-images.githubusercontent.com/89854207/205052543-a734e44e-13ae-49b5-8a59-613eeac015b5.png)

  
## 주요 기능

#### 1️⃣ 식단 등록 
촬영하기, 사진 불러오기, 검색하기를 통해 음식의 정보를 제공하고, 이를 이용하여 먹은 음식을 기록할 수 있습니다.
#### 2️⃣ 일정 장소 연동-음식점 제안
캘린더 앱과 연동하여 장소를 기반으로 근처의 채식 식당을 보여줍니다.
#### 3️⃣ 재료 기반 음식 추천
가지고 있는 재료를 입력하면 최소한의 재료를 추가하여 만들수 있는 음식을 보여줍니다.
  
## 주요 화면
<img width="716" alt="스크린샷 2022-08-08 오후 4 42 01" src="https://user-images.githubusercontent.com/88825022/183365895-485631c5-8a6b-4020-9893-00e0b6737484.png">

## 시연 동영상 

[✋ClickHere✋](https://youtu.be/XZz1xUvrJ0g)
<br><br>
### Team member
|Position|Name|Repository|
|:---:|:---:|---|
|Server|[김세현](https://early-tithonia-af2.notion.site/KIM-SEHYEON-8d0186298ecc43a797abb8de0436c15e)|https://github.com/Vector1331|
|Server|[김민서](https://www.notion.so/d6d0e962e84942a39a9e527fd602337f)|https://github.com/rosesua318|

