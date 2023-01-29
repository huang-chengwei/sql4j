package github.sql4j.jpa;

import github.sql4j.dsl.builder.ResultBuilder;
import github.sql4j.dsl.support.StructuredQuery;
import github.sql4j.dsl.support.domain.TupleImpl;
import github.sql4j.dsl.util.Tuple;
import javax.persistence.EntityManager;

import java.util.List;
import java.util.stream.Collectors;

public class JpaObjectsResultBuilder<T> extends JpaResultQuery<T> implements ResultBuilder<Tuple> {

    public JpaObjectsResultBuilder(EntityManager entityManager, Class<T> type, StructuredQuery criteria) {
        super(entityManager, type, criteria);
    }

    @Override
    public List<Tuple> getList(int offset, int maxResult) {
        return new Builder<>(Object[].class)
                .getObjectsList(offset, maxResult)
                .stream()
                .map(TupleImpl::new)
                .collect(Collectors.toList());
    }


}
