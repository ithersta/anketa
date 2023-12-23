alter table surveys
    add column created_by uuid references users (id);

alter table surveys
    add column created_at timestamp not null default '2023-12-21T12:00:00';