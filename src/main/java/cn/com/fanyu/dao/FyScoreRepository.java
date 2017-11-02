package cn.com.fanyu.dao;
import cn.com.fanyu.domain.FyGroup;
import cn.com.fanyu.domain.FyScore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FyScoreRepository extends JpaRepository<FyScore, Long> ,JpaSpecificationExecutor<FyScore> {

    @Query(value = "SELECT f.id, f.username, f.integral, u.nickname, rl.nickname as rlnk,f.zintegral FROM fy_score f LEFT JOIN fy_user u ON u.username = f.username LEFT JOIN fy_user rl ON rl.username = f.relate_username WHERE f.groupid=:groupid and f.status=1 limit :pageIndex,:pageSize",nativeQuery = true)
    List<Object[]> getMemberByGroubidNative(@Param("groupid") String groupid,@Param("pageIndex") Integer pageIndex,@Param("pageSize") Integer pageSize);

    @Query(value = "SELECT u.username, u.nickname, u.img_url FROM fy_score s LEFT JOIN fy_user u ON u.username = s.username WHERE s.groupid =:groupid and s.status=1",nativeQuery = true)
    List findUserInfo(@Param("groupid") String groupid);

    FyScore findByGroupidAndUsernameAndStatus(String groupid, String username, int i);

    @Query(value = "SELECT s.username FROM fy_score s LEFT JOIN fy_user u ON u.username = s.relate_username WHERE u.nickname =:relateNickname AND s.groupid =:groupid",nativeQuery = true)
    List<String> findRelateNicknameNative(@Param("groupid") String groupid,@Param("relateNickname") String relateNickname);

    @Query(value = "SELECT f.id, f.username, f.integral, u.nickname, rl.nickname as rlnk,f.zintegral FROM fy_score f LEFT JOIN fy_user u ON u.username = f.username LEFT JOIN fy_user rl ON rl.username = f.relate_username WHERE f.groupid=:groupid and u.nickname like :keyword and f.status=1 limit :pageIndex,:pageSize",nativeQuery = true)
    List<Object[]> getMemberByGroubidAndNickNative(@Param("groupid") String groupid,@Param("pageIndex") Integer pageIndex,@Param("pageSize") Integer pageSize, @Param("keyword")String keyword);

    @Query(value = "SELECT count(1) FROM fy_score f LEFT JOIN fy_user u ON u.username = f.username WHERE f.groupid=:groupid and u.nickname like :keyword and f.status=1",nativeQuery = true)
    long countMemberByGroubidAndNickNative(@Param("groupid") String groupid, @Param("keyword")String keyword);

    FyScore findByGroupidAndId(String groupid, Long id);
}