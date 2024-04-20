alter table if exists user_roles
    drop constraint if exists FKh8ciramu9cc9q3qcqiv4ue8a6;
alter table if exists user_roles
    drop constraint if exists FKhfh9dx7w3ubf1co1vdev94g3f;
drop table if exists roles cascade;
drop table if exists user_roles cascade;
drop table if exists users cascade;
create table roles
(
    id   serial not null,
    name varchar(20) unique check (name in ('ROLE_USER', 'ROLE_ADMIN')),
    primary key (id)
);
create table users
(
    id       serial       not null,
    username varchar(32)  not null unique,
    password varchar(60)  not null,
    email    varchar(255) not null unique,
    primary key (id)
);
create table user_roles
(
    role_id integer not null,
    user_id integer not null,
    primary key (role_id, user_id)
);
alter table if exists user_roles
    add constraint FKh8ciramu9cc9q3qcqiv4ue8a6 foreign key (role_id) references roles;
alter table if exists user_roles
    add constraint FKhfh9dx7w3ubf1co1vdev94g3f foreign key (user_id) references users;
