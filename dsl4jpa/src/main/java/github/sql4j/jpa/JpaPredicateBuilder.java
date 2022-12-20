package github.sql4j.jpa;

import github.sql4j.dsl.expression.Expression;
import github.sql4j.dsl.expression.Operator;
import github.sql4j.dsl.expression.PathExpression;
import javax.persistence.criteria.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JpaPredicateBuilder<T> {

    protected Root<T> root;

    protected CriteriaBuilder cb;

    protected final Map<PathExpression<?>, FetchParent<?, ?>> fetched = new HashMap<>();

    public JpaPredicateBuilder() {
    }

    public JpaPredicateBuilder(Root<T> root, CriteriaBuilder cb) {
        this.root = root;
        this.cb = cb;
    }

    public Predicate toPredicate(Expression<?> expression) {
        javax.persistence.criteria.Expression<?> result = toExpression(expression);
        if (result instanceof Predicate) {
            return (Predicate) result;
        }
        return cb.isTrue(cast(toExpression(expression)));
    }

    public javax.persistence.criteria.Expression<?> toExpression(Expression<?> expression) {
        if (expression.getType() == Expression.Type.CONSTANT) {
            return cb.literal(expression.getValue());
        }
        if (expression.getType() == Expression.Type.PATH) {
            return getPath(expression.asPathExpression());
        }
        if (expression.getType() == Expression.Type.OPERATOR) {
            List<? extends Expression<?>> expressions = expression.getExpressions();
            Operator operator = expression.getOperator();
            javax.persistence.criteria.Expression<?> e0 = toExpression(expressions.get(0));
            switch (operator) {
                case NOT:
                    return cb.not(cast(e0));
                case AND:
                    return cb.and(cast(e0), cast(toExpression(expressions.get(1))));
                case OR:
                    return cb.or(cast(e0), cast(toExpression(expressions.get(1))));
                case GT: {
                    Expression<?> e1 = expressions.get(1);
                    if (e1.getType() == Expression.Type.CONSTANT) {
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
                    Expression<?> e1 = expressions.get(1);
                    if (e1.getType() == Expression.Type.CONSTANT) {
                        return cb.equal(cast(e0), e1.getValue());
                    }
                    return cb.equal(e0, toExpression(e1));
                }
                case NE: {
                    Expression<?> e1 = expressions.get(1);
                    if (e1.getType() == Expression.Type.CONSTANT) {
                        return cb.notEqual(e0, e1.getValue());
                    }
                    return cb.notEqual(e0, toExpression(e1));
                }
                case GE: {
                    Expression<?> e1 = expressions.get(1);
                    if (e1.getType() == Expression.Type.CONSTANT) {
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
                    Expression<?> e1 = expressions.get(1);
                    if (e1.getType() == Expression.Type.CONSTANT) {
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
                    Expression<?> e1 = expressions.get(1);
                    if (e1.getType() == Expression.Type.CONSTANT) {
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
                    Expression<?> e1 = expressions.get(1);
                    if (e1.getType() == Expression.Type.CONSTANT && e1.getValue() instanceof String) {
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
                            Expression<?> e1 = expressions.get(i);
                            if (e1.getType() == Expression.Type.CONSTANT) {
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
                    Expression<?> se1 = expressions.get(1);
                    Expression<?> se2 = expressions.get(2);
                    if (se1.getType() == Expression.Type.CONSTANT
                            && se2.getType() == Expression.Type.CONSTANT
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
                        Expression<?> se1 = expressions.get(1);
                        if (se1.getType() == Expression.Type.CONSTANT
                                && se1.getValue() instanceof Integer) {
                            return cb.substring(cast(e0), (Integer) se1.getValue());
                        }
                        return cb.substring(cast(e0), cast(toExpression(se1)));
                    } else if (expressions.size() == 3) {
                        Expression<?> se1 = expressions.get(1);
                        Expression<?> se2 = expressions.get(2);
                        if (se1.getType() == Expression.Type.CONSTANT
                                && se1.getValue() instanceof Integer
                                && se2.getType() == Expression.Type.CONSTANT
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
                    Expression<?> e1 = expressions.get(1);
                    if (e1.getType() == Expression.Type.CONSTANT && e1.getValue() instanceof Number) {
                        return cb.sum(cast(e0), (Number) e1.getValue());
                    }
                    return cb.sum(cast(e0), cast(toExpression(e1)));
                }
                case SUBTRACT: {
                    Expression<?> e1 = expressions.get(1);
                    if (e1.getType() == Expression.Type.CONSTANT && e1.getValue() instanceof Number) {
                        return cb.diff(cast(e0), (Number) e1.getValue());
                    }
                    return cb.diff(cast(e0), cast(toExpression(e1)));
                }
                case MULTIPLY: {
                    Expression<?> e1 = expressions.get(1);
                    if (e1.getType() == Expression.Type.CONSTANT && e1.getValue() instanceof Number) {
                        return cb.prod(cast(e0), (Number) e1.getValue());
                    }
                    return cb.prod(cast(e0), cast(toExpression(e1)));
                }
                case DIVIDE: {
                    Expression<?> e1 = expressions.get(1);
                    if (e1.getType() == Expression.Type.CONSTANT && e1.getValue() instanceof Number) {
                        return cb.quot(cast(e0), (Number) e1.getValue());
                    }
                    return cb.quot(cast(e0), cast(toExpression(e1)));
                }
                case MOD: {
                    Expression<?> e1 = expressions.get(1);
                    if (e1.getType() == Expression.Type.CONSTANT
                            && e1.getValue() instanceof Integer) {
                        return cb.mod(cast(e0), ((Integer) e1.getValue()));
                    }
                    return cb.mod(cast(e0), cast(toExpression(e1)));
                }
                case NULLIF: {
                    Expression<?> e1 = expressions.get(1);
                    if (e1.getType() == Expression.Type.CONSTANT) {
                        return cb.nullif(cast(e0), ((Integer) e1.getValue()));
                    }
                    return cb.nullif(e0, toExpression(e1));
                }
                case IF_NULL: {
                    Expression<?> e1 = expressions.get(1);
                    if (e1.getType() == Expression.Type.CONSTANT) {
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

    public static <T> javax.persistence.criteria.Expression<T> cast(javax.persistence.criteria.Expression<?> expression) {
        //noinspection unchecked
        return (javax.persistence.criteria.Expression<T>) expression;
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
}