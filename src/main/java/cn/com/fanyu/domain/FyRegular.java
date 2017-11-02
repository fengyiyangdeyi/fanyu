package cn.com.fanyu.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

@Entity
public class FyRegular {
    @Id
    @GeneratedValue
    private Long id;
    @Column
    private Date addTime;
    @Column
    private String groupid;
    /**
     * 1-上下分，2-下注
     */
    @Column
    private Integer regularType;
    @Column
    private String name;
    @Column
    private String regular;
    @Column
    private String regularContent;

    public String getRegular() {
        return regular;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRegular(String regular) {
        this.regular = regular;
    }

    public String getRegularContent() {
        return regularContent;
    }

    public void setRegularContent(String regularContent) {
        this.regularContent = regularContent;
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
