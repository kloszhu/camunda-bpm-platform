--https://app.camunda.com/jira/browse/CAM-8485
drop index ACT_IDX_HI_ACT_INST_STATS;
create index ACT_IDX_HI_ACT_INST_STATS on ACT_HI_ACTINST(PROC_DEF_ID_, ACT_ID_, END_TIME_, START_TIME_, ACT_INST_STATE_);
