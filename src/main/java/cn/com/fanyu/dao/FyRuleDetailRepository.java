package cn.com.fanyu.dao;
import cn.com.fanyu.domain.FyRegular;
import cn.com.fanyu.domain.FyRuleDetail;
import cn.com.fanyu.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FyRuleDetailRepository extends JpaRepository<FyRuleDetail, Long> ,JpaSpecificationExecutor<FyRuleDetail> {

    FyRuleDetail findByTypeAndGroupidAndSelectIdAndRuleId(Integer type, String selectId, String groupid,Long ruleId);

    List<FyRuleDetail> findByTypeAndGroupidAndRuleId(int i, String groupid, Long id);
}