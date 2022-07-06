package jdbc.sql;


import github.sql4j.dsl.builder.ResultBuilder;

import java.util.List;

public class JdbcEntityResultBuilder<T> implements ResultBuilder<T> {

    private final PreparedSqlExecutor executor;
    private final PreparedSqlBuilder builder;
    private final Class<T> entityType;

    public JdbcEntityResultBuilder(PreparedSqlExecutor executor, PreparedSqlBuilder builder, Class<T> entityType) {
        this.executor = executor;
        this.builder = builder;
        this.entityType = entityType;
    }

    @Override
    public int count() {
        return executor.count(builder.count(), entityType);
    }

    @Override
    public List<T> getList(int offset, int maxResult) {
        return executor.getEntityList(builder.getEntityList(offset, maxResult), entityType);
    }

    @Override
    public boolean exist(int offset) {
        return executor.exist(builder.exist(offset), entityType);
    }

}
