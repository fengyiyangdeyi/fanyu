package cn.com.fanyu.dao;
import cn.com.fanyu.domain.FyUser;
import cn.com.fanyu.domain.FyVersionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface FyVersionStatusRepository extends JpaRepository<FyVersionStatus, Long> ,JpaSpecificationExecutor<FyVersionStatus> {

    FyVersionStatus findByVersionValueAndType(String versionValue, String type);
}