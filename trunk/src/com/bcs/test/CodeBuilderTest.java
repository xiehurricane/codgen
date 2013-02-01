package com.bcs.test;

import static org.junit.Assert.*;

import java.io.File;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.bcs.codgen.model.ColumnModel;
import com.bcs.codgen.model.InOutType;
import com.bcs.codgen.model.OutputModel;
import com.bcs.codgen.service.Builder;
import com.bcs.codgen.service.ColumnHandler;
import com.bcs.codgen.service.impl.CodeBuilder;
import com.bcs.codgen.service.impl.ProjectBuildConfig;
import com.bcs.codgen.util.ProjectConfig;
import com.bcs.codgen.util.ProjectConfigHelper;

public class CodeBuilderTest {
	ProjectBuildConfig buildConfig ;
	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
		Builder builder = new CodeBuilder(buildConfig);
		Map<String,OutputModel> omMap = builder.build();
		for(Entry<String,OutputModel> entry : omMap.entrySet()){
			//System.out.println("生成内容="+entry.getValue().getOutput());
			if(entry.getValue().getType()==InOutType.FILE){
				assertTrue("文件没有生成="+entry.getValue().getOutput()
						, new File(entry.getValue().getOutput()).exists());
			}
		}
	}

	@Test
	public void testBuild() {
		ProjectConfig projectConfig = ProjectConfigHelper.getDefaultProjectConfig(null);
		
		//增加一个额外的列模型处理器，处理Oracle的大写列名以增强列名称的可读性
		projectConfig.getDbProvider().getColumnHandlers().add(new ColumnHandler() {
			String[] columnNames = new String[]{"UserID","UserName","Sex","Remark"};
			public void handle(ColumnModel columnModel) {
				for (int i = 0; i < columnNames.length; i++) {
					if(columnModel.getColumnName().equalsIgnoreCase(columnNames[i])){
						columnModel.setColumnName(columnNames[i]);
					}
				}
			}
		});
		
		buildConfig = new ProjectBuildConfig(projectConfig);
		buildConfig.setTableName("Sys_UserInfo");
	}

}
