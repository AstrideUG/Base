package com.astrideug.base.reflections;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class AdvancedReflections {

    public static ArrayList<Field> getFieldsAnnotatedWith(Class<?> clazz, Class<? extends Annotation> annotation) {
        ArrayList<Field> fields = new ArrayList<>();

        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);
            if (field.isAnnotationPresent(annotation)) {
                field.setAccessible(true);
                fields.add(field);
            }
        }

        return fields;
    }

    public static ArrayList<Field> getFieldsOfType(Class<?> clazz, Class<?> type) {
        ArrayList<Field> fields = new ArrayList<>();

        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);
            if (field.getType().isAssignableFrom(type)) {
                fields.add(field);
            }
        }

        return fields;
    }

    public static ArrayList<Field> getFieldsImplementingType(Class<?> clazz, Class<?> type) {
        ArrayList<Field> fields = new ArrayList<>();

        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);
            if (type.isAssignableFrom(field.getType())) {
                fields.add(field);
            }
        }

        return fields;
    }

    public static ArrayList<Class> getClassesImplementingType(List<Class> classes, Class<?> type) {
        ArrayList<Class> list = new ArrayList<>();
        for (Class c : classes) {
            if (type.isAssignableFrom(c)) {
                list.add(c);
            }
        }
        return list;
    }

    public static ArrayList<Class> getClassesAnnotated(List<Class> classes, Class<? extends Annotation> annoatation) {
        ArrayList<Class> list = new ArrayList<>();
        for (Class c : classes) {
            if (c.isAnnotationPresent(annoatation)) {
                list.add(c);
            }
        }
        return list;
   }

   @SuppressWarnings("unchecked")
   public static <K> K getAnnotationOrDefault(Class clazz, K defaultValue, Class<K> kClass) {
        if (clazz.isAnnotationPresent(kClass)) {
            return (K) clazz.getAnnotation(kClass);
        } else {
            return defaultValue;
        }
   }
}
