﻿/*this table contains the food information for the user*/

DROP TABLE IF EXISTS FOOD_INFO;

CREATE TABLE FOOD_INFO(
	FOOD_ID BIGINT NOT NULL DEFAULT NEXTVAL('food_id_seq'::regclass), -- STARTS AT 11111
	USER_ID BIGINT NOT NULL,
	TYPE_OF_FOOD CHARACTER VARYING(30),
	ESTIMATED_CALORIE CHARACTER VARYING(20),
	FOOD_ENTRY_TIMESTAMP TIMESTAMP DEFAULT CURRENT_TIMESTAMP
	)


