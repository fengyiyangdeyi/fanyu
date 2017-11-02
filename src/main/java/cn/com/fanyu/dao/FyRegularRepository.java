package cn.com.fanyu.dao;
import cn.com.fanyu.domain.FyRegular;
import cn.com.fanyu.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FyRegularRepository extends JpaRepository<FyRegular, Long> ,JpaSpecificationExecutor<FyRegular> {

    @Query("from FyRegular u where u.groupid=:groupid")
    List<FyRegular> getRegular(@Param("groupid") String groupid);

    FyRegular findByRegularTypeAndGroupid(Integer regularTypet,String groupid);

    List<FyRegular> findByGroupid(String groupid);
}