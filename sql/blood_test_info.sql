DROP TABLE IF EXISTS BLOOD_GLUCOSE_INFO;

CREATE TABLE BLOOD_GLUCOSE_INFO
(
  BLOOD_TEST_ID SERIAL NOT NULL,
  USER_ID BIGINT NOT NULL,
  PRE_FAST BIGINT,
  POST_FAST BIGINT,
  GLUCOSE_TEST_DATE TIMESTAMP DEFAULT NOW(),
  NOTEs CHARACTER VARYING(256),
  IS_FASTING INTEGER,
  GLUCOSE_LEVEL BIGINT
)

