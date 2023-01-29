package jdbc.sql;

import github.sql4j.dsl.util.Tuple;

import java.util.List;

public interface PreparedSqlExecutor {

    <T> List<T> getEntityList(SelectedPreparedSql sql, Class<T> entityType);

    List<Tuple> listResult(PreparedSql sql, Class<?> entityType);

    boolean exist(PreparedSql sql, Class<?> entityType);

    int count(PreparedSql sql, Class<?> entityType);

}
