package com.ruoyi.bang.service.impl;


import com.ruoyi.bang.service.SendSms;
import com.ruoyi.bang.utils.SendSmsUtil;
import com.ruoyi.bang.utils.ValidateCodeUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

/**
 * @author qjp
 */
@Service
public class SendSmsImpl implements SendSms {
    @Resource
    StringRedisTemplate stringRedisTemplate;

    /**
     * 生成存储并发送验证码
     * @param phone
     * @return
     */
    @Override
    public boolean addSendSms(String phone) {
        String code = ValidateCodeUtils.generateValidateCode(4).toString();
        HashMap<String, Object> map = new HashMap<>();
        map.put("code",code);
        //调用方法发送信息 传入电话，模板，验证码   SMS_255290290   SMS_251070336
        boolean send = SendSmsUtil.addSendSms(phone, map);
        //返回ture则发送成功
        if (send){
            //存入redis中并设置过期时间，这里设置5分钟过期
            stringRedisTemplate.opsForValue().set(phone,code,5, TimeUnit.MINUTES);
            return true;
        }else {
            //返回false则发送失败
            return false;
        }
    }
}

