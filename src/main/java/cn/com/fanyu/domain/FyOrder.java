package cn.com.fanyu.domain;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
public class FyOrder {
    @Id
    @GeneratedValue
    private Long id;
    @Version
    private Long version;
    @Column
    private Date addTime=new Date();
    @Column
    private Date payTime=new Date();
    @Column
    private String username;
    @Column
    private String nickname;
    @Column
    private BigDecimal money=new BigDecimal(0);
    @Column
    private BigDecimal paymoney=new BigDecimal(0);
    @Column
    private Integer status=0;
    @Column
    private String code;
    @Column
    private String trade_no;
    @Column
    private BigDecimal diamondNum;
    @Column
    private BigDecimal givingDiamondNum;
    @Column
    private Long goodsId;
    @Column
    private String payType;

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public Long getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Long goodsId) {
        this.goodsId = goodsId;
    }

    public BigDecimal getDiamondNum() {
        return diamondNum;
    }

    public void setDiamondNum(BigDecimal diamondNum) {
        this.diamondNum = diamondNum;
    }

    public BigDecimal getGivingDiamondNum() {
        return givingDiamondNum;
    }

    public void setGivingDiamondNum(BigDecimal givingDiamondNum) {
        this.givingDiamondNum = givingDiamondNum;
    }

    public String getTrade_no() {
        return trade_no;
    }

    public void setTrade_no(String trade_no) {
        this.trade_no = trade_no;
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

    public Date getAddTime() {
        return addTime;
    }

    public void setAddTime(Date addTime) {
        this.addTime = addTime;
    }

    public Date getPayTime() {
        return payTime;
    }

    public void setPayTime(Date payTime) {
        this.payTime = payTime;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public BigDecimal getMoney() {
        return money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }

    public BigDecimal getPaymoney() {
        return paymoney;
    }

    public void setPaymoney(BigDecimal paymoney) {
        this.paymoney = paymoney;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}