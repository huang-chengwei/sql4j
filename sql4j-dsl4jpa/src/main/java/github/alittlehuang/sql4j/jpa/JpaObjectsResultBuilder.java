package github.alittlehuang.sql4j.jpa;

import github.alittlehuang.sql4j.dsl.builder.LockModeType;
import github.alittlehuang.sql4j.dsl.builder.ResultBuilder;
import github.alittlehuang.sql4j.dsl.support.QuerySpecification;
import github.alittlehuang.sql4j.dsl.support.builder.operator.DefaultTuple;
import github.alittlehuang.sql4j.dsl.util.Tuple;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.stream.Collectors;

public class JpaObjectsResultBuilder<T> extends JpaResultQuery<T> implements ResultBuilder<Tuple> {

    private JpaResultQuery<T>.Builder<Object[]> resultBuilder;

    public JpaObjectsResultBuilder(EntityManager entityManager, Class<T> type, QuerySpecification criteria) {
        super(entityManager, type, criteria);
    }

    @Override
    public List<Tuple> getList(int offset, int maxResult, LockModeType lockModeType) {
        if (resultBuilder == null) {
            resultBuilder = new Builder<Object[]>(Object[].class);
        }
        return resultBuilder
                .buildList(offset, maxResult, LockModeTypeAdapter.of(lockModeType))
                .stream()
                .map(DefaultTuple::new)
                .collect(Collectors.toList());
    }


}
