package com.be.bang.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.be.bang.common.R;
import com.be.bang.domain.User;
import com.be.bang.domain.UserFollow;
import com.be.bang.dto.UserInfo;
import com.be.bang.dto.UserMyInfo;
import com.be.bang.dto.UserUpdate;
import com.be.bang.mapper.UserMapper;
import com.be.bang.service.PostLikeService;
import com.be.bang.service.SendSms;
import com.be.bang.service.UserFollowService;
import com.be.bang.service.UserService;
import com.be.bang.utils.EncryptUtil;
import com.be.bang.utils.GetUserInfoUtil;
import com.be.bang.utils.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.be.bang.common.Constants.REDIS_LOGIN_KEY;
import static com.be.bang.common.Constants.REDIS_USERCACHE_KEY;

/**
 * (User)表服务实现类
 *
 * @author makejava
 * @since 2023-04-13 16:44:27
 */
@Service
@Slf4j
@CacheConfig(cacheNames = REDIS_USERCACHE_KEY)
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Resource
    JwtUtil jwtUtil;

    @Resource
    SendSms sendSms;

    @Resource
    StringRedisTemplate stringRedisTemplate;

    @Resource
    PostLikeService postLikeService;

    @Resource
    private UserFollowService userFollowService;

    /**
     * 微信登陆
     *
     * @param code
     * @return
     */
    @Override
    public R wxLogin(String code) {
        //通过code换取信息
        JSONObject resultJson = GetUserInfoUtil.getResultJson(code);

        if (resultJson.has("openid")) {
            //获取sessionKey和openId
            String sessionKey = resultJson.get("session_key").toString();
            String openid = resultJson.get("openid").toString();
            System.out.println(openid);
            //生成自定义登录态session
            String session = null;
            Map<String, String> sessionMap = new HashMap<>();

            sessionMap.put("sessionKey", sessionKey);
            sessionMap.put("openid", openid);
            session = JSONObject.fromObject(sessionMap).toString();

            //加密session
            try {
                EncryptUtil encryptUtil = new EncryptUtil();
                session = encryptUtil.encrypt(session);
            } catch (Exception e) {
                e.printStackTrace();
            }

            //生成token
            String token = jwtUtil.getToken(session);
            stringRedisTemplate.opsForValue().set(REDIS_LOGIN_KEY + openid, token, 7, TimeUnit.DAYS);
            Map<String, String> result = new HashMap<>();
            result.put("token", token);

            //查询用户是否存在
            User owner = this.getById(openid);
            if (owner == null) { //用户不存在，新建用户信息
                User user = new User();
                user.setId(openid);
                user.setSex(0);
                String substring = openid.substring(0, 8);
                user.setUsername("帮帮用户" + substring);
                user.setHead("https://qjpqjp.top/file/bbspace/photo/default.png");
                user.setSignature("这个用户懒且不够个性，暂时没有个性签名");
                boolean rs = this.save(user);
                if (!rs) {
                    log.info("用户登陆失败 id: {}", openid);
                    return R.error("登录失败");
                }
            }
            log.info("用户登陆 id: {}", openid);
            return R.success(result, "登录成功"); //用户存在，返回结果
        }
        log.info("用户授权失败");
        return R.error("授权失败" + resultJson.getString("errmsg"));
    }

    /**
     * h5登陆
     *
     * @param phone
     * @return
     */
    @Override
    public R h5Login(String phone, String code) {

        if (StrUtil.isBlank(phone)||StrUtil.isBlank(code)) {
            return R.error("手机号不能为空");
        }

        //校验验证码
        String codeInRedis = stringRedisTemplate.opsForValue().get(phone);
        if (!code.equals(codeInRedis)) {
            return R.error("验证码错误");
        }

        //生成自定义登录态session
        String session = null;
        Map<String, String> sessionMap = new HashMap<>();
        sessionMap.put("openid", phone);

        session = JSONObject.fromObject(sessionMap).toString();

        //加密session
        try {
            EncryptUtil encryptUtil = new EncryptUtil();
            session = encryptUtil.encrypt(session);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //生成token
        String token = jwtUtil.getToken(session);
        stringRedisTemplate.opsForValue().set(REDIS_LOGIN_KEY + phone, token, 7, TimeUnit.DAYS);
        Map<String, String> result = new HashMap<>();
        result.put("token", token);

        //查询用户是否存在
        User owner = this.getById(phone);
        if (owner == null) { //用户不存在，新建用户信息
            User user = new User();
            user.setId(phone);
            user.setSex(0);
            user.setPhone(phone);
            user.setUsername("帮帮用户" + phone);
            user.setHead("https://qjpqjp.top/file/bbspace/photo/default.png");
            user.setSignature("这个用户懒且不够个性，暂时没有个性签名");
            boolean rs = this.save(user);
            if (!rs) {
                log.info("用户登陆失败 id: {}", phone);
                return R.error("登录失败");
            }
        }
        log.info("用户登陆 id: {}", phone);
        return R.success(result, "登录成功"); //用户存在，返回结果
    }


    /**
     * 修改用户信息
     *
     * @param userUpdate
     * @param openid
     * @return
     */
    @Override
    public R updateInfo(UserUpdate userUpdate, String openid) {
        User user = this.getById(openid);
        BeanUtils.copyProperties(userUpdate, user);
        boolean b = this.updateById(user);
        return b ? R.success("修改成功") : R.error("修改失败");
    }

    /**
     * 发送短信验证码
     *
     * @param phone
     * @return
     */
    @Override
    public R send(String phone) {
        boolean b = sendSms.addSendSms(phone);
        if (!b) {
            return R.error("发送失败,请检查您的手机号是否填写正确!");
        }

        return R.success("发送成功!");
    }

    /**
     * 校验验证码
     *
     * @param openid
     * @param phone
     * @param code
     * @return
     */
    @Override
    public R<String> check(String openid, String phone, String code) {
        String codeInRedis = stringRedisTemplate.opsForValue().get(phone);
        User user = new User();
        user.setId(openid);
        user.setPhone(phone);
        this.updateById(user);
        return codeInRedis.equals(code) ? R.success("验证码正确") : R.error("验证码错误");
    }

    /**
     * 个人信息
     *
     * @param id
     * @return
     */


    @Override
    public R<UserMyInfo> myInfo(String id) {
        User user = this.getById(id);
        UserMyInfo userMyInfo = new UserMyInfo();
        //获取被赞数
        Long likeNum = postLikeService.getUserLikeNum(id);
        userMyInfo.setNice(likeNum);
        //获取关注数
        Long followNum = userFollowService.userFollowNum(id);
        userMyInfo.setFollow(followNum);
        //获取粉丝数
        Long fansNum = userFollowService.userFansNum(id);
        userMyInfo.setFans(fansNum);
        BeanUtils.copyProperties(user, userMyInfo);
        return R.success(userMyInfo);
    }

    /**
     * 他人信息
     *
     * @param myOpenid
     * @param toOpenid
     * @return
     */

    @Override
    public R<UserInfo> otherInfo(String myOpenid, String toOpenid) {
        boolean b = haveOne(toOpenid);
        if (!b) {
            return R.error("不存在该用户");
        }
        User user = this.getById(toOpenid);
        UserInfo userInfo = new UserInfo();
        BeanUtils.copyProperties(user, userInfo);
        LambdaQueryWrapper<UserFollow> qw = new LambdaQueryWrapper<>();
        qw.eq(UserFollow::getFollowId, toOpenid);
        qw.eq(UserFollow::getUserId, myOpenid);
        //用户是否关注他
        int count = userFollowService.count(qw);
        if (count > 0) {
            userInfo.setIsFollow(true);
        }
        //获取粉丝数
        Long fansNum = userFollowService.userFansNum(toOpenid);
        userInfo.setFans(fansNum);
        //获取被赞数
        Long likeNum = postLikeService.getUserLikeNum(toOpenid);
        userInfo.setNice(likeNum);
        //获取关注数
        Long followNum = userFollowService.userFollowNum(toOpenid);
        userInfo.setFollow(followNum);
        return R.success(userInfo);
    }

    /**
     * 获取用户资料
     *
     * @param userId
     * @return
     */
    @Override
    public Map<String, String> getOneInfo(String userId) {
        User user = this.getById(userId);
        Map<String, String> res = new HashMap<String, String>();
        res.put("head", user.getHead());
        res.put("username", user.getUsername());
        return res;
    }

    /**
     * 查询是否有该用户
     *
     * @param id
     * @return
     */
    @Override
    public boolean haveOne(String id) {
        LambdaQueryWrapper<User> qw = new LambdaQueryWrapper<>();
        qw.eq(User::getId, id);
        //是否有这个人
        int count = this.count(qw);
        return count > 0;
    }

}

