SET timezone = 'Europe/Paris';

create table if not exists scaling_instruction
(
    instruction_id serial primary key,
    request_type varchar(300) not null,
    received_timestamp timestamp(3) not null,
    processed_timestamp timestamp(3) not null
);