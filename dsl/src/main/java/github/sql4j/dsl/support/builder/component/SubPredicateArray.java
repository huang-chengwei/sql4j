package github.sql4j.dsl.support.builder.component;

import github.sql4j.dsl.expression.ConstantExpression;
import github.sql4j.dsl.expression.SqlExpression;
import github.sql4j.dsl.expression.Operator;
import github.sql4j.dsl.expression.PathExpression;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class SubPredicateArray implements SqlExpression<Boolean> {

    private final ConstantArray<SubPredicate> values;
    private SqlExpression<Boolean> value;

    public static SubPredicateArray fromExpression(SqlExpression<Boolean> expression) {
        if (expression == null) {
            return null;
        }
        if (expression instanceof SubPredicateArray) {
            return (SubPredicateArray) expression;
        }
        ConstantArray<SubPredicate> values =
                new ConstantArray<>(new SubPredicate(expression, Operator.AND, false));
        return new SubPredicateArray(values);
    }

    public SubPredicateArray(ConstantArray<SubPredicate> values) {
        this.values = values;
    }

    private SqlExpression<Boolean> value() {
        return value != null ? value : (value = merge());
    }

    private SqlExpression<Boolean> merge() {
        if (values == null || values.isEmpty()) {
            return new ConstantExpression<>(false);
        }
        if (values.length() == 1) {
            return values.get(0).getExpression();
        }

        SubPredicate[] arr = values.stream()
                .map(i -> {
                    if (i.isNegate()) {
                        return new SubPredicate(i.getExpression().then(Operator.NOT), i.getCombined(), false);
                    }
                    return i;
                })
                .toArray(SubPredicate[]::new);
        boolean and = true;
        while (true) {
            if (and) {
                and = false;
                for (int i = 1; i < arr.length; i++) {
                    SubPredicate vi = arr[i];
                    if (vi == null) {
                        continue;
                    }
                    Operator combined = vi.getCombined();
                    if (combined == Operator.AND) {
                        and = true;
                        arr[i] = null;
                        for (int j = i - 1; j >= 0; j--) {
                            SubPredicate vj = arr[j];
                            if (vj == null) {
                                continue;
                            }
                            SqlExpression<Boolean> updated = vj.getExpression().then(combined, vi.getExpression());
                            arr[j] = new SubPredicate(updated, vj.getCombined(), vj.isNegate());
                            break;
                        }
                    } else if (combined != Operator.OR) {
                        throw new UnsupportedOperationException();
                    }
                }
            } else {
                SqlExpression<Boolean> result = arr[0].getExpression();
                for (int i = 1; i < arr.length; i++) {
                    if (arr[i] != null) {
                        result = result.then(arr[i].getCombined(), arr[i].getExpression());
                    }
                }
                return result;
            }
        }
    }

    @Override
    public PathExpression<Boolean> asPathExpression() {
        return value().asPathExpression();
    }

    @Override
    public Type getType() {
        return value().getType();
    }

    @Override
    public Boolean getValue() {
        return value().getValue();
    }

    @Override
    public Operator getOperator() {
        return value().getOperator();
    }

    @Override
    public List<? extends SqlExpression<?>> getExpressions() {
        return value().getExpressions();
    }

    @Override
    public <X> SqlExpression<X> then(Operator operator, Object... args) {
        return then(operator, Arrays.asList(args));
    }

    @Override
    public <X> SqlExpression<X> then(Operator operator, @NotNull Collection<?> args) {
        if (operator == Operator.AND || operator == Operator.OR) {
            Object next = args.iterator().next();
            //noinspection unchecked
            SqlExpression<Boolean> of = (SqlExpression<Boolean>) SqlExpression.of(next);
            //noinspection unchecked
            return (SqlExpression<X>) new SubPredicateArray(values.concat(new SubPredicate(of, operator, false)));
        }
        return value().then(operator, args);
    }


}
