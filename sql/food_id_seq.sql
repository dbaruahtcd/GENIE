﻿-- This the auto increment field for the food info table

DROP SEQUENCE IF EXISTS FOOD_ID_SEQ;

CREATE SEQUENCE FOOD_ID_SEQ
	START 11111
	INCREMENT 1
	NO MAXVALUE
	NO MINVALUE
	NO CYCLE
	
