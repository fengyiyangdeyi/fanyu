package cn.com.fanyu.domain.templates;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class NiuNiu {
    private String shangfenRegular;
    private String shangfenContent;
    private String xiazhuRegular;
    private String xiazhuContent;
    private String suohaRegular;
    private String suohaContent;

    /**
     * 1-抢庄、2-固定值
     */
    private Integer bankerType;
    private String bankerTypeName;
    private String bankerReguler;
    private String bankerContent;
    private String banker;
    private BigDecimal minBankerScore;

    private Long timeout;
    /**
     * 最小下注
     */
    private BigDecimal minBet=new BigDecimal(0);
    private BigDecimal maxBet=new BigDecimal(0);
    private BigDecimal minsuoha=new BigDecimal(0);
    private BigDecimal maxsuoha=new BigDecimal(0);
    private BigDecimal szcvalue=new BigDecimal(0);//上庄抽水百分比
    private BigDecimal ylcvalue=new BigDecimal(0);//盈利抽水百分比
    private BigDecimal rtcvalue=new BigDecimal(0);//人头抽水
    private Integer xcType=1;

    private List<NiuNiuDetail> details=new ArrayList<>();
    private List<SamePointDetail> samePointDetail=new ArrayList<>();

    public BigDecimal getMinsuoha() {
        return minsuoha;
    }

    public Long getTimeout() {
        return timeout;
    }

    public void setTimeout(Long timeout) {
        this.timeout = timeout;
    }

    public void setMinsuoha(BigDecimal minsuoha) {
        this.minsuoha = minsuoha;
    }

    public BigDecimal getMaxsuoha() {
        return maxsuoha;
    }

    public void setMaxsuoha(BigDecimal maxsuoha) {
        this.maxsuoha = maxsuoha;
    }

    public BigDecimal getMaxBet() {
        return maxBet;
    }

    public void setMaxBet(BigDecimal maxBet) {
        this.maxBet = maxBet;
    }

    public BigDecimal getRtcvalue() {
        return rtcvalue;
    }

    public void setRtcvalue(BigDecimal rtcvalue) {
        this.rtcvalue = rtcvalue;
    }

    public BigDecimal getMinBet() {
        return minBet;
    }

    public void setMinBet(BigDecimal minBet) {
        this.minBet = minBet;
    }

    public String getBankerContent() {
        return bankerContent;
    }

    public void setBankerContent(String bankerContent) {
        this.bankerContent = bankerContent;
    }

    public String getShangfenRegular() {
        return shangfenRegular;
    }

    public void setShangfenRegular(String shangfenRegular) {
        this.shangfenRegular = shangfenRegular;
    }

    public String getShangfenContent() {
        return shangfenContent;
    }

    public void setShangfenContent(String shangfenContent) {
        this.shangfenContent = shangfenContent;
    }

    public String getXiazhuRegular() {
        return xiazhuRegular;
    }

    public void setXiazhuRegular(String xiazhuRegular) {
        this.xiazhuRegular = xiazhuRegular;
    }

    public String getXiazhuContent() {
        return xiazhuContent;
    }

    public void setXiazhuContent(String xiazhuContent) {
        this.xiazhuContent = xiazhuContent;
    }

    public Integer getBankerType() {
        return bankerType;
    }

    public void setBankerType(Integer bankerType) {
        this.bankerType = bankerType;
    }

    public String getBankerReguler() {
        return bankerReguler;
    }

    public void setBankerReguler(String bankerReguler) {
        this.bankerReguler = bankerReguler;
    }

    public String getBanker() {
        return banker;
    }

    public void setBanker(String banker) {
        this.banker = banker;
    }

    public BigDecimal getMinBankerScore() {
        return minBankerScore;
    }

    public void setMinBankerScore(BigDecimal minBankerScore) {
        this.minBankerScore = minBankerScore;
    }

    public List<NiuNiuDetail> getDetails() {
        return details;
    }

    public void setDetails(List<NiuNiuDetail> details) {
        this.details = details;
    }

    public String getBankerTypeName() {
        return bankerTypeName;
    }

    public void setBankerTypeName(String bankerTypeName) {
        this.bankerTypeName = bankerTypeName;
    }

    public String getSuohaRegular() {
        return suohaRegular;
    }

    public void setSuohaRegular(String suohaRegular) {
        this.suohaRegular = suohaRegular;
    }

    public String getSuohaContent() {
        return suohaContent;
    }

    public void setSuohaContent(String suohaContent) {
        this.suohaContent = suohaContent;
    }

    public BigDecimal getSzcvalue() {
        return szcvalue;
    }

    public void setSzcvalue(BigDecimal szcvalue) {
        this.szcvalue = szcvalue;
    }

    public BigDecimal getYlcvalue() {
        return ylcvalue;
    }

    public void setYlcvalue(BigDecimal ylcvalue) {
        this.ylcvalue = ylcvalue;
    }

    public Integer getXcType() {
        return xcType;
    }

    public void setXcType(Integer xcType) {
        this.xcType = xcType;
    }

    public List<SamePointDetail> getSamePointDetail() {
        return samePointDetail;
    }

    public void setSamePointDetail(List<SamePointDetail> samePointDetail) {
        this.samePointDetail = samePointDetail;
    }
}
