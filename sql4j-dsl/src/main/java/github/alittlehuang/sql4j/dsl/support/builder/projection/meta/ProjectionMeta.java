package github.alittlehuang.sql4j.dsl.support.builder.projection.meta;


public interface ProjectionMeta extends Iterable<ProjectionAttribute> {

    ProjectionAttribute get(String name);


}
