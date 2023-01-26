package github.sql4j.jpa;

import github.sql4j.dsl.expression.Expression;
import github.sql4j.dsl.expression.PathExpression;
import github.sql4j.dsl.support.StructuredQuery;
import github.sql4j.dsl.support.builder.component.Order;
import github.sql4j.dsl.util.Array;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Tuple;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Fetch;
import jakarta.persistence.criteria.JoinType;

import java.util.List;
import java.util.stream.Collectors;

public class JpaResultQuery<T> {
    protected final EntityManager entityManager;
    protected final Class<T> entityType;
    protected final StructuredQuery criteria;

    public JpaResultQuery(EntityManager entityManager, Class<T> type, StructuredQuery criteria) {
        this.entityManager = entityManager;
        this.entityType = type;
        this.criteria = criteria;
    }


    public int count() {
        return new CountBuilder().count().intValue();
    }

    public boolean exist(int offset) {
        return new ExistBuilder(Object.class).exist(offset);
    }

    class CountBuilder extends Builder<Long> {

        public CountBuilder() {
            super(Long.class);
        }

        public Long count() {
            buildWhere();
            query.select(cb.count(root));
            TypedQuery<Long> typedQuery = entityManager.createQuery(query);
            return typedQuery.getSingleResult();
        }
    }

    public class ExistBuilder extends Builder<Object> {
        public ExistBuilder(Class<Object> resultType) {
            super(resultType);
        }

        public boolean exist(int offset) {
            buildWhere();
            query.select(cb.literal(true));
            TypedQuery<?> query = entityManager.createQuery(this.query);
            if (offset > 0) {
                query = query.setFirstResult(offset);
            }
            return !query.setMaxResults(1)
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
                //noinspection unchecked
                this.query = (CriteriaQuery<R>) cb.createTupleQuery();
            } else
                this.query = cb.createQuery(resultType);
            root = query.from(entityType);
        }

        public List<R> getResultList(int offset, int maxResult) {
            Array<PathExpression<?>> list = criteria.fetch();
            if (list != null) {
                for (PathExpression<?> expression : list) {
                    Fetch<?, ?> fetch = null;
                    PathExpression<?> path = expression.asPathExpression();
                    for (int i = 0; i < path.size(); i++) {
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

            TypedQuery<R> typedQuery = entityManager.createQuery(query);
            if (offset > 0) {
                typedQuery = typedQuery.setFirstResult(offset);
            }
            if (maxResult > 0) {
                typedQuery = typedQuery.setMaxResults(maxResult);
            }
            return typedQuery.getResultList();
        }

        public List<Object[]> getObjectsList(int offset, int maxResult) {
            buildWhere();
            Array<Expression<?>> groupBy = criteria.groupBy();
            if (groupBy != null && !groupBy.isEmpty()) {
                query.groupBy(
                        groupBy.stream().map(this::toExpression).collect(Collectors.toList())
                );
            }
            builderOrderBy();
            CriteriaQuery<R> select = query.multiselect(
                    criteria.select().stream()
                            .map(this::toExpression)
                            .collect(Collectors.toList())
            );

            TypedQuery<?> typedQuery = entityManager.createQuery(select);

            if (offset > 0) {
                typedQuery = typedQuery.setFirstResult(offset);
            }
            if (maxResult > 0) {
                typedQuery = typedQuery.setMaxResults(maxResult);
            }
            return typedQuery.getResultList()
                    .stream()
                    .map(it -> {
                        if (it instanceof Object[]) {
                            return (Object[]) it;
                        }
                        return new Object[]{it};
                    })
                    .collect(Collectors.toList());
        }


        protected void builderOrderBy() {
            Array<Order> orderBy = criteria.orderBy();
            if (orderBy != null && !orderBy.isEmpty()) {
                query.orderBy(
                        orderBy.stream()
                                .map(o -> {
                                    if (o.isDesc()) {
                                        return cb.desc(toExpression(o.getExpression()));
                                    } else {
                                        return cb.asc(toExpression(o.getExpression()));
                                    }
                                })
                                .collect(Collectors.toList())
                );
            }
        }

        protected void buildWhere() {
            Expression<Boolean> where = criteria.where();
            if (where != null) {
                query.where(toPredicate(where));
            }
        }

        public List<Tuple> getTupleList(int offset, int maxResult) {
            buildWhere();
            //noinspection unchecked
            CriteriaQuery<Tuple> query = (CriteriaQuery<Tuple>) this.query;
            Array<Expression<?>> groupBy = criteria.groupBy();
            if (groupBy != null && !groupBy.isEmpty()) {
                query.groupBy(
                        groupBy.stream().map(this::toExpression).collect(Collectors.toList())
                );
            }
            builderOrderBy();
            CriteriaQuery<Tuple> select = query.multiselect(
                    criteria.select().stream()
                            .map(this::toExpression)
                            .collect(Collectors.toList())
            );

            TypedQuery<Tuple> typedQuery = entityManager.createQuery(select);

            if (offset > 0) {
                typedQuery = typedQuery.setFirstResult(offset);
            }
            if (maxResult > 0) {
                typedQuery = typedQuery.setMaxResults(maxResult);
            }
            return typedQuery.getResultList();
        }
    }

}
