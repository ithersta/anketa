alter table surveys
    add column created_by uuid references users (id);

update surveys
    set created_by = (select id from users limit 1);

alter table surveys
    add column created_at timestamp not null default '2023-12-21T12:00:00';