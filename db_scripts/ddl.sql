create database bstest;
create user 'bstest'@'localhost' identified by '*************';
grant all privileges on bstest.* to 'bstest'@'localhost';



use bstest;

create table contact (
    id int primary key auto_increment,
    email varchar(128),
    phone_number varchar(16),
    linked_id int,
    link_precedence varchar(32) not null,
    created_at datetime not null default current_timestamp,
    updated_at datetime not null default current_timestamp on update current_timestamp,
    deleted_at datetime,

    index idx_email(email),
    index idx_phone(phone_number),
    index idx_linked_ids(linked_id)
);
