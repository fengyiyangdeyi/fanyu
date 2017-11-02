package cn.com.fanyu.dao;
import cn.com.fanyu.domain.FyRuleTemplates;
import cn.com.fanyu.domain.FyUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FyRuleTemplatesRepository extends JpaRepository<FyRuleTemplates, Long> ,JpaSpecificationExecutor<FyRuleTemplates> {
    @Query("from FyRuleTemplates f where f.pageName=:pageName")
    FyRuleTemplates getTemplatesByName(@Param("pageName")String pageName);
}