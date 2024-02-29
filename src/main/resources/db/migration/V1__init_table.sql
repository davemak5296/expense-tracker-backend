drop table if exists users cascade;

create table users (
    is_block boolean default false not null,
    created_date timestamp(6) not null,
    id bigserial not null,
    last_modified_date timestamp(6),
    alias varchar(20) not null,
    email varchar(255) not null unique,
    password varchar(255) not null,
    primary key (id)
);

insert into users (email, alias, password, created_date, last_modified_date) values ('zzz@gmail.com', 'zzz', 'test1234', '2024-02-28 12:00:00', '2024-02-28 12:00:00');