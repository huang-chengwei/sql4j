package jdbc.sql;

import github.sql4j.dsl.builder.ResultBuilder;
import github.sql4j.dsl.support.StructuredQuery;
import github.sql4j.dsl.support.TypeQueryFactory;

public class JdbcQueryTypeQueryFactory implements TypeQueryFactory {
    private final PreparedSqlExecutor executor;
    private final SqlBuilderFactory sqlBuilder;

    public JdbcQueryTypeQueryFactory(PreparedSqlExecutor executor,
                                     SqlBuilderFactory sqlBuilder) {
        this.executor = executor;
        this.sqlBuilder = sqlBuilder;
    }

    @Override
    public <T> ResultBuilder<T> getEntityResultQuery(StructuredQuery criteria, Class<T> type) {
        return new JdbcEntityResultBuilder<>(executor, sqlBuilder.get(criteria, type), type);
    }

    @Override
    public <T, R> ResultBuilder<R> getProjectionQuery(StructuredQuery criteriaQuery, Class<T> type, Class<R> projectionType) {
        return new ProjectionResultBuilder<>(this, criteriaQuery, type, projectionType);
    }

    @Override
    public ResultBuilder<Object[]> getObjectsTypeQuery(StructuredQuery criteria, Class<?> type) {
        return new JdbcObjectsResultBuilder(executor, sqlBuilder.get(criteria, type), type);
    }

}
