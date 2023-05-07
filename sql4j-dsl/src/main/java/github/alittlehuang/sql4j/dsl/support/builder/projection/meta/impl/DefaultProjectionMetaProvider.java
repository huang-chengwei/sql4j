package github.alittlehuang.sql4j.dsl.support.builder.projection.meta.impl;

import github.alittlehuang.sql4j.dsl.support.builder.projection.meta.ProjectionAttribute;
import github.alittlehuang.sql4j.dsl.support.builder.projection.meta.ProjectionMeta;
import github.alittlehuang.sql4j.dsl.support.builder.projection.meta.ProjectionMetaProvider;
import github.alittlehuang.sql4j.dsl.util.BasicTypes;
import lombok.Lombok;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultProjectionMetaProvider implements ProjectionMetaProvider {

    private static final Map<Class<?>, Map<Class<?>, ProjectionMeta>> cache = new ConcurrentHashMap<>();

    public static final ProjectionMetaProvider DEFAULT = new DefaultProjectionMetaProvider();

    private DefaultProjectionMetaProvider() {
    }

    @Override
    public ProjectionMeta getProjectionAttributes(Class<?> entityType, Class<?> projectionType) {
        return cache.computeIfAbsent(entityType, k -> new ConcurrentHashMap<>())
                .computeIfAbsent(projectionType, k -> {
                    Map<String, ProjectionAttribute> nameMap = new HashMap<>();
                    try {

                        BeanInfo entityBeanInfo = Introspector.getBeanInfo(projectionType);
                        HashMap<String, PropertyDescriptor> map = new HashMap<>();
                        for (PropertyDescriptor descriptor : entityBeanInfo.getPropertyDescriptors()) {
                            String name = descriptor.getName();
                            map.put(name, descriptor);
                        }

                        BeanInfo projectionBeanInfo = Introspector.getBeanInfo(projectionType);
                        for (PropertyDescriptor descriptor : projectionBeanInfo.getPropertyDescriptors()) {
                            String name = descriptor.getName();
                            PropertyDescriptor a = map.get(name);
                            if (a == null || a.getPropertyType() != descriptor.getPropertyType() || !BasicTypes.isBasicType(a.getPropertyType())) {
                                continue;
                            }
                            Method getter = descriptor.getReadMethod();
                            Method setter = descriptor.getWriteMethod();
                            ProjectionAttribute attribute = new DefaultProjectionAttribute(name, getter, setter);
                            nameMap.put(name, attribute);
                        }
                    } catch (Exception e) {
                        throw Lombok.sneakyThrow(e);
                    }
                    return new DefaultProjectionMapping(nameMap);
                });
    }
}
