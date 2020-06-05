package io.github.fukkitmc.fukkit.nms;

import net.fabricmc.loader.api.FabricLoader;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * Remaps reflection and method handles
 */
@SuppressWarnings("unused")
public class ReflectionRemapper {

    private static final String VERSION = "v1_15_R2";
    private static final boolean DEBUG = FabricLoader.getInstance().isDevelopmentEnvironment();

    // As concerning as it is, we should just insert the "version" (R1_15_2) thing here
    public static String package_getName(Package pckg) {
        if (DEBUG) {
            System.out.println("pckg = " + pckg);
        }

        String s = pckg.getName();

        if (s.equals("net.minecraft.server")) {
            return "net.minecraft.server." + VERSION;
        } else if (s.startsWith("net.minecraft.server.")) {
            return "net.minecraft.server." + VERSION + s.substring(20);
        } else if (s.equals("org.bukkit.craftbukkit")) {
            return "org.bukkit.craftbukkit." + VERSION;
        } else if (s.startsWith("org.bukkit.craftbukkit.")) {
            return "org.bukkit.craftbukkit." + VERSION + s.substring(22);
        } else {
            return s;
        }
    }

    public static String class_getName(Class<?> clazz) {
        if (DEBUG) {
            System.err.println("clazz = " + clazz);
        }

        String s = clazz.getName();

        if (s.startsWith("net.minecraft.server.")) {
            return "net.minecraft.server." + VERSION + s.substring(20); // Up to the .
        } else if (s.startsWith("org.bukkit.craftbukkit.")) {
            return "org.bukkit.craftbukkit." + VERSION + s.substring(22);
        }

        return s;
    }

    public static String mapClassName(String className) {
        if (DEBUG) {
            System.err.println("className = " + className);
        }

        if (className.startsWith("org.bukkit.craftbukkit." + VERSION + ".")) {
            return "org.bukkit.craftbukkit." + className.substring(23 + VERSION.length() + 1);
        }

        if (className.startsWith("net.minecraft.server." + VERSION + ".")) {
            String c = className.substring(21 + VERSION.length() + 1);

            // TODO: Map class name
        }

        return className;
    }

    public static Class<?> class_forName(String name, boolean initialize, ClassLoader loader) throws ClassNotFoundException {
        return Class.forName(mapClassName(name), initialize, loader);
    }

    public static Class<?> classloader_loadClass(ClassLoader loader, String name) throws ClassNotFoundException {
        return loader.loadClass(mapClassName(name));
    }

    public static Field class_getField(Class<?> c, String name) throws NoSuchFieldException {
        // Since the JVM is going to think *we* called it, so access checks might crab
        Field field = class_getDeclaredField(c, name);
        field.setAccessible(true);
        return field;
    }

    public static Field class_getDeclaredField(Class<?> c, String name) throws NoSuchFieldException {
        if (DEBUG) {
            System.err.println("c = " + c + ", name = " + name);
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
            System.err.println("c = " + c + ", name = " + name + ", parameterTypes = " + Arrays.deepToString(parameterTypes));
        }

        return c.getDeclaredMethod(name, parameterTypes);
    }

    public static MethodHandle lookup_findGetter(MethodHandles.Lookup lookup, Class<?> refc, String name, Class<?> type) throws NoSuchFieldException, IllegalAccessException {
        if (DEBUG) {
            System.err.println("lookup = " + lookup + ", refc = " + refc + ", name = " + name + ", type = " + type);
        }

        return lookup.findGetter(refc, name, type);
    }

    public static MethodHandle lookup_findStaticGetter(MethodHandles.Lookup lookup, Class<?> refc, String name, Class<?> type) throws NoSuchFieldException, IllegalAccessException {
        if (DEBUG) {
            System.err.println("lookup = " + lookup + ", refc = " + refc + ", name = " + name + ", type = " + type);
        }

        return lookup.findStaticGetter(refc, name, type);
    }

    public static MethodHandle lookup_findSetter(MethodHandles.Lookup lookup, Class<?> refc, String name, Class<?> type) throws NoSuchFieldException, IllegalAccessException {
        if (DEBUG) {
            System.err.println("lookup = " + lookup + ", refc = " + refc + ", name = " + name + ", type = " + type);
        }

        return lookup.findSetter(refc, name, type);
    }

    public static MethodHandle lookup_findStaticSetter(MethodHandles.Lookup lookup, Class<?> refc, String name, Class<?> type) throws NoSuchFieldException, IllegalAccessException {
        if (DEBUG) {
            System.err.println("lookup = " + lookup + ", refc = " + refc + ", name = " + name + ", type = " + type);
        }

        return lookup.findStaticSetter(refc, name, type);
    }

    public static MethodHandle lookup_findVirtual(MethodHandles.Lookup lookup, Class<?> refc, String name, MethodType type) throws IllegalAccessException, NoSuchMethodException {
        if (DEBUG) {
            System.err.println("lookup = " + lookup + ", refc = " + refc + ", name = " + name + ", type = " + type);
        }

        return lookup.findVirtual(refc, name, type);
    }

    public static MethodHandle lookup_findStatic(MethodHandles.Lookup lookup, Class<?> refc, String name, MethodType type) throws IllegalAccessException, NoSuchMethodException {
        if (DEBUG) {
            System.err.println("lookup = " + lookup + ", refc = " + refc + ", name = " + name + ", type = " + type);
        }

        return lookup.findStatic(refc, name, type);
    }

    public static MethodHandle lookup_findSpecial(MethodHandles.Lookup lookup, Class<?> refc, String name, MethodType type, Class<?> specialCaller) throws IllegalAccessException, NoSuchMethodException {
        if (DEBUG) {
            System.err.println("lookup = " + lookup + ", refc = " + refc + ", name = " + name + ", type = " + type + ", specialCaller = " + specialCaller);
        }

        return lookup.findSpecial(refc, name, type, specialCaller);
    }
}
