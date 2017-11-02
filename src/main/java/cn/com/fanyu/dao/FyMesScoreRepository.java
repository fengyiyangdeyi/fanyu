package cn.com.fanyu.dao;
import cn.com.fanyu.domain.FyMesScore;
import cn.com.fanyu.domain.FyRegular;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FyMesScoreRepository extends JpaRepository<FyMesScore, Long> ,JpaSpecificationExecutor<FyMesScore> {

    @Query(value = "SELECT s.id, s.username, s.content, s.add_time, u.nickname FROM fy_mes_score s LEFT JOIN fy_user u ON u.username = s.username WHERE s.groupid =:groupid limit :pageIndex,:pageSize",nativeQuery = true)
    List<Object[]> findMesScore(@Param("groupid") String groupid,@Param("pageIndex") Integer pageIndex,@Param("pageSize") Integer pageSize);
}