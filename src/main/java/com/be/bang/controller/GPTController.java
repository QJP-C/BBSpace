package com.be.bang.controller;

import com.be.bang.common.R;
import com.be.bang.utils.GPTUtil;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

//@Api(tags = "GPT接口", value = "GPT接口")
@RestController
@CrossOrigin
@RequestMapping("bang/gpt")
public class GPTController {
    @PostMapping()
    public R get(@RequestBody Map<String,String> map){
        String text = GPTUtil.sendPost(map.get("msg"));
        System.out.println(text);
        return R.success(text);
//        JsonData jsonData = JsonData.bulidSuccess(text);
//        return jsonData;
    }
}
