CREATE TABLE post_hashtags
(
    post_id   VARCHAR(36) NOT NULL,
    hashtag VARCHAR(255) NOT NULL,

    PRIMARY KEY (post_id, hashtag),
    CONSTRAINT fk_post_hashtag_post FOREIGN KEY (post_id) REFERENCES post (id) ON DELETE CASCADE
);