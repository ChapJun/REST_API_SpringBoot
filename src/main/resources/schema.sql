drop table if exists user CASCADE;

drop sequence if exists hibernate_sequence;
create sequence hibernate_sequence start with 1 increment by 1;

create table user (
                      id integer not null,
                      join_date timestamp,
                      name varchar(255),
                      password varchar(255),
                      ssn varchar(255),
                      primary key (id)
);