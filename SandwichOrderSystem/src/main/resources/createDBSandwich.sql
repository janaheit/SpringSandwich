DROP TABLE SESSIONS CASCADE;
DROP table SANDWICHSHOPS CASCADE;
DROP TABLE SANDWICHES CASCADE;
DROP TABLE PERSONS CASCADE;
DROP TABLE ORDERS CASCADE;
--DROP TABLE CATEGORIES CASCADE;
DROP TABLE SSOPTIONS CASCADE;
DROP TABLE SSBREADTYPES CASCADE;
DROP TABLE ORDEROPTIONS CASCADE;

drop sequence orders_oid_seq;
drop sequence persons_pid_seq;
drop sequence sandwich_sandid_seq;
drop sequence sandwichshops_sandshopid_seq;
drop sequence sessions_sid_seq;
--drop sequence categories_cid_seq;

create sequence orders_oid_seq start with 1;
create sequence persons_pid_seq start with 1;
create sequence sandwich_sandid_seq start with 1;
create sequence sandwichshops_sandshopid_seq start with 1;
create sequence sessions_sid_seq start with 1;
--create sequence categories_cid_seq start with 1;


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
--CREATE TABLE CATEGORIES
--(
--    CID            INT primary key default nextval('categories_cid_seq'),
--    CTITLE         VARCHAR NOT NULL
--);

CREATE TABLE SANDWICHES
(SANDID             INT primary key  default nextval('sandwich_sandid_seq'),
 SANDNAME           VARCHAR(60) NOT NULL,
 PRICE              NUMERIC(4,2),
 DESCRIPTION        TEXT,
 CATEGORY           VARCHAR NOT NULL,
 SAND_SANDSHOPID    INT NOT NULL,
 CONSTRAINT FK_SANDSANDWICHSHOP FOREIGN KEY (SAND_SANDSHOPID) REFERENCES SANDWICHSHOPS
 --CONSTRAINT FK_CATEGORY FOREIGN KEY (SAND_CATID) REFERENCES CATEGORIES
);


CREATE TABLE ORDERS
(
 OID            INT primary key default nextval('orders_oid_seq'),
 O_SANDID       INT,
 OBREAD         VARCHAR,
 OREMARK        VARCHAR,
 OSTATUS        VARCHAR NOT NULL,
 OAMOUNT        INT DEFAULT 0,
 OPRICE         NUMERIC(4,2) default 0.0,
 ODATE          DATE NOT NULL,
 O_SHOP         INT NOT NULL,
 O_PID          INT NOT NULL,
 O_SID          INT NOT NULL,
 CONSTRAINT FK_ORDERSANDWHICHSHOP FOREIGN KEY (O_SHOP) REFERENCES SANDWICHSHOPS,
 CONSTRAINT FK_ORDERSANDWICH FOREIGN KEY (O_SANDID) REFERENCES SANDWICHES,
 CONSTRAINT FK_ORDERPERSON FOREIGN KEY (O_PID) REFERENCES PERSONS,
 CONSTRAINT FK_ORDERSESSION FOREIGN KEY (O_SID) REFERENCES SESSIONS
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
INSERT INTO PERSONS(plname, pfname, kind) VALUES ('Kim', 'Wauters', 's');

INSERT INTO SESSIONS(sstartdate, senddate, s_course) VALUES ('2022-09-01', '2022-12-31', 'JAVA_PROGRAMMING');
INSERT INTO SESSIONS(sstartdate, senddate, s_course) VALUES ('2022-09-05', '2023-01-31', 'SQL_FUNDAMENTALS');
INSERT INTO SESSIONS(sstartdate, senddate, s_course) VALUES ('2022-09-05', '2022-09-12', 'SQL_FUNDAMENTALS');

-- set instructors
UPDATE PERSONS SET p_sid = 1 WHERE pid = 3;
UPDATE PERSONS SET p_sid = 2 WHERE pid = 4;

-- set students
UPDATE PERSONS SET p_sid = 1 WHERE pid = 1;
UPDATE PERSONS SET p_sid = 2 WHERE pid = 2;
UPDATE PERSONS SET p_sid = 3 WHERE pid = 6;

-- Give sessions instructors
update sessions set sins_pid = 3 where sid = 1;
update sessions set sins_pid = 4 where sid = 2;
update sessions set sins_pid = 4 where sid = 3;

-- make sandwichshop
INSERT INTO SANDWICHSHOPS(shopname) VALUES ('TestShop');

INSERT INTO SANDWICHSHOPS(shopname) VALUES ('Vleugels');
INSERT INTO SANDWICHSHOPS(shopname) VALUES ('Pinkys');

-- configure shop
INSERT INTO SSOPTIONS VALUES (1, 'RAUWKOST');
INSERT INTO SSOPTIONS VALUES (1, 'NO_BUTTER');

INSERT INTO SSOPTIONS VALUES (2, 'GRILLEDVEGGIES');
INSERT INTO SSOPTIONS VALUES (2, 'RAUWKOST');

INSERT INTO SSOPTIONS VALUES (3, 'CLUB');
INSERT INTO SSOPTIONS VALUES (3, 'NO_BUTTER');

INSERT INTO SSBREADTYPES VALUES (1, 'GREY');
INSERT INTO SSBREADTYPES VALUES (1, 'WHITE');

INSERT INTO SSBREADTYPES VALUES (2, 'GREY');
INSERT INTO SSBREADTYPES VALUES (2, 'WHITE');

INSERT INTO SSBREADTYPES VALUES (3, 'GREY');
INSERT INTO SSBREADTYPES VALUES (3, 'WHITE');

-- make sandwich
INSERT INTO SANDWICHES(sandname, price, category, sand_sandshopid)
VALUES ('veggie test', 3.5, 'Veggie', 1);

-- Sandwiches for vleugels

INSERT INTO SANDWICHES(sandname, category, price,  sand_sandshopid)
VALUES ('Americain', 'Vlees', 3.5, 2);
INSERT INTO SANDWICHES(sandname, category, price,  sand_sandshopid) VALUES ('Boulette' , 'Vlees', 3.5, 2);
INSERT INTO SANDWICHES(sandname, category, price,  sand_sandshopid) VALUES ('Pastrami' , 'Vlees', 3.5, 2);
INSERT INTO SANDWICHES(sandname, category, price,  sand_sandshopid) VALUES ('Hesp + kaas' , 'Vlees', 3.5, 2);
INSERT INTO SANDWICHES(sandname, category, price,  sand_sandshopid) VALUES ('Martino' , 'Vlees', 3.5, 2);
INSERT INTO SANDWICHES(sandname, category, price,  sand_sandshopid) VALUES ('Vleessalade' , 'Vlees', 3.5, 2);
INSERT INTO SANDWICHES(sandname, category, price,  sand_sandshopid) VALUES ('Hesp' , 'Vlees', 3.5, 2);
INSERT INTO SANDWICHES(sandname, category, price,  sand_sandshopid) VALUES ('Parmaham' , 'Vlees', 3.5, 2);
INSERT INTO SANDWICHES(sandname, category, price,  sand_sandshopid) VALUES ('Kipfilet' , 'Kip', 3.5, 2);
INSERT INTO SANDWICHES(sandname, category, price,  sand_sandshopid) VALUES ('Spicy kip curry' , 'Kip', 3.5, 2);
INSERT INTO SANDWICHES(sandname, category, price,  sand_sandshopid) VALUES ('Kip Hawai' , 'Kip', 3.5, 2);
INSERT INTO SANDWICHES(sandname, category, price,  sand_sandshopid) VALUES ('Tonijnsalade' , 'Vis', 3.5, 2);
INSERT INTO SANDWICHES(sandname, category, price,  sand_sandshopid) VALUES ('Tonijnsalade pikant' , 'Vis', 3.5, 2);
INSERT INTO SANDWICHES(sandname, category, price,  sand_sandshopid) VALUES ('gemarineerd) + philadephia' , 'Vis', 3.5, 2);
INSERT INTO SANDWICHES(sandname, category, price,  sand_sandshopid) VALUES ('Kaas' , 'Veggie', 3.5, 2);
INSERT INTO SANDWICHES(sandname, category, price,  sand_sandshopid) VALUES ('Tomaat + mozzarella + pesto' , 'Veggie', 3.5, 2);
INSERT INTO SANDWICHES(sandname, category, price,  sand_sandshopid) VALUES ('Eiersalade' , 'Veggie', 3.5, 2);
INSERT INTO SANDWICHES(sandname, category, price,  sand_sandshopid) VALUES ('Brie' , 'Veggie', 3.5, 2);
INSERT INTO SANDWICHES(sandname, category, price,  sand_sandshopid) VALUES ('Feta' , 'Veggie', 3.5, 2);
INSERT INTO SANDWICHES(sandname, category, price,  sand_sandshopid) VALUES ('Geitenkaas' , 'Veggie', 3.5, 2);
INSERT INTO SANDWICHES(sandname, category, price,  sand_sandshopid) VALUES ('Wortelspread + sesam + tuinkers' , 'Vegan', 3.5, 2);
INSERT INTO SANDWICHES(sandname, category, price,  sand_sandshopid) VALUES ('Hummus' , 'Vegan', 3.5, 2);
INSERT INTO SANDWICHES(sandname, category, price,  sand_sandshopid) VALUES ('Vegan mayo' , 'Vegan', 3.5, 2);
INSERT INTO SANDWICHES(sandname, category, price,  sand_sandshopid) VALUES ('Avocadospread' , 'Vegan', 3.5, 2);
INSERT INTO SANDWICHES(sandname, category, price,  sand_sandshopid) VALUES ('Carolina' , 'Specials', 3.5, 2);
INSERT INTO SANDWICHES(sandname, category, price,  sand_sandshopid) VALUES ('Parmigiano' , 'Specials', 3.5, 2);
INSERT INTO SANDWICHES(sandname, category, price,  sand_sandshopid) VALUES ('Kip cocktail' , 'Specials', 3.5, 2);
INSERT INTO SANDWICHES(sandname, category, price,  sand_sandshopid) VALUES ('BBQ Chicken' , 'Specials', 3.5, 2);
INSERT INTO SANDWICHES(sandname, category, price,  sand_sandshopid) VALUES ('Flying Brie' , 'Specials', 3.5, 2);
INSERT INTO SANDWICHES(sandname, category, price,  sand_sandshopid) VALUES ('Provence' , 'Specials', 3.5, 2);

--Pinky

INSERT INTO SANDWICHES(sandname, category, price,  sand_sandshopid) VALUES ('Hesp' , 'Vlees', 3.5, 3);
INSERT INTO SANDWICHES(sandname, category, price,  sand_sandshopid) VALUES ('Rosbief' , 'Vlees', 3.5, 3);
INSERT INTO SANDWICHES(sandname, category, price,  sand_sandshopid) VALUES ('Gebraad' , 'Vlees', 3.5, 3);
INSERT INTO SANDWICHES(sandname, category, price,  sand_sandshopid) VALUES ('Gerookte nootham' , 'Vlees', 3.5, 3);
INSERT INTO SANDWICHES(sandname, category, price,  sand_sandshopid) VALUES ('Parma ham' , 'Vlees', 3.5, 3);
INSERT INTO SANDWICHES(sandname, category, price,  sand_sandshopid) VALUES ('Salami' , 'Vlees', 3.5, 3);
INSERT INTO SANDWICHES(sandname, category, price,  sand_sandshopid) VALUES ('Américain préparé' , 'Vlees', 3.5, 3);
INSERT INTO SANDWICHES(sandname, category, price,  sand_sandshopid) VALUES ('Gemengd gehakt' , 'Vlees', 3.5, 3);
INSERT INTO SANDWICHES(sandname, category, price,  sand_sandshopid) VALUES ('Martino' , 'Vlees', 3.5, 3);
INSERT INTO SANDWICHES(sandname, category, price,  sand_sandshopid) VALUES ('Kip curry' , 'Vlees', 3.5, 3);
INSERT INTO SANDWICHES(sandname, category, price,  sand_sandshopid) VALUES ('Kip zigeuner' , 'Vlees', 3.5, 3);
INSERT INTO SANDWICHES(sandname, category, price,  sand_sandshopid) VALUES ('Kipsla' , 'Vlees', 3.5, 3);
INSERT INTO SANDWICHES(sandname, category, price,  sand_sandshopid) VALUES ('Weense sla' , 'Vlees', 3.5, 3);
INSERT INTO SANDWICHES(sandname, category, price,  sand_sandshopid) VALUES ('Vleessla' , 'Vlees', 3.5, 3);
INSERT INTO SANDWICHES(sandname, category, price,  sand_sandshopid) VALUES ('Hollandse kaas' , 'Kaas', 3.5, 3);
INSERT INTO SANDWICHES(sandname, category, price,  sand_sandshopid) VALUES ('Brie' , 'Kaas', 3.5, 3);
INSERT INTO SANDWICHES(sandname, category, price,  sand_sandshopid) VALUES ('Kaassla' , 'Kaas', 3.5, 3);
INSERT INTO SANDWICHES(sandname, category, price,  sand_sandshopid) VALUES ('Tomaat garnaal' , 'Vis', 3.5, 3);
INSERT INTO SANDWICHES(sandname, category, price,  sand_sandshopid) VALUES ('Garnaalsla' , 'Vis', 3.5, 3);
INSERT INTO SANDWICHES(sandname, category, price,  sand_sandshopid) VALUES ('Tonijnsla' , 'Vis', 3.5, 3);
INSERT INTO SANDWICHES(sandname, category, price,  sand_sandshopid) VALUES ('Pikante tonijnsla' , 'Vis', 3.5, 3);
INSERT INTO SANDWICHES(sandname, category, price,  sand_sandshopid) VALUES ('Zalmsla' , 'Vis', 3.5, 3);
INSERT INTO SANDWICHES(sandname, category, price,  sand_sandshopid) VALUES ('Gerookte zalmsla' , 'Vis', 3.5, 3);
INSERT INTO SANDWICHES(sandname, category, price,  sand_sandshopid) VALUES ('Krabsla' , 'Vis', 3.5, 3);
INSERT INTO SANDWICHES(sandname, category, price,  sand_sandshopid) VALUES ('Eiersla' , 'Vis', 3.5, 3);

-- Adding some basic orders
INSERT INTO orders (o_sandid, obread, oremark, ostatus, oamount, oprice, odate, o_shop, o_pid, o_sid) VALUES (4, 'GREY', 'All fine', 'ORDERED', 1, 3.5, '2022-11-30', 2, 2, 2);
INSERT INTO orders (ostatus, odate, o_shop, o_pid, o_sid, oamount, oprice) VALUES ('UNFILLED', '2022-11-30', 2, 1, 1, 0, 0.0);
INSERT INTO orders (ostatus, odate, o_shop, o_pid, o_sid, oamount, oprice) VALUES ('UNFILLED', '2022-10-15', 2, 1, 3, 0, 0.0);

-- Adding some historic orders
INSERT INTO orders (o_sandid, obread, oremark, ostatus, oamount, oprice, odate, o_shop, o_pid, o_sid) VALUES (4, 'GREY', 'All fine', 'HANDELED', 1, 3.5, '2021-11-30', 2, 6, 2);
INSERT INTO orders (o_sandid, obread, oremark, ostatus, oamount, oprice, odate, o_shop, o_pid, o_sid) VALUES (6, 'GREY', 'All fine', 'HANDELED', 1, 3.5, '2021-11-29', 2, 6, 2);
INSERT INTO orders (o_sandid, obread, oremark, ostatus, oamount, oprice, odate, o_shop, o_pid, o_sid) VALUES (4, 'GREY', 'All fine', 'HANDELED', 1, 3.5, '2021-11-30', 2, 4, 2);
INSERT INTO orders (o_sandid, obread, oremark, ostatus, oamount, oprice, odate, o_shop, o_pid, o_sid) VALUES (4, 'GREY', 'All fine', 'HANDELED', 1, 3.5, '2021-11-28', 2, 4, 2);

-- As well as options
INSERT INTO orderoptions VALUES (1, 'GRILLEDVEGGIES');
INSERT INTO orderoptions VALUES (1, 'RAUWKOST');


