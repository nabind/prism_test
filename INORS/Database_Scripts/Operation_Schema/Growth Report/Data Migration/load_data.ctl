load data
 infile 'D:\PDocs\INORS\Growth Reports\Data Migration\Mig_User_Selection_Lookup.csv'
 APPEND
 into table MIG_GRW_USER_SELECTION_LOOKUP
 fields terminated by "," optionally enclosed by '"'
 TRAILING NULLCOLS
 (USERID,
CUST_PROD_ID,
GRADEID,
SUBTESTID,
DATETIMESTAMP)