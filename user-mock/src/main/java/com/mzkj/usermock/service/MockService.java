package com.mzkj.usermock.service;

import com.mzkj.usermock.bean.StockAccountInfo;
import com.mzkj.usermock.bean.StockMarketSnapshotPO;
import com.mzkj.usermock.bean.StockOperation;
import com.mzkj.usermock.bean_rmi.StockOrderRMI;
import com.mzkj.usermock.bean_vo.AccountInfoVO;
import com.mzkj.usermock.bean_vo.HoldStockInfoVO;
import com.mzkj.usermock.bean_vo.PositionVO;
import com.mzkj.usermock.bean_vo.UserAssets;
import com.mzkj.usermock.constant.Const;
import com.mzkj.usermock.dto.RespResult;
import com.mzkj.usermock.enums.CodeEnum;
import com.mzkj.usermock.exception.runtime.RequestException;
import com.mzkj.usermock.feign.TradingCenterAPI;
import com.mzkj.usermock.mapper.MockMapper;
import com.mzkj.usermock.utils.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.Jedis;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

@Service
public class MockService {
    private static Logger logger= LoggerFactory.getLogger(MockService.class);
    @Autowired
    TradingCenterAPI tradingCenterAPI;
    @Autowired
    MockMapper mockMapper;
    @Autowired
    AAccountService aAccountService;

    public boolean stockRMIs(StockOperation stock){
        try {
            RespResult res= tradingCenterAPI.buyStock(ObjChangeUtil.change2StockOrder(stock));
            System.out.println(res.toString());
            return res.getStatus().equals(Const.RMI_SUCCESS);
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
    public void stockRMI(StockOperation stock) throws RequestException {
        try {
            RespResult res= tradingCenterAPI.buyStock(ObjChangeUtil.change2StockOrder(stock));
            System.out.println(res.toString());
            if (!res.getStatus().equals(Const.RMI_SUCCESS)){
                throw new RequestException(CodeEnum.REQUEST_RMI_RES_ERROR);
            }
        }catch (Exception e){
            e.printStackTrace();
            throw new RequestException(CodeEnum.REQUEST_RMI_ERROR);
        }
    }

    public void saveStockOperation(StockOperation stock){
        mockMapper.saveStockOperation(stock);
        logger.info("买卖撤单成功,账号:{},股票:{},操作:{},价格:{},数量:{},时间:{}",
                stock.getA_account(),stock.getCode_type(),stock.getOperation(),stock.getPrice(),stock.getNum(),
                stock.getDatetime());
    }

    /**
     *
     * @param account  账户
     * @param totalMoney  多少钱
     * @param operation    操作类型（买，卖，取消）
     * @return
     */
    public boolean addSubMoney(String account,String totalMoney,String operation)  {
        boolean res= aAccountService.addSubMoney(account,totalMoney,operation);
        logger.info("此次对钱包操作:账户:{},钱:{},类型:{},操作结果:{}",account,totalMoney,operation,res);
        if (!res) return false;
        saveStockWalletLog(account,totalMoney,operation);
        return true;
    }

    public void saveStockWalletLog(String account, String totalcost,String type) {
        if(Const.ADD_MONEY.equals(type)) totalcost="+"+totalcost;
        if(Const.SUB_MONEY.equals(type)) totalcost="-"+totalcost;
        mockMapper.saveStockWalletLog(account,totalcost,TimeUtil.getDateTime());
    }

    public Map<String,Object> stockAccountInfo(String account) {
        //获取该用户的可用资产
        StockAccountInfo accoInfo=mockMapper.selectStockAccountInfo(account);
        if(null==accoInfo) return null;
        Map<String,Object> map=new HashMap<>();
        List<HoldStockInfoVO> holdVOs=new ArrayList<>();
        //获取该用户下所有持有的股票
        List<StockOperation> holdStock=holdingStockInfo(account);
        String lockingAssets=getLockingAssets(account);
        if(holdStock==null){
            AccountInfoVO vv=new AccountInfoVO();
            vv.setAssets_available(accoInfo.getAssets_available());
            vv.setTotal_assets(MathUtil.add(lockingAssets,accoInfo.getAssets_available()).toString());
            map.put("accountInfo",vv);
            map.put("stockInfo",null);
            map.put("lockingAssets",lockingAssets);
            return map;
        }
        String total_assets="0",total_pro_loss="0",day_pro_loss="0",total_market="0",assets_available=accoInfo.getAssets_available();
        for(StockOperation hold:holdStock){
            HoldStockInfoVO vv=genHoldVO(hold);
            //总市值
            total_market=MathUtil.add(total_market,vv.getMarket()).toString();
            //当日盈亏
            String curProLoss=MathUtil.sub(hold.getCurrent_price(),hold.getYesterday_price()).toString();
            day_pro_loss=MathUtil.add(day_pro_loss,MathUtil.mul(curProLoss,hold.getPosition())).toString();
            //总盈亏
            total_pro_loss=MathUtil.add(total_pro_loss,vv.getPro_loss()).toString();
            holdVOs.add(vv);
        }
        total_assets=MathUtil.add(lockingAssets,total_market,assets_available).toString();
        AccountInfoVO vo=new AccountInfoVO();
        vo.setTotal_assets(total_assets);
        vo.setTotal_pro_loss(total_pro_loss);
        vo.setDay_pro_loss(day_pro_loss);
        vo.setTotal_market(total_market);
        vo.setAssets_available(assets_available);
        map.put("accountInfo",vo);
        map.put("stockInfo",holdVOs);
        map.put("lockingAssets",lockingAssets);
        return map;
    }

    private String getLockingAssets(String account){
        //获取该用户的还未成交的资产
        String total_assets="0";
        List<StockOrderRMI> waitingStocks=mockMapper.selectWaitSettleBuyStock(account);
        if(waitingStocks==null||waitingStocks.isEmpty()) return total_assets;
        for(StockOrderRMI wait:waitingStocks){
            String waitAssets=MathUtil.mul2str(wait.getAmount(),wait.getPrice());
            total_assets=MathUtil.add(total_assets,waitAssets).toString();
        }
        return total_assets;
    }

    //计算每只股票的动态资产
    private HoldStockInfoVO genHoldVO(StockOperation hold) {
        //市值
        String total_market=MathUtil.mul(hold.getPosition(),hold.getCurrent_price()).toString();
        //盈亏
        String totalProLoss=MathUtil.sub(hold.getCurrent_price(),hold.getCost()).toString();
        String totalPL=MathUtil.mul(totalProLoss,hold.getPosition()).toString();
        //盈亏比
        String totalPLRate=MathUtil.div(totalProLoss,hold.getCost()).toString();

        HoldStockInfoVO vo=new HoldStockInfoVO();
        vo.setCode_type(hold.getCode_type());
        vo.setPosition(hold.getPosition());
        vo.setPosition_available(hold.getPosition_available());
        vo.setCost(hold.getCost());
        vo.setCurrent_price(hold.getCurrent_price());
        vo.setMarket(total_market);
        vo.setStock_name(hold.getStock_name());
        vo.setPro_loss(totalPL);
        vo.setPro_loss_rate(totalPLRate);
        return vo;
    }

    private List<StockOperation> holdingStockInfo(String account) {
        List<StockOperation> posiStock=mockMapper.selectListPositionStock(account);
        if(posiStock==null||posiStock.isEmpty()) return null;
        try(Jedis jedis= RedisUtil.getJedis()){
            //获取当前最新价格
            for(StockOperation stock:posiStock){
                String str=jedis.hget(Const.STOCK_LATEST, stock.getCode_type());
                StockMarketSnapshotPO po=JSONUtil.JSON2Object(str,StockMarketSnapshotPO.class);
                if(po==null) continue;
                stock.setCurrent_price(Float.parseFloat(po.getNow())>0?po.getNow():po.getClose());
                stock.setYesterday_price(po.getClose());
//                if(checkIsNextDay(stock)&&!stock.getPosition().equals(stock.getPosition_available())){
//                    stock.setPosition_available(stock.getPosition());
//                    mockMapper.updateAvaiPosition(stock);
//                }
            }
        }
        return posiStock;
    }

    private boolean checkIsNextDay(StockOperation op) {
        LocalTime time = LocalDateTime.now().toLocalTime();
        if(time.isAfter(LocalTime.of(1,0)) && time.isBefore(LocalTime.of(2,0))){
            String settlement_time=op.getSettlement_time().substring(0,10);
            return TimeUtil.getCurDateTime().compareTo(settlement_time) > 0;
        }
        return false;
    }

    public boolean addSubAvaiPosition(StockOperation stock,String addSub) {
        StockOperation posiStock=mockMapper.selectOnePositionStock(stock.getA_account(),stock.getCode_type());
        if(null==posiStock) return false;
        if(Const.SUB_AVAI_POSITION.equals(addSub)){
            long num=MathUtil.sub(posiStock.getPosition_available(),stock.getNum()).longValue();
            System.out.println("卖出可用计算后持仓数量为:"+num);
            if(num>=0){
                posiStock.setPosition_available(String.valueOf(num));
                int res=mockMapper.updateAvaiPosition(posiStock);
                return res>0;
            }
        }else if(Const.ADD_AVAI_POSITION.equals(addSub)){
            String num=MathUtil.add(posiStock.getPosition_available(),stock.getNum()).toString();
            posiStock.setPosition_available(num);
            int res=mockMapper.updateAvaiPosition(posiStock);
            return res>0;
        }
        return false;
    }

    public List<StockOrderRMI>  selectResultByAccount(String account) {
        return mockMapper.selectResultByAccount(account);
    }
    public void updateTradingStatusFinish(String oid,String trading_status) {
        mockMapper.updateTradingStatusFinish(oid,trading_status);
    }

    public List<PositionVO> selectListUserPositionStock(String account) {
        return mockMapper.selectListPositionStocks(account);
    }

    public void updateAvaiPosition() {
        mockMapper.updateAllUserAvaiPosition();
    }

    public void deleteInvalidStock() {
        mockMapper.deleteInvalidStock();
    }

    public void updateAllAvaiPosition() {
        mockMapper.updateAllAvaiPosition();
    }

    public int checkSettle(String order_num) {
        return mockMapper.checkSettle(order_num);
    }
}
