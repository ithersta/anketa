create
extension if not exists "uuid-ossp";

create table users (
    id uuid primary key default uuid_generate_v4()
);

create table oauth_connections (
    id               uuid primary key default uuid_generate_v4(),
    provider         varchar not null,
    provider_user_id varchar not null,
    user_id          uuid    not null references users (id),
    unique (provider, provider_user_id)
);

create table surveys (
    id         uuid primary key default uuid_generate_v4(),
    title      varchar   not null,
    entries    bytea     not null,
    created_by uuid      not null references users (id),
    created_at timestamp not null
);