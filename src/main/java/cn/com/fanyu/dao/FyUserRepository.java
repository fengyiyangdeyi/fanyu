package cn.com.fanyu.dao;
import cn.com.fanyu.domain.FyUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface FyUserRepository extends JpaRepository<FyUser, Long> ,JpaSpecificationExecutor<FyUser> {
    @Query("from FyUser f where f.username=:username")
    FyUser getIMUserByUserName(@Param("username") String username);

    FyUser findByUuid(String jsessionId);
}