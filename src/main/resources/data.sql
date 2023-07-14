create table contact (
    id int primary key auto_increment,
    email varchar(128),
    phone_number varchar(16),
    linked_id int,
    link_precedence varchar(32) not null,
    created_at datetime not null default current_timestamp,
    updated_at datetime not null default current_timestamp on update current_timestamp,
    deleted_at datetime
);

create index idx_email on contact (email);
create index idx_phone on contact(phone_number);
create index idx_linked_ids on contact(linked_id);
