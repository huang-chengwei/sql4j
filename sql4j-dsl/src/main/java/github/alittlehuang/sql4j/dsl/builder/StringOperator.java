package github.alittlehuang.sql4j.dsl.builder;

public interface StringOperator<T, BUILDER> extends ComparableOperator<T, String, BUILDER> {

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

    StringOperator<T, BUILDER> lower();

    StringOperator<T, BUILDER> upper();

    StringOperator<T, BUILDER> substring(int a, int b);

    StringOperator<T, BUILDER> substring(int a);

    StringOperator<T, BUILDER> trim();

    NumberOperator<T, Integer, BUILDER> length();


}
