package com.teng.mai.model.vo;

import com.teng.mai.model.enums.CommentTypeEnum;
import com.teng.mai.model.enums.UserGenderEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @description:
 * @author: ~Teng~
 * @date: 2024/3/23 15:25
 */
@Data
@ApiModel("评论VO")
@AllArgsConstructor
@NoArgsConstructor
public class CommentVO implements Serializable {
    @ApiModelProperty("主键id")
    private Long id;
    @ApiModelProperty("评论用户id")
    private Long userId;
    @ApiModelProperty("评论用户账号")
    private String username;
    @ApiModelProperty("性别：0-男性，1-女性")
    private UserGenderEnum userGender;
    @ApiModelProperty("用户头像")
    private String userAvatar;
    @ApiModelProperty("评论内容")
    private String commentContent;
    @ApiModelProperty("评论时间")
    private LocalDateTime createTime;
    @ApiModelProperty("子评论")
    private List<ReplyVO> replyVOS;
}
