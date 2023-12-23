alter table users
    add column email varchar not null unique default 'ithersta@yandex.ru';

alter table users
    add column display_name varchar not null default 'ithersta';