package jdbc;


import github.alittlehuang.sql4j.dsl.support.ResultQueryFactory;
import github.alittlehuang.sql4j.dsl.support.builder.AbstractQueryBuilder;
import jdbc.sql.*;

public class JdbcQueryBuilder extends AbstractQueryBuilder {

    public JdbcQueryBuilder(ResultQueryFactory typeQueryFactory) {
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
