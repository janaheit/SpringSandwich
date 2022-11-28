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

create sequence orders_oid_seq starts with 1;
create sequence persons_pid_seq starts with 1;
create sequence sandwich_sandid_seq starts with 1;
create sequence sandwichshops_sandshopid_seq starts with 1;
create sequence sessions_sid_seq starts with 1;
create sequence categories_cid_seq start with 1;


CREATE TABLE PERSONS
(PID         INT  primary key default nextval('persons_pid_seq'),
 PLNAME      VARCHAR(40) NOT NULL,
 PFNAME      VARCHAR(15) NOT NULL,
 KIND        CHAR(1) NOT NULL,
 P_SNO       INT,
 CONSTRAINT FK_SESSION FOREIGN KEY (P_SNO) REFERENCES SESSIONS
);

CREATE TABLE SESSIONS
(SID            INT  primary key default nextval('sessions_sid_seq'),
 SSTARTDATE     DATE NOT NULL,
 SENDDATE       DATE NOT NULL,
 SINS_PID       INT,
 S_COURSE       VARCHAR NOT NULL,
 CONSTRAINT FK_INSTRUCTOR FOREIGN KEY (SINS_PNO) REFERENCES PERSONS,
);

CREATE TABLE SANDWICHES
(SANDID             INT primary key  default nextval('sandwich_sandid_seq'),
 SANDNAME           VARCHAR(20) NOT NULL,
 PRICE              NUMERIC(4,2),
 DESCRIPTION        TEXT,
 SAND_CATID         INT NOT NULL,
 SAND_SANDSHOPID    INT NOT NULL,
 CONSTRAINT FK_SANDSANDWICHSHOP FOREIGN KEY (SAND_SHOPID) REFERENCES SANDWICHSHOPS,
 CONSTRAINT FK_CATEGORY FOREIGN KEY (SAND_CATID) REFERENCES CATEGORIES
);

--categories
CREATE TABLE CATEGORIES
(
 CID            INT primary key default nextval('categories_cid_seq'),
 CTITLE         VARCHAR NOT NULL
);

CREATE TABLE SANDWICHSHOPS
(
 SANDSHOPID     INT primary key default nextval('sandwichshops_sandshopid_seq'),
 SHOPNAME       VARCHAR NOT NULL
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
 CONSTRAINT FK_ORDERSANDWICH FOREIGN KEY (O_SAND) REFERENCES SANDWICHES
);

CREATE TABLE SSOPTIONS
(
 SS_SANDSHOPID          INT NOT NULL,
 OPTION                 VARCHAR NOT NULL,
 CONSTRAINT FK_SSOPTIONSANDWICHSHOP
)

CREATE TABLE SSBREADTYPES
(
 SS_SANDSHOPID          INT NOT NULL,
 BREAD                  VARCHAR NOT NULL,
 CONSTRAINT FK_SSBREADSANDWICHSHOP FOREIGN KEY (SS_SANDSHOPID) REFERENCES SANDWICHSHOPS
)

CREATE TABLE ORDEROPTIONS
(
 OO_OID         INT NOT NULL,
 OPTION         VARCHAR NOT NULL,
 CONSTRAINT FK_ORDER FOREIGN KEY (OO_OID) REFERENCES ORDERS
)




