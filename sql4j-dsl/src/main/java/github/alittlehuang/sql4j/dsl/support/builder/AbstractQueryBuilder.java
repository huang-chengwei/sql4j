package github.alittlehuang.sql4j.dsl.support.builder;

import github.alittlehuang.sql4j.dsl.QueryBuilder;
import github.alittlehuang.sql4j.dsl.builder.Query;
import github.alittlehuang.sql4j.dsl.support.Configure;
import github.alittlehuang.sql4j.dsl.support.ResultQueryFactory;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

@Setter
public abstract class AbstractQueryBuilder implements QueryBuilder {

    private final ResultQueryFactory typeQueryFactory;
    private Configure configure = Configure.DEFAULT;

    public AbstractQueryBuilder(ResultQueryFactory typeQueryFactory) {
        this.typeQueryFactory = typeQueryFactory;
    }

    @Override
    public <T> Query<T> query(Class<T> type) {
        QuerySupport<T> support = QuerySupport.of(type, typeQueryFactory, configure);
        return query(support);
    }

    @NotNull
    public static <T> DefaultQuery<T> query(QuerySupport<T> support) {
        return new DefaultQuery<>(support);
    }

}
