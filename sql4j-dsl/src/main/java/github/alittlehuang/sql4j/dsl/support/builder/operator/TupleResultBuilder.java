package github.alittlehuang.sql4j.dsl.support.builder.operator;

import github.alittlehuang.sql4j.dsl.builder.LockModeType;
import github.alittlehuang.sql4j.dsl.builder.ResultBuilder;
import github.alittlehuang.sql4j.dsl.support.builder.QuerySupport;
import github.alittlehuang.sql4j.dsl.util.Tuple;

import java.util.List;

public class TupleResultBuilder implements ResultBuilder<Tuple> {

    private final QuerySupport<?> support;

    public TupleResultBuilder(QuerySupport<?> support) {
        this.support = support;
    }


    @Override
    public List<Tuple> getList(int offset, int maxResult, LockModeType lockModeType) {
        return support.getTypeQueryFactory()
                .getObjectsTypeQuery(support.buildQuerySpec(), support.getType())
                .getList(offset, maxResult, lockModeType);
    }


    @Override
    public int count() {
        return support.getTypeQueryFactory()
                .getEntityResultQuery(support.buildQuerySpec(), support.getType())
                .count();
    }


    @Override
    public boolean exist(int offset) {
        return support.getTypeQueryFactory()
                .getEntityResultQuery(support.buildQuerySpec(), support.getType())
                .exist(offset);
    }
}
