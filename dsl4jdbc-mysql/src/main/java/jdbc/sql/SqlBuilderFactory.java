package jdbc.sql;


import github.alittlehuang.sql4j.dsl.support.QuerySpecification;

public interface SqlBuilderFactory {

    PreparedSqlBuilder get(QuerySpecification criteria, Class<?> type);

}
