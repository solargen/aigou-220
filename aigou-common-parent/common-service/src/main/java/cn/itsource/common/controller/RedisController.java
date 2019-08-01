package cn.itsource.common.controller;

import cn.itsource.basic.util.AjaxResult;
import cn.itsource.basic.util.RedisUtils;
import cn.itsource.common.client.RedisClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RedisController implements RedisClient{

    /**
     * 缓存数据
     * @param key
     * @param value
     * @return
     */
    @PostMapping("/redis")
    public AjaxResult set(@RequestParam("key")String key,@RequestParam("value") String value){
        try {
            RedisUtils.INSTANCE.set(key,value);
            return AjaxResult.me().setSuccess(true).setMessage("保存成功！");
        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResult.me().setSuccess(false).setMessage("保存失败！"+e.getMessage());
        }

    }

    /**
     * 获取缓存数据
     * @param key
     * @return
     */
    @GetMapping("/redis")
    public AjaxResult get(@RequestParam("key")String key){
        try {
            String value = RedisUtils.INSTANCE.get(key);
            return AjaxResult.me().setSuccess(true).setMessage("成功").setRestObj(value);
        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResult.me().setSuccess(false).setMessage("系统异常!"+e.getMessage());
        }
    }

}
