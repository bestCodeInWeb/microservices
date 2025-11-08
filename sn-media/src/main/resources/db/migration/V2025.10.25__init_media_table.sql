CREATE TABLE media
(
    id         VARCHAR(36) PRIMARY KEY,
    file_path  VARCHAR(500) NOT NULL, -- Фізичний шлях або ім'я файлу
    uri        VARCHAR(500) NOT NULL, -- Логічний URI для доступу
    mime_type  VARCHAR(100),          -- Тип контенту
    type       VARCHAR(50),           -- IMAGE, VIDEO, AUDIO
    owner_type VARCHAR(50),           -- POST, COMMENT, MESSAGE, USER_AVATAR
    owner_id   VARCHAR(36)  NOT NULL,
    creator_id VARCHAR(36)  NOT NULL  -- ID користувача, який завантажив
);