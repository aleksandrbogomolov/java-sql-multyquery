SELECT status, count(*) FROM rpl_user.fde_vetrequest_queue WHERE status IN (1, 2, 3) and enqueue_time <= sysdate - 1/48 and enqueue_time >= sysdate - 2 GROUP BY status

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