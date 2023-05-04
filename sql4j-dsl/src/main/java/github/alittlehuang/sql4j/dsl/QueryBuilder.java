package github.alittlehuang.sql4j.dsl;

import github.alittlehuang.sql4j.dsl.builder.Query;

public interface QueryBuilder {

    <T> Query<T> query(Class<T> type);

}
