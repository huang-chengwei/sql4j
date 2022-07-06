package github.sql4j.jpa;

import github.sql4j.dsl.builder.ResultBuilder;
import github.sql4j.dsl.support.StructuredQuery;

import jakarta.persistence.EntityManager;
import java.util.List;

public class JpaEntityResultBuilder<T> extends JpaResultQuery<T> implements ResultBuilder<T> {

    public JpaEntityResultBuilder(EntityManager entityManager, Class<T> type, StructuredQuery criteria) {
        super(entityManager, type, criteria);
    }

    @Override
    public List<T> getList(int offset, int maxResult) {
        return new Builder<>(entityType).getResultList(offset, maxResult);
    }

}
