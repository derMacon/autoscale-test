SET timezone = 'Europe/Paris';

create table if not exists payment
(
    payment_id serial primary key,
    extracted_element text not null,
    sent_timestamp timestamp(3) not null,
    received_timestamp timestamp(3) not null,
    processed_timestamp timestamp(3) not null,
    content text not null
);
