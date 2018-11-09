package com.mzkj.mock_trading_system.trade.repo.mapper.ow;

import org.apache.ibatis.annotations.Insert;
import org.springframework.stereotype.Repository;
import com.mzkj.mock_trading_system.trade.entity.UserOrder;

/**
 * create by zhouzhenyang on 2018/7/29
 */
@Repository
public interface OrderMapper {

    @Insert("insert into u_order ()")
    void saveOrder(UserOrder order);
}
