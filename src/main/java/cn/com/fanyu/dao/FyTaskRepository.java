package cn.com.fanyu.dao;

import cn.com.fanyu.domain.FyRule;
import cn.com.fanyu.domain.FyTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;

public interface FyTaskRepository extends JpaRepository<FyTask, Long>, JpaSpecificationExecutor<FyTask> {
    List<FyTask> findByStatus(Integer i);

    @Query(value = "SELECT * from fy_task f where f.groupid=:groupid order by add_time desc limit 1", nativeQuery = true)
    FyTask findLashTask(@Param("groupid") String groupid);
}