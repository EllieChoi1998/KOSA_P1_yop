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
```
## 2. 테이블, 시퀀스, 트리거, 프로시저 및 기본 데이터 생성과 삽입

pizza_admin@xe 계정을 사용하여 아래의 SQL 코드를 입력해 주세요.

```sql
-- Drop sequences if they exist
BEGIN
   EXECUTE IMMEDIATE 'DROP SEQUENCE orders_seq';
EXCEPTION
   WHEN OTHERS THEN
      IF SQLCODE = -2289 THEN NULL; ELSE RAISE; END IF;
END;
/

BEGIN
   EXECUTE IMMEDIATE 'DROP SEQUENCE pizza_seq';
EXCEPTION
   WHEN OTHERS THEN
      IF SQLCODE = -2289 THEN NULL; ELSE RAISE; END IF;
END;
/

BEGIN
   EXECUTE IMMEDIATE 'DROP SEQUENCE ingredient_seq';
EXCEPTION
   WHEN OTHERS THEN
      IF SQLCODE = -2289 THEN NULL; ELSE RAISE; END IF;
END;
/

BEGIN
   EXECUTE IMMEDIATE 'DROP SEQUENCE options_seq';
EXCEPTION
   WHEN OTHERS THEN
      IF SQLCODE = -2289 THEN NULL; ELSE RAISE; END IF;
END;
/
COMMIT;

-- Drop triggers if they exist
BEGIN
   EXECUTE IMMEDIATE 'DROP TRIGGER trg_orders_id';
EXCEPTION
   WHEN OTHERS THEN
      IF SQLCODE = -4080 THEN NULL; ELSE RAISE; END IF;
END;
/

BEGIN
   EXECUTE IMMEDIATE 'DROP TRIGGER trg_pizza_id';
EXCEPTION
   WHEN OTHERS THEN
      IF SQLCODE = -4080 THEN NULL; ELSE RAISE; END IF;
END;
/

BEGIN
   EXECUTE IMMEDIATE 'DROP TRIGGER trg_ingredient_id';
EXCEPTION
   WHEN OTHERS THEN
      IF SQLCODE = -4080 THEN NULL; ELSE RAISE; END IF;
END;
/

BEGIN
   EXECUTE IMMEDIATE 'DROP TRIGGER trg_optionss_id';
EXCEPTION
   WHEN OTHERS THEN
      IF SQLCODE = -4080 THEN NULL; ELSE RAISE; END IF;
END;
/
COMMIT;


-- Drop tables if they exist
BEGIN
   EXECUTE IMMEDIATE 'DROP TABLE orders_pizza';
EXCEPTION
   WHEN OTHERS THEN
      IF SQLCODE = -942 THEN NULL; ELSE RAISE; END IF;
END;
/
BEGIN
   EXECUTE IMMEDIATE 'DROP TABLE orders_options';
EXCEPTION
   WHEN OTHERS THEN
      IF SQLCODE = -942 THEN NULL; ELSE RAISE; END IF;
END;
/

BEGIN
   EXECUTE IMMEDIATE 'DROP TABLE ingredient_item';
EXCEPTION
   WHEN OTHERS THEN
      IF SQLCODE = -942 THEN NULL; ELSE RAISE; END IF;
END;
/
BEGIN
   EXECUTE IMMEDIATE 'DROP TABLE ingredient';
EXCEPTION
   WHEN OTHERS THEN
      IF SQLCODE = -942 THEN NULL; ELSE RAISE; END IF;
END;
/
BEGIN
   EXECUTE IMMEDIATE 'DROP TABLE pizza';
EXCEPTION
   WHEN OTHERS THEN
      IF SQLCODE = -942 THEN NULL; ELSE RAISE; END IF;
END;
/
BEGIN
   EXECUTE IMMEDIATE 'DROP TABLE orders';
EXCEPTION
   WHEN OTHERS THEN
      IF SQLCODE = -942 THEN NULL; ELSE RAISE; END IF;
END;
/
BEGIN
   EXECUTE IMMEDIATE 'DROP TABLE admin';
EXCEPTION
   WHEN OTHERS THEN
      IF SQLCODE = -942 THEN NULL; ELSE RAISE; END IF;
END;
/
BEGIN
   EXECUTE IMMEDIATE 'DROP TABLE customer';
EXCEPTION
   WHEN OTHERS THEN
      IF SQLCODE = -942 THEN NULL; ELSE RAISE; END IF;
END;
/
BEGIN
   EXECUTE IMMEDIATE 'DROP TABLE ingredient_type';
EXCEPTION
   WHEN OTHERS THEN
      IF SQLCODE = -942 THEN NULL; ELSE RAISE; END IF;
END;
/
BEGIN
   EXECUTE IMMEDIATE 'DROP TABLE options';
EXCEPTION
   WHEN OTHERS THEN
      IF SQLCODE = -942 THEN NULL; ELSE RAISE; END IF;
END;
/
BEGIN
   EXECUTE IMMEDIATE 'DROP TABLE options_type';
EXCEPTION
   WHEN OTHERS THEN
      IF SQLCODE = -942 THEN NULL; ELSE RAISE; END IF;
END;
/

-- Create sequences
CREATE SEQUENCE orders_seq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE pizza_seq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE ingredient_seq START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE options_seq START WITH 1 INCREMENT BY 1;
COMMIT;

-- Create tables
CREATE TABLE customer (
    id VARCHAR2(30) PRIMARY KEY,
    pwd VARCHAR2(30),
    name VARCHAR2(30),
    credits FLOAT DEFAULT 0.00
);
COMMIT;

CREATE TABLE admin (
    id VARCHAR2(30) PRIMARY KEY,
    pwd VARCHAR2(30),
    name VARCHAR2(30)
);
COMMIT;

CREATE TABLE orders (
    id NUMBER PRIMARY KEY,
    customer_id VARCHAR2(30),
    price NUMBER,
    status NUMBER CHECK (status IN (0, 1, 2, 3)),
    FOREIGN KEY (customer_id) REFERENCES customer(id)
);
COMMIT;

CREATE TABLE pizza (
    id NUMBER PRIMARY KEY,
    name VARCHAR2(30),
    pizza_size VARCHAR2(1),
    weight NUMBER,
    calories NUMBER,
    proteins NUMBER,
    fats NUMBER,
    salts NUMBER,
    sugars NUMBER,
    price NUMBER,
    customer_id VARCHAR2(30),
    FOREIGN KEY (customer_id) REFERENCES customer(id)
);
COMMIT;

CREATE TABLE ingredient_type (
    id NUMBER PRIMARY KEY,
    name VARCHAR2(30),
    mandatory VARCHAR2(1) CHECK (mandatory IN ('T', 'F'))
);
COMMIT;

CREATE TABLE ingredient (
    id NUMBER PRIMARY KEY,
    name VARCHAR2(30),
    ingredient_type_id NUMBER,
    weight NUMBER,
    calories NUMBER,
    proteins NUMBER,
    fats NUMBER,
    salts NUMBER,
    sugars NUMBER,
    price NUMBER,
    FOREIGN KEY (ingredient_type_id) REFERENCES ingredient_type(id)
);
COMMIT;

CREATE TABLE options_type (
    id NUMBER PRIMARY KEY,
    name VARCHAR2(30)
);
COMMIT;

CREATE TABLE options (
    id NUMBER PRIMARY KEY,
    name VARCHAR2(30),
    options_type_id NUMBER,
    weight NUMBER,
    calories NUMBER,
    proteins NUMBER,
    fats NUMBER,
    salts NUMBER,
    sugars NUMBER,
    price NUMBER,  
    FOREIGN KEY (options_type_id) REFERENCES options_type(id)
);
COMMIT;

CREATE TABLE ingredient_item (
    pizza_id NUMBER,
    ing_id NUMBER,
    FOREIGN KEY (pizza_id) REFERENCES pizza(id),
    FOREIGN KEY (ing_id) REFERENCES ingredient(id)
);
COMMIT;

CREATE TABLE orders_pizza (
    orders_id NUMBER,
    pizza_id NUMBER,
    quantity NUMBER,
    PRIMARY KEY(orders_id, pizza_id, quantity),
    FOREIGN KEY (orders_id) REFERENCES orders(id),
    FOREIGN KEY (pizza_id) REFERENCES pizza(id)
);

COMMIT;

CREATE TABLE orders_options (
    orders_id NUMBER,
    options_id NUMBER,
    quantity NUMBER,
    PRIMARY KEY(orders_id, options_id, quantity),
    FOREIGN KEY (orders_id) REFERENCES orders(id),
    FOREIGN KEY (options_id) REFERENCES options(id)
);
COMMIT;

-- Create triggers to auto-increment primary keys
CREATE OR REPLACE TRIGGER trg_orders_id 
BEFORE INSERT ON orders
FOR EACH ROW
BEGIN
    SELECT orders_seq.NEXTVAL INTO :NEW.id FROM dual;
END;
/
COMMIT;

CREATE OR REPLACE TRIGGER trg_pizza_id 
BEFORE INSERT ON pizza
FOR EACH ROW
BEGIN
    SELECT pizza_seq.NEXTVAL INTO :NEW.id FROM dual;
END;
/
COMMIT;

CREATE OR REPLACE TRIGGER trg_ingredient_id 
BEFORE INSERT ON ingredient
FOR EACH ROW
BEGIN
    SELECT ingredient_seq.NEXTVAL INTO :NEW.id FROM dual;
END;
/
COMMIT;

CREATE OR REPLACE TRIGGER trg_options_id 
BEFORE INSERT ON options
FOR EACH ROW
BEGIN
    SELECT options_seq.NEXTVAL INTO :NEW.id FROM dual;
END;
/
COMMIT;

-- Insert default user
INSERT INTO admin (id, pwd, name) VALUES ('admin', 'admin', 'Default Admin');
COMMIT;

-- Insert default standard pizzas
INSERT INTO pizza (name, pizza_size, weight, calories, proteins, fats, salts, sugars, price) VALUES ('고구마피자', 'L', 1070, 2654, 112, 49,3721, 61, 30000);
INSERT INTO pizza (name, pizza_size, weight, calories, proteins, fats, salts, sugars, price) VALUES ('고구마피자', 'M', 658, 1592, 59, 26, 2469, 38, 23000);
INSERT INTO pizza (name, pizza_size, weight, calories, proteins, fats, salts, sugars, price) VALUES ('포테이토피자', 'L', 1142, 2532, 104, 37, 3938, 24, 27000);
INSERT INTO pizza (name, pizza_size, weight, calories, proteins, fats, salts, sugars, price) VALUES ('포테이토피자', 'M', 658, 1505, 63, 23, 2144, 16, 20000);
INSERT INTO pizza (name, pizza_size, weight, calories, proteins, fats, salts, sugars, price) VALUES ('페퍼로니피자', 'L', 847, 2289, 108, 44, 4347, 21, 25000);
INSERT INTO pizza (name, pizza_size, weight, calories, proteins, fats, salts, sugars, price) VALUES ('페퍼로니피자', 'M', 500, 1375, 63, 25, 2353, 16, 18000);
INSERT INTO pizza (name, pizza_size, weight, calories, proteins, fats, salts, sugars, price) VALUES ('치즈피자', 'L', 766, 1992, 102, 29, 3623, 23, 23000);
INSERT INTO pizza (name, pizza_size, weight, calories, proteins, fats, salts, sugars, price) VALUES ('치즈피자', 'M', 457, 1192, 53, 18, 2055, 10, 16000);
INSERT INTO pizza (name, pizza_size, weight, calories, proteins, fats, salts, sugars, price) VALUES ('슈프림피자', 'L', 1060, 2245, 103, 41, 4393, 38, 27000);
INSERT INTO pizza (name, pizza_size, weight, calories, proteins, fats, salts, sugars, price) VALUES ('슈프림피자', 'M', 675, 1535, 72, 27, 3116, 16, 20000);
INSERT INTO pizza (name, pizza_size, weight, calories, proteins, fats, salts, sugars, price) VALUES ('불고기피자', 'L', 992, 2313, 114, 37, 3508, 39, 29000);
INSERT INTO pizza (name, pizza_size, weight, calories, proteins, fats, salts, sugars, price) VALUES ('불고기피자', 'M', 508, 1319, 60, 18, 2232, 22, 22000);
INSERT INTO pizza (name, pizza_size, weight, calories, proteins, fats, salts, sugars, price) VALUES ('스테이크피자', 'L', 1071, 2515, 121, 40, 5313, 31, 28000);
INSERT INTO pizza (name, pizza_size, weight, calories, proteins, fats, salts, sugars, price) VALUES ('스테이크피자', 'M', 714, 1589, 75, 28, 3751, 24, 36000);
INSERT INTO pizza (name, pizza_size, weight, calories, proteins, fats, salts, sugars, price) VALUES ('아보카도새우피자', 'L', 1030, 2388, 112, 39, 4851, 21, 27000);
INSERT INTO pizza (name, pizza_size, weight, calories, proteins, fats, salts, sugars, price) VALUES ('아보카도새우피자', 'M', 634, 1432, 63, 24, 2789, 16, 20000);
INSERT INTO pizza (name, pizza_size, weight, calories, proteins, fats, salts, sugars, price) VALUES ('베이컨체더치즈피자', 'L', 902, 2478, 115, 52, 4202, 24, 27000);
INSERT INTO pizza (name, pizza_size, weight, calories, proteins, fats, salts, sugars, price) VALUES ('베이컨체더치즈피자', 'M', 516, 1792, 91, 57, 3323, 19, 20000);
INSERT INTO pizza (name, pizza_size, weight, calories, proteins, fats, salts, sugars, price) VALUES ('치즈크레이프샌드피자', 'L', 1075, 2650, 102, 63, 4807, 54, 34000);
INSERT INTO pizza (name, pizza_size, weight, calories, proteins, fats, salts, sugars, price) VALUES ('치즈크레이프샌드피자', 'M', 728, 1806, 71, 44, 3293, 43, 29000);

-- insert ingredient types
INSERT INTO ingredient_type (id, name, mandatory) VALUES (1, 'dow', 'T');
INSERT INTO ingredient_type (id, name, mandatory) VALUES (2, 'edge', 'T');
INSERT INTO ingredient_type (id, name, mandatory) VALUES (3, 'sauce', 'T');
INSERT INTO ingredient_type (id, name, mandatory) VALUES (4, 'cheese', 'T');
INSERT INTO ingredient_type (id, name, mandatory) VALUES (5, 'topping', 'F');

-- insert dow types (M size = standard / L size = M * 1.5 for every attributes)
INSERT INTO ingredient (name, ingredient_type_id, weight, calories, proteins, fats, salts, sugars, price) VALUES ('오리지널도우', 1, 443, 302, 12, 6, 516, 4, 14000);
INSERT INTO ingredient (name, ingredient_type_id, weight, calories, proteins, fats, salts, sugars, price) VALUES ('씬도우', 1, 251, 352, 8, 12, 832, 4, 14000);

-- insert edge types (M size = standard / L size = M * 1.5 for every attributes)
INSERT INTO ingredient (name, ingredient_type_id, weight, calories, proteins, fats, salts, sugars, price) VALUES ('고구마엣지', 2, 581, 900, 15, 6, 500, 2, 3000);
INSERT INTO ingredient (name, ingredient_type_id, weight, calories, proteins, fats, salts, sugars, price) VALUES ('치즈엣지', 2, 581, 398, 18, 8, 666, 2, 3000);

-- insert base sauce types (M size = standard / L size = M * 1.5 for every attributes)
INSERT INTO ingredient (name, ingredient_type_id, weight, calories, proteins, fats, salts, sugars, price) VALUES ('토마토소스', 3, 70, 62, 2.1, 0.1, 582, 7.6, 0);
INSERT INTO ingredient (name, ingredient_type_id, weight, calories, proteins, fats, salts, sugars, price) VALUES ('불고기소스', 3, 30, 38, 0.5, 0, 189, 6.7, 0);

-- insert base cheese types (M size = standard / L size = M * 1.5 for every attributes)
INSERT INTO ingredient (name, ingredient_type_id, weight, calories, proteins, fats, salts, sugars, price) VALUES ('모짜렐라치즈', 4, 100, 288, 21, 13, 565, 0, 3000);
INSERT INTO ingredient (name, ingredient_type_id, weight, calories, proteins, fats, salts, sugars, price) VALUES ('체더치즈', 4, 40, 166, 9, 9, 218, 0, 1500);

-- insert topping types (M size = standard / L size = M * 1.5 for every attributes)
INSERT INTO ingredient (name, ingredient_type_id, weight, calories, proteins, fats, salts, sugars, price) VALUES ('페퍼로니', 5, 20, 76, 3, 2, 122, 0, 500);
INSERT INTO ingredient (name, ingredient_type_id, weight, calories, proteins, fats, salts, sugars, price) VALUES ('베이컨', 5, 24, 89, 3, 2, 79, 0, 1500);
INSERT INTO ingredient (name, ingredient_type_id, weight, calories, proteins, fats, salts, sugars, price) VALUES ('햄', 5, 28, 45, 4, 1, 196, 0, 500);
INSERT INTO ingredient (name, ingredient_type_id, weight, calories, proteins, fats, salts, sugars, price) VALUES ('꼬리알새우', 5, 54, 65, 7, 1, 293, 1, 4000);
INSERT INTO ingredient (name, ingredient_type_id, weight, calories, proteins, fats, salts, sugars, price) VALUES ('불고기', 5, 40, 74, 7, 2, 172, 0, 1000);
INSERT INTO ingredient (name, ingredient_type_id, weight, calories, proteins, fats, salts, sugars, price) VALUES ('양파', 5, 40, 11, 0, 0, 0, 2, 500);
INSERT INTO ingredient (name, ingredient_type_id, weight, calories, proteins, fats, salts, sugars, price) VALUES ('피망', 5, 40, 9, 0, 0, 0, 1, 500);
INSERT INTO ingredient (name, ingredient_type_id, weight, calories, proteins, fats, salts, sugars, price) VALUES ('파인애플', 5, 40, 34, 0, 0, 1, 8, 500);


-- insert options types
INSERT INTO options_type (id, name) VALUES (1, 'side');
INSERT INTO options_type (id, name) VALUES (2, 'drink');

-- insert side types
INSERT INTO options (name, options_type_id, weight, calories, proteins, fats, salts, sugars, price) VALUES ('베이컨 까르보나라', 1, 420, 929, 35, 35, 1533, 5, 9000);
INSERT INTO options (name, options_type_id, weight, calories, proteins, fats, salts, sugars, price) VALUES ('마라 로제 파스타', 1, 409, 695, 32, 17, 1604, 9, 9000);

-- insert drink types
INSERT INTO options (name, options_type_id, weight, calories, proteins, fats, salts, sugars, price) VALUES ('스프라이트 1.5L', 2, 1500, 660, 0, 0, 135, 165, 2000);
INSERT INTO options (name, options_type_id, weight, calories, proteins, fats, salts, sugars, price) VALUES ('스프라이트 500ml', 2, 500, 228, 0, 0, 47, 57, 1000);
INSERT INTO options (name, options_type_id, weight, calories, proteins, fats, salts, sugars, price) VALUES ('코카콜라 1.25L', 2, 1250, 550, 0, 0, 38, 138, 2000);
INSERT INTO options (name, options_type_id, weight, calories, proteins, fats, salts, sugars, price) VALUES ('코카콜라 500ml', 2, 500, 216, 0, 0, 15, 54, 1000);
INSERT INTO options (name, options_type_id, weight, calories, proteins, fats, salts, sugars, price) VALUES ('코카콜라 제로 1.25L', 2, 1250, 0, 0, 0, 75, 0, 2000);
INSERT INTO options (name, options_type_id, weight, calories, proteins, fats, salts, sugars, price) VALUES ('마라 로제 파스타', 2, 500, 0, 0, 0, 30, 0, 1000);

COMMIT;

-- Create Procedure for returning order_id
CREATE OR REPLACE PROCEDURE create_new_order (
    p_customer_id IN VARCHAR2,
    p_price IN NUMBER,
    p_status IN NUMBER,
    p_new_order_id OUT NUMBER
)
AS
BEGIN
    INSERT INTO orders (customer_id, price, status) VALUES (p_customer_id, p_price, p_status)
    RETURNING id INTO p_new_order_id;
    COMMIT;
END create_new_order;
/

commit;

-- Create Procedure for returing pizza_id 
CREATE OR REPLACE PROCEDURE create_new_pizza (
    p_name IN VARCHAR2,
    p_pizza_size IN VARCHAR2,
    p_weight IN NUMBER,
    p_calories IN NUMBER,
    p_proteins IN NUMBER,
    p_fats IN NUMBER,
    p_salts IN NUMBER,
    p_sugars IN NUMBER,
    p_price IN NUMBER,
    p_customer_id IN VARCHAR2,
    p_new_pizza_id OUT NUMBER
)
AS
BEGIN
    INSERT INTO pizza (name, pizza_size, weight, calories, proteins, fats, salts, sugars, price, customer_id)
    VALUES (p_name, p_pizza_size, p_weight, p_calories, p_proteins, p_fats, p_salts, p_sugars, p_price, p_customer_id)
    RETURNING id INTO p_new_pizza_id;
    COMMIT;
END create_new_pizza;
/

commit;

```
## 공지: CustomPizza 사진 삭제 안내
애플리케이션 이용에 불편을 드려 죄송합니다.

본 애플리케이션 소스파일을 다운 받은 후, 아래 경로로 이동하여 .png 파일들을 모두 삭제해 주세요:

경로: src/main/resources/yop/kosa_p1_yop/Custom_Pizzas
해당 경로에 있는 모든 .png 파일을 삭제해 주시면 됩니다.
이용에 불편을 드려 다시 한 번 사과드립니다.
