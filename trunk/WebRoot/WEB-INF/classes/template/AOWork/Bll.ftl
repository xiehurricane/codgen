<#include "include/head.ftl">
package ${NamespaceBll};

import com.qtone.aow.bll.BaseBll;
import ${NamespaceModel}.${Po};

<#include "include/copyright.ftl">

/**
 * 《${tableLabel}》 业务逻辑层接口
 * @author ${copyright.author}
 *
 */
public interface ${Po}Bll extends BaseBll<${Po}> {
	/**
	 * 取得符合默认条件的所有记录数
	 * @return
	 */
	long getRecordCount();  
	<#list table.columnList as column>
	<#if column.primaryKey>
	/**
	 * 根据主键取得一个实体模型
	 * @param ${column.columnName?uncap_first} 主键
	 * @return
	 */
	${Po} queryForObject(${column.columnSimpleClassName} ${column.columnName?uncap_first});
	/**
	 * 根据主键删除一个实体模型
	 * @param ${column.columnName?uncap_first} 主键
	 * @return 影响记录数，正常返回1，否则返回0
	 */
	int delete(${column.columnSimpleClassName} ${column.columnName?uncap_first});
	</#if>	
	</#list>
}