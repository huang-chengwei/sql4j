package github.sql4j.dsl;

import github.sql4j.dsl.builder.Query;

public interface QueryBuilder {

    <T> Query<T> query(Class<T> type);

}
