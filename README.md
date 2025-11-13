# 퀵배송

## 프로젝트 소개

- 개발기간 : 25.10.31 ~ 25.11.13
- 개발인원 : 5명
- 프로젝트 목적 : 팀 협업을 통해 실무적인 MSA 구축 및 운영에 대한 역량을 강화하고 학습하는 것을 목표로 Spring Cloud를 활용한 MSA 기반의 물류 배송 시스템을 개발하였습니다.

## 개발환경

| 분류 | 상세 |
|------|-------|
| IDE | IntelliJ |
| Language | Java 17 |
| Framework | Spring Boot 3.5.7 |
| MSA | Spring Cloud Eureka Server / Client,<br>Spring Cloud Gateway,<br>Spring Cloud OpenFeign |
| Repository | (Local) H2 In-memory,<br>(Prod) PostgreSQL |
| Build Tool | Gradle |
| Infra | Docker |

## 프로젝트 구조

- 아키텍처 : 모노레포
- 빌드 시스템 : Gradle 멀티 모듈
- 공통 모듈 : `common`
- 서비스모듈
    - auth-service
    - order-service
    - item-service
    - delivery-service
    - company-service
    - hub-service
    - notification-service

## 팀원 소개

- 태성원(팀장)
    - 업체
    - 알림
- 이승언(테크리더)
    - 배달 담당자 API 개발
    - 배달 API 개발
    - 배달경로 API 개발
- 박수현
    - 상품 서비스 및 API 개발
    - 주문 서비스 및 API 개발
- 서지희
    - 공통 모듈 개발
    - 인증 / 인가 API 개발
    - 회원 관리 서비스 및 API 개발
- 이호준
    - 허브 서비스 및 API 개발
    - 허브 경로 서비스 및 API 개발
    - 테이블 명세서 및 erd 작성
  
## 프로젝트 실행 방법

1. 환경변수 설정
    ```
    POSTGRES_ROOT_PASSWORD=admin
    POSTGRES_APP_USER=quickbaesong
    POSTGRES_APP_PASSWORD=quickbaesong
    POSTGRES_DB_NAME=quickbaesong_main
    ```
2. Docker Compose
      ```yml
        version: '3.8'
        
        services:
          postgres_db:
            image: postgres:16
            container_name: msa-postgres-db
            restart: always
            environment:
              POSTGRES_ROOT_PASSWORD: ${POSTGRES_ROOT_PASSWORD}
              POSTGRES_DB: ${POSTGRES_DB_NAME}
              POSTGRES_USER: ${POSTGRES_APP_USER}
              POSTGRES_PASSWORD: ${POSTGRES_APP_PASSWORD}
            ports:
              - "5432:5432"
            volumes:
              - postgres-data:/var/lib/postgresql/data
              - ./init-db/init-schemas.sh:/docker-entrypoint-initdb.d/init-schemas.sh
            healthcheck:
              test: ["CMD-SHELL", "pg_isready -U ${POSTGRES_APP_USER} -d ${POSTGRES_DB_NAME}"]
              interval: 10s
              timeout: 5s
              retries: 5
        
        volumes:
          postgres-data:
      ```
3. init-db/init-schemas.sh
    ``` shall
        set -e
        
        # 환경 변수에서 기본 DB 정보 로드
        DB_NAME="$POSTGRES_DB"
        DB_USER="$POSTGRES_USER"
        
        echo "Initializing PostgreSQL schemas for QuickBaesong MSA..."
        
        # PostgreSQL에 접속하여 스키마 생성 및 권한 부여
        # ${DB_NAME} (quickbaesong_main) 데이터베이스에 접속하여 SQL 실행
        psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname "$DB_NAME" <<-EOSQL
            -- Service용 스키마 생성 및 권한 부여
            CREATE SCHEMA IF NOT EXISTS order_schema AUTHORIZATION $DB_USER;
            CREATE SCHEMA IF NOT EXISTS user_schema AUTHORIZATION $DB_USER;
            CREATE SCHEMA IF NOT EXISTS item_schema AUTHORIZATION $DB_USER;
            CREATE SCHEMA IF NOT EXISTS company_schema AUTHORIZATION $DB_USER;
            CREATE SCHEMA IF NOT EXISTS hub_schema AUTHORIZATION $DB_USER;
            CREATE SCHEMA IF NOT EXISTS notification_schema AUTHORIZATION $DB_USER;
            CREATE SCHEMA IF NOT EXISTS delivery_schema AUTHORIZATION $DB_USER;
            -- 생성된 스키마에 대한 권한을 DB 사용자에게 부여
            GRANT ALL ON SCHEMA order_schema TO $DB_USER;
            GRANT ALL ON SCHEMA user_schema TO $DB_USER;
            GRANT ALL ON SCHEMA item_schema TO $DB_USER;
            GRANT ALL ON SCHEMA company_schema TO $DB_USER;
            GRANT ALL ON SCHEMA hub_schema TO $DB_USER;
            GRANT ALL ON SCHEMA notification_schema TO $DB_USER;
            GRANT ALL ON SCHEMA delivery_schema TO $DB_USER;
            SELECT 'All schemas created and permissions granted successfully' AS status;
        EOSQL
        
        echo "Initialization complete."
    
    ```
4. ```$ ./gradlew bootRun```

## ERD
<img width="2430" height="1552" alt="image" src="https://github.com/user-attachments/assets/7ac4f303-564c-4487-86ca-6e94840968ea" />


