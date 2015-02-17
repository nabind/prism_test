--CUSTOMERID

nvl((select SWAPPED_VALUE_NUM
                     from TASC_LOOKUP_DATA_SWAP
                    where COLUMN_TYPE = 'CUSTOMER_ID'
                      AND PRESENT_VALUE_NUM = CUSTOMERID),
                   CUSTOMERID) CUSTOMERID,

--TP_ID
				   
nvl((select SWAPPED_VALUE_NUM
               from TASC_LOOKUP_DATA_SWAP
              where COLUMN_TYPE = 'TP_ID'
                AND PRESENT_VALUE_NUM = TP_ID),
             TP_ID) TP_ID,

--ADMIN_ID

nvl((select SWAPPED_VALUE_NUM
               from TASC_LOOKUP_DATA_SWAP
              where COLUMN_TYPE = 'ADMIN_ID'
                AND PRESENT_VALUE_NUM = ADMINID),
             ADMINID) ADMINID,			 
			 
--GENDER_ID		 
(nvl((select SWAPPED_VALUE_NUM
                from TASC_LOOKUP_DATA_SWAP
               where COLUMN_TYPE = 'GENDER_ID'
                 AND PRESENT_VALUE_NUM = GENDERID),
              GENDERID)) GENDERID,		

--CONTENT_ID
nvl((select SWAPPED_VALUE_NUM
               from TASC_LOOKUP_DATA_SWAP
              where COLUMN_TYPE = 'CONTENT_ID'
                AND PRESENT_VALUE_NUM = CONTENTID),
             CONTENTID) CONTENTID,
			 
--SUBTEST_ID
nvl((select SWAPPED_VALUE_NUM
               from TASC_LOOKUP_DATA_SWAP
              where COLUMN_TYPE = 'SUBTEST_ID'
                AND PRESENT_VALUE_NUM = SUBTESTID),
             SUBTESTID) SUBTESTID,	

--OBJECTIVEID
nvl((select SWAPPED_VALUE_NUM
               from TASC_LOOKUP_DATA_SWAP
              where COLUMN_TYPE = 'OBJECTIVE_ID'
                AND PRESENT_VALUE_NUM = OBJECTIVEID),
             OBJECTIVEID) OBJECTIVEID,
			 
--ITEMSET_ID
nvl((select SWAPPED_VALUE_NUM
                           from TASC_LOOKUP_DATA_SWAP
                          where COLUMN_TYPE = 'ITEMSET_ID'
                            AND PRESENT_VALUE_NUM = ITEMSETID),
                         ITEMSETID) ITEMSETID,			 