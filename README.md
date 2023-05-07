# sql4j

## Quick start

### META-INF/persistence.xml

```xml
<persistence xmlns="http://java.sun.com/xml/ns/persistence"
             xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
             xsi:schemaLocation="http://java.sun.com/xml/ns/persistence
             http://java.sun.com/xml/ns/persistence/persistence_2_0.xsd"
             version="2.0">
    <persistence-unit name="org.hibernate.jpa">
        <properties>
            <property name="javax.persistence.jdbc.url" value="jdbc:mysql:///sql-dsl"/>
            <property name="javax.persistence.jdbc.driver" value="com.mysql.cj.jdbc.Driver"/>
            <property name="javax.persistence.jdbc.password" value="root"/>
            <property name="javax.persistence.jdbc.user" value="root"/>
            <property name="hibernate.hbm2ddl.auto" value="update"/>
            <property name="hibernate.format_sql" value="true"/>
            <property name="hibernate.show_sql" value="true"/>
            <property name="hibernate.jdbc.batch_size" value="10000"/>
            <property name="hibernate.physical_naming_strategy"
                      value="org.hibernate.boot.model.naming.CamelCaseToUnderscoresNamingStrategy"/>
        </properties>

    </persistence-unit>
</persistence>
```

### logback.xml
```xml
<?xml version="1.0" encoding="UTF-8"?>
<configuration>
    <appender name="STDOUT" class="ch.qos.logback.core.ConsoleAppender"/>
</configuration>
```

### database

```sql
SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for company
-- ----------------------------
DROP TABLE IF EXISTS `company`;
CREATE TABLE `company`  (
  `id` int(11) NOT NULL,
  `addr` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of company
-- ----------------------------
INSERT INTO `company` VALUES (1, 'ShangHai', 'Microsoft');

-- ----------------------------
-- Table structure for employee
-- ----------------------------
DROP TABLE IF EXISTS `employee`;
CREATE TABLE `employee`  (
  `id` int(11) NOT NULL,
  `age` int(11) NULL DEFAULT NULL,
  `company_id` int(11) NULL DEFAULT NULL,
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `FK2yuxsfrkkrnkn5emoobcnnc3r`(`company_id`) USING BTREE,
  CONSTRAINT `FK_company_id` FOREIGN KEY (`company_id`) REFERENCES `company` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of employee
-- ----------------------------
INSERT INTO `employee` VALUES (1, 18, 1, 'Luna');

SET FOREIGN_KEY_CHECKS = 1;
```

### github/alittlehuang/sql4j/example/Company.java

```java
package github.alittlehuang.sql4j.example;

import github.alittlehuang.sql4j.dsl.expression.path.Persistable;
import javax.persistence.Entity;
import javax.persistence.Id;
import lombok.Data;

@Data
@Entity
public class Company implements Persistable {

    @Id
    private Integer id;
    private String name;
    private String addr;

}
```

### github/alittlehuang/sql4j/example/Employee.java
```java
package github.alittlehuang.sql4j.example;

import javax.persistence.*;
import lombok.Data;

@Data
@Entity
public class Employee {
    @Id
    private Integer id;
    private String name;
    private Integer age;
    private Integer companyId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "companyId", updatable = false, insertable = false)
    private Company company;
}
```

### github/alittlehuang/sql4j/example/Example.java

```java
package github.alittlehuang.sql4j.example;

import github.alittlehuang.sql4j.dsl.QueryBuilder;
import github.alittlehuang.sql4j.dsl.builder.Query;
import github.alittlehuang.sql4j.jpa.JpaQueryBuilder;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class Example {

    public static void main(String[] args) {

        try (EntityManagerFactory factory = Persistence.createEntityManagerFactory("org.hibernate.jpa")) {
            EntityManager em = factory.createEntityManager();
            QueryBuilder builder = new JpaQueryBuilder(em);
            Query<Employee> query = builder.query(Employee.class);
            runExample(query);
        }
    }

    private static void runExample(Query<Employee> query) {

        // select * from employee where id = 1
        query.where(Employee::getId).eq(1).getSingle();


        // select employee.*, company.* from
        // employee left join company
        // on employee.company_id = company.id
        // where employee.id = 1
        query.where(Employee::getId).eq(1)
                .fetch(Employee::getCompany)
                .getSingle();

        // select * from employee where name = 'Luna' and age > 10
        query.where(Employee::getName).eq("Luna")
                .and(Employee::getAge).gt(10)
                .getList();

        // select * from employee where name = 'Luna' and age > 10 order by id desc limit 0,100
        query.where(Employee::getName).eq("Luna")
                .and(Employee::getAge).gt(10)
                .orderBy(Employee::getId).desc()
                .getList(0, 100);

        // select employee.* from
        // employee left join company
        // on employee.company_id = company.id
        // where company.name = 'Microsoft'
        query.where(Employee::getCompany).map(Company::getName).eq("Microsoft").getList();
    }

}

```
