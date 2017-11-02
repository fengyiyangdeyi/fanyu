package cn.com.fanyu.domain.templates;

import java.math.BigDecimal;

public class NiuNiuDetail {
    private String name;
    private Integer selectId;
    private Integer type;//是否多个下拉时用
    private BigDecimal value;
    private BigDecimal xcvalue;//闲家抽水面分比
    private BigDecimal startFrom;
    private BigDecimal endTo;

    public BigDecimal getStartFrom() {
        return startFrom;
    }

    public void setStartFrom(BigDecimal startFrom) {
        this.startFrom = startFrom;
    }

    public BigDecimal getEndTo() {
        return endTo;
    }

    public void setEndTo(BigDecimal endTo) {
        this.endTo = endTo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public Integer getSelectId() {
        return selectId;
    }

    public void setSelectId(Integer selectId) {
        this.selectId = selectId;
    }

    public BigDecimal getXcvalue() {
        return xcvalue;
    }

    public void setXcvalue(BigDecimal xcvalue) {
        this.xcvalue = xcvalue;
    }
}
