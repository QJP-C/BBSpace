package com.be.bang.dto;

import com.be.bang.domain.Topic;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class PersonalTopicDto {
    private List<Topic> res;
    @ApiModelProperty("关注话题数")
    private Integer followNum;

}
