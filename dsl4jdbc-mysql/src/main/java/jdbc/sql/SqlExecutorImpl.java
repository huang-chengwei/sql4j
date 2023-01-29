package jdbc.sql;

import github.sql4j.dsl.expression.PathExpression;
import github.sql4j.dsl.support.domain.TupleImpl;
import github.sql4j.dsl.support.meta.Attribute;
import github.sql4j.dsl.support.meta.EntityInformation;
import github.sql4j.dsl.util.Tuple;
import jdbc.util.JdbcUtil;
import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SqlExecutorImpl implements PreparedSqlExecutor {

    protected final SqlExecutor sqlExecutor;

    public SqlExecutorImpl(SqlExecutor sqlExecutor) {
        this.sqlExecutor = sqlExecutor;
    }

    @Override
    public <T> List<T> getEntityList(SelectedPreparedSql sql, Class<T> entityType) {
        return getResultSet(sql, resultSet -> mapToEntity(sql, entityType, resultSet));
    }

    @SneakyThrows
    @NotNull
    private <T> List<T> mapToEntity(SelectedPreparedSql sql, Class<T> type, ResultSet resultSet) {
        List<T> result = new ArrayList<>();
        int columnsCount = resultSet.getMetaData().getColumnCount();
        while (resultSet.next()) {
            List<PathExpression<?>> selectedPath = sql.getSelectedPath();
            T row = type.getConstructor().newInstance();
            for (int i = 0; i < columnsCount; i++) {
                PathExpression<?> path = selectedPath.get(i);
                int size = path.size();
                EntityInformation<T> info = EntityInformation.getInstance(type);
                Object entity = row;
                Object object = resultSet.getObject(i + 1);
                if (object != null) {
                    for (int j = 0; j < size; j++) {
                        Attribute attribute = info.getAttribute(path.get(j));
                        if (j == size - 1) {
                            Class<?> javaType = attribute.getJavaType();
                            if (PRIMITIVE_MAP.getOrDefault(javaType, javaType).isInstance(object)) {
                                attribute.setValue(entity, object);
                            } else {
                                Object value = JdbcUtil.getValue(resultSet, i + 1, javaType);
                                attribute.setValue(entity, value);
                            }
                        } else {
                            Object next = attribute.getValue(entity);
                            if (next == null) {
                                next = attribute.getJavaType().getConstructor().newInstance();
                                attribute.setValue(entity, next);
                            }
                            entity = next;
                        }
                    }
                }
            }
            result.add(row);
        }
        return result;
    }

    @Override
    public List<Tuple> listResult(PreparedSql sql, Class<?> entityType) {
        List<Object[]> objects = getResultSet(sql, resultSet -> {
            List<Object[]> result = new ArrayList<>();
            int columnsCount = resultSet.getMetaData().getColumnCount();
            while (resultSet.next()) {
                Object[] row = new Object[columnsCount];
                for (int i = 0; i < columnsCount; i++) {
                    row[i] = resultSet.getObject(i + 1);
                }
                result.add(row);
            }
            return result;
        });
        return objects.stream()
                .map(TupleImpl::new)
                .collect(Collectors.toList());
    }

    @Override
    public boolean exist(PreparedSql sql, Class<?> entityType) {
        return getResultSet(sql, ResultSet::next);
    }

    @Override
    public int count(PreparedSql sql, Class<?> entityType) {
        return getResultSet(sql, resultSet -> {
            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
            return 0;
        });
    }


    private <T> T getResultSet(PreparedSql sql, SqlExecutor.ResultSetCallback<T> callback) {
        return sqlExecutor.query(sql.getSql(), sql.getArgs().toArray(), callback);
    }

    private static final Map<Class<?>, Class<?>> PRIMITIVE_MAP = getPrimitiveMap();

    @NotNull
    private static Map<Class<?>, Class<?>> getPrimitiveMap() {
        HashMap<Class<?>, Class<?>> map = new HashMap<>();
        Class<?>[] types = {
                Boolean.TYPE,
                Character.TYPE,
                Byte.TYPE,
                Short.TYPE,
                Integer.TYPE,
                Long.TYPE,
                Float.TYPE,
                Double.TYPE,
                Void.TYPE
        };

        Class<?>[] types2 = {Boolean.class,
                Character.class,
                Byte.class,
                Short.class,
                Integer.class,
                Long.class,
                Float.class,
                Double.class,
                Void.class};

        for (int i = 0; i < types.length; i++) {
            map.put(types[i], types2[i]);
        }
        return map;
    }

}
