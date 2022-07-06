package jdbc;


import github.sql4j.dsl.support.TypeQueryFactory;
import github.sql4j.dsl.support.builder.query.AbstractQueryBuilder;
import jdbc.sql.*;

public class JdbcQueryBuilder extends AbstractQueryBuilder {

    public JdbcQueryBuilder(TypeQueryFactory typeQueryFactory) {
        super(typeQueryFactory);
    }

    public JdbcQueryBuilder(PreparedSqlExecutor executor,
                            SqlBuilderFactory sqlBuilderFactory) {
        this(new JdbcQueryTypeQueryFactory(executor, sqlBuilderFactory));
    }


    public JdbcQueryBuilder(SqlExecutor sqlExecutor,
                            SqlBuilderFactory sqlBuilderFactory) {
        this(new SqlExecutorImpl(sqlExecutor), sqlBuilderFactory);
    }

}
