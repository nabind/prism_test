update stg_export_job_status j
   set j.org_node_code = j.org_node_code || 'S'
 where j.project_id = 10001 --CHANGE PROJECT_ID
   and length(j.org_node_code) = 2;

update stg_eiss_org_info e
   set e.org_node_code        = e.org_node_code || 'S',
       e.parent_org_node_code = e.parent_org_node_code || 'S',
       e.special_codes        = e.special_codes || 'S'
 where e.project_id = 10001 --CHANGE PROJECT_ID
   and e.org_node_code = e.parent_org_node_code
   and length(e.org_node_code) = 2;
   
select e.parent_org_node_code||'S'||e.org_node_code
UPDATE stg_eiss_org_info e    
       set e.special_codes = e.parent_org_node_code||'S'||e.org_node_code,
       e.parent_org_node_code = e.parent_org_node_code||'S'
where e.project_id = 10001 --CHANGE PROJECT_ID
and   e.org_node_code <> e.parent_org_node_code
and length(e.parent_org_node_code) = 2;

COMMIT;
