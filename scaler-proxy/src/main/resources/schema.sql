SET timezone = 'Europe/Paris';

create table if not exists scale_instruction
(
    instruction_id serial primary key,
    received_timestamp timestamp(3) not null,
    request_name varchar(300) not null
);