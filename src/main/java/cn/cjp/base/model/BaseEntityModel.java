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

import lombok.Data;

/**
 * base of domain
 *
 * @author Administrator
 */
@MappedSuperclass
@Data
public class BaseEntityModel extends BaseModel {

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

    @Column(name = "delete_flag", columnDefinition = "int(1) default 0")
    private int deleteFlag = 0;

    @Column(name = "update_date")
    private Date updateDate = new Date();

    @Column(name = "save_date", columnDefinition = "timestamp")
    private Date saveDate;

    /**
     * 备注
     */
    private String remarks;

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
