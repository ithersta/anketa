alter table users
    add column email varchar not null unique default substr(uuid_generate_v4()::text,1,15);

alter table users
    add column display_name varchar not null default 'ithersta';