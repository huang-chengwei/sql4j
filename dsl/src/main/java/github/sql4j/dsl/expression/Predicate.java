package github.sql4j.dsl.expression;

import github.sql4j.dsl.builder.PredicateCombiner;
import github.sql4j.dsl.builder.ComparablePredicate;
import github.sql4j.dsl.builder.NumberPredicate;
import github.sql4j.dsl.builder.BasePredicate;
import github.sql4j.dsl.builder.StringPredicate;
import github.sql4j.dsl.expression.path.AttributePath;
import github.sql4j.dsl.expression.path.attribute.*;
import github.sql4j.dsl.support.builder.component.ComparablePredicateImpl;
import github.sql4j.dsl.support.builder.component.NumberPredicateImpl;
import github.sql4j.dsl.support.builder.component.BasePredicateImpl;
import github.sql4j.dsl.support.builder.component.StringPredicateImpl;

public interface Predicate<T> extends Expression<Boolean> {

    Predicate<T> not();

    Predicate<T> and(Predicate<T> predicate);

    Predicate<T> or(Predicate<T> predicate);

    interface Builder<T> extends PredicateCombiner<T, Builder<T>>, Predicate<T> {

    }

    static <T, R> BasePredicate<T, R, Builder<T>> of(Attribute<T, R> attribute) {
        return new BasePredicateImpl<>(AttributePath.exchange(attribute), Operator.AND, false,
                subPredicate -> new PredicateBuilder<>(subPredicate.getExpression())
        );
    }

    static <T, R extends Number & Comparable<?>> NumberPredicate<T, R, Builder<T>> of(NumberAttribute<T, R> attribute) {
        return new NumberPredicateImpl<>(AttributePath.exchange(attribute), Operator.AND, false,
                subPredicate -> new PredicateBuilder<>(subPredicate.getExpression())
        );
    }

    static <T> StringPredicate<T, Builder<T>> of(StringAttribute<T> attribute) {
        return new StringPredicateImpl<>(AttributePath.exchange(attribute), Operator.AND, false,
                subPredicate -> new PredicateBuilder<>(subPredicate.getExpression())
        );
    }

    static <T, R extends Comparable<?>> ComparablePredicate<T, R, Builder<T>> of(ComparableAttribute<T, R> attribute) {
        return new ComparablePredicateImpl<>(AttributePath.exchange(attribute), Operator.AND, false,
                subPredicate -> new PredicateBuilder<>(subPredicate.getExpression())
        );
    }

    static <T> Predicate.Builder<T> of(BooleanAttribute<T> attribute) {
        return new PredicateBuilder<>(AttributePath.exchange(attribute).then(Operator.EQ, true));
    }


}
