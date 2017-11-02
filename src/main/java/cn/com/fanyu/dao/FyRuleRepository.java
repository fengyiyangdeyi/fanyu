package cn.com.fanyu.dao;
import cn.com.fanyu.domain.FyRule;
import cn.com.fanyu.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;

public interface FyRuleRepository extends JpaRepository<FyRule, Long> ,JpaSpecificationExecutor<FyRule> {

    @Query("from FyRule f where f.groupId=:groupId")
    FyRule getRule(@Param("groupId") String groupId);

    @Query("delete FyRule f where f.groupId=:groupId")
    @Modifying
    @Transactional
    void deleteByGroupid(@Param("groupId") String groupId);

    FyRule findByName(String s);
}