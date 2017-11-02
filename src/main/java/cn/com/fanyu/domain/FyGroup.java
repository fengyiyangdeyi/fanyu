package cn.com.fanyu.domain;

import com.google.gson.annotations.SerializedName;
import io.swagger.client.model.UserName;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

@Entity
public class FyGroup {
    @Id
    @GeneratedValue
    private Long id;
    @Column
    private String groupname;
    @Column
    private String remark;
    @Column
    private Boolean _public ;
    @Column
    private Integer maxusers ;
    @Column
    private Boolean approval ;
    @Column
    private String owner;
    @Column
    private Date addTime;
    @Column
    private String groupid;
    @Column
    private String code;
    /**
     * 房间类型（1-AA,2-房主付费）
     */
    @Column
    private Integer payType;

    /**
     * （1-自动续费,2-非自动续费）
     */
    @Column
    private Integer autoType;
    /**
     * 房间类型（1-24h,2-48h,3-72h,3-一个月）
     */
    @Column
    private Integer dateType;

    /**
     * 0-不审核，1-审核
     */
    @Column
    private Integer comfirmStatus=0;

    public Integer getComfirmStatus() {
        return comfirmStatus;
    }

    public void setComfirmStatus(Integer comfirmStatus) {
        this.comfirmStatus = comfirmStatus;
    }

    public Integer getPayType() {
        return payType;
    }

    public void setPayType(Integer payType) {
        this.payType = payType;
    }

    public Integer getAutoType() {
        return autoType;
    }

    public void setAutoType(Integer autoType) {
        this.autoType = autoType;
    }

    public Integer getDateType() {
        return dateType;
    }

    public void setDateType(Integer dateType) {
        this.dateType = dateType;
    }

    public String getGroupid() {
        return groupid;
    }

    public void setGroupid(String groupid) {
        this.groupid = groupid;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGroupname() {
        return groupname;
    }

    public void setGroupname(String groupname) {
        this.groupname = groupname;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Boolean get_public() {
        return _public;
    }

    public void set_public(Boolean _public) {
        this._public = _public;
    }

    public Integer getMaxusers() {
        return maxusers;
    }

    public void setMaxusers(Integer maxusers) {
        this.maxusers = maxusers;
    }

    public Boolean getApproval() {
        return approval;
    }

    public void setApproval(Boolean approval) {
        this.approval = approval;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public Date getAddTime() {
        return addTime;
    }

    public void setAddTime(Date addTime) {
        this.addTime = addTime;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}