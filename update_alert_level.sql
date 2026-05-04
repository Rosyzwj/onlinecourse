ALTER TABLE homework_alert
    ADD COLUMN alert_level INT(1) NOT NULL DEFAULT 0
        COMMENT '预警等级(0=逾期触发旧逻辑,1=关注,2=预警,3=危险)',
    ADD COLUMN risk_score INT(3) NOT NULL DEFAULT 0
        COMMENT '风险评分(0-100)';