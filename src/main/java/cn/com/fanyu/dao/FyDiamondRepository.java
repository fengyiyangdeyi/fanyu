package cn.com.fanyu.dao;
import cn.com.fanyu.domain.FyDiamond;
import cn.com.fanyu.domain.FyGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface FyDiamondRepository extends JpaRepository<FyDiamond, Long> ,JpaSpecificationExecutor<FyDiamond> {
    List<FyDiamond> findByIsTimeOutAndAddTimeLessThan(int i, Date time);
}