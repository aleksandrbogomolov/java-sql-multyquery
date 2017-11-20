SELECT status, count(*) FROM rpl_user.fde_vetrequest_queue WHERE status IN (1, 2, 3) and enqueue_time <= sysdate - 1/48 and enqueue_time >= sysdate - 2 GROUP BY status
