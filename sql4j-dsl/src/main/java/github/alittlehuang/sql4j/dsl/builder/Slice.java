package github.alittlehuang.sql4j.dsl.builder;

import java.util.List;

public record Slice<T>(List<T> date, Sliceable sliceable, long total) {


}