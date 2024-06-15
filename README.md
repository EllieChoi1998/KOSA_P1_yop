# KOSA_P1_yop

# Your Own Pizza Application Setup

본 애플리케이션을 실행하기 전에 Oracle DBMS (SQL*Plus)를 아래와 같이 설정해 주세요.

## 1. 사용자 생성 및 권한 부여

System@xe 계정을 사용하여 아래의 SQL 코드를 입력해 주세요.

```sql
DROP USER pizza_admin CASCADE;

-- 사용자 생성하기
CREATE USER pizza_admin
IDENTIFIED BY admin
DEFAULT TABLESPACE USERS
TEMPORARY TABLESPACE TEMP;

-- 계정 권한 부여
GRANT CONNECT TO pizza_admin;
GRANT RESOURCE TO pizza_admin;
GRANT DBA TO pizza_admin;

GRANT CREATE TRIGGER TO pizza_admin;
GRANT SELECT ON SYS.DUAL TO pizza_admin;
GRANT CREATE SEQUENCE TO pizza_admin;
GRANT CREATE TABLE TO pizza_admin;
GRANT DROP ANY TABLE TO pizza_admin;
