<#include "include/head.ftl">
package ${NamespaceBllImpl};

import com.qtone.aow.bll.BaseBllImpl;
import ${NamespaceDao}.${Po}Dao;
import com.qtone.aow.factory.DaoFactory;
import ${NamespaceModel}.${Po};

<#include "include/copyright.ftl">

/**
 * 《${table.tabComment?default(tableLabel)}	》 业务逻辑层实现类
 * @author ${copyright.author}
 *
 */
public class ${Po}BllImpl extends BaseBllImpl<${Po}> implements ${Po}Bll {

	@Override
	protected ${Po}Dao getDao() {
		return DaoFactory.CreateDao(${Po}Dao.class);
	}
	@Override
	public long getRecordCount() {
		return getDao().getRecordCount();
	}
	<#list table.columnList as column>
	<#if column.primaryKey>
	@Override
	public ${Po} queryForObject(${column.columnSimpleClassName} ${column.columnName?uncap_first}){
		return getDao().queryForObject(${column.columnName?uncap_first});
	}

	@Override
	public int delete(${column.columnSimpleClassName} ${column.columnName?uncap_first}) {
		return getDao().delete(${column.columnName?uncap_first});
	}
	</#if>	
	</#list>
}
