# sns-server

## 개요

---

* twitter, instagram 같은 sns 서비스 구현 프로젝트
* 기능마다 성능과 유지보수성을 고려하여 구현하는 것을 목표로 개발

## 사용 기술 및 개발 환경

---

* Java, Spring Boot, IntelliJ, Maven, Mysql, Mybatis

## Application UI

---

UI 전체 [https://github.com/jinhoa52/sns-server/wiki/UI](https://github.com/jinhoa52/sns-server/wiki/UI)  

![Part of Application UI](https://raw.githubusercontent.com/9bini/repository/master/img/ui.png "UI 일부분")

## 주요 기능

---

* 회원가입 / 탈퇴  
* 로그인 / 로그아웃  
* 회원정보 수정



## 브랜치 관리 전략

---

* Git Flow 전략으로 브랜치를 관리합니다.
* Pull Request에 리뷰는 혼자 프로젝트를 진행해 리뷰는 개인적으로 코드 점검하는 방식으로 대체합니다.

### 작업 종류

***

* master : 제품으로 출시될 수 있는 브랜치
* develop : 다음 출시 버전을 개발하는 브랜치
* feature : 기능을 개발하는 브랜치
* release : 이번 출시 버전을 준비하는 브랜치
* hotfix : 출시 버전에서 발생한 버그를 수정 하는 브랜치
