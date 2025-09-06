CREATE TABLE users
(
    id         VARCHAR(255) PRIMARY KEY,

    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,

    first_name VARCHAR(255),
    last_name  VARCHAR(255),
    gender     VARCHAR(50),
    birth_date DATE,

    country    VARCHAR(255),
    city       VARCHAR(255),
    street     VARCHAR(255),

    language   VARCHAR(20),
    is_private BOOLEAN,

    avatar     VARCHAR(500),
    background VARCHAR(500)
);

CREATE TABLE user_mobiles
(
    user_id VARCHAR(255),
    mobile  VARCHAR(36),

    PRIMARY KEY (user_id, mobile),
    CONSTRAINT fk_user_mobile_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);

CREATE TABLE user_emails
(
    user_id VARCHAR(255),
    email   VARCHAR(36),

    PRIMARY KEY (user_id, email),
    CONSTRAINT fk_user_email_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);

CREATE TABLE user_sites
(
    user_id VARCHAR(255),
    site    VARCHAR(36),

    PRIMARY KEY (user_id, site),
    CONSTRAINT fk_user_site_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);

CREATE TABLE post
(
    id         VARCHAR(255) PRIMARY KEY,
    created_at TIMESTAMP    NOT NULL,
    updated_at TIMESTAMP    NOT NULL,
    creator_id VARCHAR(255) NOT NULL,
    text       TEXT,

    CONSTRAINT fk_post_creator FOREIGN KEY (creator_id) REFERENCES users (id)
);

CREATE TABLE message
(
    id         VARCHAR(255) PRIMARY KEY,
    created_at TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    creator_id VARCHAR(255) NOT NULL,
    chat_id    VARCHAR(255) NOT NULL,
    content    TEXT,
    read       BOOLEAN,
    deleted    BOOLEAN,

    CONSTRAINT fk_message_creator FOREIGN KEY (creator_id) REFERENCES users (id),
    CONSTRAINT fk_message_chat FOREIGN KEY (chat_id) REFERENCES chat (id)
);

CREATE TABLE media
(
    id         VARCHAR(255) PRIMARY KEY,
    uri        VARCHAR(500) NOT NULL,
    type       VARCHAR(50),
    owner_type VARCHAR(50),
    owner_id   VARCHAR(255) NOT NULL
);

CREATE TABLE comment
(
    id         VARCHAR(255) PRIMARY KEY,
    created_at TIMESTAMP    NOT NULL,
    updated_at TIMESTAMP    NOT NULL,
    creator_id VARCHAR(255) NOT NULL,
    text       TEXT,

    CONSTRAINT fk_comment_creator FOREIGN KEY (creator_id) REFERENCES users (id)
);

CREATE TABLE chat
(
    id         VARCHAR(255) PRIMARY KEY,
    created_at TIMESTAMP    NOT NULL,
    updated_at TIMESTAMP    NOT NULL,
    creator_id VARCHAR(255) NOT NULL,
    text       TEXT,

    CONSTRAINT fk_comment_creator FOREIGN KEY (creator_id) REFERENCES users (id)
);


