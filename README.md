# Prism

Objective
To create a common reporting system that can accommodate almost any type of reports with high performance and minimal development effort.

Technology stack:
PRISM application is built with Spring 3.1, Java 1.6, Jaspersoft 5.5, Maven 3, Oracle 11g, Informatica 9 (ETL), AWS Elasticache with Memcached, AWS Simple Queue Service,  AWS S3 for storage, Tomcat 7

Deployment architecture:
PRISM application is deployed in Amazon Cloud in auto-scaling environment into multiple availability zone 

Application:
A single codebase is used for all contracts. All contracts shares common DB structures. Any new contract can be created and associated with PRISM with minimal development effort. All menus and most of the content of web page is configurable. PRISM application is integrated with jasper reports seamlessly fro delivering high-end reports.

Data Warehousing:
Like all common data warehousing model, PRISM uses Dimensional data model with STAR schema. Common tables (for META data) are kept separate and for dynamic data each contract has individual schemas.

Dimension
Slow changing objects are kept in dimensional (DIM) tables. Like, organization hierarchy – where attributes are like State, District, School and the hierarchy relation State  District  School  Class.

Fact Table
Fast changing objects are kept in FACT tables. Like, student’s score table – where scale score, proficiency level, etc. as measures

Lookup Table
These tables holds relations between multiple dim tables

PRISM uses STAR or Snowflake schema where fact table sits in the middle and is radically connected to other dimension tables. Here is an example
