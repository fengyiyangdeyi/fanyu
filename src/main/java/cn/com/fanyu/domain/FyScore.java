package cn.com.fanyu.domain;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
public class FyScore {
    @Id
    @GeneratedValue
    private Long id;
    @Version
    private Long version;
    @Column
    private Date addTime;
    @Column
    private String username;
    @Column(columnDefinition = "decimal(19,2) default 0.00 COMMENT '积分'")
    private BigDecimal integral=new BigDecimal(0);
    @Column(columnDefinition = "decimal(19,2) default 0.00 COMMENT '冻结积分'")
    private BigDecimal freeIntegral=new BigDecimal(0);
    @Column(columnDefinition = "decimal(19,2) default 0.00 COMMENT '庄分'")
    private BigDecimal zintegral=new BigDecimal(0);
    @Column
    private String groupid;
    @Column
    private Integer status=1;
    @Column
    private String relateUsername;

    public String getRelateUsername() {
        return relateUsername;
    }

    public BigDecimal getZintegral() {
        return zintegral;
    }

    public void setZintegral(BigDecimal zintegral) {
        this.zintegral = zintegral;
    }

    public void setRelateUsername(String relateUsername) {
        this.relateUsername = relateUsername;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getAddTime() {
        return addTime;
    }

    public void setAddTime(Date addTime) {
        this.addTime = addTime;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public BigDecimal getIntegral() {
        return integral;
    }

    public void setIntegral(BigDecimal integral) {
        this.integral = integral;
    }

    public String getGroupid() {
        return groupid;
    }

    public void setGroupid(String groupid) {
        this.groupid = groupid;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public BigDecimal getFreeIntegral() {
        return freeIntegral;
    }

    public void setFreeIntegral(BigDecimal freeIntegral) {
        this.freeIntegral = freeIntegral;
    }
}