INSERT INTO USER VALUES (1, sysdate, 'User1', 'test1', '701211-1234992');

INSERT INTO USER VALUES (2, sysdate, 'User2', 'test2', '801211-2234992');

INSERT INTO USER VALUES (3, sysdate, 'User3','test3', '921211-1044992');

-- 우선순위 Hibernate > Script

-- Hibernate (JPA) <-> import.sql
-- spring.jpa.generate-ddl=true
-- spring.jpa.hibernate.ddl-auto=create-drop

-- script --> schema.sql, data.sql
-- spring.jpa.generate-ddl=false
-- spring.jpa.hibernate.ddl-auto=none
-- spring.sql.init.mode=always