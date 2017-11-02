package cn.com.fanyu.dao;
import cn.com.fanyu.domain.FyGoods;
import cn.com.fanyu.domain.FyUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface FyGoodsRepository extends JpaRepository<FyGoods, Long> ,JpaSpecificationExecutor<FyGoods> {
    FyGoods findByAppleid(String product_id);
}