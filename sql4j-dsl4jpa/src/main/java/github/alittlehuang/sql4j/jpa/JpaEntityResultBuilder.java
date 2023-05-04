package github.alittlehuang.sql4j.jpa;

import github.alittlehuang.sql4j.dsl.builder.ResultBuilder;
import github.alittlehuang.sql4j.dsl.support.QuerySpecification;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;

import java.util.List;

public class JpaEntityResultBuilder<T> extends JpaResultQuery<T> implements ResultBuilder<T> {

    private JpaResultQuery<T>.Builder<T> resultBuilder;

    public JpaEntityResultBuilder(EntityManager entityManager, Class<T> type, QuerySpecification criteria) {
        super(entityManager, type, criteria);
    }

    @Override
    public List<T> getList(int offset, int maxResult, LockModeType lockModeType) {
        return getResultBuilder().buildList(offset, maxResult, lockModeType);
    }

    private JpaResultQuery<T>.Builder<T> getResultBuilder() {
        if (resultBuilder == null) {
            resultBuilder = new Builder<T>(entityType);
        }
        return resultBuilder;
    }

}
