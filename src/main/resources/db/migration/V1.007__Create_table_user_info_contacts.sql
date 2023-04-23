CREATE TABLE IF NOT EXISTS user_info_contacts(
    UID VARCHAR(100)  NOT NULL DEFAULT public.uuid_generate_v4(),
    USER_INFO_UID varchar NOT NULL,
    GITHUB varchar,
    FACEBOOK varchar,
    EMAIL varchar,
    TWITTER varchar,
    website varchar,
    FOREIGN KEY (USER_INFO_UID) REFERENCES user_info(UID),
    CONSTRAINT PK_USER_CONTACT PRIMARY KEY (UID)
);
