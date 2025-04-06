//package com.xiong.bitmanager.common.util;
//
//import com.baomidou.mybatisplus.annotation.TableField;
//import com.baomidou.mybatisplus.annotation.TableName;
//import org.springframework.jdbc.core.JdbcTemplate;
//import org.springframework.stereotype.Component;
//
//import java.lang.reflect.Field;
//import java.lang.reflect.Modifier;
//import java.util.*;
//
//@Component
//public class TableSchemaComparator {
//    private final JdbcTemplate jdbcTemplate;
//
//    public TableSchemaComparator(JdbcTemplate jdbcTemplate) {
//        this.jdbcTemplate = jdbcTemplate;
//    }
//
//    public List<String> generateAlterStatements(Class<?> entityClass) {
//        String tableName = getTableName(entityClass);
//        Map<String, String> dbColumns = getDatabaseColumns(tableName);
//        Map<String, Class<?>> entityFields = getEntityFields(entityClass);
//
//        List<String> sqlList = new ArrayList<>();
//
//        // 检测新增字段
//        entityFields.forEach((fieldName, fieldType) -> {
//            if (!dbColumns.containsKey(fieldName.toUpperCase())) {
//                sqlList.add(String.format("ALTER TABLE %s ADD COLUMN %s %s;",
//                        tableName, fieldName, getSqlType(fieldType)));
//            }
//        });
//
//        return sqlList;
//    }
//
//    public String getTableName(Class<?> entityClass) {
//        TableName tableAnnotation = entityClass.getAnnotation(TableName.class);
//        return (tableAnnotation != null) ? tableAnnotation.value() :
//                entityClass.getSimpleName().toLowerCase();
//    }
//
//    private Map<String, String> getDatabaseColumns(String tableName) {
//        return jdbcTemplate.query("PRAGMA table_info(" + tableName + ")", rs -> {
//            Map<String, String> columns = new HashMap<>();
//            while (rs.next()) {
//                columns.put(rs.getString("name").toUpperCase(),
//                        rs.getString("type").toUpperCase());
//            }
//            return columns;
//        });
//    }
//
//    private Map<String, Class<?>> getEntityFields(Class<?> entityClass) {
//        Map<String, Class<?>> fields = new HashMap<>();
//        for (Field field : entityClass.getDeclaredFields()) {
//            TableField annotation = field.getAnnotation(TableField.class);
//            if (annotation == null || (annotation.exist() && !Modifier.isStatic(field.getModifiers()))) {
//                if (!Modifier.isStatic(field.getModifiers())) {
//                    fields.put(field.getName().toUpperCase(), field.getType());
//                }
//            }
//        }
//        return fields;
//    }
//
//    private String getSqlType(Class<?> javaType) {
//        if (javaType == String.class) return "TEXT";
//        if (javaType == Integer.class || javaType == int.class) return "INTEGER";
//        if (javaType == Long.class || javaType == long.class) return "BIGINT";
//        if (javaType == Boolean.class || javaType == boolean.class) return "BOOLEAN";
//        if (javaType == Date.class) return "DATETIME";
//        if (javaType == Double.class || javaType == double.class) return "REAL";
//        return "TEXT";
//    }
//}
