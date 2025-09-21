CREATE TABLE users
(
    id         VARCHAR(36) PRIMARY KEY,

    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,

    user_name VARCHAR(255),
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
    user_id VARCHAR(36),
    mobile  VARCHAR(36),

    PRIMARY KEY (user_id, mobile),
    CONSTRAINT fk_user_mobile_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);

CREATE TABLE user_emails
(
    user_id VARCHAR(36),
    email   VARCHAR(36),

    PRIMARY KEY (user_id, email),
    CONSTRAINT fk_user_email_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);

CREATE TABLE user_sites
(
    user_id VARCHAR(36),
    site    VARCHAR(36),

    PRIMARY KEY (user_id, site),
    CONSTRAINT fk_user_site_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);

CREATE TABLE post
(
    id         VARCHAR(36) PRIMARY KEY,
    created_at TIMESTAMP   NOT NULL,
    updated_at TIMESTAMP   NOT NULL,
    creator_id VARCHAR(36) NOT NULL,
    text       TEXT,

    CONSTRAINT fk_post_creator FOREIGN KEY (creator_id) REFERENCES users (id)
);

CREATE TABLE chat
(
    id         VARCHAR(36) PRIMARY KEY,
    created_at TIMESTAMP   NOT NULL,
    updated_at TIMESTAMP   NOT NULL,
    creator_id VARCHAR(36) NOT NULL,
    text       TEXT,

    CONSTRAINT fk_comment_creator FOREIGN KEY (creator_id) REFERENCES users (id)
);

CREATE TABLE chat_user
(
    user_id VARCHAR(36) NOT NULL,
    chat_id VARCHAR(36) NOT NULL,

    PRIMARY KEY (user_id, chat_id),
    CONSTRAINT fk_l_user FOREIGN KEY (user_id) REFERENCES users (id),
    CONSTRAINT fk_chat_user_chat FOREIGN KEY (chat_id) REFERENCES chat (id)
);

CREATE TABLE message
(
    id         VARCHAR(36) PRIMARY KEY,
    created_at TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    creator_id VARCHAR(36) NOT NULL,
    chat_id    VARCHAR(36) NOT NULL,
    content    TEXT,
    read       BOOLEAN,
    deleted    BOOLEAN,

    CONSTRAINT fk_message_creator FOREIGN KEY (creator_id) REFERENCES users (id),
    CONSTRAINT fk_message_chat FOREIGN KEY (chat_id) REFERENCES chat (id)
);

CREATE TABLE media
(
    id         VARCHAR(36) PRIMARY KEY,
    uri        VARCHAR(500) NOT NULL,
    type       VARCHAR(50),
    owner_type VARCHAR(50),
    owner_id   VARCHAR(36)  NOT NULL
);

CREATE TABLE comment
(
    id         VARCHAR(36) PRIMARY KEY,
    created_at TIMESTAMP   NOT NULL,
    updated_at TIMESTAMP   NOT NULL,
    creator_id VARCHAR(36) NOT NULL,
    text       TEXT,

    CONSTRAINT fk_comment_creator FOREIGN KEY (creator_id) REFERENCES users (id)
);
