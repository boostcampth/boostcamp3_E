# boostcamp3_E <img src="https://image.flaticon.com/icons/svg/458/458842.svg" width="24" height="24">

> 주제 : 해외 여행시에 챙겨야할 "꿀템"을 알려주는 앱 (이거 사!)

> 주제설명 : 여행또는 출장등의 목적으로 국내,해외로 가야 하는 상황에서 각 국가마다 꼭 사야하는 꿀템들을 알려주고 아이템에 대한 개인의 의견들을 모아 판단에 도움을 주는 어플리케이션

> 화면정의서
- https://docs.google.com/document/d/1vVOnKNhLd52Nxs6HSa2ITW-s6aV5g4Dg6K3eIxgleHg/edit

> 기능정의서
- 시나리오 : https://docs.google.com/document/d/1TTTtluPmB0fyN89_CRHUAK8T2xtDxzuhcyXaWuzecbM/edit
- 기능 별 정리 : https://docs.google.com/spreadsheets/d/1usNgQ-SyEAl3jbu0Sbijl_P1VRxrJf0II-cLIESnH2c/edit?ts=5c493e1a#gid=0

> 프로젝트 일정
- https://docs.google.com/spreadsheets/d/1j1As8H0BpbUjSVd2XuXzCKQ_vNdLAyI5-lePyBhfS8M/edit#gid=1772226991

## 앱 라이브러리 및 핵심 기능 <img src="https://image.flaticon.com/icons/svg/225/225932.svg" width="24">
### 핵심기술

- 서버리스환경과 실시간을 위한 Firebase
- 워크매니저 시간기반 백그라운드 알림 서비스
- Retrofit/Rx를 통한 API 비동기 처리
- SNS에서 여러 이미지가져오기위한  Glide
- 위치기반 서비스를 위한 Geo Fire Query (알림)

### 사용 라이브러리 (가이드 링크) 
- Use [MVP](https://github.com/googlesamples/android-architecture)
- Image : [Glide](https://github.com/bumptech/glide)
- AAC : [WorkManager](https://developer.android.com/topic/libraries/architecture/workmanager/basics)
- Serverless & Network : [Firebase](https://firebase.google.com/docs/android/setup), [Naver shopping API](https://developers.naver.com/docs/search/shopping/), [retrofit2](https://github.com/square/retrofit)
- Degine : [material](https://material.io/develop/android/)
- Util : [AndroidX](https://developer.android.com/jetpack/androidx/), [DataBinding](https://developer.android.com/topic/libraries/data-binding/?hl=ko), [Rxjava2](https://github.com/ReactiveX/RxJava) ([Rxjava2 Marble](http://reactivex.io/documentation/operators.html)), [TedPermission](https://github.com/ParkSangGwon/TedPermission)

## 코드 컨벤션 <img src="https://d29fhpw069ctt2.cloudfront.net/icon/image/74149/preview.svg" width="24" height="24">

- 아래의 내용은 [PRND](https://github.com/PRNDcompany/android-style-guide)의 컨벤션을 가져왔습니다

### 목록
각 항목별 가이드는 아래에서 확인해볼 수 있으며 계속 유지보수되어 추가될 예정입니다.
- [Java](Java.md)
- [Resource](Resource.md)
- [Gradle](Gradle.md)
- [Architecture](Architecture.md)

### 참고
이 스타일가이드는 아래 글을 기반으로 PRND프로젝트의 스타일을 가져왔습니다.
- [A successful XML naming convention](https://jeroenmols.com/blog/2016/03/07/resourcenaming/)
- [Best practices for happy Android resources](https://blog.shazam.com/best-practices-for-happy-android-resources-9445c1b521d6)
- [Project guidelines](https://github.com/ribot/android-guidelines/blob/master/project_and_code_guidelines.md)


### Feature 작성 방법 
1. 이슈 작성
2. 브랜치 생성(feature/BCE-이슈번호-이슈제목)
- 작업하기 전에 PULL하기
3. 커밋 & 푸쉬를 하고 develop branch에 merge PR
4. condition이 충족된 PR은 담당자가 merge

### 앱 타이틀 컬러
- [머테리얼 추천 조합 컬러사용](https://material.io/tools/color/#!/?view.left=0&view.right=0&primary.color=0000c6&primary.text.color=ffffff&secondary.color=1b00d4)

### 패키지 생성 참고자료
- [google i/o sample](https://github.com/google/iosched)
- [google blueprint](https://github.com/googlesamples/android-architecture)
- [내생각](https://github.com/GODueol)