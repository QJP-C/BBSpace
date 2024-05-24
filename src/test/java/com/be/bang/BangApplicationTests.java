package com.be.bang;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.LocalDateTimeUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.be.bang.common.R;
import com.be.bang.domain.OnlineMs;
import com.be.bang.domain.Task;
import com.be.bang.domain.User;
import com.be.bang.domain.UserFollow;
import com.be.bang.dto.PostListResDto;
import com.be.bang.dto.TaskListResDto;
import com.be.bang.dto.UserUpdate;
import com.be.bang.service.*;
import com.be.bang.utils.JwtUtil;
import io.netty.util.internal.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BangApplicationTests {
    @Autowired
    private RedisTemplate redisTemplate;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private UserService userService;

    @Resource
    private TaskService taskService;
    @Test
    void csss() {
        UserUpdate userUpdate = new UserUpdate();
        userUpdate.setPhone("191911151");
        userUpdate.setSex(0);
        User user = userService.getById("1");
        BeanUtils.copyProperties(userUpdate, user);
        boolean b = userService.updateById(user);
        System.out.println(b);
    }

    @Test
    void ss() {
        String phone = "s18119451226";
        String code = "s1611";
        redisTemplate.opsForValue().set(phone, code, 5, TimeUnit.MINUTES);
        System.out.println(redisTemplate.opsForValue().get(phone));
    }

    @Test
    void  cscw(){
        User user = new User();
        user.setId("1");
        userService.getOne(new LambdaQueryWrapper<>(user));

    }
    @Test
    void  csscw(){
        String openid = "dnsondn";
        String token = "odinsoncon";
        redisTemplate.opsForValue().set(openid,token,7, TimeUnit.DAYS);
        System.out.println(redisTemplate.opsForValue().get(openid));
    }
    @Resource
    private TaskClassService taskClassService;
//    @Test
//    void taskClass(){
//        List<TaskClass> list = taskClassService.list();
//        Map<Integer, TaskClassDto> map = new HashMap<>();
//        for (TaskClass aClass : list) {
//            //是父节点
//            if (aClass.getFather()==0){
//                TaskClassDto taskClassDto = new TaskClassDto();
//                BeanUtils.copyProperties(aClass,taskClassDto);
//                List<TaskClass> list1 = new ArrayList<>();
//                taskClassDto.setSon(list1);
//                map.put(aClass.getId(),taskClassDto);
//            }
//        }
//        for (TaskClass aClass : list) {
//            if (aClass.getFather()!=0){
//                TaskClassDto taskClassDto = map.get(aClass.getFather());
//                List<TaskClass> son = taskClassDto.getSon();
//                son.add(aClass);
//                taskClassDto.setSon(son);
//                map.put(aClass.getFather(),taskClassDto);
//            }
//        }
//
//
//        System.out.println(map);
//
//
//    }

    @Test
    void nnn(){
        User user = new User();
        user.setId("oI1vd5DC3H0lVyJizpK58ZPS9Mz8");
        user.setPhone("121161611");
        userService.updateById(user);
    }

    @Test
    void redissss(){
        String key = "topicTest";
        String cishu = "topicTest";
        boolean member = redisTemplate.opsForSet().isMember(key, cishu);
        System.out.println(member);
    }

    @Test
    void testPage(){
        //分页构造器对象
        LambdaQueryWrapper<Task> qw = new LambdaQueryWrapper<>();
        Page<Task> pageInfo = new Page<>(1, 2);
        qw.orderByDesc(Task::getReleaseTime);
        taskService.page(pageInfo,qw);
        System.out.println(pageInfo.getRecords());
        List<Task> list = pageInfo.getRecords();
//        BeanUtils.copyProperties(pageInfo,list);
        list.forEach(System.out::println);
    }
    @Test
    void rediskeyadd(){
        System.out.println(redisTemplate.opsForValue().get("ss"));
    }
    @Test
    void rediskeyadd1(){
        System.out.println(redisTemplate.opsForValue().increment("ss"));
    }
    @Test
    void rediskeyadd2(){
        System.out.println(redisTemplate.opsForValue().increment("ss",-1));
    }
    @Test
    void snovn(){
//        System.out.println(DateUtil.getZodiac(Month.JANUARY.getValue(), 28));
        String dateStr1 = "2017-03-01 22:33:23";
        Date date1 = DateUtil.parse(dateStr1);
        LocalDateTime of = LocalDateTimeUtil.of(date1);
        System.out.println(LocalDateTimeUtil.ofUTC(date1.getTime()));
    }
    @Test
    void testListSort(){
        LambdaQueryWrapper<Task> qw = new LambdaQueryWrapper<>();
        List<Task> list = taskService.list();
        list.forEach(System.out::println);
        System.out.println("================================================");
        ListUtil.sortByProperty(list, "limitTime");
        ListUtil.reverse(list);
        list.forEach(System.out::println);
    }
    @Test
    void RedisClass(){
        redisTemplate.opsForValue().set("aaa","aaaa");
        stringRedisTemplate.opsForValue().set("bbb","bbbb");
    }
    @Test
    void RedisClass1(){
        String date ="2023-04-24 19:41:08";
        Date date1 = DateUtil.parse(date);
        System.out.println(date1.getTime());
    }
    @Test
    void RedisClass21(){
        //当前 1682339809761
        System.out.println(System.currentTimeMillis());
    }
    @Test
    void RedisClass244(){

        LambdaQueryWrapper<Task> qw = new LambdaQueryWrapper<>();
        String search = "陪我";
        String typeId = null;
        qw.like(!StringUtil.isNullOrEmpty(search), Task::getLocation, search)
                .or()
                .like(!StringUtil.isNullOrEmpty(search), Task::getDetails, search)
                .or()
                .like(!StringUtil.isNullOrEmpty(search), Task::getTitle, search);

        qw.eq(!StringUtil.isNullOrEmpty(typeId), Task::getTypeId, typeId).eq(Task::getState, 1).orderByDesc(Task::getReleaseTime);
//        //分页构造器对象
//        Page<Task> pageInfo = new Page<>(page, pageSize);
//        pageInfo.setOptimizeCountSql(false);
//        Page<TaskListResDto> dtoPage = new Page<>(page, pageSize);
//        int count = this.count(qw);
//        if (count <= 0) dtoPage.setRecords(new ArrayList<TaskListResDto>());
//        this.page(pageInfo, qw);
//        BeanUtils.copyProperties(pageInfo, dtoPage, "records");
//        List<Task> list = pageInfo.getRecords();
//        List<TaskListResDto> res = list.stream().map(task -> {
//            return getTaskListResDto(openid, task);
//        }).collect(Collectors.toList());
//        dtoPage.setRecords(res);


        List<Task> taskList = taskService.list(qw);
        taskList.forEach(System.out::println);
    }
    @Test
    void RedisClass243(){
        R<Page<TaskListResDto>> taskedList = taskService.taskList("oI1vd5BUGnDzqKfkJbprGklQnIDk", null, null, 1, 5);
        Page<TaskListResDto> result = taskedList.getResult();
        List<TaskListResDto> records = result.getRecords();
        records.forEach(System.out::println);
    }
    @Resource
    OnlineMsService onlineMsService;
    @Test
    void RedisClass253(){
        onlineMsService.message("oI1vd5BUGnDzqKfkJbprGklQnIDk","oI1vd5DC3H0lVyJizpK58ZPS9Mz8","hi");
    }
    @Resource
    UserFollowService userFollowService;
    @Test
    void RedisClass257(){
        R<String> follow = userFollowService.follow("oI1vd5DC3H0lVyJizpK58ZPS9Mz8", "oI1vd5DC3H0lVyJizpK58ZPS9Mz8");
        System.out.println(follow);
    }

    @Test
    void rrrr(){
        UserFollow userFollow = new UserFollow();
        userFollow.setUserId("oI1vd5DC3H0lVyJizpK58ZPS9Mz8");
        userFollow.setFollowId("oI1vd5DC3H0lVyJizpK58ZPS9Mz8");
        userFollow.setCreateTime(LocalDateTime.now());
        userFollowService.save(userFollow);
    }

    @Test
    void fff(){
        String openid = "oI1vd5BUGnDzqKfkJbprGklQnIDk";
        LambdaQueryWrapper<OnlineMs> wrapper = new LambdaQueryWrapper<>();
        String toId ="oI1vd5DC3H0lVyJizpK58ZPS9Mz8";
        wrapper.eq(OnlineMs::getFromId, openid)
                .eq(OnlineMs::getToId, toId)
                .or()
                .eq(OnlineMs::getFromId, toId)
                .eq(OnlineMs::getToId, openid)
                .orderByAsc(OnlineMs::getSendTime);
        List<OnlineMs> list = onlineMsService.list(wrapper);
        for (OnlineMs onlineMs : list) {
            onlineMs.setIsRead(1);
        }
        onlineMsService.updateBatchById(list);
        list.forEach(System.out::println);
    }

    @Resource
    JwtUtil jwtUtil;
    @Test
    void testToken(){
        String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIyMDdjZTg5MGU0MzdmNjQ5ZmNmOWQ0Njc4MjI4MDgwYjZjNTI4YWZjMzRiZTgzOThhYTRiMThkODU5ZDU0NzIyIiwiaWF0IjoxNzE0NDY0NzYwLCJleHAiOjE3MTUwNjk1NjB9.m585VX7ULb97B7QFXh9vsxvekp4Tykb2VmOyPgbFkpDJpHufI9cV1cN-_-V0iV7f274BWNsrtzh2VQNc4KzrPg";
        String openid = jwtUtil.getOpenidFromToken(token);
        System.out.println(openid);
    }

    @Resource
    PostService postService;
    @Test
    public void test() {
        R<Page<PostListResDto>> r = postService.queryPostOfImageText("18119451226", 1, 5, null);
        Page<PostListResDto> result = r.getResult();
        List<PostListResDto> records = result.getRecords();
        System.out.printf("=-=============");
        for (PostListResDto record : records) {
            System.out.println(record);
        }
    }

    @Test
    public void dvsdv(){
        R<Page<PostListResDto>> r = postService.newQueryPostOfFollow("18671682176", 1, 5);
//        System.out.printf(r.getMessage());
        Page<PostListResDto> result = r.getResult();
        List<PostListResDto> records = result.getRecords();
        for (PostListResDto record : records) {
            System.out.println(record);
        }
    }
}


