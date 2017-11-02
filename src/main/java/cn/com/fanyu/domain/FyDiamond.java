package cn.com.fanyu.domain;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
public class FyDiamond {
    @Id
    @GeneratedValue
    private Long id;
    @Version
    private Long version;
    @Column
    private Date addTime=new Date();
    @Column
    private String username;
    @Column(columnDefinition = "decimal(19,2) default 0.00 COMMENT '钻石数量'")
    private BigDecimal diamondNum;
    @Column(columnDefinition = "decimal(19,2) default 0.00 COMMENT '剩余钻石数量'")
    private BigDecimal diamondEmainingNum;
    @Column(columnDefinition = "decimal(19,2) default 0.00 COMMENT '已抢钻石数量'")
    private BigDecimal diamondPastNum=new BigDecimal(0);
    @Column(columnDefinition = "int(11) COMMENT '个数'")
    private Integer num;
    @Column(columnDefinition = "int(11) COMMENT '已抢个数'")
    private Integer numPast=0;
    @Column
    private String remark;
    @Column
    private String groupid;
    /**
     * 是否规则抢包
     */
    @Column
    private Integer isRule=0;

    @Column
    private Integer isTimeOut=0;

    @Column
    private Integer isEndof=0;

    public Integer getIsEndof() {
        return isEndof;
    }

    public void setIsEndof(Integer isEndof) {
        this.isEndof = isEndof;
    }

    public Integer getIsTimeOut() {
        return isTimeOut;
    }

    public void setIsTimeOut(Integer isTimeOut) {
        this.isTimeOut = isTimeOut;
    }

    public Integer getIsRule() {
        return isRule;
    }

    public void setIsRule(Integer isRule) {
        this.isRule = isRule;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public Integer getNumPast() {
        return numPast;
    }

    public void setNumPast(Integer numPast) {
        this.numPast = numPast;
    }

    public Long getId() {
        return id;
    }

    public BigDecimal getDiamondEmainingNum() {
        return diamondEmainingNum;
    }

    public void setDiamondEmainingNum(BigDecimal diamondEmainingNum) {
        this.diamondEmainingNum = diamondEmainingNum;
    }

    public BigDecimal getDiamondPastNum() {
        return diamondPastNum;
    }

    public void setDiamondPastNum(BigDecimal diamondPastNum) {
        this.diamondPastNum = diamondPastNum;
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

    public BigDecimal getDiamondNum() {
        return diamondNum;
    }

    public void setDiamondNum(BigDecimal diamondNum) {
        this.diamondNum = diamondNum;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getGroupid() {
        return groupid;
    }

    public void setGroupid(String groupid) {
        this.groupid = groupid;
    }
}