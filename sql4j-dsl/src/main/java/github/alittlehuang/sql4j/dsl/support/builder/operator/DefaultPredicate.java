package github.alittlehuang.sql4j.dsl.support.builder.operator;

import github.alittlehuang.sql4j.dsl.expression.Expression;
import github.alittlehuang.sql4j.dsl.expression.Operator;
import github.alittlehuang.sql4j.dsl.expression.Predicate;
import github.alittlehuang.sql4j.dsl.util.TypeCastUtil;

public class DefaultPredicate<T> implements Predicate<T> {

    public static final Predicate<?> TRUE = new DefaultPredicate<>(Expression.TRUE);
    public static final Predicate<?> FALSE = new DefaultPredicate<>(Expression.FALSE);

    private final Expression combiner;

    public DefaultPredicate(Expression combiner) {
        this.combiner = combiner;
    }

    @Override
    public Expression expression() {
        return combiner.expression();
    }

    @Override
    public Predicate<T> not() {
        Expression combiner = expression().operate(Operator.NOT);
        return new DefaultPredicate<>(combiner);
    }

    @Override
    public Predicate<T> and(Predicate<T> predicate) {
        Expression expression = FlowPredicateOperate.and(combiner, predicate.expression());
        return new DefaultPredicate<>(expression);
    }

    @Override
    public Predicate<T> or(Predicate<T> predicate) {
        Expression expression = FlowPredicateOperate.or(combiner, predicate.expression());
        return new DefaultPredicate<>(expression);
    }


    public static <T> Predicate<T> ofTrue() {
        return TypeCastUtil.cast(TRUE);
    }

    public static <T> Predicate<T> ofFalse() {
        return TypeCastUtil.cast(FALSE);
    }

}
