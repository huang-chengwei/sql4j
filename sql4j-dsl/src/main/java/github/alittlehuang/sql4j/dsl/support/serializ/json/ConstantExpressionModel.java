package github.alittlehuang.sql4j.dsl.support.serializ.json;

import github.alittlehuang.sql4j.dsl.expression.ConstantExpression;
import github.alittlehuang.sql4j.dsl.expression.Expression;
import github.alittlehuang.sql4j.dsl.expression.ExpressionSupplier;
import github.alittlehuang.sql4j.dsl.util.BasicTypes;
import github.alittlehuang.sql4j.dsl.util.TypeCastUtil;

import java.math.BigDecimal;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Objects;

public class ConstantExpressionModel implements ExpressionSupplier {

    private Object value;

    public ConstantExpressionModel() {
    }

    public ConstantExpressionModel(Object value) {
        boolean isBasic = BasicTypes.isBasicType(value);
        if (!isBasic) {
            throw new IllegalArgumentException();
        }
        this.value = value;
    }


    @Override
    public Expression expression() {
        return new ConstantExpression(value);
    }

    public Object value() {
        return value;
    }

    private <T> T get(Class<T> type) {
        return type == value.getClass() ? TypeCastUtil.cast(value) : null;
    }

    private void set(Object value) {
        if (value != null) {
            this.value = value;
        }
    }

    /*
     * Json序列化保留类型
     */


    public void setByte(Byte value) {
        set(value);
    }

    public Byte getByte() {
        return get(Byte.class);
    }

    public void setShort(Short value) {
        set(value);
    }

    public Short getShort() {
        return get(Short.class);
    }

    public Integer getInt() {
        return get(Integer.class);
    }

    public void setInt(Integer value) {
        set(value);
    }

    public Float getFlout() {
        return get(Float.class);
    }

    public void setFlout(Float value) {
        set(value);
    }

    public Long getLong() {
        return get(Long.class);
    }

    public void setLong(Long value) {
        set(value);
    }

    public Double getDouble() {
        return get(Double.class);
    }

    public void setDouble(Double value) {
        set(value);
    }

    public Boolean getBoolean() {
        return get(Boolean.class);
    }

    public void setBoolean(Boolean value) {
        set(value);
    }

    public BigDecimal getBigDecimal() {
        return get(BigDecimal.class);
    }

    public void setBigDecimal(BigDecimal value) {
        set(value);
    }

    public Date getDate() {
        return get(Date.class);
    }

    public void setDate(Date value) {
        set(value);
    }

    public String getString() {
        return get(String.class);
    }

    public void setString(String value) {
        set(value);
    }

    public Time getTime() {
        return get(Time.class);
    }

    public void setTime(Time value) {
        set(value);
    }

    public Timestamp getTimestamp() {
        return get(Timestamp.class);
    }

    public void setTimestamp(Timestamp value) {
        set(value);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ConstantExpressionModel value1 = (ConstantExpressionModel) o;

        return Objects.equals(value, value1.value);
    }

    @Override
    public int hashCode() {
        return value != null ? value.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Value{value=" + value + '}';
    }


}