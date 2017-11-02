package cn.com.fanyu.dao;
import cn.com.fanyu.domain.FyGlobalValue;
import cn.com.fanyu.domain.FyUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FyGlobalValueRepository extends JpaRepository<FyGlobalValue, Long> ,JpaSpecificationExecutor<FyGlobalValue> {
    List<FyGlobalValue> findByStatus(int i);

    FyGlobalValue findByStatusAndGlobalKey(int i, String addDiamond);
}