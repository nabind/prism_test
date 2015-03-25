

ALTER TABLE ITEMSET_DIM DROP CONSTRAINT CHK_ITEM_TYPE;

alter table ITEMSET_DIM
  add constraint CHK_ITEM_TYPE
  check (ITEM_TYPE IN ('SR', 'CR', 'GR','OBJ','TE'));
  
  
insert into ITEMSET_DIM (ITEMSETID, ITEM_NAME, ITEM_CODE, SESSION_ID, ITEM_SEQ, POINT_POSSIBLE, ITEM_TYPE, PDF_FILENAME, SUBT_OBJ_MAPID, SUBTESTID, ITEM_NUMBER, ITEM_PART, MODEULEID, PROJECTID, DATETIMESTAMP)
values (10001, 'Item Responses for MC -- ELA', '1001', null, 1, null, 'SR', null, null, 2054, null, null, null, 3, SYSDATE);
insert into ITEMSET_DIM (ITEMSETID, ITEM_NAME, ITEM_CODE, SESSION_ID, ITEM_SEQ, POINT_POSSIBLE, ITEM_TYPE, PDF_FILENAME, SUBT_OBJ_MAPID, SUBTESTID, ITEM_NUMBER, ITEM_PART, MODEULEID, PROJECTID, DATETIMESTAMP)
values (10002, 'Item Scores for MC -- ELA', '1002', null, 2, null, 'SR', null, null, 2054, null, null, null, 3, SYSDATE);
insert into ITEMSET_DIM (ITEMSETID, ITEM_NAME, ITEM_CODE, SESSION_ID, ITEM_SEQ, POINT_POSSIBLE, ITEM_TYPE, PDF_FILENAME, SUBT_OBJ_MAPID, SUBTESTID, ITEM_NUMBER, ITEM_PART, MODEULEID, PROJECTID, DATETIMESTAMP)
values (10003, 'Raw Score for MC Items -- ELA', '1003', null, 3, null, 'SR', null, null, 2054, null, null, null, 3, SYSDATE);
insert into ITEMSET_DIM (ITEMSETID, ITEM_NAME, ITEM_CODE, SESSION_ID, ITEM_SEQ, POINT_POSSIBLE, ITEM_TYPE, PDF_FILENAME, SUBT_OBJ_MAPID, SUBTESTID, ITEM_NUMBER, ITEM_PART, MODEULEID, PROJECTID, DATETIMESTAMP)
values (10004, 'Item Scores CR scores -- ELA', '1004', null, 4, null, 'CR', null, null, 2054, null, null, null, 3, SYSDATE);
insert into ITEMSET_DIM (ITEMSETID, ITEM_NAME, ITEM_CODE, SESSION_ID, ITEM_SEQ, POINT_POSSIBLE, ITEM_TYPE, PDF_FILENAME, SUBT_OBJ_MAPID, SUBTESTID, ITEM_NUMBER, ITEM_PART, MODEULEID, PROJECTID, DATETIMESTAMP)
values (10005, 'Raw Score for CR Items -- ELA', '1005', null, 5, null, 'CR', null, null, 2054, null, null, null, 3, SYSDATE);
insert into ITEMSET_DIM (ITEMSETID, ITEM_NAME, ITEM_CODE, SESSION_ID, ITEM_SEQ, POINT_POSSIBLE, ITEM_TYPE, PDF_FILENAME, SUBT_OBJ_MAPID, SUBTESTID, ITEM_NUMBER, ITEM_PART, MODEULEID, PROJECTID, DATETIMESTAMP)
values (10006, 'Item score for TE -- ELA', '1006', null, 6, null, 'TE', null, null, 2054, null, null, null, 3, SYSDATE);
insert into ITEMSET_DIM (ITEMSETID, ITEM_NAME, ITEM_CODE, SESSION_ID, ITEM_SEQ, POINT_POSSIBLE, ITEM_TYPE, PDF_FILENAME, SUBT_OBJ_MAPID, SUBTESTID, ITEM_NUMBER, ITEM_PART, MODEULEID, PROJECTID, DATETIMESTAMP)
values (10007, 'Raw Score for TE Items -- ELA', '1007', null, 7, null, 'TE', null, null, 2054, null, null, null, 3, SYSDATE);
insert into ITEMSET_DIM (ITEMSETID, ITEM_NAME, ITEM_CODE, SESSION_ID, ITEM_SEQ, POINT_POSSIBLE, ITEM_TYPE, PDF_FILENAME, SUBT_OBJ_MAPID, SUBTESTID, ITEM_NUMBER, ITEM_PART, MODEULEID, PROJECTID, DATETIMESTAMP)
values (10008, 'Item Responses for MC -- MA', '2001', null, 8, null, 'SR', null, null, 2055, null, null, null, 3, SYSDATE);
insert into ITEMSET_DIM (ITEMSETID, ITEM_NAME, ITEM_CODE, SESSION_ID, ITEM_SEQ, POINT_POSSIBLE, ITEM_TYPE, PDF_FILENAME, SUBT_OBJ_MAPID, SUBTESTID, ITEM_NUMBER, ITEM_PART, MODEULEID, PROJECTID, DATETIMESTAMP)
values (10009, 'Item Scores for MC -- MA', '2002', null, 9, null, 'SR', null, null, 2055, null, null, null, 3, SYSDATE);
insert into ITEMSET_DIM (ITEMSETID, ITEM_NAME, ITEM_CODE, SESSION_ID, ITEM_SEQ, POINT_POSSIBLE, ITEM_TYPE, PDF_FILENAME, SUBT_OBJ_MAPID, SUBTESTID, ITEM_NUMBER, ITEM_PART, MODEULEID, PROJECTID, DATETIMESTAMP)
values (10010, 'Raw Score for MC Items -- MA', '2003', null, 10, null, 'SR', null, null, 2055, null, null, null, 3, SYSDATE);
insert into ITEMSET_DIM (ITEMSETID, ITEM_NAME, ITEM_CODE, SESSION_ID, ITEM_SEQ, POINT_POSSIBLE, ITEM_TYPE, PDF_FILENAME, SUBT_OBJ_MAPID, SUBTESTID, ITEM_NUMBER, ITEM_PART, MODEULEID, PROJECTID, DATETIMESTAMP)
values (10011, 'Item Scores CR scores -- MA', '2004', null, 11, null, 'CR', null, null, 2055, null, null, null, 3, SYSDATE);
insert into ITEMSET_DIM (ITEMSETID, ITEM_NAME, ITEM_CODE, SESSION_ID, ITEM_SEQ, POINT_POSSIBLE, ITEM_TYPE, PDF_FILENAME, SUBT_OBJ_MAPID, SUBTESTID, ITEM_NUMBER, ITEM_PART, MODEULEID, PROJECTID, DATETIMESTAMP)
values (10012, 'Raw Score for CR Items -- MA', '2005', null, 12, null, 'CR', null, null, 2055, null, null, null, 3, SYSDATE);
insert into ITEMSET_DIM (ITEMSETID, ITEM_NAME, ITEM_CODE, SESSION_ID, ITEM_SEQ, POINT_POSSIBLE, ITEM_TYPE, PDF_FILENAME, SUBT_OBJ_MAPID, SUBTESTID, ITEM_NUMBER, ITEM_PART, MODEULEID, PROJECTID, DATETIMESTAMP)
values (10013, 'Item score for TE -- MA', '2006', null, 13, null, 'TE', null, null, 2055, null, null, null, 3, SYSDATE);
insert into ITEMSET_DIM (ITEMSETID, ITEM_NAME, ITEM_CODE, SESSION_ID, ITEM_SEQ, POINT_POSSIBLE, ITEM_TYPE, PDF_FILENAME, SUBT_OBJ_MAPID, SUBTESTID, ITEM_NUMBER, ITEM_PART, MODEULEID, PROJECTID, DATETIMESTAMP)
values (10014, 'Raw Score for TE Items -- MA', '2007', null, 14, null, 'TE', null, null, 2055, null, null, null, 3, SYSDATE);
insert into ITEMSET_DIM (ITEMSETID, ITEM_NAME, ITEM_CODE, SESSION_ID, ITEM_SEQ, POINT_POSSIBLE, ITEM_TYPE, PDF_FILENAME, SUBT_OBJ_MAPID, SUBTESTID, ITEM_NUMBER, ITEM_PART, MODEULEID, PROJECTID, DATETIMESTAMP)
values (10015, 'Item Responses for MC -- SCI', '3001', null, 15, null, 'SR', null, null, 2056, null, null, null, 3, SYSDATE);
insert into ITEMSET_DIM (ITEMSETID, ITEM_NAME, ITEM_CODE, SESSION_ID, ITEM_SEQ, POINT_POSSIBLE, ITEM_TYPE, PDF_FILENAME, SUBT_OBJ_MAPID, SUBTESTID, ITEM_NUMBER, ITEM_PART, MODEULEID, PROJECTID, DATETIMESTAMP)
values (10016, 'Item Scores for MC -- SCI', '3002', null, 16, null, 'SR', null, null, 2056, null, null, null, 3, SYSDATE);
insert into ITEMSET_DIM (ITEMSETID, ITEM_NAME, ITEM_CODE, SESSION_ID, ITEM_SEQ, POINT_POSSIBLE, ITEM_TYPE, PDF_FILENAME, SUBT_OBJ_MAPID, SUBTESTID, ITEM_NUMBER, ITEM_PART, MODEULEID, PROJECTID, DATETIMESTAMP)
values (10017, 'Raw Score for MC Items -- SCI', '3003', null, 17, null, 'SR', null, null, 2056, null, null, null, 3, SYSDATE);
insert into ITEMSET_DIM (ITEMSETID, ITEM_NAME, ITEM_CODE, SESSION_ID, ITEM_SEQ, POINT_POSSIBLE, ITEM_TYPE, PDF_FILENAME, SUBT_OBJ_MAPID, SUBTESTID, ITEM_NUMBER, ITEM_PART, MODEULEID, PROJECTID, DATETIMESTAMP)
values (10018, 'Item Scores CR scores -- SCI', '3004', null, 18, null, 'CR', null, null, 2056, null, null, null, 3, SYSDATE);
insert into ITEMSET_DIM (ITEMSETID, ITEM_NAME, ITEM_CODE, SESSION_ID, ITEM_SEQ, POINT_POSSIBLE, ITEM_TYPE, PDF_FILENAME, SUBT_OBJ_MAPID, SUBTESTID, ITEM_NUMBER, ITEM_PART, MODEULEID, PROJECTID, DATETIMESTAMP)
values (10019, 'Raw Score for CR Items -- SCI', '3005', null, 19, null, 'CR', null, null, 2056, null, null, null, 3, SYSDATE);
insert into ITEMSET_DIM (ITEMSETID, ITEM_NAME, ITEM_CODE, SESSION_ID, ITEM_SEQ, POINT_POSSIBLE, ITEM_TYPE, PDF_FILENAME, SUBT_OBJ_MAPID, SUBTESTID, ITEM_NUMBER, ITEM_PART, MODEULEID, PROJECTID, DATETIMESTAMP)
values (10020, 'Item score for TE -- SCI', '3006', null, 20, null, 'TE', null, null, 2056, null, null, null, 3, SYSDATE);
insert into ITEMSET_DIM (ITEMSETID, ITEM_NAME, ITEM_CODE, SESSION_ID, ITEM_SEQ, POINT_POSSIBLE, ITEM_TYPE, PDF_FILENAME, SUBT_OBJ_MAPID, SUBTESTID, ITEM_NUMBER, ITEM_PART, MODEULEID, PROJECTID, DATETIMESTAMP)
values (10021, 'Raw Score for TE Items -- SCI', '3007', null, 21, null, 'TE', null, null, 2056, null, null, null, 3, SYSDATE);
commit;