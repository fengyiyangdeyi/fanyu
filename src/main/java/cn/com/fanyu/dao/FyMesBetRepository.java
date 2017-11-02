package cn.com.fanyu.dao;
import cn.com.fanyu.domain.FyMesBet;
import cn.com.fanyu.domain.FyMesScore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface FyMesBetRepository extends JpaRepository<FyMesBet, Long> ,JpaSpecificationExecutor<FyMesBet> {
    List<FyMesBet> findByStatusAndGroupid(Integer integer, String groupid);

    FyMesBet findByStatusAndGroupidAndUsername(int i, String groupid, String username);

    List<FyMesBet> findByStatusAndTaskId(int i, Long id);

    FyMesBet findByStatusAndTaskIdAndUsername(int i, Long id, String banker);
}