package com.st.testtool.controller;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.deser.std.DateDeserializers.TimestampDeserializer;
import com.st.testtool.service.RunService;
import com.st.testtool.util.JavaCompile;
import com.st.testtool.view.ClassInfo;
import com.st.testtool.view.Result;

@RestController
public class TestController {
	public static Class<?> userCls;
	public static File csvFile;
	
	@Autowired
	private RunService runService;
	private String tempDir = "C:\\Users\\ziyi\\Desktop\\";
	
	@RequestMapping("/getClass")
	public ClassInfo getClass(MultipartFile file) {
		Map<String, Object> classMap = new HashMap<>();
		String clsName = file.getOriginalFilename();
		File file2 = new File(tempDir + clsName);
		if (file2.exists()) {
			file2.delete();
		}
		try {
			file.transferTo(file2);
		} catch (IOException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		boolean res = JavaCompile.CompilerJavaFile(tempDir + clsName, tempDir);
		
		userCls = null;
		if (res == true) {
			try {
				JavaCompile.ClassLoader(tempDir);
				userCls = JavaCompile.LoadClass(clsName);
			} catch (MalformedURLException | ClassNotFoundException e) {
				// TODO: handle exception
				e.printStackTrace();
			}
			
		}
		if (userCls != null) {
			Method[] methods= userCls.getDeclaredMethods();
			int i = 0;
			for (Method method : methods) {
				classMap.put("method" + i, method.getName());
				i++;
			}
			classMap.put("methodNum", new Integer(methods.length));
		}
		
		
		classMap.put("status", "ok");
		return new ClassInfo(userCls);
	}
	
	@RequestMapping("/getCsv")
	public Map getCsv(MultipartFile file) {
		Map<String, Object> classMap = new HashMap<>();
		
		String fileName = file.getOriginalFilename();
		
		File file2 = new File(tempDir + fileName);
		if (file2.exists()) {
			file2.delete();
		}
		try {
			file.transferTo(file2);
		} catch (IOException e) {
			// TODO: handle exception
			classMap.put("status", "error");
			e.printStackTrace();
		}
		
        csvFile = file2;
        classMap.put("status", "ok");
		return classMap;
	}
	
	@RequestMapping(value = "/run")
    @ResponseBody
    public Result test(@RequestParam("signature") String signature) {
        return runService.run(signature);
    }
	
}
