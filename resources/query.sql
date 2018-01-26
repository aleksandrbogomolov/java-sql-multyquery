select max(code || ' ' || fullname) from warehouse where id_controwner is not null;
SELECT
  status,
  count(status)
FROM rpl_user.fde_vetrequest_queue
WHERE status IN (0, 1, 2, 3, 4, 5)
      AND create_date > sysdate - 7
GROUP BY status
ORDER BY status