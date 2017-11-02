package cn.com.fanyu.dao;
import cn.com.fanyu.domain.FyDiamond;
import cn.com.fanyu.domain.FyDiamondDetail;
import cn.com.fanyu.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FyDiamondDetailRepository extends JpaRepository<FyDiamondDetail, Long> ,JpaSpecificationExecutor<FyDiamondDetail> {
    @Query("from FyDiamondDetail dt where dt.username=:username and dt.fyDiamondId=:fyDiamondId")
    FyDiamondDetail getDt(@Param("username") String username,@Param("fyDiamondId") Long fyDiamondId);

    @Query("from FyDiamondDetail dt where dt.fyDiamondId=:fyDiamondId")
    List<FyDiamondDetail> getDtByDiamondId(@Param("fyDiamondId") Long fyDiamondId);

    @Query(value = "SELECT dt.username, dt.add_time, dt.diamond_num, u.nickname, u.img_url FROM fy_diamond_detail dt LEFT JOIN fy_user u ON dt.username = u.username where dt.fy_diamond_id=:fyDiamondId",nativeQuery = true)
    List<Object[]> getDtAndUserinfoByDiamondId(@Param("fyDiamondId") Long fyDiamondId);
}