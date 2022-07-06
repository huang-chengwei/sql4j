package jdbc.sql;


import github.sql4j.dsl.support.StructuredQuery;

public interface SqlBuilderFactory {

    PreparedSqlBuilder get(StructuredQuery criteria, Class<?> type);

}
