create table shares (
    survey_id uuid not null,
    user_id   uuid not null,
    primary key (survey_id, user_id)
);
