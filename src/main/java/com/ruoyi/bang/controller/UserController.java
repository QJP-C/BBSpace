package com.ruoyi.bang.controller;


import com.ruoyi.bang.common.R;
import com.ruoyi.bang.dto.Check;
import com.ruoyi.bang.dto.UserInfo;
import com.ruoyi.bang.dto.UserMyInfo;
import com.ruoyi.bang.dto.UserUpdate;
import com.ruoyi.bang.service.UserFollowService;
import com.ruoyi.bang.service.UserService;
import com.ruoyi.bang.utils.JwtUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.constraints.NotBlank;
import java.util.Map;

/**
 * (User)表控制层
 *
 * @author makejava
 * @since 2023-04-13 16:44:27
 */
@Api(tags = "用户相关接口", value = "用户相关接口")
@RestController
@CrossOrigin
@RequestMapping("bang/user")
public class UserController {
    /**
     * 服务对象
     */
    @Resource
    private UserService userService;
    @Resource
    private UserFollowService userFollowService;
    @Resource
    JwtUtil jwtUtil;

    @ApiOperation("微信授权登录")
    @PostMapping("login")
    public R wxLogin(@NotBlank @RequestBody Map<String,String> map) {
        return userService.wxLogin(map.get("code"));
    }



    @ApiOperation("用户资料修改")
    @PutMapping("updateInfo")
    public R updateInfo(@RequestBody UserUpdate userUpdate, @RequestHeader("Authorization") String header) {
        String openid = jwtUtil.getOpenidFromToken(header);
        return userService.updateInfo(userUpdate, openid);
    }

    @ApiOperation("发送短信验证码")
    @GetMapping("send")
    public R send(@NotBlank String phone) {
        return userService.send(phone);
    }

    @ApiOperation("校验验证码")
    @PostMapping("check")
    public R<String> check(@RequestHeader("Authorization") String header,@NotBlank @RequestBody Check check) {
        String openid = jwtUtil.getOpenidFromToken(header);
        return userService.check(openid,check.getPhone(), check.getCode());
    }

    @ApiOperation("个人信息")
    @GetMapping("person")
    public R<UserMyInfo> myInfo(@RequestHeader("Authorization") String header){
        String openid = jwtUtil.getOpenidFromToken(header);
        return userService.myInfo(openid);
    }

    @ApiOperation("他人信息")
    @GetMapping("other")
    public R<UserInfo> otherInfo(@RequestHeader("Authorization") String header, @RequestParam String toOpenid){
        String myOpenid = jwtUtil.getOpenidFromToken(header);
        return userService.otherInfo(myOpenid,toOpenid);
    }
    @ApiOperation("关注取关")
    @GetMapping("follow/{toId}")
    public R<String> follow(@RequestHeader("Authorization") String header,
                            @PathVariable("toId")String toId){
        String openid = jwtUtil.getOpenidFromToken(header);
        return userFollowService.follow(toId,openid);
    }


    @ApiOperation("我的关注")
    @GetMapping("myFollow")
    public R myFollow(@RequestHeader("Authorization") String header){
        String openid = jwtUtil.getOpenidFromToken(header);
        return userFollowService.myFollow(openid);
    }
    @ApiOperation("我的粉丝")
    @GetMapping("myFans")
    public R myFans(@RequestHeader("Authorization") String header){
        String openid = jwtUtil.getOpenidFromToken(header);
        return userFollowService.myFans(openid);
    }
}

