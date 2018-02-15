SELECT status, count(*) FROM rpl_user.fde_vetrequest_queue WHERE status IN (1, 2, 3) and enqueue_time <= sysdate - 1/24 and enqueue_time >= sysdate - 2 GROUP BY status

SELECT request_type, status, count(*) FROM rpl_user.fde_vetrequest_queue
WHERE status IN (1, 2, 3)
and enqueue_time <= sysdate - 1/48
and enqueue_time >= sysdate - 2
GROUP BY request_type, status

select max(code || ' ' || fullname) from warehouse where id_controwner is not null;
with bad_requests as
(select status, trunc(cast(create_date as timestamp), 'HH') date_hour, count(id_queue) requests_cnt,
  min(id_queue) min_request_id, max(id_queue) max_request_id
  from rpl_user.fde_vetrequest_queue
  where enqueue_time >= sysdate - 1 and enqueue_time <= sysdate - 1 / 48 and status in (0, 1, 2, 3)
  group by status, trunc(cast(create_date as timestamp), 'HH')
  order by status, trunc(cast(create_date as timestamp), 'HH'))
select null status, null date_time, sum(requests_cnt) request_count, null min_id_queue, null max_id_queue
from bad_requests
union all
select * from bad_requests

select count(*) from rpl_user.fde_vetrequest_queue
where status in (1, 2, 3)
and enqueue_time >= trunc(sysdate)

SELECT status, count(*) FROM rpl_user.fde_vetrequest_queue WHERE status IN (1) and enqueue_time >= trunc(sysdate) GROUP BY status

select count(*) from rpl_user.fde_vetrequest_out_qt

select trunc(systimestamp, 'MI') exec_date
      ,count(*) response_cnt
      ,count(*) / 30 response_per_min
      ,max((select fullname
         from wms.warehouse w
        where w.id_controwner is not null)) dc_name
      ,max((select count(*)
         from rpl_user.fde_vetrequest_queue
        where create_date > sysdate - 2
          and status in (0, 1, 2, 3, 5))) queue_size
      ,max((select count(*)
         from vet.form_trans_vet_doc_queue
        where status not in (2, 3))) small_queue_size
  from rpl_user.fde_vetrequest_queue
 where response_time >= sysdate - 1 / 48
   and create_date > sysdate - 7
   and status in (4)

select count(*)
  from rpl_user.fde_vetrequest_queue
 where create_date > sysdate - 7
   and request_type in ('GetStockEntryChangesListRequest',
                        'GetStockEntryListRequest',
                        'GetVetDocumentChangesListRequest',
                        'GetVetDocumentListRequest')
   and length(response_body) >= 50 * 1024 * 1024
