package github.alittlehuang.sql4j.dsl.expression;

import github.alittlehuang.sql4j.dsl.support.builder.operator.ConstantArray;
import github.alittlehuang.sql4j.dsl.util.Array;
import org.jetbrains.annotations.NotNull;

public record PathExpression(Array<String> path) implements Expression {

    public PathExpression(String[] values) {
        this(new ConstantArray<>(values));
    }

    public PathExpression(String value) {
        this(new ConstantArray<>(value));
    }

    public PathExpression to(String to) {
        return new PathExpression(path.join(to));
    }

    public PathExpression join(PathExpression to) {
        return new PathExpression(path.join(to.path));
    }

    public int length() {
        return path.length();
    }


    public PathExpression offset(int length) {
        return new PathExpression(path.offset(length));
    }

    public String @NotNull [] toArray() {
        return path.toArray(String[].class);
    }


    public String get(int i) {
        return path.get(i);
    }

    public PathExpression parent() {
        int length = length();
        if (length <= 1) {
            return null;
        }
        return new PathExpression(path.offset(length - 1));
    }
}
