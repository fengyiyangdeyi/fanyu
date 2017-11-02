package cn.com.fanyu.dao;

import cn.com.fanyu.domain.FyDiamondAccounts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface FyDiamondAccountsRepository extends JpaRepository<FyDiamondAccounts, Long>,JpaSpecificationExecutor<FyDiamondAccounts> {

    FyDiamondAccounts findByTransactionId(String transaction_id);
}
