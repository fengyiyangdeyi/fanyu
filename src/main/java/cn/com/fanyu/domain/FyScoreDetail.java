package cn.com.fanyu.domain;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
public class FyScoreDetail {
    @Id
    @GeneratedValue
    private Long id;
    @Column
    private Date addTime;
    @Column
    private String username;
    @Column(columnDefinition = "decimal(19,2) default 0.00 COMMENT '积分'")
    private BigDecimal integral=new BigDecimal(0);
    @Column
    private String groupid;
    @Column
    private Long scoreId;
    @Column(columnDefinition = "decimal(19,2) default 0.00 COMMENT '前数量'")
    private BigDecimal integralBefore;
    @Column(columnDefinition = "decimal(19,2) default 0.00 COMMENT '后数量'")
    private BigDecimal integralAfter;
    @Column(columnDefinition = "decimal(19,2) default 0.00 COMMENT '前数量'")
    private BigDecimal freeBefore;
    @Column(columnDefinition = "decimal(19,2) default 0.00 COMMENT '后数量'")
    private BigDecimal freeAfter;
    @Column(columnDefinition = "decimal(19,2) default 0.00 COMMENT '盈利抽水'")
    private BigDecimal ylc=new BigDecimal(0);
    @Column(columnDefinition = "decimal(19,2) default 0.00 COMMENT '闲家抽水'")
    private BigDecimal xc=new BigDecimal(0);
    @Column(columnDefinition = "decimal(19,2) default 0.00 COMMENT '人头抽水'")
    private BigDecimal  rtc=new BigDecimal(0);
    @Column
    private String reMark;
    /**
     * 1-积分充值，2-庄分，3-闲家游戏，4-庄家游戏
     */
    @Column
    private Integer type;
    @Column
    private Long betId;
    @Column
    private Long taskId;

    public Long getBetId() {
        return betId;
    }

    public void setBetId(Long betId) {
        this.betId = betId;
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

    public BigDecimal getRtc() {
        return rtc;
    }

    public void setRtc(BigDecimal rtc) {
        this.rtc = rtc;
    }

    public BigDecimal getXc() {
        return xc;
    }

    public void setXc(BigDecimal xc) {
        this.xc = xc;
    }

    public BigDecimal getYlc() {
        return ylc;
    }

    public void setYlc(BigDecimal ylc) {
        this.ylc = ylc;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getReMark() {
        return reMark;
    }

    public BigDecimal getFreeBefore() {
        return freeBefore;
    }

    public void setFreeBefore(BigDecimal freeBefore) {
        this.freeBefore = freeBefore;
    }

    public BigDecimal getFreeAfter() {
        return freeAfter;
    }

    public void setFreeAfter(BigDecimal freeAfter) {
        this.freeAfter = freeAfter;
    }

    public void setReMark(String reMark) {
        this.reMark = reMark;
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

    public Long getScoreId() {
        return scoreId;
    }

    public void setScoreId(Long scoreId) {
        this.scoreId = scoreId;
    }

    public BigDecimal getIntegralBefore() {
        return integralBefore;
    }

    public void setIntegralBefore(BigDecimal integralBefore) {
        this.integralBefore = integralBefore;
    }

    public BigDecimal getIntegralAfter() {
        return integralAfter;
    }

    public void setIntegralAfter(BigDecimal integralAfter) {
        this.integralAfter = integralAfter;
    }
}