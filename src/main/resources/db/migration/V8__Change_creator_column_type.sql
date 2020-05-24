alter table question alter column CREATOR bigint default NOT NULL;
alter table comment alter column COMMENTATOR bigint default NOT NULL;