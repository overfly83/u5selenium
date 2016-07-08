drop table "SYSTEM"."PERSON"
;;
CREATE COLUMN TABLE "SYSTEM"."PERSON" ("ID" VARCHAR(1) NOT NULL ,
	 "NUMBER" INTEGER CS_INT,
	 "H1" VARCHAR(1),
	 PRIMARY KEY ("ID")) UNLOAD PRIORITY 5 AUTO MERGE
;;
	 
grant select on "SYSTEM"."PERSON" to _SYS_REPO with grant option
;;

insert into "SYSTEM"."PERSON" values('a',11,'')
;;
insert into "SYSTEM"."PERSON" values('b',2,'a')
;;
insert into "SYSTEM"."PERSON" values('c',2,'b')
;;
insert into "SYSTEM"."PERSON" values('d',2,'b')
;;
insert into "SYSTEM"."PERSON" values('f',3,'a')
;;
insert into "SYSTEM"."PERSON" values('e',2,'d')
;;