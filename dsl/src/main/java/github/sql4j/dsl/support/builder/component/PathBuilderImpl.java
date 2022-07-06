package github.sql4j.dsl.support.builder.component;

import github.sql4j.dsl.builder.ComparablePredicate;
import github.sql4j.dsl.builder.NumberPredicate;
import github.sql4j.dsl.builder.BasePredicate;
import github.sql4j.dsl.builder.StringPredicate;
import github.sql4j.dsl.expression.Operator;
import github.sql4j.dsl.expression.path.AttributePath;
import github.sql4j.dsl.expression.path.Entity;
import github.sql4j.dsl.expression.path.PathBuilder;
import github.sql4j.dsl.expression.path.attribute.*;

import java.util.function.Function;

public class PathBuilderImpl<T, U, BUILDER> implements PathBuilder<T, U, BUILDER> {

    private final AttributePath<T, U> path;
    private final Operator combined;
    protected final boolean negate;
    private final Function<SubPredicate, BUILDER> mapper;

    public PathBuilderImpl(AttributePath<T, U> path,
                           Operator combined,
                           boolean negate,
                           Function<SubPredicate, BUILDER> mapper) {
        this.path = path;
        this.combined = combined;
        this.negate = negate;
        this.mapper = mapper;
    }

    @Override
    public <R extends Entity> PathBuilderImpl<T, R, BUILDER> map(EntityAttribute<U, R> attribute) {
        AttributePath<T, R> strings = path.map(attribute);
        return new PathBuilderImpl<>(strings, combined, negate, mapper);
    }

    @Override
    public <R extends Number & Comparable<?>> NumberPredicate<T, R, BUILDER> map(NumberAttribute<U, R> attribute) {
        AttributePath<T, R> strings = path.map(attribute);
        return new NumberPredicateImpl<>(strings, combined, negate, mapper);
    }

    @Override
    public <R extends Comparable<?>> ComparablePredicate<T, R, BUILDER> map(ComparableAttribute<U, R> attribute) {
        AttributePath<T, R> strings = path.map(attribute);
        return new ComparablePredicateImpl<>(strings, combined, negate, mapper);

    }

    @Override
    public <R extends Comparable<?>> BasePredicate<T, R, BUILDER> map(Attribute<U, R> attribute) {
        AttributePath<T, R> strings = path.map(attribute);
        return new BasePredicateImpl<>(strings, combined, negate, mapper);
    }

    @Override
    public StringPredicate<T, BUILDER> map(StringAttribute<U> attribute) {
        AttributePath<T, String> strings = path.map(attribute);
        return new StringPredicateImpl<>(strings, combined, negate, mapper);
    }
}
