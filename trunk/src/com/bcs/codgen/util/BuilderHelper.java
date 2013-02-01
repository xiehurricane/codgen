package com.bcs.codgen.util;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

/**
 * 构建辅助类
 */
public class BuilderHelper {
		
	/**
	 * 根据指定的数据模型集合解析构建参数
	 * @param dataMap 
	 * @param param
	 * @return
	 */
	public static String parseBuildParams(Map<String, Object> dataMap, String param){
		Configuration cfg = new Configuration(); 
		cfg.setTemplateLoader(new StringTemplateLoader(param)); 
		cfg.setDefaultEncoding("UTF-8");    
		Template template;
		StringWriter writer = new StringWriter();  
		try {
			template = cfg.getTemplate("");  
			template.process(dataMap, writer);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TemplateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return writer.toString();
	}
		
}
