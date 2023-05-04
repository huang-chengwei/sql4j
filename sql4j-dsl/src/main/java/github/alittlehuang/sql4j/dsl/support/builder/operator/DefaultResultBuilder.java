package github.alittlehuang.sql4j.dsl.support.builder.operator;

import github.alittlehuang.sql4j.dsl.builder.ResultBuilder;
import github.alittlehuang.sql4j.dsl.support.builder.QuerySupport;
import jakarta.persistence.LockModeType;

import java.util.List;

public class DefaultResultBuilder<T> implements ResultBuilder<T> {
    private final QuerySupport<T> structured;

    public DefaultResultBuilder(QuerySupport<T> structured) {
        this.structured = structured;
    }

    @Override
    public int count() {
        return structured.getTypeQueryFactory().getEntityResultQuery(structured.buildQuerySpec(), structured.getType()).count();
    }

    @Override
    public List<T> getList(int offset, int maxResult, LockModeType lockModeType) {
        return structured.getTypeQueryFactory()
                .getEntityResultQuery(structured.buildQuerySpec(), structured.getType())
                .getList(offset, maxResult, lockModeType);
    }

    @Override
    public boolean exist(int offset) {
        return structured.getTypeQueryFactory()
                .getEntityResultQuery(structured.buildQuerySpec(), structured.getType())
                .exist(offset);
    }
}
