package cn.com.fanyu.dao;
import cn.com.fanyu.domain.FyScore;
import cn.com.fanyu.domain.FyScoreDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FyScoreDetailRepository extends JpaRepository<FyScoreDetail, Long> ,JpaSpecificationExecutor<FyScoreDetail> {
    FyScoreDetail findByScoreId(Long id);
}