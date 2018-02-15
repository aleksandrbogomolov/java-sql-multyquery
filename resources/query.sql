select max(code || ' ' || fullname) from warehouse where id_controwner is not null;
select status, trunc(cast(create_date as timestamp), 'HH') date_hour, count(id_queue) requests_cnt,
          trunc(cast(lastdate as timestamp), 'HH') lastdate, min(id_queue) min_request_id, max(id_queue) max_request_id
   from rpl_user.fde_vetrequest_queue
 where enqueue_time >= sysdate - 1 and enqueue_time <= sysdate - 1/24 and status in (0, 1, 2, 3)
 group by status, trunc(cast(create_date as timestamp), 'HH'), trunc(cast(lastdate as timestamp), 'HH')
 order by status, trunc(cast(create_date as timestamp), 'HH')