package com.github.wjt.poitrans.mapping;

import com.github.wjt.poitrans.parser.BaseSQLInfo;
import com.github.wjt.poitrans.statement.ResultMap;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.List;
import java.util.Objects;

public class DefaultBeanMapping implements BeanMapping {

    private static DefaultBeanMapping single;

    private DefaultBeanMapping() {
    }

    public static DefaultBeanMapping getMapping() {
        if (Objects.isNull(single))
            single = new DefaultBeanMapping();
        return single;
    }

    @Override
    public <T> T getBean(ResultMap<String, String> resultMap, BaseSQLInfo baseSQLInfo, Class<T> tClass) throws IllegalAccessException, InstantiationException {
        T t = tClass.newInstance();
        Method[] declaredMethods = tClass.getDeclaredMethods();
        List<String> fields = baseSQLInfo.getFields();
        if (fields.contains("*")) {
            // 表示獲取到所有的元素
            resultMap.entrySet().forEach(item -> {
                String key = item.getKey();
                String value = item.getValue();
                findAndInvokeMethod(declaredMethods, key, value, t);
            });
        } else {
            fields.forEach(field -> {
                resultMap.entrySet().forEach(item -> {
                    String key = item.getKey();
                    String value = item.getValue();
                    if (Objects.equals(field, key)) {
                        findAndInvokeMethod(declaredMethods, key, value, t);
                    }
                });
            });
        }
        return t;
    }

    private <T> void findAndInvokeMethod(Method[] declaredMethods, String key, String value, T t) {
        for (Method declaredMethod : declaredMethods) {
            String name = declaredMethod.getName();
            Parameter[] parameters = declaredMethod.getParameters();
            if (name.startsWith("set") && Objects.equals(name.substring(3, name.length()).toUpperCase(), key.toUpperCase())) {
                Class<?> type = parameters[0].getType();
                try {
                    this.handleSetValue(type, value, t, declaredMethod);
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private <T> void handleSetValue(Class<?> type, String value, T t, Method declaredMethod) throws InvocationTargetException, IllegalAccessException {
        switch (type.getName()) {
            case "java.lang.String" :
                declaredMethod.invoke(t, value);
                break;
            case "java.lang.Integer" :

                declaredMethod.invoke(t, new Double(value).intValue());
        }
    }

}
