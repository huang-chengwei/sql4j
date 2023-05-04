package jdbc.sql;

import github.alittlehuang.sql4j.dsl.builder.ResultBuilder;
import github.alittlehuang.sql4j.dsl.support.QuerySpecification;
import github.alittlehuang.sql4j.dsl.support.ResultQueryFactory;
import github.alittlehuang.sql4j.dsl.support.builder.ProjectionResultBuilder;
import github.alittlehuang.sql4j.dsl.util.Tuple;

public class JdbcQueryTypeQueryFactory implements ResultQueryFactory {
    private final PreparedSqlExecutor executor;
    private final SqlBuilderFactory sqlBuilder;

    public JdbcQueryTypeQueryFactory(PreparedSqlExecutor executor,
                                     SqlBuilderFactory sqlBuilder) {
        this.executor = executor;
        this.sqlBuilder = sqlBuilder;
    }

    @Override
    public <T> ResultBuilder<T> getEntityResultQuery(QuerySpecification criteria, Class<T> type) {
        return new JdbcEntityResultBuilder<>(executor, sqlBuilder.get(criteria, type), type);
    }

    @Override
    public <T, R> ResultBuilder<R> getProjectionQuery(QuerySpecification criteriaQuery, Class<T> type, Class<R> projectionType) {
        return new ProjectionResultBuilder<>(this, criteriaQuery, type, projectionType);
    }

    @Override
    public ResultBuilder<Tuple> getObjectsTypeQuery(QuerySpecification criteria, Class<?> type) {
        return new JdbcObjectsResultBuilder(executor, sqlBuilder.get(criteria, type), type);
    }

}
