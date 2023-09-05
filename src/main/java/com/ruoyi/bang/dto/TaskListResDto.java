package com.ruoyi.bang.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@ApiModel("任务列表响应")
public class TaskListResDto {
    private String id;
    //用户头像
    @ApiModelProperty("用户头像")
    private String head;
    //用户头像
    @ApiModelProperty("用户昵称")
    private String username;
    //任务标题
    @ApiModelProperty("任务标题")
    private String title;
    //任务详情
    @ApiModelProperty("任务详情")
    private String details;
    //地址
    @ApiModelProperty("地址")
    private String location;
    //截止时间
    @ApiModelProperty("截止时间")
    private LocalDateTime limitTime;
    //是否收藏   1:已收藏  2:未收藏
    @ApiModelProperty("是否收藏   1:已收藏  2:未收藏")
    private Integer isCollect;
    //类型id
    @ApiModelProperty("类型id")
    private String typeId;
    //类型id
    @ApiModelProperty("类型名称")
    private String type;
    //赏金
    @ApiModelProperty("赏金")
    private Double money;
    //任务状态  0:待审核  1:已发布  2:已接取  3:已完成   4:已逾期
    @ApiModelProperty("任务状态  0:待审核  1:已发布  2:已接取  3:已完成   4:已逾期")
    private Integer state;
}
