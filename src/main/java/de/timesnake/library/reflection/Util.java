/*
 * Copyright (C) 2023 timesnake
 */

package de.timesnake.library.reflection;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Optional;

@Deprecated
public class Util {

    public static Object getInstanceField(Object instance, String fieldName) {
        Field field = null;
        try {
            field = instance.getClass().getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            if (instance.getClass().getSuperclass() != null) {
                return getInstanceField(instance.getClass().getSuperclass(), instance, fieldName);
            }
            e.printStackTrace();
        }
        field.setAccessible(true);

        try {
            return field.get(instance);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Object getInstanceField(Class<?> clazz, Object instance, String fieldName) {
        Field field = null;
        try {
            field = clazz.getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            if (clazz.equals(Object.class)) {
                return null;
            }
            if (clazz.getSuperclass() != null) {
                return getInstanceField(clazz.getSuperclass(), instance, fieldName);
            }
            e.printStackTrace();
        }
        field.setAccessible(true);

        try {
            return field.get(instance);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void setInstanceField(Object instance, String fieldName, Object value) {
        Field field = null;
        try {
            field = instance.getClass().getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            if (instance.getClass().getSuperclass() != null) {
                setInstanceField(instance.getClass().getSuperclass(), instance, fieldName, value);
                return;
            }
            e.printStackTrace();
        }
        field.setAccessible(true);

        try {
            field.set(instance, value);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public static void setInstanceField(Class<?> clazz, Object instance, String fieldName, Object value) {
        Field field = null;
        try {
            field = clazz.getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            if (clazz.getSuperclass() != null) {
                setInstanceField(clazz.getSuperclass(), instance, fieldName, value);
                return;
            }
            e.printStackTrace();
        }
        field.setAccessible(true);

        try {
            field.set(instance, value);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public static Class<?> getClass(String name) {
        try {
            return Class.forName(name);
        } catch (ClassNotFoundException e) {
            return null;
        }
    }

    public static Object invokeMethod(Object handle, String methodName, Object... args) {
        Class<?>[] parameterClasses = new Class[args.length];
        for (int i = 0; i < args.length; i++) {
            parameterClasses[i] = args[i].getClass();
        }
        return invokeMethod(handle.getClass(), handle, methodName, parameterClasses, args);
    }

    public static Object invokeMethod(Class<?> clazz, Object handle, String methodName, Object... args) {
        Class<?>[] parameterClasses = new Class[args.length];
        for (int i = 0; i < args.length; i++) {
            parameterClasses[i] = args[i].getClass();
        }
        return invokeMethod(clazz, handle, methodName, parameterClasses, args);
    }

    @SuppressWarnings("rawtypes")
    public static Object invokeMethod(Object handle, String methodName, Class[] parameterClasses, Object... args) {
        return invokeMethod(handle.getClass(), handle, methodName, parameterClasses, args);
    }

    @SuppressWarnings("rawtypes")
    protected static Object invokeMethod(Class<?> clazz, Object handle, String methodName, Class[] parameterClasses, Object... args) {
        Optional<Method> methodOptional = getMethod(clazz, methodName, parameterClasses);
        if (methodOptional.isEmpty()) {
            if (clazz.equals(Object.class)) {
                return null;
            }
            if (clazz.getSuperclass() != null) {
                return invokeMethod(clazz.getSuperclass(), handle, methodName, (Object[]) parameterClasses);
            }
            return null;
        }

        Method method = methodOptional.get();
        method.setAccessible(true);
        try {
            return method.invoke(handle, args);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Object getEnumConstant(Class<?> enumClass, String name) {
        if (!enumClass.isEnum()) return null;
        for (Object o : enumClass.getEnumConstants())
            if (name.equals(invokeMethod(o, "name", (Object[]) new Class[0]))) return o;
        return null;
    }

    public static Optional<Constructor<?>> getConstructor(Class<?> clazz, Class<?>... params) {
        try {
            return Optional.of(clazz.getConstructor(params));
        } catch (NoSuchMethodException e) {
            try {
                return Optional.of(clazz.getDeclaredConstructor(params));
            } catch (NoSuchMethodException ignored) {
            }
        }
        return Optional.empty();
    }

    public static Object instantiate(Constructor<?> constructor, Object... arguments) {
        constructor.setAccessible(true);
        try {
            return constructor.newInstance(arguments);
        } catch (Exception ignored) {
        }
        return null;
    }

    public static Object instantiate(Class<?> clazz, Class<?>[] params, Object... arguments) {
        Optional<Constructor<?>> c = getConstructor(clazz, params);
        return c.map(constructor -> instantiate(constructor, arguments)).orElse(null);
    }

    protected static Optional<Method> getMethod(Class<?> clazz, String name, Class<?>... params) {
        try {
            return Optional.of(clazz.getMethod(name, params));
        } catch (NoSuchMethodException e) {
            try {
                return Optional.of(clazz.getDeclaredMethod(name, params));
            } catch (NoSuchMethodException ignored) {
            }
        }
        return Optional.empty();
    }

    protected static Optional<Field> getField(Class<?> clazz, String name) {
        try {
            return Optional.of(clazz.getField(name));
        } catch (NoSuchFieldException e) {
            try {
                return Optional.of(clazz.getDeclaredField(name));
            } catch (NoSuchFieldException ignored) {
            }
        }
        return Optional.empty();
    }
}
