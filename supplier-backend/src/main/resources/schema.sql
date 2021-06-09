create table if not exists batch_instruction
(
    batch_id serial primary key,
    path_option varchar(300) not null,
    payment_option varchar(300) not null,
    message_cnt integer not null,
    duration integer not null
);