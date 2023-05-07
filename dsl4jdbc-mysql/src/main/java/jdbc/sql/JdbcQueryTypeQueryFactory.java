package jdbc.sql;

import github.alittlehuang.sql4j.dsl.builder.ResultBuilder;
import github.alittlehuang.sql4j.dsl.support.QuerySpecification;
import github.alittlehuang.sql4j.dsl.support.ResultBuilderFactory;
import github.alittlehuang.sql4j.dsl.support.builder.projection.meta.ProjectionMetaProvider;
import github.alittlehuang.sql4j.dsl.support.builder.projection.ProjectionResultBuilder;
import github.alittlehuang.sql4j.dsl.util.Tuple;

public class JdbcQueryTypeQueryFactory implements ResultBuilderFactory {
    private final PreparedSqlExecutor executor;
    private final SqlBuilderFactory sqlBuilder;
    private final ProjectionMetaProvider metaProvider;

    public JdbcQueryTypeQueryFactory(PreparedSqlExecutor executor,
                                     SqlBuilderFactory sqlBuilder) {
        this.executor = executor;
        this.sqlBuilder = sqlBuilder;
        metaProvider = null;
    }

    public JdbcQueryTypeQueryFactory(PreparedSqlExecutor executor,
                                     SqlBuilderFactory sqlBuilder,
                                     ProjectionMetaProvider metaProvider) {
        this.executor = executor;
        this.sqlBuilder = sqlBuilder;
        this.metaProvider = metaProvider;
    }

    @Override
    public <T> ResultBuilder<T> getEntityResultQuery(QuerySpecification criteria, Class<T> type) {
        return new JdbcEntityResultBuilder<>(executor, sqlBuilder.get(criteria, type), type);
    }

    @Override
    public <T, R> ResultBuilder<R> getProjectionQuery(QuerySpecification spec, Class<T> type, Class<R> projectionType) {
        return new ProjectionResultBuilder<>(this, spec, type, projectionType, metaProvider);
    }

    @Override
    public ResultBuilder<Tuple> getObjectsTypeQuery(QuerySpecification criteria, Class<?> type) {
        return new JdbcObjectsResultBuilder(executor, sqlBuilder.get(criteria, type), type);
    }

}
