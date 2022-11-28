DROP TABLE SESSIONS CASCADE;
DROP table SANDWICHSHOPS CASCADE;
DROP TABLE SANDWICHES CASCADE;
DROP TABLE PERSONS CASCADE;
DROP TABLE ORDERS CASCADE;
DROP TABLE CATEGORIES CASCADE;
DROP TABLE SSOPTIONS CASCADE;
DROP TABLE SSBREADTYPES CASCADE;
DROP TABLE ORDEROPTIONS CASCADE;

drop sequence orders_oid_seq;
drop sequence persons_pid_seq;
drop sequence sandwich_sandid_seq;
drop sequence sandwichshops_sandshopid_seq;
drop sequence sessions_sid_seq;
drop sequence categories_cid_seq;

create sequence orders_oid_seq start with 1;
create sequence persons_pid_seq start with 1;
create sequence sandwich_sandid_seq start with 1;
create sequence sandwichshops_sandshopid_seq start with 1;
create sequence sessions_sid_seq start with 1;
create sequence categories_cid_seq start with 1;


CREATE TABLE PERSONS
(PID         INT  primary key default nextval('persons_pid_seq'),
 PLNAME      VARCHAR(40) NOT NULL,
 PFNAME      VARCHAR(15) NOT NULL,
 KIND        CHAR(1) NOT NULL,
 P_SID       INT
);

CREATE TABLE SESSIONS
(SID            INT  primary key default nextval('sessions_sid_seq'),
 SSTARTDATE     DATE NOT NULL,
 SENDDATE       DATE NOT NULL,
 SINS_PID       INT,
 S_COURSE       VARCHAR NOT NULL
);

CREATE TABLE SANDWICHSHOPS
(
    SANDSHOPID     INT primary key default nextval('sandwichshops_sandshopid_seq'),
    SHOPNAME       VARCHAR NOT NULL
);

--categories
CREATE TABLE CATEGORIES
(
    CID            INT primary key default nextval('categories_cid_seq'),
    CTITLE         VARCHAR NOT NULL
);

CREATE TABLE SANDWICHES
(SANDID             INT primary key  default nextval('sandwich_sandid_seq'),
 SANDNAME           VARCHAR(20) NOT NULL,
 PRICE              NUMERIC(4,2),
 DESCRIPTION        TEXT,
 SAND_CATID         INT NOT NULL,
 SAND_SANDSHOPID    INT NOT NULL,
 CONSTRAINT FK_SANDSANDWICHSHOP FOREIGN KEY (SAND_SANDSHOPID) REFERENCES SANDWICHSHOPS,
 CONSTRAINT FK_CATEGORY FOREIGN KEY (SAND_CATID) REFERENCES CATEGORIES
);


CREATE TABLE ORDERS
(
 OID            INT primary key default nextval('orders_oid_seq'),
 O_SANDID       INT,
 OBREAD         VARCHAR,
 OREMARK        VARCHAR,
 OSTATUS        VARCHAR NOT NULL,
 OAMOUNT        INT,
 OPRICE         NUMERIC(4,2),
 ODATE          DATE NOT NULL,
 O_SHOP         INT NOT NULL,
 CONSTRAINT FK_ORDERSANDWHICHSHOP FOREIGN KEY (O_SHOP) REFERENCES SANDWICHSHOPS,
 CONSTRAINT FK_ORDERSANDWICH FOREIGN KEY (O_SANDID) REFERENCES SANDWICHES
);

CREATE TABLE SSOPTIONS
(
 SS_SANDSHOPID          INT NOT NULL,
 OPTION                 VARCHAR NOT NULL,
 CONSTRAINT FK_SSOPTIONSANDWICHSHOP FOREIGN KEY(SS_SANDSHOPID) REFERENCES SANDWICHSHOPS
);

CREATE TABLE SSBREADTYPES
(
 SS_SANDSHOPID          INT NOT NULL,
 BREAD                  VARCHAR NOT NULL,
 CONSTRAINT FK_SSBREADSANDWICHSHOP FOREIGN KEY (SS_SANDSHOPID) REFERENCES SANDWICHSHOPS
);

CREATE TABLE ORDEROPTIONS
(
 OO_OID         INT NOT NULL,
 OPTION         VARCHAR NOT NULL,
 CONSTRAINT FK_ORDER FOREIGN KEY (OO_OID) REFERENCES ORDERS
);

ALTER TABLE PERSONS ADD CONSTRAINT FK_PSESSION FOREIGN KEY(P_SID) REFERENCES SESSIONS;
ALTER TABLE SESSIONS ADD CONSTRAINT FK_INSTRUCTOR FOREIGN KEY (SINS_PID) REFERENCES PERSONS;

INSERT INTO PERSONS(plname, pfname, kind) VALUES ('Jana', 'Heitkeper', 's');
INSERT INTO PERSONS(plname, pfname, kind) VALUES ('Marcel', 'van Hassel', 's');
INSERT INTO PERSONS(plname, pfname, kind) VALUES ('Sandy', 'Schillebeeckx', 'i');
INSERT INTO PERSONS(plname, pfname, kind) VALUES ('Peter', 'idk', 'i');
INSERT INTO PERSONS(plname, pfname, kind) VALUES ('Emily', 'Admin', 'a');

INSERT INTO SESSIONS(sstartdate, senddate, s_course) VALUES ('2022-09-01', '2022-12-31', 'Java programming');
INSERT INTO SESSIONS(sstartdate, senddate, s_course) VALUES ('2022-09-05', '2023-01-31', 'SQL fundamentals');

-- set instructors
UPDATE PERSONS SET p_sid = 1 WHERE pid = 3;
UPDATE PERSONS SET p_sid = 2 WHERE pid = 4;

-- set students
UPDATE PERSONS SET p_sid = 1 WHERE pid = 1;
UPDATE PERSONS SET p_sid = 2 WHERE pid = 2;

-- make sandwichshop
INSERT INTO SANDWICHSHOPS(shopname) VALUES ('TestShop');

-- configure shop
INSERT INTO SSOPTIONS VALUES (1, 'Rauwkost');
INSERT INTO SSOPTIONS VALUES (1, 'Zonder boter');

INSERT INTO SSBREADTYPES VALUES (1, 'grijs');
INSERT INTO SSBREADTYPES VALUES (1, 'wit');

-- make sandwich
INSERT INTO CATEGORIES(ctitle) VALUES ('Veggie');

INSERT INTO SANDWICHES(sandname, price, sand_catid, sand_sandshopid)
VALUES ('veggie test', 3.5, 1, 1);
