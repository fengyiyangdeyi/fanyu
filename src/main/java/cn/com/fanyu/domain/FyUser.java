package cn.com.fanyu.domain;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
public class FyUser {
    @Id
    @GeneratedValue
    private Long id;
    @Version
    private Long version;
    @Column
    private Date addTime=new Date();
    @Column
    private String username;
    @Column
    private String wxid;
    @Column
    private String phone;
    @Column
    private String nickname;
    @Column
    private String password;
    @Column(columnDefinition = "decimal(19,2) default 0.00 COMMENT '钻石数量'")
    private BigDecimal diamondNum=new BigDecimal(0);
    @Column(columnDefinition = "varchar(255) default'' COMMENT '头像'")
    private String imgUrl="";
    @Column
    private String permissions="1";
    @Column
    private String uuid;

    public String getWxid() {
        return wxid;
    }

    public void setWxid(String wxid) {
        this.wxid = wxid;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getPermissions() {
        return permissions;
    }

    public void setPermissions(String permissions) {
        this.permissions = permissions;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public BigDecimal getDiamondNum() {
        return diamondNum;
    }

    public void setDiamondNum(BigDecimal diamondNum) {
        this.diamondNum = diamondNum;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}