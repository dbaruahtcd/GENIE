﻿/*This is the auto increament field for the id's of the doctor*/

DROP SEQUENCE IF EXISTS DOCTOR_ID_SEQ;

CREATE SEQUENCE DOCTOR_ID_SEQ
	START 12345
	INCREMENT 1
	NO MINVALUE
	NO MAXVALUE
	NO CYCLE;