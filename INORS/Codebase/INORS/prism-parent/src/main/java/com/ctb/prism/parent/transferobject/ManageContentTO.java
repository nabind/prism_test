package com.ctb.prism.parent.transferobject;

import java.util.List;

import com.ctb.prism.core.transferobject.BaseTO;

/**
 * @author Arunava Datta
 * version 1.1
 */
public class ManageContentTO extends BaseTO {

	private static final long serialVersionUID = 1L;
	private long contentId;
	private long articleId; 
	private String contentName;
	private String subHeader;
	private String gradeName;
	private String profLevel;
	
	public long getContentId() {
		return contentId;
	}
	public void setContentId(long contentId) {
		this.contentId = contentId;
	}
	public long getArticleId() {
		return articleId;
	}
	public void setArticleId(long articleId) {
		this.articleId = articleId;
	}
	public String getContentName() {
		return contentName;
	}
	public void setContentName(String contentName) {
		this.contentName = contentName;
	}
	public String getSubHeader() {
		return subHeader;
	}
	public void setSubHeader(String subHeader) {
		this.subHeader = subHeader;
	}
	public String getGradeName() {
		return gradeName;
	}
	public void setGradeName(String gradeName) {
		this.gradeName = gradeName;
	}
	public String getProfLevel() {
		return profLevel;
	}
	public void setProfLevel(String profLevel) {
		this.profLevel = profLevel;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	
}

