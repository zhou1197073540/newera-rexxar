package com.mzkj.news.service;

import com.alibaba.fastjson.JSONObject;
import com.mzkj.news.bean.*;
import com.mzkj.news.bean_vo.CheckStock;
import com.mzkj.news.bean_vo.StockUpDownVO;
import com.mzkj.news.bean_vo.UserVo;
import com.mzkj.news.mapper.UserMapper;
import com.mzkj.news.secondMapper.StockMapper;
import com.mzkj.news.utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

@Service
public class UserService {

    @Autowired
    UserMapper userMapper;
    @Autowired
    TokenUtil tokenUtil;
    @Autowired
    StockMapper stockMapper;

    public UserVo saveOrUpdateUserPhone(String phone_num) throws Exception {
        String uid=HashUtil.EncodeByMD5(phone_num+ TimeUtil.getTimeStamp());
        String token = tokenUtil.createToken(uid);
        User us=userMapper.selectCountByPhone(phone_num);
        if(us==null){
            String psd="abc"+HashUtil.generateRandomNum(5);
            String mockStock=HashUtil.generateRandomNum(12);
            User user=new User();
            user.setPhone_num(phone_num);
            user.setPassword(psd);
            user.setUid(uid);
            user.setType("app");
            user.setUsername("wz_"+phone_num);
            user.setToken(token);
            user.setA_account(mockStock);
            userMapper.saveUser(user);
            if(!isExistMessage(uid)){
                userMapper.saveAppMessage(new MessageCenter(uid,"person",psd));
                userMapper.saveMockStockAssets(mockStock);
            }
            tokenUtil.uploadToken(token, StringMap.New().add("uid",user.getUid()).add("username",user.getUsername()),30 * 24 * 60 * 60);
            return new UserVo(user.getUsername(),user.getPhone_num(),token,user.getA_account());
        }else{
            System.out.println("手机登录用户uid:"+us.getUid()+",用户名："+us.getUsername());
            tokenUtil.uploadToken(token, StringMap.New().add("uid",us.getUid()).add("username",us.getPhone_num()),30 * 24 * 60 * 60);
            String a_account=us.getA_account();
            if(StringUtil.isEmpty(a_account)){
                String a_acc=HashUtil.generateRandomNum(12);
                us.setA_account(a_acc);
                userMapper.updateAaccount(us);
                System.out.println("生成新的A股账户:"+a_acc);
            }
            return new UserVo(us.getUsername(),us.getPhone_num(),token,us.getA_account());
        }
    }

    public boolean isExistMessage(String title){
        int num=userMapper.isExistMessage(title);
        if(num>0) return true;
        return false;
    }

    public String getUidByToken(String token) {
        return tokenUtil.getProperty(token,"uid");
    }

    public void saveOrUpdateSelfSelectStock(String uid, String code) {
        String recode= StringUtil.formatStockCode(code);
        int num=userMapper.selectCountByUidInSelfStocks(uid,recode);
        if(num<=0){
            userMapper.saveUserSelfStock(new SelfSelectStock(uid,recode));
        }
    }
    public void selfSelectedListStock(String uid, String codes) {
        if(StringUtil.isEmpty(codes)) return;
        if(codes.contains(",")){
            String[] strs=codes.split(",");
            for(String code:strs){
                saveOrUpdateSelfSelectStock(uid,code);
            }
        }else{
            saveOrUpdateSelfSelectStock(uid, codes.trim());
        }
    }

    public List<StockUpDownVO> selectedStockByUid(String uid) {
        List<String> codes= userMapper.selectStringListCodeByUidInSelfStock(uid);
        List<StockUpDownVO> vos=new ArrayList<>();
        for(String code:codes){
            StockInfo stock=null;
            try{
                String date=TimeUtil.addSubDate(0);
                stock=stockMapper.findStockInfo(code,date);
            }catch (Exception e){
                String date=TimeUtil.addSubDate(-1);
                stock=stockMapper.findStockInfo(code,date);
            }
            if(stock==null) continue;
            StockUpDownVO vo=calcuteRate(stock);
            vo.setCode(stock.getCode());
            vos.add(vo);
        }
        return vos;
    }

    public List<StockUpDownVO> selectedStockFromRedis(String uid) {
        List<StockUpDownVO> lsu=new ArrayList<StockUpDownVO>();
        List<StockUpDownVO> codes= userMapper.selectListCodeByUidInSelfStock(uid);
        if(codes.size()<=0) return lsu;
        Map<String,StockUpDownVO> map=new HashMap<>();
        try(Jedis jedis=RedisUtil.getJedis()){
            Pipeline pipeline=jedis.pipelined();
            for(StockUpDownVO codevo:codes){
                if(!map.containsKey(codevo.getCode_type())){
                    String code=StringUtil.removeStockCode(codevo.getCode_type());
                    codevo.setCode(code);
                    map.put(codevo.getCode_type(),codevo);
                }
                pipeline.hget("stock_latest",codevo.getCode_type());
            }
            List<Object> list=pipeline.syncAndReturnAll();
            for(Object obj:list){
                if(null==obj) continue;
                calcuteJsonRate(obj,map);
            }
        }
        Collection<StockUpDownVO> values = map.values();
        List<StockUpDownVO> sus= new ArrayList<>(values);
        for(StockUpDownVO vv:sus){
            if(StringUtil.isEmpty(vv.getRank()))vv.setRank("9999");
            if(StringUtil.isEmpty(vv.getName())){
                StockUpDownVO dbVo=userMapper.selectStockCodeAndName(vv.getCode_type());
                if(dbVo!=null) vv.setName(dbVo.getName());
            }
        }
        sus.sort(this::compartStock);
        return sus;
    }

    public  Map<String,String> queryStockLatestPriceByCode(List<String> codes) {
        if(codes==null) return null;
        try(Jedis jedis=RedisUtil.getJedis()){
            Map<String,String> map=new HashMap<String,String>();
            Pipeline pipeline=jedis.pipelined();
            for(String code_type:codes){
                if(!map.containsKey(code_type)){
                    map.put(code_type,null);
                }
                pipeline.hget("stock_latest",code_type);
            }
            List<Object> list=pipeline.syncAndReturnAll();
            for(Object obj:list){
                if(null==obj) continue;
                JSONObject json = JSONObject.parseObject(obj.toString());
                String now=json.getString("now");
                String full_code = json.getString("full_code");
                if(!StringUtil.isEmpty(full_code))map.put(full_code,now);
            }
            return map;
        }
    }

    private int compartStock(StockUpDownVO v1,StockUpDownVO v2){
        if(v1!=null &&v2!=null&&!StringUtil.isEmpty(v1.getRank(),v2.getRank())){
            return  Integer.parseInt(v1.getRank())-Integer.parseInt(v2.getRank());
        }
        return 0;
    }

    private StockUpDownVO calcuteRate(StockInfo stock) {
        JSONObject json=JSONObject.parseObject(stock.getC_value());
        BigDecimal now=new BigDecimal(json.getString("now"));
        BigDecimal close=new BigDecimal(json.getString("close"));
        String rate= (now.subtract(close)).divide(close,4, RoundingMode.HALF_UP).toString();
        StockUpDownVO vo= new StockUpDownVO();
        vo.setName(json.getString("name"));
        vo.setPrice(json.getString("now"));
        vo.setUp_down(rate);
        return vo;
    }

    private void calcuteJsonRate(Object obj,Map<String,StockUpDownVO> map) {
        try {
            if(null==obj) return;
            JSONObject json = JSONObject.parseObject(obj.toString());
            BigDecimal now = new BigDecimal(json.getString("now"));
            BigDecimal close = new BigDecimal(json.getString("close"));
            String full_code = json.getString("full_code");
            String _code = json.getString("code");
            String type=json.getString("type");
            String status=json.getString("status");
            if (StringUtil.isEmpty(full_code)) {
                full_code = StringUtil.formatStockCode(_code);
            }
            StockUpDownVO vo = map.get(full_code);
            if (vo != null) {
                vo.setName(json.getString("name"));
                vo.setPrice(json.getString("now"));
                vo.setType(type);
                vo.setStatus(status);
                if (close.intValue() == 0) {
                    vo.setUp_down("0.00");
                } else {
                    String rate = (now.subtract(close)).divide(close, 4, RoundingMode.HALF_UP).toString();
                    vo.setUp_down(rate);
                }
                map.put(full_code, vo);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void delSelfSelectStock(String uid, String code) {
        userMapper.delSelfSelectStock(new SelfSelectStock(uid,code));
    }

    public void addThumbsUp(String user_uid, String news_guid) {
        userMapper.addThumbsUp(user_uid,news_guid);
    }

    public void delThumbsUp(String user_uid, String news_guid) {
        userMapper.delThumbsUp(user_uid,news_guid);
    }

    public AppVersion checkVersion(String version) {
        AppVersion ver= userMapper.selectAppVersion();
        if(ver.getNew_version().compareTo(version)>0){
            ver.setUpdate("Yes");
        }else{
            ver.setUpdate("No");
        }
        return ver;
    }

    public List<MessageCenter> messageCenter(String user_uid) {
        return userMapper.messageCenter(user_uid);
    }

    public List<CheckStock> checkStockExists(String uid,String codes) {
        if(StringUtil.isEmpty(codes)) return null;
        String strs=null;
        if(codes.contains(",")){
            strs=StringUtil.list2SingleMarkStr(codes.split(","));
        }else {
            strs="'"+codes+"'";
        }
        List<CheckStock> stocks= userMapper.checkStockExists(strs);
        if(stocks==null||stocks.isEmpty()) return null;
        for(CheckStock one:stocks){
            int count=userMapper.selectCountByUidInSelfStocks(uid,one.getCode_type());
            if(count<=0) one.setResult("notExist");
        }
        return stocks;
    }

    public List<News> selectSelfStockNews() {
        return userMapper.queryTenNewsByRandoms();
    }

    public int updateWarmPriceByUid(String uid, String code, String price,String type) {
        return userMapper.updateWarmPriceByUid(uid,code,price,type);
    }

    public boolean cancelStockWarning(String uid, String code) {
        int num=userMapper.cancelStockWarning(uid,code);
        return num>0;
    }
}
