package cn.com.fanyu.domain;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
public class FyRule {
    @Id
    @GeneratedValue
    private Long id;
    @Version
    private Long version;
    @Column
    private Integer xiazhuFlag=0;
    @Column
    private Integer qiangbaoStatus=0;
    @Column
    private Date addTime;
    @Column
    private String name;
    @Column
    private String groupId;
    /**
     * 最大赔
     */
    @Column
    private BigDecimal maxValue=new BigDecimal(18);
    /**
     * 页面名字（后台每种玩法一个模板页面，方便开发）
     */
    @Column
    private String pageName;

    @Basic(fetch=FetchType.LAZY)
    @Column(columnDefinition="TEXT")
    private String rule;

    public Long getId() {
        return id;
    }

    public String getRule() {
        return rule;
    }

    public void setRule(String rule) {
        this.rule = rule;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(BigDecimal maxValue) {
        this.maxValue = maxValue;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getPageName() {
        return pageName;
    }

    public void setPageName(String pageName) {
        this.pageName = pageName;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public Integer getXiazhuFlag() {
        return xiazhuFlag;
    }

    public void setXiazhuFlag(Integer xiazhuFlag) {
        this.xiazhuFlag = xiazhuFlag;
    }

    public Integer getQiangbaoStatus() {
        return qiangbaoStatus;
    }

    public void setQiangbaoStatus(Integer qiangbaoStatus) {
        this.qiangbaoStatus = qiangbaoStatus;
    }
}