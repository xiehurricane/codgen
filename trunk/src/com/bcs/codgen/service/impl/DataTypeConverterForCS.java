package com.bcs.codgen.service.impl;

import org.apache.commons.lang.StringUtils;

import com.bcs.codgen.model.ColumnModel;
import com.bcs.codgen.service.ColumnHandler;

/**
 * 针对C#编程环境的数据类型转换器
 * @author 黄天政
 *
 */
public class DataTypeConverterForCS implements ColumnHandler {

	public void handle(ColumnModel columnModel) {
		String javaType = columnModel.getColumnClassName();
		String typeName = columnModel.getColumnTypeName();
		String className = typeName;
		String simpleClassName = typeName;
		
		//根据javaType确定C#编程语言中的数据类型
		if("java.math.BigDecimal".equals(javaType) ){
			if(columnModel.getScale()>0){
				className = "System.Decimal";
				simpleClassName = "decimal";
			}else{
				className = "System.Int64";
				simpleClassName = "long";
			}
        }else if("java.lang.Boolean".equals(javaType)){
        	className = "System.Boolean";
			simpleClassName = "bool";
        }else if("java.lang.Integer".equals(javaType)){
        	className = "System.Int32";
			simpleClassName = "int";
        }else if("java.lang.Long".equals(javaType)){
        	className = "System.Int64";
			simpleClassName = "long";
        }else if("java.lang.Float".equals(javaType)){
        	className = "System.Single";
			simpleClassName = "float";
        }else if("java.lang.Double".equals(javaType)){
        	className = "System.Decimal";
			simpleClassName = "decimal";
        }else if("java.sql.Date".equals(javaType)
        		||"java.sql.Time".equals(javaType)
        		||"java.sql.Timestamp".equals(javaType)){
        	className = "System.DateTime";
			simpleClassName = "DateTime";
        }else{
        	className = "System.String";
			simpleClassName = "string";
        }
		
		//根据具体数据库方言中的数据类型(sqlTypeName)确定C#编程语言中的数据类型
		if("int".equalsIgnoreCase(typeName)){
			className = "System.Int32";
			simpleClassName = "int";
		}else if("bit".equalsIgnoreCase(typeName)){
			className = "System.Boolean";
			simpleClassName = "bool";
		}else if("decimal".equalsIgnoreCase(typeName)){
			className = "System.Decimal";
			simpleClassName = "decimal";
		}else if("float".equalsIgnoreCase(typeName)){
			className = "System.Single";
			simpleClassName = "float";
		}else if("smallint".equalsIgnoreCase(typeName)){
			className = "System.Short";
			simpleClassName = "short";
		}
		
		//把float统一为decimal
		if("float".equals(simpleClassName)){
			className = "System.Decimal";
			simpleClassName = "decimal";
		}
		//用具体的编程语言数据类型覆盖默认的java类型。
		columnModel.setColumnClassName(className);
		//设置具体编程语言的数据类型所在的包，在C#里也叫命名空间。如System.String的命名空间为System
		columnModel.setColumnClassPackage(StringUtils.substringBeforeLast(className, "."));
		//设置具体编程语言的数据类型的简单类名
		columnModel.setColumnSimpleClassName(simpleClassName);
	}

}
