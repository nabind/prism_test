ALTER TABLE OBJECTIVE_DIM ADD (OBJECTIVE_DESC VARCHAR2(100),DATETIMESTAMP1  DATE);

UPDATE OBJECTIVE_DIM 
  SET DATETIMESTAMP1 = DATETIMESTAMP; 
  
COMMIT;
  
ALTER  TABLE OBJECTIVE_DIM MODIFY DATETIMESTAMP1 DEFAULT SYSDATE  NOT NULL; 
  
ALTER TABLE OBJECTIVE_DIM DROP COLUMN DATETIMESTAMP; 
  
ALTER TABLE OBJECTIVE_DIM RENAME COLUMN DATETIMESTAMP1 TO DATETIMESTAMP;  

--For Science

UPDATE OBJECTIVE_DIM
   SET OBJECTIVE_DESC = 'Standard 1-The Nature of Science and Technology'
 WHERE objective_name = 'Nature of Sci & Tech';
 
UPDATE OBJECTIVE_DIM
   SET OBJECTIVE_DESC = 'Standard 2-Scientific Thinking'
 WHERE objective_name = 'Scientific Thinking';
 
UPDATE OBJECTIVE_DIM
   SET OBJECTIVE_DESC = 'Standard 3-The Physical Setting'
 WHERE objective_name = 'The Physical Setting';
 
UPDATE OBJECTIVE_DIM
   SET OBJECTIVE_DESC = 'Standard 4-The Living Environment'
 WHERE objective_name = 'The Living Environment';
 
UPDATE OBJECTIVE_DIM
   SET OBJECTIVE_DESC = 'Standard 5-The Mathematical World'
 WHERE objective_name = 'The Mathematical World';
 
UPDATE OBJECTIVE_DIM
   SET OBJECTIVE_DESC = 'Standard 7-Common Themes'
 WHERE objective_name = 'Common Themes'; 
 
UPDATE OBJECTIVE_DIM
   SET OBJECTIVE_DESC = 'Standard 2 - Earth Science'
 WHERE objective_name = 'Earth Science'; 
 
-- Grade 6 Science

UPDATE OBJECTIVE_DIM
   SET OBJECTIVE_DESC = 'Standard 1 - Physical Science'
 WHERE objective_name = 'Physical Science';
 
UPDATE OBJECTIVE_DIM
   SET OBJECTIVE_DESC = 'Standard 2 - Earth and Space Science'
 WHERE objective_name = 'Earth & Space Science';
 
UPDATE OBJECTIVE_DIM
   SET OBJECTIVE_DESC = 'Standard 3 - Life Science'
 WHERE objective_name = 'Life Science';
 
UPDATE OBJECTIVE_DIM
   SET OBJECTIVE_DESC = 'Standard 4 - Science, Engineering and Technology'
 WHERE objective_name = 'Science Eng & Tech';
 
UPDATE OBJECTIVE_DIM
   SET OBJECTIVE_DESC = 'Standard 5 - The Nature of Science'
 WHERE objective_name = 'The Nature of Science';
 
UPDATE OBJECTIVE_DIM
   SET OBJECTIVE_DESC = 'Standard 6 - The Design Process'
 WHERE objective_name = 'The Design Process'; 
 
--For ELA

UPDATE OBJECTIVE_DIM
   SET OBJECTIVE_DESC = 'Standard 1-Word Recognition, Fluency, and Vocabulary Development'
 WHERE objective_name = 'Vocabulary';
 
UPDATE OBJECTIVE_DIM
   SET OBJECTIVE_DESC = 'Standard 2-Comprehension and Analysis of Nonfiction and Informational Text'
 WHERE objective_name = 'Nonfiction/Info Text†';
 
UPDATE OBJECTIVE_DIM
   SET OBJECTIVE_DESC = 'Standard 3-Comprehension and Analysis of Literary Text'
 WHERE objective_name = 'Literary Text†';
 
UPDATE OBJECTIVE_DIM
   SET OBJECTIVE_DESC = 'Standard 4 WRITING: Processes and Features'
 WHERE objective_name = 'Writing Process';
 
UPDATE OBJECTIVE_DIM
   SET OBJECTIVE_DESC = 'Standard 5 WRITING: Applications (Different Types of Writing and Their Characteristics)'
 WHERE objective_name = 'Writing Applications';
 
UPDATE OBJECTIVE_DIM
   SET OBJECTIVE_DESC = 'Standard 6 WRITING: English Language Conventions'
 WHERE objective_name = 'Lang. Conventions'; 

--For Mathematics

UPDATE OBJECTIVE_DIM
   SET OBJECTIVE_DESC = 'Standard 1-Number Sense'
 WHERE objective_name = 'Number Sense';
 
UPDATE OBJECTIVE_DIM
   SET OBJECTIVE_DESC = 'Standard 2-Computation'
 WHERE objective_name = 'Computation';
 
UPDATE OBJECTIVE_DIM
   SET OBJECTIVE_DESC = 'Standard 3-Algebra and Functions'
 WHERE objective_name = 'Algebra & Functions';
 
UPDATE OBJECTIVE_DIM
   SET OBJECTIVE_DESC = 'Standard 4-Geometry'
 WHERE objective_name = 'Geometry';
 
UPDATE OBJECTIVE_DIM
   SET OBJECTIVE_DESC = 'Standard 5-Measurement'
 WHERE objective_name = 'Measurement';
 
UPDATE OBJECTIVE_DIM
   SET OBJECTIVE_DESC = 'Standard 6-Problem Solving'
 WHERE objective_name = 'Problem Solving'; 
 
UPDATE OBJECTIVE_DIM
   SET OBJECTIVE_DESC = 'Standard 6-Data Analysis and Probability'
 WHERE objective_name = 'Data Analysis & Prob';  
 
 -- Social Study
 
UPDATE OBJECTIVE_DIM
   SET OBJECTIVE_DESC = 'Standard 1-History'
 WHERE objective_name = 'History';
 
UPDATE OBJECTIVE_DIM
   SET OBJECTIVE_DESC = 'Standard 2-Civics and Government'
 WHERE objective_name = 'Civics & Government'; 
 
UPDATE OBJECTIVE_DIM
   SET OBJECTIVE_DESC = 'Standard 3-Geography'
 WHERE objective_name = 'Geography';
 
UPDATE OBJECTIVE_DIM
   SET OBJECTIVE_DESC = 'Standard 4-Economics'
 WHERE objective_name = 'Economics'; 
 
COMMIT;
 

