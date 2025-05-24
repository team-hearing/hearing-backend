# 역사를 듣다_Hearing
한국의 주요 역사사건과 참고 영상,이미지 자료를 제공하는 RESTful API 서버
![Hist Event Demo]()

#### 기술스택 
- 웹서버 : Nginx
- 언어 : Java 17
- 프레임워크 : Spring Boot 3.4.3
- 데이터베이스 : PosgreSQL
- 빌드 도구 : Maven
- 주요 라이브러리: Spring Data JPA, Spring Security, Lombok

#### 프로젝트 구조 : 도메인형
```
src
├── 📂 common                
│   ├── 📂 dto             
│   │   ├── HistEventDTO.java   
│   │   ├── HistImageDTO.java  
│   │   └── HistVideoDTO.java   
│   ├── 📂 exception         
│   │   ├── NotFoundException.java
│   │   └── ValidationException.java
│   └── 📂 handler          
│       └── GlobalExceptionHandler.java
│
│
│
├── 📂 domain               
│   ├── 📂 hist_event       
│   │   ├── HistEvent.java  
│   │   └── HistEventService.java
│   ├── 📂 hist_image        # 이미지 도메인
│   │   ├── HistImage.java   # 이미지 엔터티
│   │   └── HistImageService.java
│   └── 📂 hist_video        # 비디오 도메인
│       ├── HistVideo.java   # 비디오 엔터티
│       └── HistVideoService.java 

```
