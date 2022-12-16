package github.sql4j.dsl.support;

import github.sql4j.dsl.expression.*;
import lombok.Data;

import java.math.BigDecimal;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@SuppressWarnings("ALL")
@Data
public class JsonSerializablePredicate {

    private String[] pathExpression;

    private SqlExpression.Type type;

    private Operator operator;

    private JsonSerializablePredicate[] expressions;

    private Value value;

    public JsonSerializablePredicate() {
    }

    public JsonSerializablePredicate(SqlExpression<?> expression) {
        this.type = expression.getType();
        switch (type) {
            case PATH:
                this.pathExpression = expression.asPathExpression().toArray();
                break;
            case OPERATOR:
                this.operator = expression.getOperator();
                List<? extends SqlExpression<?>> list = expression.getExpressions();
                if (list != null) {
                    expressions = list.stream().map(JsonSerializablePredicate::new).toArray(JsonSerializablePredicate[]::new);
                } else {
                    expressions = null;
                }
                break;
            case CONSTANT:
                this.value = new Value(expression.getValue());
                break;
        }
    }

    public <T> Predicate<T> toPredicate() {
        //noinspection unchecked
        return new PredicateBuilder<>((SqlExpression<Boolean>) toSqlExpression());
    }

    private SqlExpression<?> toSqlExpression() {
        return new SqlExpression<>() {
            @Override
            public PathExpression<Object> asPathExpression() {
                return new PathExpression<>(pathExpression);
            }

            @Override
            public Type getType() {
                return type;
            }

            @Override
            public Object getValue() {
                return value.value();
            }

            @Override
            public Operator getOperator() {
                return operator;
            }

            @Override
            public List<? extends SqlExpression<?>> getExpressions() {
                return expressions == null ? null : Arrays.stream(expressions)
                        .map(JsonSerializablePredicate::toSqlExpression)
                        .collect(Collectors.toList());
            }
        };
    }

    public static class Value {

        private static List<Class<?>> typeIndexes = Arrays.asList(
                Byte.class,
                Short.class,
                Integer.class,
                Float.class,
                Long.class,
                Double.class,
                Boolean.class,
                BigDecimal.class,
                Date.class,
                String.class,
                Time.class,
                Timestamp.class
        );


        private Object value;

        public Value() {
        }

        public Value(Object value) {
            int type = typeIndexes.indexOf(value.getClass());
            if (type < 0) {
                throw new IllegalArgumentException();
            }
            this.value = value;
        }

        public Object value() {
            return value;
        }

        private <T> T getValue(Class<T> type) {
            //noinspection unchecked
            return type == value.getClass() ? (T) value : null;
        }

        private void setValue(Object value) {
            if (value != null) {
                this.value = value;
            }
        }


        /*
         * Json序列化保留类型
         */


        public void setByteValue(Byte value) {
            setValue(value);
        }

        public Byte getByteValue() {
            return getValue(Byte.class);
        }

        public void setShortValue(Short value) {
            setValue(value);
        }

        public Short getShortValue() {
            return getValue(Short.class);
        }

        public Integer getIntegerValue() {
            return getValue(Integer.class);
        }

        public void setIntegerValue(Integer value) {
            setValue(value);
        }

        public Float getFloutValue() {
            return getValue(Float.class);
        }

        public void setFloutValue(Float value) {
            setValue(value);
        }

        public Long getLongValue() {
            return getValue(Long.class);
        }

        public void setLongValue(Long value) {
            setValue(value);
        }

        public Double getDoubleValue() {
            return getValue(Double.class);
        }

        public void setDoubleValue(Double value) {
            setValue(value);
        }

        public Boolean getBooleanValue() {
            return getValue(Boolean.class);
        }

        public void setBooleanValue(Boolean value) {
            setValue(value);
        }

        public BigDecimal getBigDecimalValue() {
            return getValue(BigDecimal.class);
        }

        public void setBigDecimalValue(BigDecimal value) {
            setValue(value);
        }

        public Date getDateValue() {
            return getValue(Date.class);
        }

        public void setDateValue(Date value) {
            setValue(value);
        }

        public String getStringValue() {
            return getValue(String.class);
        }

        public void setStringValue(String value) {
            setValue(value);
        }

        public Time getTimeValue() {
            return getValue(Time.class);
        }

        public void setTimeValue(Time value) {
            setValue(value);
        }

        public Timestamp getTimestampValue() {
            return getValue(Timestamp.class);
        }

        public void setTimestampValue(Timestamp value) {
            setValue(value);
        }


        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Value value1 = (Value) o;

            return Objects.equals(value, value1.value);
        }

        @Override
        public int hashCode() {
            return value != null ? value.hashCode() : 0;
        }

        @Override
        public String toString() {
            return "Value{" +
                    "value=" + value +
                    '}';
        }
    }
}

