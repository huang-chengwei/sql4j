package github.sql4j.jpa;

import github.sql4j.dsl.expression.Operator;
import github.sql4j.dsl.expression.PathExpression;
import github.sql4j.dsl.expression.SqlExpression;
import github.sql4j.dsl.support.StructuredQuery;
import github.sql4j.dsl.support.builder.component.Order;
import github.sql4j.dsl.util.Array;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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


    protected class Builder<R> {
        protected final Class<R> resultType;
        protected final CriteriaBuilder cb;
        protected final CriteriaQuery<R> query;
        protected final Root<T> root;
        protected final Map<PathExpression<?>, FetchParent<?, ?>> fetched = new HashMap<>();

        public Builder(Class<R> resultType) {
            this.resultType = resultType;
            cb = entityManager.getCriteriaBuilder();
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
            Array<SqlExpression<?>> groupBy = criteria.groupBy();
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


        public Predicate toPredicate(SqlExpression<?> expression) {
            Expression<?> result = toExpression(expression);
            if (result instanceof Predicate) {
                return (Predicate) result;
            }
            return cb.isTrue(cast(toExpression(expression)));
        }

        public Expression<?> toExpression(SqlExpression<?> expression) {
            if (expression.getType() == SqlExpression.Type.CONSTANT) {
                return cb.literal(expression.getValue());
            }
            if (expression.getType() == SqlExpression.Type.PATH) {
                return getPath(expression.asPathExpression());
            }
            if (expression.getType() == SqlExpression.Type.OPERATOR) {
                List<? extends SqlExpression<?>> expressions = expression.getExpressions();
                Operator operator = expression.getOperator();
                Expression<?> e0 = toExpression(expressions.get(0));
                switch (operator) {

                    case NOT:
                        return cb.not(cast(e0));
                    case AND:
                        return cb.and(cast(e0), cast(toExpression(expressions.get(1))));
                    case OR:
                        return cb.or(cast(e0), cast(toExpression(expressions.get(1))));
                    case GT: {
                        SqlExpression<?> e1 = expressions.get(1);
                        if (e1.getType() == SqlExpression.Type.CONSTANT) {
                            if (e1.getValue() instanceof Number) {
                                return cb.gt(cast(e0), (Number) e1.getValue());
                            } else if (e1.getValue() instanceof Comparable) {
                                //noinspection unchecked
                                return cb.greaterThan(cast(e0), (Comparable<Object>) e1.getValue());
                            }
                        }
                        return cb.gt(cast(e0), cast(toExpression(e1)));
                    }
                    case EQ: {
                        SqlExpression<?> e1 = expressions.get(1);
                        if (e1.getType() == SqlExpression.Type.CONSTANT) {
                            return cb.equal(cast(e0), e1.getValue());
                        }
                        return cb.equal(e0, toExpression(e1));
                    }
                    case NE: {
                        SqlExpression<?> e1 = expressions.get(1);
                        if (e1.getType() == SqlExpression.Type.CONSTANT) {
                            return cb.notEqual(e0, e1.getValue());
                        }
                        return cb.notEqual(e0, toExpression(e1));
                    }
                    case GE: {
                        SqlExpression<?> e1 = expressions.get(1);
                        if (e1.getType() == SqlExpression.Type.CONSTANT) {
                            if (e1.getValue() instanceof Number) {
                                return cb.ge(cast(e0), (Number) e1.getValue());
                            } else if (e1.getValue() instanceof Comparable) {
                                //noinspection unchecked
                                return cb.greaterThanOrEqualTo(cast(e0), (Comparable<Object>) e1.getValue());
                            }
                        }
                        return cb.ge(cast(e0), cast(toExpression(e1)));
                    }
                    case LT: {
                        SqlExpression<?> e1 = expressions.get(1);
                        if (e1.getType() == SqlExpression.Type.CONSTANT) {
                            Object ve1 = e1.getValue();
                            if (ve1 instanceof Number) {
                                return cb.lt(cast(e0), (Number) ve1);
                            } else if (ve1 instanceof Comparable) {
                                //noinspection unchecked
                                return cb.lessThan(cast(e0), (Comparable<Object>) ve1);
                            }
                        }
                        return cb.lt(cast(e0), cast(toExpression(e1)));
                    }
                    case LE: {
                        SqlExpression<?> e1 = expressions.get(1);
                        if (e1.getType() == SqlExpression.Type.CONSTANT) {
                            Object ve1 = e1.getValue();
                            if (ve1 instanceof Number) {
                                return cb.le(cast(e0), (Number) ve1);
                            } else if (ve1 instanceof Comparable) {
                                //noinspection unchecked
                                return cb.lessThanOrEqualTo(cast(e0), (Comparable<Object>) ve1);
                            }
                        }
                        return cb.le(cast(e0), cast(toExpression(e1)));
                    }
                    case LIKE: {
                        SqlExpression<?> e1 = expressions.get(1);
                        if (e1.getType() == SqlExpression.Type.CONSTANT && e1.getValue() instanceof String) {
                            return cb.like(cast(e0), (String) e1.getValue());
                        }
                        return cb.like(cast(e0), cast(toExpression(e1)));
                    }
                    case ISNULL:
                        return cb.isNull(e0);
                    case IN: {
                        if (expressions.size() > 1) {
                            CriteriaBuilder.In<Object> in = cb.in(e0);
                            for (int i = 1; i < expressions.size(); i++) {
                                SqlExpression<?> e1 = expressions.get(i);
                                if (e1.getType() == SqlExpression.Type.CONSTANT) {
                                    in = in.value(e1.getValue());
                                } else {
                                    in = in.value(toExpression(e1));
                                }
                            }
                            return in;
                        } else {
                            return cb.literal(false);
                        }
                    }
                    case BETWEEN: {
                        SqlExpression<?> se1 = expressions.get(1);
                        SqlExpression<?> se2 = expressions.get(2);
                        if (se1.getType() == SqlExpression.Type.CONSTANT
                                && se2.getType() == SqlExpression.Type.CONSTANT
                                && se1.getValue() instanceof Comparable
                                && se2.getValue() instanceof Comparable) {
                            //noinspection unchecked
                            return cb.between(cast(e0),
                                    (Comparable<Object>) se1.getValue(),
                                    (Comparable<Object>) se2.getValue()
                            );
                        }
                        return cb.between(
                                cast(e0),
                                cast(toExpression(se1)),
                                cast(toExpression(se2))
                        );
                    }
                    case LOWER:
                        return cb.lower(cast(e0));
                    case UPPER:
                        return cb.upper(cast(e0));
                    case SUBSTRING: {
                        if (expressions.size() == 2) {
                            SqlExpression<?> se1 = expressions.get(1);
                            if (se1.getType() == SqlExpression.Type.CONSTANT
                                    && se1.getValue() instanceof Integer) {
                                return cb.substring(cast(e0), (Integer) se1.getValue());
                            }
                            return cb.substring(cast(e0), cast(toExpression(se1)));
                        } else if (expressions.size() == 3) {
                            SqlExpression<?> se1 = expressions.get(1);
                            SqlExpression<?> se2 = expressions.get(2);
                            if (se1.getType() == SqlExpression.Type.CONSTANT
                                    && se1.getValue() instanceof Integer
                                    && se2.getType() == SqlExpression.Type.CONSTANT
                                    && se2.getValue() instanceof Integer) {
                                return cb.substring(cast(e0),
                                        (Integer) se1.getValue(),
                                        (Integer) se2.getValue());
                            }
                            return cb.substring(
                                    cast(e0),
                                    cast(toExpression(se1)),
                                    cast(toExpression(se2))
                            );
                        } else {
                            throw new IllegalArgumentException("argument length error");
                        }
                    }
                    case TRIM:
                        return cb.trim(cast(e0));
                    case LENGTH:
                        return cb.length(cast(e0));
                    case ADD: {
                        SqlExpression<?> e1 = expressions.get(1);
                        if (e1.getType() == SqlExpression.Type.CONSTANT && e1.getValue() instanceof Number) {
                            return cb.sum(cast(e0), (Number) e1.getValue());
                        }
                        return cb.sum(cast(e0), cast(toExpression(e1)));
                    }
                    case SUBTRACT: {
                        SqlExpression<?> e1 = expressions.get(1);
                        if (e1.getType() == SqlExpression.Type.CONSTANT && e1.getValue() instanceof Number) {
                            return cb.diff(cast(e0), (Number) e1.getValue());
                        }
                        return cb.diff(cast(e0), cast(toExpression(e1)));
                    }
                    case MULTIPLY: {
                        SqlExpression<?> e1 = expressions.get(1);
                        if (e1.getType() == SqlExpression.Type.CONSTANT && e1.getValue() instanceof Number) {
                            return cb.prod(cast(e0), (Number) e1.getValue());
                        }
                        return cb.prod(cast(e0), cast(toExpression(e1)));
                    }
                    case DIVIDE: {
                        SqlExpression<?> e1 = expressions.get(1);
                        if (e1.getType() == SqlExpression.Type.CONSTANT && e1.getValue() instanceof Number) {
                            return cb.quot(cast(e0), (Number) e1.getValue());
                        }
                        return cb.quot(cast(e0), cast(toExpression(e1)));
                    }
                    case MOD: {
                        SqlExpression<?> e1 = expressions.get(1);
                        if (e1.getType() == SqlExpression.Type.CONSTANT
                                && e1.getValue() instanceof Integer) {
                            return cb.mod(cast(e0), ((Integer) e1.getValue()));
                        }
                        return cb.mod(cast(e0), cast(toExpression(e1)));
                    }
                    case NULLIF: {
                        SqlExpression<?> e1 = expressions.get(1);
                        if (e1.getType() == SqlExpression.Type.CONSTANT) {
                            return cb.nullif(cast(e0), ((Integer) e1.getValue()));
                        }
                        return cb.nullif(e0, toExpression(e1));
                    }
                    case IF_NULL: {
                        SqlExpression<?> e1 = expressions.get(1);
                        if (e1.getType() == SqlExpression.Type.CONSTANT) {
                            return cb.coalesce(cast(e0), ((Integer) e1.getValue()));
                        }
                        return cb.coalesce(e0, toExpression(e1));
                    }
                    case MIN:
                        return cb.min(cast(e0));
                    case MAX:
                        return cb.max(cast(e0));
                    case COUNT:
                        return cb.count(cast(e0));
                    case AVG:
                        return cb.avg(cast(e0));
                    case SUM:
                        return cb.sum(cast(e0));
                    default:
                        throw new UnsupportedOperationException(operator.name());
                }
            } else {
                throw new UnsupportedOperationException("unknown expression type " + expression.getClass());
            }
        }

        protected Path<?> getPath(PathExpression<?> expression) {
            From<?, ?> r = root;
            int size = expression.size();
            for (int i = 0; i < size; i++) {
                String s = expression.get(i);
                if (i != size - 1) {
                    r = join(expression.offset(i + 1));
                } else {
                    return r.get(s);
                }
            }

            return r;
        }

        private Join<?, ?> join(PathExpression<?> offset) {
            return (Join<?, ?>) fetched.compute(offset, (k, v) -> {
                if (v instanceof Join<?, ?>) {
                    return v;
                } else {
                    From<?, ?> r = root;
                    for (String s : offset) {
                        r = r.join(s, JoinType.LEFT);
                    }
                    return r;
                }
            });
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
            SqlExpression<Boolean> where = criteria.where();
            if (where != null) {
                query.where(toPredicate(where));
            }
        }
    }


    public static <T> Expression<T> cast(Expression<?> expression) {
        //noinspection unchecked
        return (Expression<T>) expression;
    }
}
