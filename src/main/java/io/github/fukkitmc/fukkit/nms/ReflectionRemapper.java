package io.github.fukkitmc.fukkit.nms;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * Remaps reflection and method handles
 */
public class ReflectionRemapper {

    private static final boolean DEBUG = true;

    // As concerning as it is, we should just insert the "version" (R1_15_2) thing here
    public static String class_getName(Class<?> clazz) {
        if (DEBUG) {
            System.out.println("clazz = " + clazz);
        }

        return clazz.getName();
    }

    public static Class<?> class_forName(String className) throws ClassNotFoundException {
        if (DEBUG) {
            System.out.println("className = " + className);
        }

        return Class.forName(className);
    }

    public static Class<?> class_forName(String name, boolean initialize, ClassLoader loader) throws ClassNotFoundException {
        if (DEBUG) {
            System.out.println("name = " + name + ", initialize = " + initialize + ", loader = " + loader);
        }

        return Class.forName(name, initialize, loader);
    }

    public static Class<?> classloader_loadClass(ClassLoader loader, String name) throws ClassNotFoundException {
        if (DEBUG) {
            System.out.println("loader = " + loader + ", name = " + name);
        }

        return loader.loadClass(name);
    }

    public static Field class_getField(Class<?> c, String name) throws NoSuchFieldException {
        // Since the JVM is going to think *we* called it, so access checks might crab
        Field field = class_getDeclaredField(c, name);
        field.setAccessible(true);
        return field;
    }

    public static Field class_getDeclaredField(Class<?> c, String name) throws NoSuchFieldException {
        if (DEBUG) {
            System.out.println("c = " + c + ", name = " + name);
        }

        return c.getDeclaredField(name);
    }

    public static Method class_getMethod(Class<?> c, String name, Class<?>[] parameterTypes) throws NoSuchMethodException {
        // Since the JVM is going to think *we* called it, so access checks might crab
        Method method = class_getDeclaredMethod(c, name, parameterTypes);
        method.setAccessible(true);
        return method;
    }

    public static Method class_getDeclaredMethod(Class<?> c, String name, Class<?>[] parameterTypes) throws NoSuchMethodException {
        if (DEBUG) {
            System.out.println("c = " + c + ", name = " + name + ", parameterTypes = " + Arrays.deepToString(parameterTypes));
        }

        return c.getDeclaredMethod(name, parameterTypes);
    }

    public static MethodHandle lookup_findGetter(MethodHandles.Lookup lookup, Class<?> refc, String name, Class<?> type) throws NoSuchFieldException, IllegalAccessException {
        if (DEBUG) {
            System.out.println("lookup = " + lookup + ", refc = " + refc + ", name = " + name + ", type = " + type);
        }

        return lookup.findGetter(refc, name, type);
    }

    public static MethodHandle lookup_findStaticGetter(MethodHandles.Lookup lookup, Class<?> refc, String name, Class<?> type) throws NoSuchFieldException, IllegalAccessException {
        if (DEBUG) {
            System.out.println("lookup = " + lookup + ", refc = " + refc + ", name = " + name + ", type = " + type);
        }

        return lookup.findStaticGetter(refc, name, type);
    }

    public static MethodHandle lookup_findSetter(MethodHandles.Lookup lookup, Class<?> refc, String name, Class<?> type) throws NoSuchFieldException, IllegalAccessException {
        if (DEBUG) {
            System.out.println("lookup = " + lookup + ", refc = " + refc + ", name = " + name + ", type = " + type);
        }

        return lookup.findSetter(refc, name, type);
    }

    public static MethodHandle lookup_findStaticSetter(MethodHandles.Lookup lookup, Class<?> refc, String name, Class<?> type) throws NoSuchFieldException, IllegalAccessException {
        if (DEBUG) {
            System.out.println("lookup = " + lookup + ", refc = " + refc + ", name = " + name + ", type = " + type);
        }

        return lookup.findStaticSetter(refc, name, type);
    }

    public static MethodHandle lookup_findVirtual(MethodHandles.Lookup lookup, Class<?> refc, String name, MethodType type) throws IllegalAccessException, NoSuchMethodException {
        if (DEBUG) {
            System.out.println("lookup = " + lookup + ", refc = " + refc + ", name = " + name + ", type = " + type);
        }

        return lookup.findVirtual(refc, name, type);
    }

    public static MethodHandle lookup_findStatic(MethodHandles.Lookup lookup, Class<?> refc, String name, MethodType type) throws IllegalAccessException, NoSuchMethodException {
        if (DEBUG) {
            System.out.println("lookup = " + lookup + ", refc = " + refc + ", name = " + name + ", type = " + type);
        }

        return lookup.findStatic(refc, name, type);
    }

    public static MethodHandle lookup_findSpecial(MethodHandles.Lookup lookup, Class<?> refc, String name, MethodType type, Class<?> specialCaller) throws IllegalAccessException, NoSuchMethodException {
        if (DEBUG) {
            System.out.println("lookup = " + lookup + ", refc = " + refc + ", name = " + name + ", type = " + type + ", specialCaller = " + specialCaller);
        }

        return lookup.findSpecial(refc, name, type, specialCaller);
    }
}
