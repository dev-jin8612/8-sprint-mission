-- -- --------------------------------------------------------
-- -- db 생성 및 유저 권한 할당 (필요하면 실행)
-- -- --------------------------------------------------------
-- -- 1. 유저 생성: 이미 생성했다면 SKIP
-- -- 접속 유저: root 계정인 postgres로 진행
-- -- 접속 대상: postgres 데이터베이스의 public 스키마
-- CREATE USER discodeit_user PASSWORD 'discodeit1234' CREATEDB;
--
-- -- 2. 데이터베이스 생성
-- CREATE DATABASE discodeit
--     WITH
--     OWNER = discodeit_user
--     ENCODING = 'UTF8';
--
-- -- 3. 스키마 생성 (discodeit_user 계정으로 진행)
-- -- 접속 유저: 일반 계정인 discodeit_user로 진행
-- -- 접속 대상: discodeit 데이터베이스의 public 스키마
-- CREATE SCHEMA IF NOT EXISTS discodeit;
-- -- DROP SCHEMA discodeit;
--
-- -- 4. 권한 할당 / 안돼면 root 계정으로
-- GRANT ALL PRIVILEGES ON SCHEMA discodeit TO ohgiraffers;
-- GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA discodeit TO discodeit_user;
--
-- -- 5. 검색 경로 설정
-- ALTER ROLE discodeit_user SET search_path TO discodeit;
-- SHOW search_path;
--
-- -- 6. 최종 접속 설정
-- -- 접속 유저: 일반 계정인 discodeit_user로 진행
-- -- 접속 대상: discodeit 데이터베이스의 discodeit 스키마
-- -- _______________________________________________________________--

CREATE TABLE binary_contents
(
    id           uuid primary key,
    created_at   timestamptz  not null,
    file_name    varchar(255) not null,
    size         BIGINT       not null,
    content_type varchar(100) not null,
    bytes        BYTEA        not null
);

-- INSERT INTO binary_contents
-- VALUES (
--         random(),time.now(),
--         "프로필이미지",100,
--         "img","13223"
--        );

CREATE TABLE users
(
    id         uuid primary key,
    created_at timestamptz  not null,
    updated_at timestamptz,
    username   varchar(50)  not null unique,
    email      varchar(100) not null unique,
    password   varchar(60)  not null,
    profile_id uuid unique,

    CONSTRAINT fk_users_binary_contents
        FOREIGN KEY (profile_id)
            REFERENCES binary_contents (id)
            ON DELETE SET NULL
);

-- INSERT INTO users
-- VALUES (
--         random(),time.now,
--         null,"황",
--         "test1@mail.com","1234",
--         random());

-- UPDATE users
-- SET updated_at = time.now();


CREATE TABLE user_statuses
(
    id             uuid primary key,
    created_at     timestamptz not null,
    updated_at     timestamptz,
    user_id        uuid unique not null,
    last_active_at timestamptz not null,

    CONSTRAINT fk_user_status_user
        FOREIGN KEY (user_id)
            REFERENCES users (id)
            ON DELETE CASCADE
);

-- UPDATE user_statuses
-- SET updated_at = time.now(),
--     last_active_at = time.now();


-- INSERT INTO user_statuses
-- VALUES (
--         random(),time.now(),
--         null,users.id,
--         time.now()
--        );

CREATE TABLE channels
(
    id          uuid primary key,
    created_at  timestamptz not null,
    updated_at  timestamptz,
    name        varchar(100),
    description varchar(500),
    type        varchar(10) not null,

    CHECK (type IN ('PUBLIC', 'PRIVATE'))
);

-- UPDATE channels
-- SET  updated_at = time.now(),
--      name = "입력받은 name",
--      description = "입력받은 description";

-- INSERT INTO channels
-- VALUES (
--         random(),time.now(),
--         null,"1번채널",
--         "비공개일 경우 name과 description은 null",
--         "PUBLIC"
--        );

CREATE TABLE messages
(
    id         uuid primary key,
    created_at timestamptz not null,
    updated_at timestamptz,
    author_id  uuid,
    channel_id uuid        not null,
    content    TEXT,

    CONSTRAINT fk_message_channel
        FOREIGN KEY (channel_id)
            REFERENCES channels (id)
            ON DELETE CASCADE,

    CONSTRAINT fk_message_author
        FOREIGN KEY (author_id)
            REFERENCES users (id)
            ON DELETE SET NULL
);

-- UPDATE messages
-- SET updated_at= time.now(),
--     content="입력 받은 글";

-- INSERT INTO messages
-- VALUES (
--         random(), time.now(),
--         null,user.id,
--         channel.id,
--         "메세지 내용을 작성"
--        );

CREATE TABLE read_statuses
(
    id           uuid primary key,
    created_at   timestamptz not null,
    updated_at   timestamptz,
    user_id      uuid        not null,
    channel_id   uuid        not null,
    last_read_at timestamptz not null,

    CONSTRAINT uk_read_status unique (user_id, channel_id),

    CONSTRAINT fk_read_status_user
        FOREIGN KEY (user_id)
            REFERENCES users (id)
            ON DELETE CASCADE,

    CONSTRAINT fk_read_status_channel
        FOREIGN KEY (channel_id)
            REFERENCES channels (id)
);

-- UPDATE read_statuses
-- SET updated_at = time.now(),
--     last_read_at = time.now();

-- INSERT INTO read_statuses
-- VALUES (
--         random(),time.now(),
--         null, user.id,
--         chnnel.id, time.now()
--        );

CREATE TABLE message_attachments
(
-- serial하면 postgres는 자동으로 INT타입 됨
    id            SERIAL primary key,
    message_id    uuid not null,
    attachment_id uuid not null,

    CONSTRAINT fk_message_attach_message
        FOREIGN KEY (message_id)
            REFERENCES messages (id)
            ON DELETE CASCADE,

    CONSTRAINT fk_message_attach_attachment
        FOREIGN KEY (attachment_id)
            REFERENCES binary_contents (id)
            ON DELETE CASCADE
);

-- INSERT INTO message_attachments
-- VALUES (
--         random(),message.id,
--         binary_contents.id
--        )

SELECT *
FROM message_attachments ma
         JOIN messages m ON m.id = ma.message_id
         JOIN binary_contents bc ON ma.attachment_id = bc.id;


-- __________________________________________________________________-
-- DROP TABLE IF EXISTS message_attachments;
-- DROP TABLE IF EXISTS read_statuses;
-- DROP TABLE IF EXISTS messages;
-- DROP TABLE IF EXISTS user_statuses;
-- DROP TABLE IF EXISTS users;
-- DROP TABLE IF EXISTS channels;
-- DROP TABLE IF EXISTS binary_contents;
