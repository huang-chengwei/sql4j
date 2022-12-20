package github.sql4j.dsl.support.builder.component;

import github.sql4j.dsl.builder.BasePredicate;
import github.sql4j.dsl.expression.ConstantExpression;
import github.sql4j.dsl.expression.Expression;
import github.sql4j.dsl.expression.Operator;
import lombok.Getter;

import java.util.Collection;
import java.util.function.Function;

@Getter
public class AbstractExpressionBuilder<T, U, BUILDER> extends SubExpression<U> {

    protected final Function<SubPredicate, BUILDER> mapper;

    public AbstractExpressionBuilder(Expression<U> expression,
                                     Operator combined,
                                     boolean negate,
                                     Function<SubPredicate, BUILDER> mapper) {
        super(expression, combined, negate);
        this.mapper = mapper;
    }

    protected BUILDER next(Operator operator, Object... value) {
        Expression<Boolean> then = expression.then(operator, value);
        return mapper.apply(new SubPredicate(then, combined, negate));
    }

    protected BUILDER next(Operator operator, Collection<?> values) {
        return next(operator, values.toArray());
    }

    public BUILDER isNull() {
        return next(Operator.ISNULL);
    }

    public BUILDER eq(U value) {
        return next(Operator.EQ, value);
    }

    public BUILDER ne(U value) {
        return next(Operator.NE, value);
    }

    public BUILDER in(Collection<U> values) {
        return next(Operator.IN, values);
    }

    public BUILDER ge(Expression<U> value) {
        return next(Operator.GE, value);
    }

    public BUILDER gt(Expression<U> value) {
        return next(Operator.GT, value);
    }

    public BUILDER le(Expression<U> value) {
        return next(Operator.LE, value);
    }

    public BUILDER between(Expression<U> a, Expression<U> b) {
        return next(Operator.BETWEEN, a, b);
    }

    public BUILDER lt(Expression<U> value) {
        return next(Operator.LT, value);
    }


    public BUILDER ge(U value) {
        return next(Operator.GE, value);
    }

    public BUILDER gt(U value) {
        return next(Operator.GT, value);
    }

    public BUILDER le(U value) {
        return next(Operator.LE, value);
    }

    public BUILDER between(U a, U b) {
        return next(Operator.BETWEEN, a, b);
    }

    public BUILDER lt(U value) {
        return next(Operator.LT, value);
    }

    public BasePredicate<T, U, BUILDER> nullIf(U value) {
        Expression<U> expression = this.expression.then(Operator.NULLIF, new ConstantExpression<>(value));
        return new BasePredicateImpl<>(expression, combined, negate, mapper);
    }

    public BasePredicate<T, U, BUILDER> ifNull(U value) {
        Expression<U> expression = this.expression.then(Operator.IF_NULL, new ConstantExpression<>(value));
        return new BasePredicateImpl<>(expression, combined, negate, mapper);
    }

}
