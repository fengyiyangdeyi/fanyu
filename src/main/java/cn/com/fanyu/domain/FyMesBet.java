package cn.com.fanyu.domain;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 下注
 */
@Entity
public class FyMesBet {
    @Id
    @GeneratedValue
    private Long id;
    @Version
    private Long version;
    @Column
    private Date addTime=new Date();
    @Column
    private Date sendDiamondTime;
    @Column
    private Date receivedDiamondTime;
    @Column
    private BigDecimal totalDiamond;
    @Column
    private String username;
    @Column
    private String nickname;
    @Column
    private String groupid;
    /**
     * 0-正常下注，1-梭哈
     */
    @Column
    private Integer regularType=0;
    @Column
    private String content;
    @Column
    private BigDecimal data;
    /**
     * 0-下注，1-已计算结果
     */
    @Column
    private Integer status=0;
    @Column
    private Long diamondId;
    @Column
    private Long taskId;
    @Column
    private Long diamondDetailId;
    /**
     * 0-正常下注，1-未下注抢包的
     */
    @Column
    private Integer type=0;
    /**
     * 抢到的红包金额
     */
    @Column
    private BigDecimal point=new BigDecimal(0);
    @Column
    private String remark;
    @Column
    private BigDecimal result;
    @Column
    private BigDecimal xshuifei;//
    @Column
    private BigDecimal ylshuifei;
    @Column
    private BigDecimal rtcshuifei;

    public BigDecimal getTotalDiamond() {
        return totalDiamond;
    }

    public void setTotalDiamond(BigDecimal totalDiamond) {
        this.totalDiamond = totalDiamond;
    }

    public BigDecimal getRtcshuifei() {
        return rtcshuifei;
    }

    public Date getSendDiamondTime() {
        return sendDiamondTime;
    }

    public void setSendDiamondTime(Date sendDiamondTime) {
        this.sendDiamondTime = sendDiamondTime;
    }

    public Date getReceivedDiamondTime() {
        return receivedDiamondTime;
    }

    public void setReceivedDiamondTime(Date receivedDiamondTime) {
        this.receivedDiamondTime = receivedDiamondTime;
    }

    public void setRtcshuifei(BigDecimal rtcshuifei) {
        this.rtcshuifei = rtcshuifei;
    }

    public BigDecimal getYlshuifei() {
        return ylshuifei;
    }

    public void setYlshuifei(BigDecimal ylshuifei) {
        this.ylshuifei = ylshuifei;
    }

    public BigDecimal getXshuifei() {
        return xshuifei;
    }

    public void setXshuifei(BigDecimal xshuifei) {
        this.xshuifei = xshuifei;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public BigDecimal getResult() {
        return result;
    }

    public void setResult(BigDecimal result) {
        this.result = result;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public BigDecimal getPoint() {
        return point;
    }

    public void setPoint(BigDecimal point) {
        this.point = point;
    }

    public BigDecimal getData() {
        return data;
    }

    public void setData(BigDecimal data) {
        this.data = data;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

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

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
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

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Long getDiamondId() {
        return diamondId;
    }

    public void setDiamondId(Long diamondId) {
        this.diamondId = diamondId;
    }

    public Long getDiamondDetailId() {
        return diamondDetailId;
    }

    public void setDiamondDetailId(Long diamondDetailId) {
        this.diamondDetailId = diamondDetailId;
    }
}