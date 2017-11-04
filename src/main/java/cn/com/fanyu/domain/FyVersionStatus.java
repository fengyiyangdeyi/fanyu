package cn.com.fanyu.domain;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
public class FyVersionStatus {
    @Id
    @GeneratedValue
    private Long id;
    @Column
    private String versionValue;
    @Column
    private String type;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getVersionValue() {
        return versionValue;
    }

    public void setVersionValue(String versionValue) {
        this.versionValue = versionValue;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}