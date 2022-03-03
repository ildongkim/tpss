/* 파일업로드 */
CREATE TABLE comtnsample (
	cntry CHARACTER VARYING (50) NOT NULL,
	[name] CHARACTER VARYING (20) NOT NULL,
	RANK CHARACTER VARYING (10),
	birth CHARACTER VARYING (10),
	phone CHARACTER VARYING (15),
	sfile BLOB,
	sfilename CHARACTER VARYING (255),
	sfilesize CHARACTER VARYING (255),
	sfiletype CHARACTER VARYING (255),
	frst_regist_pnttm DATETIME,
	frst_register_id CHARACTER VARYING (60),
	last_updt_pnttm DATETIME,
	last_updusr_id CHARACTER VARYING (60),
	CONSTRAINT pk PRIMARY KEY(cntry, [name])
)
COLLATE utf8_bin
REUSE_OID;