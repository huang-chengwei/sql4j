package jdbc.meta;

import github.alittlehuang.sql4j.dsl.util.Assert;
import github.alittlehuang.sql4j.dsl.util.TypeCastUtil;
import lombok.Getter;

import javax.persistence.*;
import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author ALittleHuang
 */
public class EntityInformation<T> {

    private static final Map<Class<?>, EntityInformation<?>> MAP = new ConcurrentHashMap<>();
    private static final String FIX = "`";

    @Getter
    private final Class<T> javaType;
    @Getter
    private final Attribute idAttribute;
    @Getter
    private final Attribute versionAttribute;
    @Getter
    private final List<Attribute> allAttributes;
    @Getter
    private final List<Attribute> basicAttributes;
    @Getter
    private final List<Attribute> basicUpdatableAttributes;
    @Getter
    private final List<Attribute> basicInsertableAttributes;
    @Getter
    private final List<Attribute> manyToOneAttributes;
    @Getter
    private final List<Attribute> oneToManyAttributes;
    @Getter
    private final List<Attribute> manyToManyAttributes;
    @Getter
    private final List<Attribute> oneToOneAttributes;
    @Getter
    private final String tableName;

    private final boolean hasVersion;
    /**
     * k->field name, v->attribute
     */
    private final Map<String, Attribute> nameMap;
    /**
     * k->column name, v->attribute
     */
    private final Map<String, Attribute> columnNameMap;

    private final Map<Method, Attribute> getterMap;


    private EntityInformation(Class<T> javaType) {
        this.javaType = javaType;
        this.allAttributes = initAttributes(javaType);
        this.idAttribute = initIdAttribute();
        this.versionAttribute = initVersionAttribute();
        this.hasVersion = versionAttribute != null;
        this.tableName = initTableName();
        List<Attribute> basicAttributes = new ArrayList<>();
        List<Attribute> manyToOneAttributes = new ArrayList<>();
        List<Attribute> oneToManyAttributes = new ArrayList<>();
        List<Attribute> manyToManyAttributes = new ArrayList<>();
        List<Attribute> oneToOneAttributes = new ArrayList<>();
        for (Attribute attribute : this.allAttributes) {
            if (attribute.getManyToOne() != null) {
                manyToOneAttributes.add(attribute);
            } else if (attribute.getOneToMany() != null) {
                oneToManyAttributes.add(attribute);
            } else if (attribute.getManyToMany() != null) {
                manyToManyAttributes.add(attribute);
            } else if (attribute.getOneToOne() != null) {
                oneToOneAttributes.add(attribute);
            } else {
                basicAttributes.add(attribute);
            }
        }

        this.basicAttributes = Collections.unmodifiableList(basicAttributes);
        this.basicUpdatableAttributes = this.basicAttributes.stream()
                .filter(it -> it.getColumn() == null
                              || it.getColumn().updatable()).collect(Collectors.toList());
        this.basicInsertableAttributes = this.basicAttributes.stream()
                .filter(it -> it.getColumn() == null
                              || it.getColumn().insertable()).collect(Collectors.toList());
        this.manyToOneAttributes = Collections.unmodifiableList(manyToOneAttributes);
        this.oneToManyAttributes = Collections.unmodifiableList(oneToManyAttributes);
        this.manyToManyAttributes = Collections.unmodifiableList(manyToManyAttributes);
        this.oneToOneAttributes = Collections.unmodifiableList(oneToOneAttributes);

        nameMap = Collections.unmodifiableMap(
                allAttributes.stream().collect(Collectors.toMap(Attribute::getFieldName, Function.identity())));
        columnNameMap = Collections.unmodifiableMap(
                allAttributes.stream().collect(Collectors.toMap(Attribute::getColumnName, Function.identity())));
        getterMap = Collections.unmodifiableMap(allAttributes
                .stream().filter(it -> it.getGetter() != null)
                .collect(Collectors.toMap(Attribute::getGetter, Function.identity()))
        );
    }

    public static <X> EntityInformation<X> getInstance(Class<X> clazz) {
        EntityInformation<?> information = MAP.computeIfAbsent(clazz, EntityInformation::new);
        return TypeCastUtil.cast(information);
    }

    private List<Attribute> initAttributes(Class<T> javaType) {
        List<Attribute> attributes = new ArrayList<>();
        Field[] fields = javaType.getDeclaredFields();
        Map<Field, Method> readerMap = new HashMap<>();
        Map<Field, Method> writeMap = new HashMap<>();
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(javaType);
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            for (PropertyDescriptor descriptor : propertyDescriptors) {
                Field field = getDeclaredField(javaType, descriptor.getName());
                if (field == null) {
                    continue;
                }
                readerMap.put(field, descriptor.getReadMethod());
                writeMap.put(field, descriptor.getWriteMethod());
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        for (Field field : fields) {
            if (!readerMap.containsKey(field)) {
                readerMap.put(field, null);
            }
            if (!writeMap.containsKey(field)) {
                writeMap.put(field, null);
            }
        }

        for (Field field : writeMap.keySet()) {

            if (Modifier.isStatic(field.getModifiers())) {
                continue;
            }

            Attribute attribute = new Attribute(field, readerMap.get(field), writeMap.get(field), javaType);
            if (attribute.getAnnotation(Transient.class) == null
                && !Modifier.isStatic(field.getModifiers())) {
                attributes.add(attribute);
            }
        }
        return Collections.unmodifiableList(attributes);
    }

    private Attribute initIdAttribute() {
        Attribute id = null;
        for (Attribute attribute : allAttributes) {
            if (attribute.getAnnotation(Id.class) != null) {
                id = attribute;
                break;
            }

            if ("id".equals(attribute.getFieldName())) {
                id = attribute;
            }

        }
        if (id != null) {
            return id;
        }
        throw new RuntimeException("entity " + javaType + " has no id attribute");
    }

    private Attribute initVersionAttribute() {
        for (Attribute attribute : allAttributes) {
            if (attribute.getAnnotation(Version.class) != null) {
                Class<?> type = attribute.getJavaType();
                Assert.state(type == Integer.class || type == int.class
                             || type == Long.class || type == long.class,
                        "version attribute type must be integer or long");
                return attribute;
            }
        }
        return null;
    }

    private String initTableName() {
        Entity entity = javaType.getAnnotation(Entity.class);
        if (entity != null && entity.name().length() > 0) {
            return entity.name();
        }
        Table table = javaType.getAnnotation(Table.class);
        if (table != null && table.name().length() > 0) {
            return table.name();
        }
        String tableName = javaType.getSimpleName().replaceAll("([a-z])([A-Z])", "$1_$2").toLowerCase();
        if (tableName.startsWith(FIX) && tableName.endsWith(FIX)) {
            tableName = tableName.substring(1, tableName.length() - 1);
        }
        return tableName;
    }

    public static Field getDeclaredField(Class<?> clazz, String name) {
        try {
            return clazz.getDeclaredField(name);
        } catch (NoSuchFieldException e) {
            Class<?> superclass = clazz.getSuperclass();
            if (superclass != null) {
                return getDeclaredField(superclass, name);
            }
        }
        return null;
    }

    public Attribute getAttribute(String name) {
        return nameMap.get(name);
    }

    public Attribute getAttributeByGetter(Method method) {
        return getterMap.get(method);
    }

    public Attribute getAttributeByColumnName(String name) {
        return columnNameMap.get(name);
    }

    public boolean hasVersion() {
        return hasVersion;
    }

}
