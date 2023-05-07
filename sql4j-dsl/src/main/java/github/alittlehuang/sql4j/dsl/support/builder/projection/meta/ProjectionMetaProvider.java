package github.alittlehuang.sql4j.dsl.support.builder.projection.meta;


import github.alittlehuang.sql4j.dsl.support.builder.projection.meta.impl.DefaultProjectionMetaProvider;

public interface ProjectionMetaProvider {

    ProjectionMetaProvider DEFAULT = DefaultProjectionMetaProvider.DEFAULT;
    ProjectionMeta getProjectionAttributes(Class<?> entityType, Class<?> projectionType);

}
