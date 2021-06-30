SET timezone = 'Europe/Paris';

create table if not exists parsed_instruction
(
    message_id     serial primary key,
    destination    varchar(300) not null,
    path_option    varchar(300) not null,
    payment_option varchar(300) not null,
    message_cnt    integer      not null,
    duration       integer      not null,
    received       timestamp(3) not null
);