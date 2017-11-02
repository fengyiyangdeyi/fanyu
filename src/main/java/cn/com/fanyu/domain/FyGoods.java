package cn.com.fanyu.domain;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
public class FyGoods {
    @Id
    @GeneratedValue
    private Long id;
    @Column
    private Date addTime=new Date();
    @Column(columnDefinition = "varchar(255) default'' COMMENT '头像'")
    private String imgUrl="";
    @Column
    private BigDecimal price;
    @Column
    private BigDecimal diamondNum;
    @Column
    private BigDecimal givingDiamondNum;
    @Column
    private String appleid;

    public BigDecimal getGivingDiamondNum() {
        return givingDiamondNum;
    }

    public void setGivingDiamondNum(BigDecimal givingDiamondNum) {
        this.givingDiamondNum = givingDiamondNum;
    }

    public String getAppleid() {
        return appleid;
    }

    public void setAppleid(String appleid) {
        this.appleid = appleid;
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

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getDiamondNum() {
        return diamondNum;
    }

    public void setDiamondNum(BigDecimal diamondNum) {
        this.diamondNum = diamondNum;
    }
}