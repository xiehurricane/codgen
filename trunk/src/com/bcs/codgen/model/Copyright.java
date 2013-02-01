package com.bcs.codgen.model;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang.StringUtils;

/**
 * 版权模型
 * @author 黄天政
 *
 */
public class Copyright {
	private String description="";
	private String author=System.getProperty("user.name");
	private String createDate=new SimpleDateFormat("yyyy-MM-dd").format(new Date());
	private String authorEmail="360138308@qq.com";
	private String modifier=author;
	private String modifyDate=createDate;
	private String modifierEmail=authorEmail;
	private String company="BCSSOFT";
	private String version="1.0";
	
	/**
	 * 构建一个版权信息实例
	 */
	public Copyright() {
		if(StringUtils.isBlank(author)){
			this.author = "codgen";
		}
	}
	/**
	 * 由一个作者名称构建一个版权信息实例
	 * @param author 作者名称
	 */
	public Copyright(String author) {
		super();
		this.author = author;
	}
	/**
	 * @return 取得版本描述信息
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * @param description 设置版本描述信息
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	/**
	 * @return 取得作者信息
	 */
	public String getAuthor() {
		return author;
	}
	/**
	 * @param author 设置作者信息
	 */
	public void setAuthor(String author) {
		this.author = author;
	}
	/**
	 * @return 取得创建日期
	 */
	public String getCreateDate() {
		return createDate;
	}
	/**
	 * @param createDate 设置创建日期
	 */
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}
	/**
	 * @return 取得作者的Email
	 */
	public String getAuthorEmail() {
		return authorEmail;
	}
	/**
	 * @param authorEmail 设置作者的Email
	 */
	public void setAuthorEmail(String authorEmail) {
		this.authorEmail = authorEmail;
	}
	/**
	 * @return 取得修改人信息
	 */
	public String getModifier() {
		return modifier;
	}
	/**
	 * @param modifier 设置修改人信息
	 */
	public void setModifier(String modifier) {
		this.modifier = modifier;
	}
	/**
	 * @return 取得修改日期
	 */
	public String getModifyDate() {
		return modifyDate;
	}
	/**
	 * @param modifyDate 设置修改日期
	 */
	public void setModifyDate(String modifyDate) {
		this.modifyDate = modifyDate;
	}
	/**
	 * @return 取得修改人的Email
	 */
	public String getModifierEmail() {
		return modifierEmail;
	}
	/**
	 * @param modifierEmail 设置修改人的Email
	 */
	public void setModifierEmail(String modifierEmail) {
		this.modifierEmail = modifierEmail;
	}
	/**
	 * @return 取得公司信息
	 */
	public String getCompany() {
		return company;
	}
	/**
	 * @param company 设置公司信息
	 */
	public void setCompany(String company) {
		this.company = company;
	}
	/**
	 * @return 取得版本号
	 */
	public String getVersion() {
		return version;
	}
	/**
	 * @param version 设置版本号
	 */
	public void setVersion(String version) {
		this.version = version;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Copyright [author=");
		builder.append(author);
		builder.append(", authorEmail=");
		builder.append(authorEmail);
		builder.append(", company=");
		builder.append(company);
		builder.append(", createDate=");
		builder.append(createDate);
		builder.append(", description=");
		builder.append(description);
		builder.append(", modifier=");
		builder.append(modifier);
		builder.append(", modifierEmail=");
		builder.append(modifierEmail);
		builder.append(", modifyDate=");
		builder.append(modifyDate);
		builder.append(", version=");
		builder.append(version);
		builder.append("]");
		return builder.toString();
	}
	
	
}
