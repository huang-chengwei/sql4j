package github.sql4j.test;

import com.mysql.cj.jdbc.MysqlDataSource;
import github.alittlehuang.sql4j.dsl.QueryBuilder;
import github.sql4j.test.entity.User;
import jdbc.JdbcQueryBuilder;
import jdbc.mysql.MysqlSqlBuilder;
import jdbc.sql.SqlExecutor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;


@Slf4j
public class JdbcTest extends JpaTest {

    @BeforeAll
    public static void init() {
        JpaTest.init();
        MysqlDataSource source = new MysqlDataSource();
        source.setUrl("jdbc:mysql:///sql-dsl");
        source.setUser("root");
        source.setPassword("root");
        QueryBuilder queryBuilder = new JdbcQueryBuilder(SqlExecutor.fromDatasource(source), MysqlSqlBuilder::new);
        userQuery = queryBuilder.query(User.class);
    }
}
