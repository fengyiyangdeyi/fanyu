package cn.com.fanyu.domain;

import javax.persistence.*;
import java.util.Date;

@Entity
public class FyGlobalValue {
    @Id
    @GeneratedValue
    private Long id;
    @Version
    private Long version;
    @Column
    private Integer status=0;
    @Column
    private String globalKey;
    @Column
    private String globalValue;

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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getGlobalKey() {
        return globalKey;
    }

    public void setGlobalKey(String globalKey) {
        this.globalKey = globalKey;
    }

    public String getGlobalValue() {
        return globalValue;
    }

    public void setGlobalValue(String globalValue) {
        this.globalValue = globalValue;
    }
}