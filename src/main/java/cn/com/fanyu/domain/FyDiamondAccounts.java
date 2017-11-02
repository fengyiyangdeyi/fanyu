package cn.com.fanyu.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 钻石流水账
 */
@Entity
public class FyDiamondAccounts {
    @Id
    @GeneratedValue
    private Long id;
    @Column
    private Date addTime=new Date();
    @Column
    private String username;
    @Column(columnDefinition = "decimal(19,2) default 0.00 COMMENT '钻石数量'")
    private BigDecimal diamondNum;

    @Column(columnDefinition = "decimal(19,2) default 0.00 COMMENT '前用户钻石数量'")
    private BigDecimal userBeforeNum;
    @Column(columnDefinition = "decimal(19,2) default 0.00 COMMENT '后用户钻石数量'")
    private BigDecimal userAfterNum;
    @Column
    private String remark;
    @Column
    private String transactionId;

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
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

    public BigDecimal getDiamondNum() {
        return diamondNum;
    }

    public void setDiamondNum(BigDecimal diamondNum) {
        this.diamondNum = diamondNum;
    }

    public BigDecimal getUserBeforeNum() {
        return userBeforeNum;
    }

    public void setUserBeforeNum(BigDecimal userBeforeNum) {
        this.userBeforeNum = userBeforeNum;
    }

    public BigDecimal getUserAfterNum() {
        return userAfterNum;
    }

    public void setUserAfterNum(BigDecimal userAfterNum) {
        this.userAfterNum = userAfterNum;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}