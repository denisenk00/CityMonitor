CREATE DATABASE CityMonitorDB OWNER postgres;


-- Create tables section -------------------------------------------------

-- Table USERS

CREATE TABLE USERS(
    user_id Integer GENERATED ALWAYS AS IDENTITY,
    username VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(300) NOT NULL,
    role VARCHAR(100) NOT NULL CHECK ( role IN ('SUPER_ADMIN', 'ADMIN', 'VIEWER')),
    account_status VARCHAR(100) NOT NULL CHECK ( account_status IN ('ACTIVE', 'NOT_ACTIVE'))
);

-- Table LOCALS


CREATE TABLE LOCALS(
                       local_id Integer GENERATED ALWAYS AS IDENTITY,
                       chat_id Integer UNIQUE NOT NULL,
                       name Varchar(100) NOT NULL,
                       phone Varchar(35) NOT NULL UNIQUE,
                       location_point GEOMETRY NOT NULL,
                       is_active BOOLEAN NOT NULL,
                       PRIMARY KEY (local_id)
);

-- Table LAYOUTS

CREATE TABLE LAYOUTS(
                        layout_id Integer GENERATED ALWAYS AS IDENTITY,
                        name Varchar(200) NOT NULL,
                        status VARCHAR(80) NOT NULL,
                        PRIMARY KEY (layout_id),
                        CHECK (status IN ('IN_USE', 'AVAILABLE', 'DEPRECATED'))
);

-- Table POLYGONS

CREATE TABLE POLYGONS(
                         polygon_id Integer GENERATED ALWAYS AS IDENTITY,
                         name Varchar(200) NOT NULL,
                         polygon GEOMETRY NOT NULL,
                         layout_id Integer NOT NULL,
                         PRIMARY KEY (polygon_id)
);

-- Table QUIZZES

CREATE TABLE QUIZZES(
                        quiz_id Integer GENERATED ALWAYS AS IDENTITY,
                        title Varchar(220) NOT NULL, --JUST FOR ADMIN PANEL
                        description VARCHAR(2200) NOT NULL,
                        status VARCHAR(80) NOT NULL,
                        start_date TIMESTAMP NOT NULL,
                        end_date TIMESTAMP NOT NULL,
                        layout_id Integer NOT NULL,
                        PRIMARY KEY (quiz_id),
                        CHECK (status IN ('PLANNED', 'IN_PROGRESS', 'FINISHED'))
);

-- Table OPTIONS

CREATE TABLE OPTIONS(
                        option_id Integer GENERATED ALWAYS AS IDENTITY,
                        title Varchar(1100) NOT NULL,
                        quiz_id Integer NOT NULL,
                        PRIMARY KEY (option_id)
);

-- Table ANSWERS

CREATE TABLE ANSWERS(
                        answer_id BIGINT GENERATED ALWAYS AS IDENTITY,
                        option_id Integer NOT NULL,
                        local_id Integer NOT NULL,
                        quiz_id Integer NOT NULL,
                        PRIMARY KEY (answer_id)
);

-- Table APPEALS

CREATE TABLE APPEALS(
                        appeal_id Integer GENERATED ALWAYS AS IDENTITY,
                        text Varchar(4000),
                        status Varchar(80) NOT NULL,
                        post_date TIMESTAMP NOT NULL,
                        point GEOMETRY,
                        local_id Integer NOT NULL,
                        PRIMARY KEY (appeal_id),
                        CHECK ( status IN ('UNREAD', 'VIEWED', 'PROCESSED', 'IN_PROGRESS', 'TRASH'))
);

-- Table RESULTS

CREATE TABLE RESULTS(
                        option_id Integer NOT NULL,
                        answers_count Integer NOT NULL,
                        result_id Integer GENERATED ALWAYS AS IDENTITY,
                        polygon_id Integer,
                        PRIMARY KEY (option_id,result_id)
);

--Table FILES

CREATE TABLE FILES(
                      file_id Integer GENERATED ALWAYS AS IDENTITY,
                      name VARCHAR(200),
                      tg_file_id VARCHAR(150) UNIQUE,
                      file_object OID,
                      appeal_id INTEGER,
                      quiz_id INTEGER,
                      PRIMARY KEY (file_id)
);
-- Create foreign keys (relationships) section -------------------------------------------------

ALTER TABLE FILES ADD CONSTRAINT file_appeal_fk FOREIGN KEY (appeal_id) REFERENCES APPEALS (appeal_id) ON DELETE CASCADE;

ALTER TABLE POLYGONS ADD CONSTRAINT polygon_layout_fk FOREIGN KEY (layout_id) REFERENCES LAYOUTS (layout_id) ON DELETE CASCADE;

ALTER TABLE QUIZZES ADD CONSTRAINT quiz_layout_fk FOREIGN KEY (layout_id) REFERENCES LAYOUTS (layout_id);

ALTER TABLE OPTIONS ADD CONSTRAINT option_quiz_fk FOREIGN KEY (quiz_id) REFERENCES QUIZZES (quiz_id) ON DELETE CASCADE;

ALTER TABLE ANSWERS ADD CONSTRAINT answer_option_fk FOREIGN KEY (option_id) REFERENCES OPTIONS (option_id);

ALTER TABLE ANSWERS ADD CONSTRAINT answer_local_fk FOREIGN KEY (local_id) REFERENCES LOCALS (local_id);

ALTER TABLE APPEALS ADD CONSTRAINT appeal_local_fk FOREIGN KEY (local_id) REFERENCES LOCALS (local_id);

ALTER TABLE RESULTS ADD CONSTRAINT result_option_fk FOREIGN KEY (option_id) REFERENCES OPTIONS (option_id) ON DELETE CASCADE;

ALTER TABLE RESULTS ADD CONSTRAINT result_polygon_fk FOREIGN KEY (polygon_id) REFERENCES POLYGONS (polygon_id);

ALTER TABLE FILES ADD CONSTRAINT file_quiz_fk FOREIGN KEY (quiz_id) REFERENCES QUIZZES ON DELETE CASCADE;

ALTER TABLE ANSWERS ADD CONSTRAINT  answer_quiz_fk FOREIGN KEY (quiz_id) REFERENCES QUIZZES ON DELETE CASCADE;


INSERT INTO USERS VALUES (DEFAULT, 'sysadm', '$2y$12$fa1H8AwmY42d0B3nBAusLOvOdk7O1GGPOKBhkmonqvLCMAM8IszAy', 'SUPER_ADMIN', 'ACTIVE');





