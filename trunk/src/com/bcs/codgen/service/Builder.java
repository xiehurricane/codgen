package com.bcs.codgen.service;

import java.util.Map;

import com.bcs.codgen.model.OutputModel;

/**
 * 一个简单的构建接口
 * @author 黄天政
 *
 */
public interface  Builder {	
	/**
	 * 构建操作。返回类型Map&lt;输出标识,输出模型&gt;
	 * @return 返回输出模型的映射。
	 */
	Map<String, OutputModel> build();
}
