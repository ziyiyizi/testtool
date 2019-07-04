package com.st.testtool.view;

import java.lang.reflect.Method;
import java.util.Map;

public class ClassInfo {
	//编译成功后的类名
    public String clsName;
    //方法名称列表
    public String[] methodNames;
    //参数列表
    public String[] paras;
    //返回值列表
    public String[] returnTypes;
    
    public Map classMap;

    /**
     * 传入类名，分析类的方法，参数，和返回类型
     * @param cls 类对象
     */
    public ClassInfo(Class<?> cls) {
        clsName = cls.getName();//类名
        Method[] methods = cls.getDeclaredMethods();//类的方法列表
        //初始化
        methodNames = new String[methods.length];
        paras = new String[methods.length];
        returnTypes = new String[methods.length];

        //遍历方法列表
        for (int i = 0; i < methods.length; ++i) {
            //获取方法名
            methodNames[i] = methods[i].getName();
            Class<?>[] paraTypes = methods[i].getParameterTypes();
            paras[i] = "";
            //遍历参数列表
            for (int j = 0; j < paraTypes.length; ++j) {
                //获取参数名
                paras[i] += (paraTypes[j].getSimpleName());
                if (j != paraTypes.length - 1) {
                    paras[i] += ",";
                }
            }
            //获取返回类型
            returnTypes[i] = methods[i].getReturnType().getSimpleName();
        }
    }

}
