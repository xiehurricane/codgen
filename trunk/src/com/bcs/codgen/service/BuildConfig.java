package com.bcs.codgen.service;

import java.util.Map;

import com.bcs.codgen.model.OutputModel;
import com.bcs.codgen.model.TemplateModel;

/**
 * 【构建配置】接口
 * @author 黄天政
 *
 */
public interface BuildConfig {
	/**
	 * 获取输出编码类型
	 * @return
	 */
	String getOutputEncoding();
	/**
	 * 获取数据模型
	 * @return
	 */
	Map<String,Object> getDataModel();
	
	/**
	 * 获取输出模型
	 * @return
	 */
	Map<String,OutputModel> getOutputModel();
	
}
