package cn.com.fanyu.domain;

import javax.persistence.*;
import java.util.Date;

/**
 * 无用
 */
@Entity
public class FyRuleTemplates {
    @Id
    @GeneratedValue
    private Long id;
    @Column
    private Date addTime;
    @Column
    private String ruleName;
    @Column
    private String pageName;

    @Basic(fetch=FetchType.LAZY)
    @Column(columnDefinition="TEXT")
    private String templates;

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

    public String getRuleName() {
        return ruleName;
    }

    public void setRuleName(String ruleName) {
        this.ruleName = ruleName;
    }

    public String getPageName() {
        return pageName;
    }

    public void setPageName(String pageName) {
        this.pageName = pageName;
    }

    public String getTemplates() {
        return templates;
    }

    public void setTemplates(String templates) {
        this.templates = templates;
    }
}