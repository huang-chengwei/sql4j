package github.alittlehuang.sql4j.dsl.support.builder.projection.meta.impl;

import github.alittlehuang.sql4j.dsl.support.builder.projection.meta.ProjectionAttribute;
import github.alittlehuang.sql4j.dsl.support.builder.projection.meta.ProjectionMeta;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class DefaultProjectionMapping implements ProjectionMeta {

    private final Map<String, ProjectionAttribute> nameMap;

    public DefaultProjectionMapping(Map<String, ProjectionAttribute> nameMap) {
        this.nameMap = new HashMap<>(nameMap);
    }


    @Override
    public ProjectionAttribute get(String name) {
        return nameMap.get(name);
    }

    @NotNull
    @Override
    public Iterator<ProjectionAttribute> iterator() {
        return nameMap.values().iterator();
    }
}
