package github.alittlehuang.sql4j.jpa;

import github.alittlehuang.sql4j.dsl.expression.Expression;
import github.alittlehuang.sql4j.dsl.expression.PathExpression;
import github.alittlehuang.sql4j.dsl.expression.SortSpecification;
import github.alittlehuang.sql4j.dsl.support.QuerySpecification;
import github.alittlehuang.sql4j.dsl.util.Array;
import github.alittlehuang.sql4j.dsl.util.TypeCastUtil;

import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.Tuple;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Fetch;
import javax.persistence.criteria.JoinType;
import java.util.List;
import java.util.stream.Collectors;

public class JpaResultQuery<T> {
    protected final EntityManager entityManager;
    protected final Class<T> entityType;
    protected final QuerySpecification criteria;
    private CountBuilder countBuilder;
    private ExistBuilder existBuilder;

    public JpaResultQuery(EntityManager entityManager, Class<T> type, QuerySpecification criteria) {
        this.entityManager = entityManager;
        this.entityType = type;
        this.criteria = criteria;
    }


    public int count() {
        if (countBuilder == null) {
            countBuilder = new CountBuilder();
        }
        return countBuilder.count().intValue();
    }

    public boolean exist(int offset) {
        if (existBuilder == null) {
            existBuilder = new ExistBuilder(Object.class);
        }
        return existBuilder.exist(offset);
    }

    protected class CountBuilder extends Builder<Long> {

        TypedQuery<Long> typedQuery = getQuery();

        public CountBuilder() {
            super(Long.class);
        }

        public Long count() {
            return typedQuery.getSingleResult();
        }

        private TypedQuery<Long> getQuery() {
            buildWhere();
            query.select(cb.count(root));
            return entityManager.createQuery(query);
        }
    }

    protected class ExistBuilder extends Builder<Object> {

        public ExistBuilder(Class<Object> resultType) {
            super(resultType);
        }

        public boolean exist(int offset) {
            buildWhere();
            query.select(cb.literal(true));
            TypedQuery<Object> typedQuery = entityManager.createQuery(this.query);
            int firstResult = Math.max(offset, 0);
            return !typedQuery.setFirstResult(firstResult)
                    .setMaxResults(1)
                    .getResultList()
                    .isEmpty();
        }
    }


    protected class Builder<R> extends JpaPredicateBuilder<T> {
        protected final Class<R> resultType;
        protected final CriteriaQuery<R> query;

        public Builder(Class<R> resultType) {
            this.resultType = resultType;
            cb = entityManager.getCriteriaBuilder();
            if (resultType == Tuple.class) {
                CriteriaQuery<Tuple> tupleQuery = cb.createTupleQuery();
                this.query = TypeCastUtil.cast(tupleQuery);
            } else
                this.query = cb.createQuery(resultType);
            root = query.from(entityType);
        }

        public List<R> buildList(int offset, int maxResult, LockModeType lockModeType) {
            if (resultType == Object[].class) {
                return TypeCastUtil.cast(getObjectsList(offset, maxResult, lockModeType));
            } else {
                return getResultList(offset, maxResult, lockModeType);
            }
        }

        private List<R> getResultList(int offset, int maxResult, LockModeType lockModeType) {

            TypedQuery<R> entityQuery = entityQuery();

            if (offset > 0) {
                entityQuery = entityQuery.setFirstResult(offset);
            }
            if (maxResult > 0) {
                entityQuery = entityQuery.setMaxResults(maxResult);
            }
            if (lockModeType != null) {
                entityQuery.setLockMode(lockModeType);
            }
            return entityQuery.getResultList();
        }

        private TypedQuery<R> entityQuery() {
            Array<PathExpression> list = criteria.fetchClause();
            if (list != null) {
                for (PathExpression path : list) {
                    Fetch<?, ?> fetch = null;
                    for (int i = 0; i < path.length(); i++) {
                        Fetch<?, ?> cur = fetch;
                        String stringPath = path.get(i);
                        fetch = (Fetch<?, ?>) fetched.computeIfAbsent(path.offset(i + 1), k -> {
                            if (cur == null) {
                                return root.fetch(stringPath, JoinType.LEFT);
                            } else {
                                return cur.fetch(stringPath, JoinType.LEFT);
                            }
                        });
                    }
                }
            }

            buildWhere();
            builderOrderBy();
            return entityManager.createQuery(query);
        }

        private List<Object[]> getObjectsList(int offset, int maxResult, LockModeType lockModeType) {
            TypedQuery<?> objectsQuery = getObjectsQuery();

            if (offset > 0) {
                objectsQuery = objectsQuery.setFirstResult(offset);
            }
            if (maxResult > 0) {
                objectsQuery = objectsQuery.setMaxResults(maxResult);
            }
            if (lockModeType != null) {
                objectsQuery.setLockMode(lockModeType);
            }
            return objectsQuery.getResultList()
                    .stream()
                    .map(it -> {
                        if (it instanceof Object[]) {
                            return (Object[]) it;
                        }
                        return new Object[]{it};
                    })
                    .collect(Collectors.toList());
        }

        private TypedQuery<?> getObjectsQuery() {
            buildWhere();
            Array<Expression> groupBy = criteria.groupByClause();
            if (groupBy != null && !groupBy.isEmpty()) {
                List<javax.persistence.criteria.Expression<?>> grouping = groupBy.stream().map(this::toExpression).collect(Collectors.toList());
                query.groupBy(
                        grouping
                );
            }
            builderOrderBy();
            CriteriaQuery<R> select = query.multiselect(
                    criteria.selectClause().stream()
                            .map(this::toExpression)
                            .collect(Collectors.toList())
            );

            return entityManager.createQuery(select);
        }


        protected void builderOrderBy() {
            Array<SortSpecification> orderBy = criteria.sortSpec();
            if (orderBy != null && !orderBy.isEmpty()) {
                query.orderBy(
                        orderBy.stream()
                                .map(o -> {
                                    if (o.desc()) {
                                        return cb.desc(toExpression(o.expression()));
                                    } else {
                                        return cb.asc(toExpression(o.expression()));
                                    }
                                })
                                .collect(Collectors.toList())
                );
            }
        }

        protected void buildWhere() {
            Expression where = criteria.whereClause();
            if (where != null && !Expression.TRUE.equals(where)) {
                query.where(toPredicate(where));
            }
        }

    }

}
