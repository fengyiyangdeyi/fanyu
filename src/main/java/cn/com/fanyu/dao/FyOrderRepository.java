package cn.com.fanyu.dao;
import cn.com.fanyu.domain.FyOrder;
import cn.com.fanyu.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FyOrderRepository extends JpaRepository<FyOrder, Long> ,JpaSpecificationExecutor<FyOrder> {

    FyOrder findByCode(String code);
}