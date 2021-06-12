SET timezone = 'Europe/Paris';

create table if not exists scaling_instruction
(
    instruction_id serial primary key,
    scaling_direction varchar(100) not null,
    received_request_timestamp timestamp(3) not null,
    scale_acknowledgement_timestamp timestamp(3) not null
);