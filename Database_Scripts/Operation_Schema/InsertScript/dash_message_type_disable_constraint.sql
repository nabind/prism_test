ALTER TABLE
   DASH_MESSAGES
DISABLE CONSTRAINT
   FK_DASH_MESSAGES_3;
   
ALTER TABLE
   DASH_MESSAGE_TYPE
DISABLE CONSTRAINT
   PK_MESSAGE_TYPE;
   
ALTER TABLE
   DASH_MESSAGE_TYPE
DISABLE CONSTRAINT
   DASH_MESSAGE_TYPE_UNIQUE;
   
   