package github.sql4j.dsl.builder;

public interface StringPredicate<T, BUILDER> extends ComparablePredicate<T, String, BUILDER> {

    BUILDER like(String value);

    default BUILDER startWith(String value) {
        return like(value + "%");
    }

    default BUILDER endsWith(String value) {
        return like("%" + value);
    }

    default BUILDER contains(String value) {
        return like("%" + value + "%");
    }

    StringPredicate<T, BUILDER> lower();

    StringPredicate<T, BUILDER> upper();

    StringPredicate<T, BUILDER> substring(int a, int b);

    StringPredicate<T, BUILDER> substring(int a);

    StringPredicate<T, BUILDER> trim();

    NumberPredicate<T, Integer, BUILDER> length();


}
