package cn.com.fanyu.domain;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
public class FyAnnouncement {
    /**
     * 公告
     */
    @Id
    @GeneratedValue
    private Long id;
    @Column(columnDefinition = "varchar(1024) default'' COMMENT '公告'")
    private String content;
    @Column(columnDefinition = "varchar(1024) default'' COMMENT 'app公告'")
    private String appContent;
    @Column
    private Date addTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getAddTime() {
        return addTime;
    }

    public void setAddTime(Date addTime) {
        this.addTime = addTime;
    }

    public String getAppContent() {
        return appContent;
    }

    public void setAppContent(String appContent) {
        this.appContent = appContent;
    }
}