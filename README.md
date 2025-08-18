# Book Search App

카카오 도서 검색 API를 활용해 만든 Android 앱입니다.  
도서 검색, 즐겨찾기, 정렬/필터 기능을 제공합니다.  
(https://developers.kakao.com/docs/latest/ko/daum-search/dev-guide#search-book)




## 🚀 빌드 방법

프로젝트를 빌드하기 위해 `local.properties` 파일에 아래 내용을 추가해주세요

```properties
KAKAO_API_URL="https://dapi.kakao.com/"
KAKAO_API_KEY="KakaoAK c458758c6e4a03e15fb4e176538b22f6"
```




## 🛠 사용 프레임워크와 라이브러리

- **Android Jetpack**
  - **Compose** : 선언형 UI
  - **Navigation** : 화면 전환
  - **ViewModel** : UI 상태 관리
- **Hilt (DI)** : 의존성 주입
- **Coroutines / Flow**
  - **비동기 처리** : `suspend`, `viewModelScope.launch`
  - **데이터 스트림 관리** : `combine`, `collect`, `stateIn`
- **Coil**
  - 이미지 로딩
- **Room**
  - 로컬 DB

## 📂 프로젝트 구조
com.example.book_paging
* app  
     - BookApplication  
     - MainActivity  
* api  
     - ApiService  
     - di/  
          - ApiModule  
          - NetworkModule  
     - dto/  
          - BookDto  
          - BookResponse  
          - Meta  
* data/  
     - datasource/  
          - BookDataSource  
          - LikeDataSource  
     - di/
          - RepositoryModule
     - mapper
     - repository/
          - BookRepositoryImpl
          - LikeRepositoryImpl
* domain/  
     - model/
          - BookModel
     - repository/
          - BookRepository
          - LikeRepository
     - usecase/
          - BookUseCase
          - LikeUseCase
          - ToggleLikeUseCase  
* presentation/  
     - Const  
     - ui/  
          - MainContent  
          - search/  
                - SearchContent  
          - like/  
                - LikeContent  
          - detail/  
                - DetailContent  
          - widget/  
                - BookItemWidget  
                - LeftImageButton  
                - RowTextWidget  
                - SearchWidget  
                - TopWidget
                - PopupWidget  
     - viewModel/  
           - BookViewModel  
* database  
     - di/  
           - DatabaseModule  
     - Converter  
     - LikeDao 
     - LikeDb  
     - LikeEntity  


## 👩🏻‍💻 주요 구현 포인트

- **Clean Architecture**
  - `presentation` / `domain` / `data` / `api` / `database` 모듈화
  - `ViewModel` → `UseCase` → `Repository` → `DataSource` 흐름
  - `Hilt`를 이용한 의존성 주입(DI) 관리
  - `BookRepositoryImpl`에서 서버에서 데이터 가져와 데이터와 관련된 페이징 로직 담당
  - `BookUseCase`는 `BookRepositoryImpl`에서에서 제공한 데이터를 결합 combine 사용해 데이터 가공
 

- **상태 관리**
  - UI에 `StateFlow`와 `collectAsState()` 사용해 상태 반영


- **Enum 관리**
  - UI 제어용 Enum 정의
      - `TabType` : 탭 (검색 / 즐겨찾기)
      - `SearchSortType`, `LikeSortType` : 정렬
      - `LikeFilterType` : 가격 필터 (단위포함)
      - `ButtonType` : 팝업 선택 시 정렬/필터 구분
  - Enum 값으로 팝업 리스트 및 정렬/필터 UI 동작을 제어
 
    
- **검색 화면 (SearchContent)**
  - 페이징 기능
  - 검색어 입력 후 이동 클릭 시 ViewModel의 `searchQuery` 업데이트하여 자동으로 데이터 로드
  - `scrollToTop Flow` 구독하고 있어 이벤트 처리 시 스크롤 최상단 이동
  - `SearchSortType`을 통해 정렬 팝업(PopupWidget) 리스트 노출 및 변경 가능


- **즐겨찾기 화면 (LikeContent)**
  - 내부 DB 데이터 리스트를 combine으로 가공 : 정렬, 필터(금액), 검색어
  - 정렬/필터 버튼 클릭시 팝업(PopupWidget) 노출, 리스트는 `LikeSortType`, `LikeFilterType` 를 통해 노출 및 데이터 변경 가능
  - 즐겨찾기 토글 시 DB 반영 및 UI 자동 갱신


- **상세 화면 (DetailContent)**
  - bookId를 `NavController`를 이용해 전달 및 화면 노출
  - ViewModel의 `getBookFlow(bookId)`로 단일 `BookModel Flow` 이용해 데이터 노출
  - `DisposableEffect`로 화면 종료 시에 추가/삭제 로직 적용하여 DB업데이트 최소화
