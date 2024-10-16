CREATE TABLE scheduled_tasks
(
    task_name varchar(100) NOT NULL,
    task_instance varchar(100) NOT NULL,
    task_data bytea,
    execution_time TIMESTAMPTZ,
    picked boolean,
    picked_by varchar(50),
    last_success TIMESTAMPTZ,
    last_failure TIMESTAMPTZ,
    consecutive_failures INT,
    last_heartbeat TIMESTAMPTZ,
    version BIGINT,
    PRIMARY KEY (task_name, task_instance)
);