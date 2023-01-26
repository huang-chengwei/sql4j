package jdbc.sql;


import github.sql4j.dsl.builder.ResultBuilder;
import github.sql4j.dsl.util.Tuple;

import java.util.List;

public class JdbcObjectsResultBuilder implements ResultBuilder<Tuple> {

    private final PreparedSqlExecutor executor;
    private final PreparedSqlBuilder builder;
    private final Class<?> entityType;

    public JdbcObjectsResultBuilder(PreparedSqlExecutor executor, PreparedSqlBuilder builder, Class<?> entityType) {
        this.executor = executor;
        this.builder = builder;
        this.entityType = entityType;
    }

    @Override
    public List<Tuple> getList(int offset, int maxResult) {
        return executor.listResult(builder.getObjectsList(offset, maxResult), entityType);
    }

    @Override
    public int count() {
        return executor.count(builder.count(), entityType);
    }

    @Override
    public boolean exist(int offset) {
        return executor.exist(builder.exist(offset), entityType);
    }

}
