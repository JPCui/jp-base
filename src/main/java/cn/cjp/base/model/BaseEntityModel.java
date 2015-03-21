package cn.cjp.base.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * base of domain
 * @author Administrator
 *
 */
@MappedSuperclass
public class BaseEntityModel extends BaseModel{
	
	private static final long serialVersionUID = -8234288925841604769L;
	
	public BaseEntityModel() {
	}
	
	public BaseEntityModel(long id) {
		this.id = id;
	}

	@Id
	@Column(nullable = false)
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Column(name="delete_flag", columnDefinition="int(1) default 0")
	private int deleteFlag = 0;
	
	@Column(name="update_date")
	private Date updateDate = new Date();
	
	@Column(name="save_date", columnDefinition="timestamp")
	private Date saveDate;
	
	/**
	 * 备注
	 */
	private String remarks;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the updateDate
	 */
	public Date getUpdateDate() {
		return updateDate;
	}

	/**
	 * @param updateDate the updateDate to set
	 */
	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	/**
	 * @return the deleteFlag
	 */
	public int getDeleteFlag() {
		return deleteFlag;
	}

	/**
	 * @param deleteFlag the deleteFlag to set
	 */
	public void setDeleteFlag(int deleteFlag) {
		this.deleteFlag = deleteFlag;
	}

	public Date getSaveDate() {
		return saveDate;
	}

	public void setSaveDate(Date saveDate) {
		this.saveDate = saveDate;
	}

	/**
	 * @return the remarks
	 */
	public String getRemarks() {
		return remarks;
	}

	/**
	 * @param remarks the remarks to set
	 */
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	/*
	 * public String getFrom() { return from; }
	 */
	/*
	 * public void setFrom(String from) { this.from = from; }
	 */

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this,
				ToStringStyle.MULTI_LINE_STYLE);
	}

	@Override
	public int hashCode() {
		if (this.id == null) {
			this.id = Long.valueOf(0);
		}
		return HashCodeBuilder.reflectionHashCode(this.id);
	}

	@Override
	public boolean equals(Object obj) {
		if (null != obj) {
			if (obj instanceof BaseEntityModel) {
				BaseEntityModel domain = (BaseEntityModel) obj;
				if (this.id == domain.id) {
					return true;
				}
			}
		}
		return false;
	}
	
}
