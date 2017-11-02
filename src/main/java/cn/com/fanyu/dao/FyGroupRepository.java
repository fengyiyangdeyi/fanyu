package cn.com.fanyu.dao;
import cn.com.fanyu.domain.FyGroup;
import cn.com.fanyu.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FyGroupRepository extends JpaRepository<FyGroup, Long> ,JpaSpecificationExecutor<FyGroup> {
    @Query("from FyGroup f where f.owner=:owner")
    List<FyGroup> ownerChatGroup(@Param("owner") String owner);

    @Query("from FyGroup f where f.groupid=:groupid")
    FyGroup getChatGroup(@Param("groupid") String groupid);

    FyGroup findByCode(String code);

    @Query(value = "SELECT g.id, g.groupid, g.groupname, u.nickname,g.code FROM fy_group g LEFT JOIN fy_user u ON u.username = g.`owner` limit :pageIndex,:pageSize",nativeQuery = true)
    List<Object[]> findAllWithUserinfo(@Param("pageIndex") Integer pageIndex,@Param("pageSize") Integer pageSize);
}