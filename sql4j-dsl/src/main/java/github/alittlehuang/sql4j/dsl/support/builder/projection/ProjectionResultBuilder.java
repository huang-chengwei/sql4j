package github.alittlehuang.sql4j.dsl.support.builder.projection;

import github.alittlehuang.sql4j.dsl.builder.ResultBuilder;
import github.alittlehuang.sql4j.dsl.expression.Expression;
import github.alittlehuang.sql4j.dsl.expression.PathExpression;
import github.alittlehuang.sql4j.dsl.support.QuerySpecification;
import github.alittlehuang.sql4j.dsl.support.ResultBuilderFactory;
import github.alittlehuang.sql4j.dsl.support.builder.operator.ConstantArray;
import github.alittlehuang.sql4j.dsl.support.builder.projection.meta.ProjectionAttribute;
import github.alittlehuang.sql4j.dsl.support.builder.projection.meta.ProjectionMeta;
import github.alittlehuang.sql4j.dsl.support.builder.projection.meta.ProjectionMetaProvider;
import github.alittlehuang.sql4j.dsl.util.Array;
import github.alittlehuang.sql4j.dsl.util.Tuple;
import github.alittlehuang.sql4j.dsl.util.TypeCastUtil;
import jakarta.persistence.LockModeType;
import lombok.SneakyThrows;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

import static java.lang.invoke.MethodHandles.lookup;

public class ProjectionResultBuilder<T, R> implements ResultBuilder<R> {

    private final ResultBuilderFactory typeQueryFactory;
    private final QuerySpecification querySpec;
    private final Class<T> type;
    private final Class<R> projectionType;
    private final ProjectionMetaProvider projectionInformationProvider;

    public ProjectionResultBuilder(ResultBuilderFactory resultBuilderFactory,
                                   QuerySpecification querySpec,
                                   Class<T> type,
                                   Class<R> projectionType,
                                   ProjectionMetaProvider metaProvider) {
        this.typeQueryFactory = resultBuilderFactory;
        this.querySpec = querySpec;
        this.type = type;
        this.projectionType = projectionType;
        this.projectionInformationProvider = metaProvider == null
                ? ProjectionMetaProvider.DEFAULT
                : metaProvider;
    }

    @Override
    public int count() {
        return typeQueryFactory.getEntityResultQuery(querySpec, type).count();
    }

    @Override
    public List<R> getList(int offset, int maxResult, LockModeType lockModeType) {
        var info = projectionInformationProvider.getProjectionAttributes(type, projectionType);
        ArrayList<String> paths = new ArrayList<>();
        for (ProjectionAttribute attribute : info) {
            paths.add(attribute.getFieldName());
        }
        List<Expression> selections = paths.stream()
                .map(PathExpression::new)
                .collect(Collectors.toList());
        Array<Expression> array = new ConstantArray<>(selections);

        QuerySpecification structuredQuery = querySpec.updateSelect(array);
        List<Tuple> objects = typeQueryFactory.getObjectsTypeQuery(structuredQuery, type)
                .getList(offset, maxResult, lockModeType);
        return objects.stream()
                .map(os -> mapToRejection(info, paths, os, projectionType))
                .collect(Collectors.toList());
    }

    @SneakyThrows
    private R mapToRejection(ProjectionMeta info, ArrayList<String> paths, Tuple os, Class<R> projectionType) {
        ClassLoader classLoader = projectionType.getClassLoader();
        Class<?>[] interfaces = {projectionType, ProjectionProxyInstance.class};

        if (projectionType.isInterface()) {
            Object result = Proxy.newProxyInstance(classLoader, interfaces, (proxy, method, args) -> {
                Map<Method, Object> map = new HashMap<>();
                int i = 0;
                for (ProjectionAttribute attribute : info) {
                    Object value = os.get(i++);
                    map.put(attribute.getGetter(), value);
                }
                if (map.containsKey(method)) {
                    return map.get(method);
                }
                if (ProjectionProxyInstance.TO_STRING_METHOD.equals(method)) {
                    Map<String, Object> stringMap = new HashMap<>();
                    for (ProjectionAttribute attribute : info) {
                        stringMap.put(attribute.getFieldName(), map.get(attribute.getGetter()));
                    }
                    return projectionType.getSimpleName() + stringMap;
                }

                if (ProjectionProxyInstance.MAP_METHOD.equals(method)) {
                    return map;
                }

                if (ProjectionProxyInstance.CLASS_METHOD.equals(method)) {
                    return projectionType;
                }

                if (ProjectionProxyInstance.EQUALS_METHOD.equals(method)) {
                    if (proxy == args[0]) {
                        return true;
                    }
                    if (args[0] instanceof ProjectionProxyInstance instance) {
                        if (instance.__projectionClassOfProjectionProxyInstance__() == projectionType) {
                            return map.equals(instance.__dataMapOfProjectionProxyInstance__());
                        }
                    }
                    return false;
                }
                if (method.getDeclaringClass() == Object.class) {
                    return method.invoke(map, args);
                }
                if (method.isDefault()) {
                    return invokeDefaultMethod(proxy, method, args);
                }
                throw new AbstractMethodError(method.toString());
            });
            return TypeCastUtil.cast(result);
        } else {
            R result = projectionType.getConstructor().newInstance();
            for (int j = 0; j < os.length(); j++) {
                String name = paths.get(j);
                ProjectionAttribute attribute = info.get(name);
                Object value = os.get(j);
                attribute.setValue(result, value);
            }
            return result;
        }
    }

    private Object invokeDefaultMethod(Object proxy, Method method, Object[] args) throws Throwable {
        final float version = Float.parseFloat(System.getProperty("java.class.version"));
        if (version <= 52) {
            final Constructor<MethodHandles.Lookup> constructor = MethodHandles.Lookup.class
                    .getDeclaredConstructor(Class.class);
            constructor.setAccessible(true);

            final Class<?> clazz = method.getDeclaringClass();
            MethodHandles.Lookup lookup = constructor.newInstance(clazz);
            return lookup
                    .in(clazz)
                    .unreflectSpecial(method, clazz)
                    .bindTo(proxy)
                    .invokeWithArguments(args);
        } else {
            return lookup()
                    .findSpecial(
                            method.getDeclaringClass(),
                            method.getName(),
                            MethodType.methodType(method.getReturnType(), new Class[0]),
                            method.getDeclaringClass()
                    ).bindTo(proxy)
                    .invokeWithArguments(args);
        }
    }

    @Override
    public boolean exist(int offset) {
        return typeQueryFactory.getEntityResultQuery(querySpec, type).exist(offset);
    }

    private interface ProjectionProxyInstance {

        Method TO_STRING_METHOD = getMethod(() -> Object.class.getMethod("toString"));
        Method EQUALS_METHOD = getMethod(() -> Object.class.getMethod("equals", Object.class));
        Method MAP_METHOD = getMethod(() ->
                ProjectionProxyInstance.class.getMethod("__dataMapOfProjectionProxyInstance__"));
        Method CLASS_METHOD = getMethod(() ->
                ProjectionProxyInstance.class.getMethod("__projectionClassOfProjectionProxyInstance__"));

        @SneakyThrows
        static Method getMethod(Callable<Method> method) {
            return method.call();
        }

        Map<Method, Object> __dataMapOfProjectionProxyInstance__();

        Class<?> __projectionClassOfProjectionProxyInstance__();

    }
}
