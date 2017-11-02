package cn.com.fanyu.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.util.Date;

@Entity
public class FyResult {
    @Id
    @GeneratedValue
    private Long id;
    @Column
    private Date addTime;
    @Column
    private String groupId;

    /**
     * 对局唯一标识
     */
    @Column
    private String wheel;
    @Column
    private String reultInfo;
    @Column
    private String remark;
}