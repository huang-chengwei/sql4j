package github.sql4j.dsl.expression;

import github.sql4j.dsl.builder.PredicateAssembler;
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

public interface Predicate<T> extends SqlExpression<Boolean> {

    Predicate<T> not();

    Predicate<T> and(Predicate<T> predicate);

    Predicate<T> or(Predicate<T> predicate);

    interface Builder<T> extends PredicateAssembler<T, Builder<T>>, Predicate<T> {

    }

    static <T, R> BasePredicate<T, R, Builder<T>> get(Attribute<T, R> attribute) {
        return new BasePredicateImpl<>(AttributePath.exchange(attribute), Operator.AND, false,
                subPredicate -> new PredicateBuilder<>(subPredicate.getExpression())
        );
    }

    static <T, R extends Number & Comparable<?>> NumberPredicate<T, R, Builder<T>> get(NumberAttribute<T, R> attribute) {
        return new NumberPredicateImpl<>(AttributePath.exchange(attribute), Operator.AND, false,
                subPredicate -> new PredicateBuilder<>(subPredicate.getExpression())
        );
    }

    static <T> StringPredicate<T, Builder<T>> get(StringAttribute<T> attribute) {
        return new StringPredicateImpl<>(AttributePath.exchange(attribute), Operator.AND, false,
                subPredicate -> new PredicateBuilder<>(subPredicate.getExpression())
        );
    }

    static <T, R extends Comparable<?>> ComparablePredicate<T, R, Builder<T>> get(ComparableAttribute<T, R> attribute) {
        return new ComparablePredicateImpl<>(AttributePath.exchange(attribute), Operator.AND, false,
                subPredicate -> new PredicateBuilder<>(subPredicate.getExpression())
        );
    }

    static <T> Predicate.Builder<T> get(BooleanAttribute<T> attribute) {
        return new PredicateBuilder<>(AttributePath.exchange(attribute).then(Operator.EQ, true));
    }


}
