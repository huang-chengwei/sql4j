package github.sql4j.dsl.expression;

public enum Operator {

    NOT, AND, OR, GT, EQ, NE, GE, LT, LE, LIKE, ISNULL, IN, BETWEEN, LOWER, UPPER,
    SUBSTRING, TRIM, LENGTH, ADD, SUBTRACT, MULTIPLY, DIVIDE, MOD, NULLIF, IF_NULL,
    //aggregate function
    MIN, MAX, COUNT, AVG, SUM

}
