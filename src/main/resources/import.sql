-- 우선순위 Hibernate > Script

-- Hibernate (JPA) <-> import.sql
-- spring.jpa.generate-ddl=true
-- spring.jpa.hibernate.ddl-auto=create-drop

-- script --> schema.sql, data.sql
-- spring.jpa.generate-ddl=false
-- spring.jpa.hibernate.ddl-auto=none
-- spring.sql.init.mode=always

-- http://localhost:8088/h2-console

INSERT INTO USER VALUES (90001, sysdate, 'Jun', 'test1', '701211-1234992');
INSERT INTO USER VALUES (90002, sysdate, 'User2', 'test2', '801211-2234992');
INSERT INTO USER VALUES (90003, sysdate, 'User3','test3', '921211-1044992');

INSERT INTO POST VALUES (10003, 'This is Post',  90002);
INSERT INTO POST (POST_ID, USER_ID, DESCRIPTION) VALUES (10001, 90001, 'My first Post');
INSERT INTO POST (POST_ID, USER_ID, DESCRIPTION) VALUES (10002, 90001, 'My second Post');



