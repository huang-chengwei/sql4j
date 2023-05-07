// package github.alittlehuang.sql4j.dsl.util;
//
// import java.lang.reflect.Field;
//
// public class ReflectUtil {
//     public static Field getDeclaredField(Class<?> clazz, String name) {
//         try {
//             return clazz.getDeclaredField(name);
//         } catch (NoSuchFieldException e) {
//             Class<?> superclass = clazz.getSuperclass();
//             if (superclass != null) {
//                 return getDeclaredField(superclass, name);
//             }
//         }
//         return null;
//     }
//
// }
