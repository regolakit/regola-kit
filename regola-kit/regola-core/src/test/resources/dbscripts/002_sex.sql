CREATE MEMORY TABLE SEX (ID VARCHAR PRIMARY KEY ,DESCRIPTION VARCHAR NOT NULL)
INSERT INTO SEX VALUES ('M','MALE')
INSERT INTO SEX VALUES ('F','FEMALE')
INSERT INTO SEX VALUES ('O','OMOSEX')
ALTER TABLE customer add COLUMN sex varchar
UPDATE customer set sex='F' where id<25
UPDATE customer set sex='M' where id>=25