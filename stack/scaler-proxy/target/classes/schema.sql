SET timezone = 'Europe/Paris';

create table if not exists scaling_instruction
(
    instruction_id serial primary key,
    scaling_batch_id varchar(100) not null,
    logical_service_name varchar(100) not null,
    container_id varchar(100) not null,
    swarm_service_name varchar(100) not null,
    scaling_direction varchar(100) not null,
    received_request_timestamp timestamp(3) not null,
    scale_acknowledgement_timestamp timestamp(3) not null
);