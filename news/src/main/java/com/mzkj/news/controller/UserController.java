package com.mzkj.news.controller;

import com.alibaba.fastjson.JSONObject;
import com.mzkj.news.bean.*;
import com.mzkj.news.bean_vo.CheckStock;
import com.mzkj.news.bean_vo.StockUpDownVO;
import com.mzkj.news.bean_vo.UserVo;
import com.mzkj.news.dto.RespResult;
import com.mzkj.news.service.UserService;
import com.mzkj.news.utils.RedisUtil;
import com.mzkj.news.utils.StringUtil;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import redis.clients.jedis.Jedis;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class UserController {

    @Autowired
    UserService userService;

    /**
     * 手机验证码登录
     */
    @ResponseBody
    @PostMapping("/vcode_login")
    public RespResult VCodeLogin(@RequestBody JSONObject obj) throws Exception {
        String phone_num=obj.getString("phone_num");
        String v_code=obj.getString("v_code");
        try (Jedis jedis= RedisUtil.getJedis()){
            if (!jedis.exists(phone_num + "_vcode")) {
                return RespResult.genErrorResult("验证码超时！");
            } else {
                String oriCode = jedis.get(phone_num + "_vcode");
                if (v_code.equals(oriCode)) {
                    UserVo user=userService.saveOrUpdateUserPhone(phone_num);
                    return RespResult.genSuccessResult(user);
                }else {
                    return RespResult.genErrorResult("验证码输入错误！");
                }
            }
        }
    }

    /**
     * 手机验证码登录
     */
    @ResponseBody
    @PostMapping("/vcode_logins")
    public RespResult VCodeLogins(@RequestBody PhoneVcodeVO obj) throws Exception {
        String phone_num=obj.getPhone_num();
        String v_code=obj.getV_code();
        System.out.println("登录电话号码:"+phone_num);
        try (Jedis jedis= RedisUtil.getJedis()){
            if (!jedis.exists(phone_num + "_vcode")) {
                return RespResult.genErrorResult("验证码超时！");
            } else {
                String oriCode = jedis.get(phone_num + "_vcode");
                if (v_code.equals(oriCode)) {
                    UserVo user=userService.saveOrUpdateUserPhone(phone_num);
                    return RespResult.genSuccessResult(user);
                }else {
                    return RespResult.genErrorResult("验证码输入错误！");
                }
            }
        }
    }

    /**
     * 用户关注自选股
     */
    @ResponseBody
    @GetMapping("/selfSelectedStock")
    public RespResult selfSelectedStock(@RequestParam("token")String token,@RequestParam("code")String code){
        System.out.println("用户选股token:"+token);
        String uid=userService.getUidByToken(token);
        if(StringUtil.isEmpty(uid)) return RespResult.genErrorResult("token已过期！");
        userService.saveOrUpdateSelfSelectStock(uid,code);
        return RespResult.genSuccessResult("添加成功！");
    }

    /**
     * 用户关注一组股票
     */
    @ResponseBody
    @GetMapping("/u/self-selected-list-stock")
    public RespResult selfSelectedListStock(@RequestParam("token")String token,@RequestParam("code-list")String codes){
        System.out.println("用户选股token:"+token);
        String uid=userService.getUidByToken(token);
        if(StringUtil.isEmpty(uid)) return RespResult.genErrorResult("token已过期！");
        userService.selfSelectedListStock(uid,codes);
        return RespResult.genSuccessResult("添加成功！");
    }
    /**
     * 检查股票代码是否存在
     */
    @ResponseBody
    @GetMapping("/u/check-stock-exists")
    public RespResult checkStockExists(@RequestParam("token")String token,@RequestParam("code-list") String codes){
        System.out.println("用户选股token:"+token);
        String uid=userService.getUidByToken(token);
        if(StringUtil.isEmpty(uid)) return RespResult.genErrorResult("token已过期！");
        List<CheckStock> stocks = userService.checkStockExists(uid,codes);
        return RespResult.genSuccessResult(stocks);
    }



    /**
     * 删除选股
     */
    @ResponseBody
    @GetMapping("/delSelfSelectedStock")
    public RespResult delSelfSelectedStock(@RequestParam("token")String token,@RequestParam("code")String code){
        String uid=userService.getUidByToken(token);
        if(StringUtil.isEmpty(uid)) return RespResult.genErrorResult("token已过期！");
        if(code.contains(",")){
            String[] cs=code.split(",");
            for(String one:cs){
                one=one.trim();
                if(StringUtil.isEmpty(one)) continue;
                userService.delSelfSelectStock(uid,StringUtil.formatStockCode(one));
            }
        }else{
            userService.delSelfSelectStock(uid,StringUtil.formatStockCode(code));
        }
        return RespResult.genSuccessResult("删除成功！");
    }

    /**
     * 显示用户自选股
     */
    @ResponseBody
    @GetMapping("/showSelectedStock")
    public RespResult showSelectedStock(@RequestParam("token")String token){
        String uid=userService.getUidByToken(token);
        if(StringUtil.isEmpty(uid)) return RespResult.genErrorResult("token已过期！");
//        List<StockUpDownVO> vos= userService.selectedStockByUid(uid);
        List<StockUpDownVO> vos= userService.selectedStockFromRedis(uid);
        return RespResult.genSuccessResult(vos);
    }

    /**
     * 修改户自选股推送的提醒价格
     */
    @ResponseBody
    @GetMapping("/uc/warm_price/token/{token}/code/{code}/price/{price}/type/{type}")
    public RespResult showSelectedStock(@PathVariable("token")String token,@PathVariable("price")String price,
                                        @PathVariable("code") String code,@PathVariable("type") String type){
        String uid=userService.getUidByToken(token);
        if(StringUtil.isEmpty(uid)) return RespResult.genErrorResult("token已过期！");
        int num=userService.updateWarmPriceByUid(uid,code,price,type);
        if(num<=0) return RespResult.genErrorResult("修改提醒价格失败");
        return RespResult.genSuccessResult("修改提醒价格成功");
    }

    /**
     * 点赞接口
     */
    @ResponseBody
    @GetMapping("/u/thumbs-up/{type}")
    public RespResult userThumbsUp(@RequestParam("token")String token,@RequestParam("guid")String news_guid,@PathVariable("type") String type){
        String user_uid=userService.getUidByToken(token);
        if(StringUtil.isEmpty(user_uid,news_guid)) return RespResult.genErrorResult("token失效或者guid为空！");
        if("delete".equals(type)){
            userService.delThumbsUp(user_uid,news_guid);
            return RespResult.genSuccessResult("点赞成功！");
        }else if("add".equals(type)){
            userService.addThumbsUp(user_uid,news_guid);
            return RespResult.genSuccessResult("取消点赞成功！");
        }else{
            return RespResult.genErrorResult("看看你的操作类型是不是正确的！！！");
        }
    }
    /**
     * app版本控制接口
     */
    @ResponseBody
    @GetMapping("/u/check_version")
    public RespResult<AppVersion> checkVersion(@RequestParam("new_version")String new_version){
        AppVersion ver=userService.checkVersion(new_version);
        return RespResult.genSuccessResult(ver);
    }
    /**
     * app消息中心
     */
    @ResponseBody
    @GetMapping("/u/message-center")
    public RespResult messageCenter(@RequestParam("token")String token){
        String user_uid=userService.getUidByToken(token);
        if(StringUtil.isEmpty(user_uid)) return RespResult.genErrorResult("token失效");
        List<MessageCenter> vers=userService.messageCenter(user_uid);
        return RespResult.genSuccessResult(vers);
    }

    /**
     * app首页10条新闻和3个自选股
     */
    @ResponseBody
    @GetMapping("/u/stock-news")
    public RespResult<Map<String, List>> stockNews(HttpServletRequest request){
        String token=request.getParameter("token");
        String user_uid=userService.getUidByToken(token);
        Map<String, List> map=new HashMap<String, List>();
        if(StringUtil.isEmpty(user_uid)){
            map.put("stocks",null);
        }else{
            List<StockUpDownVO> stocks = userService.selectedStockFromRedis(user_uid);
            if(stocks.size()>3) map.put("stocks",stocks.subList(0,3));
            else map.put("stocks",stocks);
        }
        List<News> news = userService.selectSelfStockNews();
        map.put("news",news);
        return RespResult.genSuccessResult(map);
    }

    /**
     * app首页10条新闻和3个自选股
     */
    @ResponseBody
    @GetMapping("/u/cancel-stock-warning")
    public RespResult stockNews(@RequestParam("token")String token,@RequestParam("code")String code){
        String user_uid=userService.getUidByToken(token);
        if(StringUtil.isEmpty(user_uid)) return RespResult.genErrorResult("token失效");
        if(userService.cancelStockWarning(user_uid,code)) {
            return RespResult.genSuccessResult("取消成功");
        }
        return RespResult.genErrorResult("取消失败");
    }

}
