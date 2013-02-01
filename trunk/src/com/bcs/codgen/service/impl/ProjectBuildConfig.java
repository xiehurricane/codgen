package com.bcs.codgen.service.impl;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;

import com.bcs.codgen.model.Copyright;
import com.bcs.codgen.model.InOutType;
import com.bcs.codgen.model.OutputModel;
import com.bcs.codgen.model.TableModel;
import com.bcs.codgen.service.BuildConfig;
import com.bcs.codgen.util.BuilderHelper;
import com.bcs.codgen.util.ClassLoaderUtil;
import com.bcs.codgen.util.FilenameUtil;
import com.bcs.codgen.util.ProjectConfig;

/**
 * 针对项目的构建配置信息
 * @author 黄天政
 *
 */
public class ProjectBuildConfig implements BuildConfig {

	/**
	 * 数据模型键：版权信息实体
	 */
	public static final String DMK_COPYRIGHT = "copyright";
	/**
	 * 数据模型键：模块名称
	 */
	public static final String DMK_MODULE_NAME = "moduleName";
	/**
	 * 数据模型键：分组名称
	 */
	public static final String DMK_GROUP_NAME = "groupName";
	/**
	 * 数据模型键：表标签
	 */
	public static final String DMK_TABLE_LABEL = "tableLabel";
	/**
	 * 数据模型键：表模型实体
	 */
	public static final String DMK_TABLE = "table";
	/**
	 * 数据模型键：表名
	 */
	public static final String DMK_TABLE_NAME = "tableName";
	/**
	 * 数据模型键：输出编码类型
	 */
	public static final String DMK_OUTPUT_ENCODING = "outputEncoding";
	/**
	 * 数据模型键：项目标签
	 */
	public static final String DMK_PROJECT_LABEL = "projectLabel";
	/**
	 * 数据模型键：项目名称
	 */
	public static final String DMK_PROJECT_NAME = "projectName";
	/**
	 * 数据模型键：默认的模板文件夹
	 */
	public final static String DMK_TEMPLATEDIRECTORY = "templateDirectory";
	
	private ProjectConfig projectConfig;
	private Map<String,Object> dataModelMap = new LinkedHashMap<String,Object>();
	private Map<String,OutputModel> outputModelMap = new LinkedHashMap<String, OutputModel>();
	private String moduleName;
	private String tableName;
	private String tableLabel;
	private String groupName;
	private Copyright copyright = new Copyright();
	
	/**
	 * 根据一个项目配置模型实例化一个项目构建配置信息
	 * @param projectConfig 项目配置模型
	 */
	public ProjectBuildConfig(ProjectConfig projectConfig) {
		super();
		this.projectConfig = projectConfig;
	}
	
	/**
	 * @return 获取当前的项目配置模型
	 */
	public ProjectConfig getProjectConfig() {
		return projectConfig;
	}

	/**
	 * @param 设置当前的项目配置模型
	 */
	public void setProjectConfig(ProjectConfig projectConfig) {
		this.projectConfig = projectConfig;
	}

	/**
	 * @return 获取当前构建的模块名称
	 */
	public String getModuleName() {
		return moduleName;
	}

	/**
	 * @param 设置当前构建的模块名称
	 */
	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}

	/**
	 * @return 获取当前构建的表名称
	 */
	public String getTableName() {
		return tableName;
	}

	/**
	 * @param 设置当前构建的表名称
	 */
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	/**
	 * @return 获取当前构建的表标签
	 */
	public String getTableLabel() {
		return tableLabel;
	}

	/**
	 * @param 设置当前构建的表标签
	 */
	public void setTableLabel(String tableLabel) {
		this.tableLabel = tableLabel;
	}

	/**
	 * @return 获取当前构建的所属组名称
	 */
	public String getGroupName() {
		return groupName;
	}

	/**
	 * @param 设置当前构建的所属组名称
	 */
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	
	/**
	 * @return 获取当前构建的版权信息
	 */
	public Copyright getCopyright() {
		return copyright;
	}

	/**
	 * @param copyright 设置当前构建的版权信息
	 */
	public void setCopyright(Copyright copyright) {
		this.copyright = copyright;
	}

	public Map<String, Object> getDataModel() {
		if(dataModelMap.size()>0){
			return dataModelMap;
		}
		/*try {
			dataModelMap.put("codgen.dir", ClassLoaderUtil.getExtendResource("com/bcs/codgen/").getPath());
		} catch (MalformedURLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}*/
		
		dataModelMap.put(DMK_PROJECT_NAME, projectConfig.getProjectName());		
		dataModelMap.put(DMK_PROJECT_LABEL, projectConfig.getProjectLabel());
		dataModelMap.put(DMK_OUTPUT_ENCODING, projectConfig.getOutputEncoding());
		
		//创建表模型	
		if(StringUtils.isBlank(getTableName())){
			//throw new Exception(this.getClass().getName()+"属性tableName的值不允许为空！");
		}else{
			dataModelMap.put(DMK_TABLE_NAME, getTableName());
			TableModel tm = projectConfig.getDbProvider().createTableModel(getTableName());			
			if(StringUtils.isNotBlank(this.tableLabel)){
				tm.setTableLabel(this.tableLabel);
			}else{
				this.tableLabel = tm.getTableLabel();
			}
			dataModelMap.put(DMK_TABLE, tm);
		
			if(StringUtils.isBlank(this.groupName)&&this.tableName.contains("_")){
				this.groupName = StringUtils.substringBefore(this.tableName, "_");
			}
			if(StringUtils.isBlank(this.moduleName)){
				if(this.tableName.contains("_")){
					this.moduleName = StringUtils.substringAfter(this.tableName, "_");
				}else{
					this.moduleName = this.tableName;
				}
			}
			dataModelMap.put(DMK_TABLE_LABEL, this.tableLabel);
			dataModelMap.put(DMK_GROUP_NAME, this.groupName);
			dataModelMap.put(DMK_MODULE_NAME, this.moduleName);
		}
		
		//设置版权信息
		dataModelMap.put(DMK_COPYRIGHT, this.copyright);
		
		
		
		//追加项目配置的【数据模型】信息
		String key,value;
		for(Entry<String, String> entry: projectConfig.getDataModelMap().entrySet()){
			key = entry.getKey();
			value = entry.getValue();
			//System.out.println("开始解析数据模型="+key);
			if(StringUtils.isNotBlank(value)&&value.contains("${")){
				value = BuilderHelper.parseBuildParams(dataModelMap, value); //解析带有构建参数的字符串
			}
			dataModelMap.put(key, value);
		}
		
		return dataModelMap;
	}
	
	public String getOutputEncoding() {
		return projectConfig.getOutputEncoding();
	}

	public Map<String, OutputModel> getOutputModel() {
		if(outputModelMap.size()>0){
			return outputModelMap;
		}
		
		getDataModel(); //确保已经获取数据模型
		
		String templateFile,outputFile;
		OutputModel outputModel;
		for(Entry<String,OutputModel> entry: projectConfig.getOutputMap().entrySet()){
			outputModel = entry.getValue();
			
			templateFile = outputModel.getTemplateModel().getTemplate();
			if(templateFile.contains("${")){
				templateFile = BuilderHelper.parseBuildParams(dataModelMap, templateFile); //解析带有构建参数的模板
			}
			
			if(entry.getValue().getTemplateModel().getType()==InOutType.FILE){
				templateFile = formatTemplateFilename(templateFile);
			}
			
			outputFile = outputModel.getOutput();
			if(StringUtils.isNotBlank(outputFile) && outputFile.contains("${")){
				outputFile = BuilderHelper.parseBuildParams(dataModelMap, outputFile); //解析带有构建参数的输出路径
			}
			try {
				outputModel = outputModel.deepClone();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			outputModel.getTemplateModel().setTemplate(templateFile);
			outputModel.setOutput(outputFile);
			outputModelMap.put(outputModel.getName(), outputModel);
		}
		return outputModelMap;
	}

	/**
	 * 格式化模板文件名为完整的文件URL，如果名称未包含路径，则默认为类根路径下的 “template/”+项目名称
	 * @return
	 * @throws IOException 
	 */
	private String formatTemplateFilename(String filename){
		filename = filename.replace("\\", "/");
		if(filename.contains("/")==false){
			if(dataModelMap.containsKey(DMK_TEMPLATEDIRECTORY)){
				filename = dataModelMap.get(DMK_TEMPLATEDIRECTORY)+"/"+filename;
			}else{
				filename = "template/" + projectConfig.getProjectName()+"/"+filename;
			}
		}
		
		if(filename.contains(".")==false 
			|| StringUtils.substringAfterLast(filename, ".").contains("/")){
			filename += ".ftl";
		}
		
		if(filename.contains(":")==false ){
			URL url = ClassLoaderUtil.getResource(filename);
			if(url==null){
				try {
					throw new IOException(String.format("模板文件%s不存在！",filename));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}else{
				filename = url.getFile();
			}
		}
		
		filename = FilenameUtil.normalize(filename);
		return filename;
	}

}
