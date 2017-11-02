package cn.com.fanyu.domain;

import javax.persistence.*;
import java.util.Date;

@Entity
public class FyTask {
    @Id
    @GeneratedValue
    private Long id;
    @Version
    private Long version;
    /**
     * 0-未处理，1-已处理，2-处理失败
     */
    @Column
    private Integer status=0;

    @Basic(fetch=FetchType.LAZY)
    @Column(columnDefinition="TEXT")
    private String message;

    @Column
    private Long diamondId;

    @Column
    private String groupid;

    @Column
    private Date addTime;

    @Column
    private Date changeTime;

    public Date getAddTime() {
        return addTime;
    }

    public void setAddTime(Date addTime) {
        this.addTime = addTime;
    }

    public Date getChangeTime() {
        return changeTime;
    }

    public void setChangeTime(Date changeTime) {
        this.changeTime = changeTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getDiamondId() {
        return diamondId;
    }

    public void setDiamondId(Long diamondId) {
        this.diamondId = diamondId;
    }

    public String getGroupid() {
        return groupid;
    }

    public void setGroupid(String groupid) {
        this.groupid = groupid;
    }
}