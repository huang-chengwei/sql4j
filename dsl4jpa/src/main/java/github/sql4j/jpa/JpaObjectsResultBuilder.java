package github.sql4j.jpa;

import github.sql4j.dsl.builder.ResultBuilder;
import github.sql4j.dsl.support.StructuredQuery;

import jakarta.persistence.EntityManager;
import java.util.List;

public class JpaObjectsResultBuilder<T> extends JpaResultQuery<T> implements ResultBuilder<Object[]> {

    public JpaObjectsResultBuilder(EntityManager entityManager, Class<T> type, StructuredQuery criteria) {
        super(entityManager, type, criteria);
    }

    @Override
    public List<Object[]> getList(int offset, int maxResult) {
        return new Builder<>(Object[].class).getObjectsList(offset, maxResult);
    }


}
