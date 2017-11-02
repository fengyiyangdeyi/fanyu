package cn.com.fanyu.domain;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
public class FyMesScore {
    @Id
    @GeneratedValue
    private Long id;
    @Column
    private Date addTime;
    @Column
    private String username;
    @Column
    private String groupid;
    @Column
    private Integer regularType;
    @Column
    private String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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

    public String getGroupid() {
        return groupid;
    }

    public void setGroupid(String groupid) {
        this.groupid = groupid;
    }

    public Integer getRegularType() {
        return regularType;
    }

    public void setRegularType(Integer regularType) {
        this.regularType = regularType;
    }
}