## 일정관리 백엔드

1. 자신이 개발한 앱에 대한 설명
   - 인증 및 권한 관리: Spring Security와 JWT를 사용해 로그인 및 회원가입 기능을 구현했습니다. 테스트 편의성을 위해 Access Token의 만료 기한을 24시간으로 설정했습니다. / 그렇기에 accessToken을 재발급 받는 로직을 추가하지 않았습니다.
   - 사용자 상태 관리: 회원가입 시 사용자는 Pending 상태로 등록됩니다. 사용자가 로그인 후 마이페이지에서 이름을 업데이트하면 자동으로 Active 상태로 변경되어 앱의 모든 기능을 사용할 수 있습니다.
   - 그룹 및 일정 공유: 모든 사용자는 개인 일정 관리를 위한 기본 그룹을 자동으로 갖게 됩니다. 다른 사용자와 일정을 공유하려면 새로운 그룹을 생성하고, 회원가입된 사용자를 초대해야 합니다. 초대받은 사용자가 초대를 수락해야만 그룹의 구성원이 될 수 있습니다.
   - 다양한 일정 설정: 일정은 특정 날짜에만 종속되는 일간 일정 (예: 회의)과 시작/종료 시간을 포함하는 시간 일정 (예: 10:00 ~ 12:00 미팅)을 모두 설정할 수 있습니다. 이미 생성된 일정은 다른 그룹으로 쉽게 옮길 수 있습니다.
2. 소스 빌드 및 실행 방법 메뉴얼
   - Application을 사용하기 위해서는 mysql localhost에 SCHEDULE이라는 데이터베이스가 생성되어야 합니다.
   - src/main/resources/application.yml 의 spring:datasource:username 과 password에 로컬 계정정보를 입력해주시길 바랍니다.
   - database는 아래의 쿼리를 사용합니다.
    ```mysql
    CREATE DATABASE `SCHEDULE` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  DEFAULT ENCRYPTION='N';
    CREATE TABLE `USER` (
        `ID` bigint NOT NULL AUTO_INCREMENT,
        `EMAIL` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '이메일',
        `NAME` varchar(10) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '사용자 명',
        `PASSWORD` varchar(100) COLLATE utf8mb4_general_ci NOT NULL COMMENT '비밀번호',
        `STATUS` enum('ACTIVE','PENDING','RESIGN') COLLATE utf8mb4_general_ci NOT NULL COMMENT '회원 상태 (추가 정보 미 입력시 = Pending, 탈퇴 시 = Resign, 정상 = ACTIVE)',
        `CREATED_AT` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
        `UPDATED_AT` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
        PRIMARY KEY (`ID`),
        UNIQUE KEY `USER_EMAIL_UQ` (`EMAIL`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
       
    CREATE TABLE `GROUP` (
        `ID` bigint NOT NULL AUTO_INCREMENT,
        `NAME` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '그룹명',
        `OWNER` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
        `PRIVATE` tinyint(1) NOT NULL COMMENT '비공개 여부, true = 공개 그룹, false = 비공개 그룹',
        `CREATED_AT` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
        `UPDATED_AT` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
        PRIMARY KEY (`ID`),
        KEY `GROUP_USER_FK` (`OWNER`),
        CONSTRAINT `GROUP_USER_FK` FOREIGN KEY (`OWNER`) REFERENCES `USER` (`EMAIL`) ON DELETE CASCADE ON UPDATE CASCADE
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
        
    CREATE TABLE `GROUP_USER` (
        `ID` bigint NOT NULL AUTO_INCREMENT,
        `GROUP_ID` bigint NOT NULL COMMENT '그룹아이디',
        `USER_EMAIL` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '사용자아이디',
        `ACCEPTED` tinyint(1) NOT NULL COMMENT '그룹 초대 수락 여부',
        `CREATED_AT` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
        `UPDATED_AT` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
        PRIMARY KEY (`ID`),
        KEY `GROUP_USER_GROUP_FK` (`GROUP_ID`),
        KEY `GROUP_USER_USER_FK` (`USER_EMAIL`),
        CONSTRAINT `GROUP_USER_GROUP_FK` FOREIGN KEY (`GROUP_ID`) REFERENCES `GROUP` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE,
        CONSTRAINT `GROUP_USER_USER_FK` FOREIGN KEY (`USER_EMAIL`) REFERENCES `USER` (`EMAIL`) ON DELETE CASCADE ON UPDATE CASCADE
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
        
    CREATE TABLE `SCHEDULE` (
        `ID` bigint NOT NULL AUTO_INCREMENT,
        `GROUP_ID` bigint NOT NULL,
        `TITLE` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
        `START_DATE` date NOT NULL COMMENT '일정 시작 일자',
        `START_TIME` time DEFAULT NULL COMMENT '일정 시작 시각 | NULL = 하루 종일 일정',
        `END_DATE` date NOT NULL COMMENT '일정 종료 일자',
        `END_TIME` time DEFAULT NULL COMMENT '일정 종료 시각 | NULL = 하루 종일 일정',
        `MEMO` text COLLATE utf8mb4_general_ci COMMENT '일정에 대한 추가 메모',
        `WRITER` varchar(30) COLLATE utf8mb4_general_ci NOT NULL COMMENT '일정에 대한 추가 메모',
        `CREATED_AT` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
        `UPDATED_AT` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
        PRIMARY KEY (`ID`),
        KEY `SCHEDULE_GROUP_FK` (`GROUP_ID`),
        CONSTRAINT `SCHEDULE_GROUP_FK` FOREIGN KEY (`GROUP_ID`) REFERENCES `GROUP` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='일정';
        
    CREATE TABLE `TOKEN` (
        `ID` bigint NOT NULL AUTO_INCREMENT,
        `EMAIL` varchar(30) COLLATE utf8mb4_general_ci NOT NULL,
        `ROLE` enum('ROLE_USER') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
        `REFRESH_TOKEN` text COLLATE utf8mb4_general_ci NOT NULL,
        `REFRESH_EXPIRED_AT` datetime NOT NULL,
        `CREATED_AT` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
        `UPDATED_AT` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
        PRIMARY KEY (`ID`),
        KEY `TOKEN_USER_FK` (`EMAIL`),
        CONSTRAINT `TOKEN_USER_FK` FOREIGN KEY (`EMAIL`) REFERENCES `USER` (`EMAIL`) ON DELETE CASCADE ON UPDATE CASCADE
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='REFRESH TOKEN';

    ```
    - 아래의 Insert 문을 사용하여, 기본 데이터를 삽입합니다.
    ```mysql
   INSERT INTO SCHEDULE.`USER` (ID, EMAIL, NAME, PASSWORD, STATUS, CREATED_AT, UPDATED_AT) VALUES
   (1, 'test1@test.com', '테스터1', '{bcrypt}$2a$10$ASuBzKH/HDhQEq19YZAlxOhVhcegTxpEcMFHeie8DpGFIVdE08FQe', 'ACTIVE', '2025-08-22 21:55:27', '2025-08-22 22:56:01'),
   (2, 'test2@test.com', '테스터2', '{bcrypt}$2a$10$u/z8rDmmFiAglCZTH2b.O.ELtbtQOkxVR8mOzvGbOOZHoIapai27G', 'ACTIVE', '2025-08-22 21:55:32', '2025-08-22 22:56:01');
    
   INSERT INTO SCHEDULE.`GROUP` (ID, NAME, OWNER, PRIVATE, CREATED_AT, UPDATED_AT) VALUES
   (1, '나의 일정', 'test1@test.com', 1, '2025-08-22 21:55:27', '2025-08-22 22:55:27'),
   (2, '나의 일정', 'test2@test.com', 1, '2025-08-22 21:55:32', '2025-08-22 22:55:32'),
   (3, '가족여행', 'test1@test.com', 0, '2025-08-22 21:56:38', '2025-08-22 22:56:38');
   
   INSERT INTO SCHEDULE.GROUP_USER(ID, GROUP_ID, USER_EMAIL, ACCEPTED, CREATED_AT, UPDATED_AT) VALUES
   (1, 1, 'test1@test.com', 1, '2025-08-22 21:55:27', '2025-08-22 22:55:27'),
   (2, 2, 'test2@test.com', 1, '2025-08-22 21:55:32', '2025-08-22 22:55:32'),
   (3, 3, 'test1@test.com', 1, '2025-08-22 21:56:38', '2025-08-22 22:56:38'),
   (4, 3, 'test2@test.com', 0, '2025-08-22 21:57:30', '2025-08-22 22:57:30');
   
   INSERT INTO SCHEDULE.SCHEDULE(ID, GROUP_ID, TITLE, START_DATE, START_TIME, END_DATE, END_TIME, MEMO, WRITER, CREATED_AT, UPDATED_AT) VALUES
   (1, 1, '점심시간', '2025-08-01', '12:00:00', '2025-08-01', '14:00:00', '제육볶음먹자', 'test1@test.com', '2025-08-22 21:57:48', '2025-08-22 22:57:48'),
   (2, 1, '가족여행', '2025-08-15', NULL, '2025-08-18', NULL, '부산여행', 'test1@test.com', '2025-08-23 00:43:28', '2025-08-23 00:43:28'),
   (3, 3, '부산 출발', '2025-08-15', '14:00:00', '2025-08-15', '14:00:00', 'KTX 서울역', 'test1@test.com', '2025-08-23 00:44:25', '2025-08-23 00:44:25'),
   (4, 2, '낮잠자기', '2025-08-01', '15:00:00', '2025-08-01', '16:00:00', '집', 'test2@test.com', '2025-08-23 00:57:43', '2025-08-23 00:57:43');
    ```
    - 기본적으로 메인 사용자
      ```json
      {
          "id" : "test1@test.com",
          "pw" : "Qwer1234!"
      }
      ```
      서브 사용자
      ```json
      {
          "id" : "test2@test.com",
          "pw" : "Qwer1234!"
      }
      ```
      로 구분되어있습니다.
3. 주요 라이브러리 및 기술 스택
   - Spring Security & JWT: 사용자 인증/인가를 분리 처리하고, Access Token에서 필요한 정보를 추출하기 위해 사용했습니다.
   - QueryDSL: 복잡한 JPA 쿼리를 더 간결하고 타입 안전하게 작성하기 위해 채택했습니다. 이를 통해 필요한 데이터만 효율적으로 조회할 수 있습니다.
   - Mockito-Kotlin: Kotlin의 장점을 활용해 테스트 코드를 간결하게 작성하기 위해 사용했습니다.
4. api 명세서
   - http://localhost:8008/gs/swagger-ui/index.html
5. DB ERD
   - https://www.erdcloud.com/d/D7pvqffi8yDpA6h3W
