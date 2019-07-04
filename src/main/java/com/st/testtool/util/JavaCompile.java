package com.st.testtool.util;

import java.io.File;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;

import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;

public class JavaCompile {
	private static JavaCompiler javaCompiler = ToolProvider.getSystemJavaCompiler();
	private static URLClassLoader classLoader = null;
	 
    private static JavaCompiler getJavaCompiler() {

        return javaCompiler;
    }
    
    private static URLClassLoader getClassLoader() {

        return classLoader;
    }
    
    public static boolean CompilerJavaFile(File file, String classFileOutputPath) {
        // 设置编译选项，配置class文件输出路径
        Iterable<String> options = Arrays.asList("-d", classFileOutputPath);

        StandardJavaFileManager fileManager = getJavaCompiler()
                .getStandardFileManager(null, null, null);
 
        Iterable<? extends JavaFileObject> compilationUnits = fileManager
                .getJavaFileObjectsFromFiles(Arrays.asList(file));
 
        return getJavaCompiler().getTask(null, fileManager, null, options,
                null, compilationUnits).call();
    }
 
    public static boolean CompilerJavaFile(String sourceFileInputPath,
            String classFileOutputPath) {
        // 设置编译选项，配置class文件输出路径
        Iterable<String> options = Arrays.asList("-d", classFileOutputPath);

        StandardJavaFileManager fileManager = getJavaCompiler()
                .getStandardFileManager(null, null, null);
 
        Iterable<? extends JavaFileObject> compilationUnits = fileManager
                .getJavaFileObjectsFromFiles(Arrays.asList(new File(
                        sourceFileInputPath)));
 
        return getJavaCompiler().getTask(null, fileManager, null, options,
                null, compilationUnits).call();
    }
    
    public static void ClassLoader(String sourceFileInputPath) throws MalformedURLException {
    	URL[] urls = new URL[] {
    			new URL("file:/" + sourceFileInputPath)
    	};
    	classLoader = new URLClassLoader(urls);
    	
    }
    
    public static Class<?> LoadClass(String clsName) throws ClassNotFoundException{
    	if (classLoader == null) {
    		return null;
    	}
    	if (clsName.contains(".java")) {
    		clsName = clsName.substring(0, clsName.length() - 5);
    	}
    	Class<?> userCls = classLoader.loadClass(clsName);
    	return userCls;
    }
    
    public static Object Invoke(Class<?> cls, String methodName, Class<?>[] paramsCls, Object[] params) {
        Object result = null;
        try {
            Method method = cls.getDeclaredMethod(methodName, paramsCls);
            Object obj = cls.newInstance();
            result = method.invoke(obj, params);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
