--Check the max id of those table then drop and create the new sequences
select max(article_content_id) from article_content; --1392
select article_content_seq.nextval from dual; --328

select max(articleid) from article_metadata; --1484
select article_metadata_seq.nextval from dual; --764

drop sequence ARTICLE_CONTENT_SEQ;

create sequence ARTICLE_CONTENT_SEQ
minvalue 1
maxvalue 9999999999999999999999999999
start with 1393
increment by 1
nocache;

drop sequence Article_Metadata_Seq;

create sequence ARTICLE_METADATA_SEQ
minvalue 1
maxvalue 9999999999999999999999999999
start with 1485
increment by 1
nocache;
