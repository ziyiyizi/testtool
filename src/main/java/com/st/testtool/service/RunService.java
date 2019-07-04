package com.st.testtool.service;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import com.csvreader.CsvReader;

import org.springframework.stereotype.Service;

import com.st.testtool.controller.TestController;
import com.st.testtool.util.JavaCompile;
import com.st.testtool.view.Result;

@Service
public class RunService {
	public Result run(String signature)
	{
		//初始化 类，方法参数列表，输入输出
        Class<?> cls = TestController.userCls;
        CsvReader csvReader;
        Class<?>[] paraTypes = getParamTypes(signature);
        Object[] objs = new Object[paraTypes.length];
        List<String> inputs = new ArrayList<>();
        List<String> expects = new ArrayList<>();
        List<Boolean> results = new ArrayList<>();
        List<Object> outputs = new ArrayList<>();

        try {
            //读取测试用例
            csvReader = new CsvReader(TestController.csvFile.getPath(), ',', Charset.forName("UTF-8"));
            csvReader.readHeaders();

            //遍历每一条测试用例
            while (csvReader.readRecord()) {
                String input = "";
                for (int i = 0; i < csvReader.getColumnCount() - 1; i++) {
                    input += csvReader.get(i);
                    input += " ";

                    //根据参数类型，将测试用例中的输入转换到对应的类型
                    if (paraTypes[i] == int.class) {
                        objs[i] = Integer.valueOf(csvReader.get(i));
                    } else if (paraTypes[i] == String.class) {
                        objs[i] = csvReader.get(i);
                    } else if (paraTypes[i] == double.class) {
                        objs[i] = Double.valueOf(csvReader.get(i));
                    } else if (paraTypes[i] == boolean.class) {
                        objs[i] = Boolean.valueOf(csvReader.get(i));
                    } else if (paraTypes[i] == float.class) {
                        objs[i] = Float.valueOf(csvReader.get(i));
                    } else if (paraTypes[i] == char.class) {
                        objs[i] = csvReader.get(i).charAt(0);
                    } else if (paraTypes[i] == long.class) {
                        objs[i] = Long.valueOf(csvReader.get(i));
                    } else {
						System.out.println("null");
					}
                }
                //将执行测试用例后的结果暂存到数组中
                inputs.add(input);
                Object output = JavaCompile.Invoke(cls, getMethodName(signature), paraTypes, objs);
                outputs.add(output);
                String expect = csvReader.get(csvReader.getColumnCount() - 1);
                expects.add(expect);
                System.out.println("columnCount: " + csvReader.getColumnCount());

                if (output == null) {
                	System.out.println("output is null");
                } else {
                	results.add(expect.equals(output.toString()));
                }
                
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new Result(inputs, outputs, expects, results);
	}
	
	/**
     * 获取方法名
     * @param signature 函数签名
     * @return 函数名
     */
    private String getMethodName(String signature) {
        String[] split = signature.split(",");
        return split[0].trim();
    }

    /**
     * 给一个函数名，获取函数的参数类型
     * @param signature 函数名
     * @return 每个类型作为一个Class返回
     */
    private Class<?>[] getParamTypes(String signature) {
        String[] split = signature.split(",");
        Class<?>[] classes = new Class[split.length - 1];
        for (int i = 1; i < split.length; ++i) {
            switch (split[i].trim()) {
                case "int":
                    classes[i - 1] = int.class;
                    break;
                case "double":
                    classes[i - 1] = double.class;
                    break;
                case "String":
                    classes[i - 1] = String.class;
                    break;
                case "boolean":
                    classes[i - 1] = boolean.class;
                    break;
                case "float":
                    classes[i - 1] = float.class;
                    break;
                case "char":
                    classes[i - 1] = char.class;
                    break;
                case "long":
                    classes[i - 1] = long.class;
                    break;
                default:
                    classes[i - 1] = null;
                    break;
            }
        }
        return classes;
    }
}
