DROP TABLE SESSIONS CASCADE;
DROP table SANDWICHSHOPS CASCADE;
DROP TABLE SANDWICHES CASCADE;
DROP TABLE PERSONS CASCADE;
DROP TABLE ORDERS CASCADE;

drop sequence orders_ono_seq;
drop sequence persons_pno_seq;
drop sequence sandwich_sandno_seq;
drop sequence sandwichshops_sandshopno_seq;
drop sequence sessions_sno_seq;

create sequence orders_ono_seq starts with 1;
create sequence persons_pno_seq starts with 1;
create sequence sandwich_sandno_seq starts with 1;
create sequence sandwichshops_sandshopno_seq starts with 1;
create sequence sessions_sno_seq starts with 1;

CREATE TABLE PERSONS
(PNO         INT  primary key default nextval('persons_pno_seq'),
 PLNAME      CHAR(40) NOT NULL,
 PFNAME      VARCHAR(15),
 KIND        CHAR(1),
 P_SNO       INT,
 CONSTRAINT FK_SESSION FOREIGN KEY (P_SNO) REFERENCES SESSIONS
);

