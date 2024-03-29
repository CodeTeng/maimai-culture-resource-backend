package com.teng.mai.model.vo;

import com.teng.mai.model.enums.ArticleTypeEnum;
import com.teng.mai.model.enums.UserGenderEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @description:
 * @author: ~Teng~
 * @date: 2024/3/23 0:28
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("文章VO")
public class ArticleVO implements Serializable {
    @ApiModelProperty("主键id")
    private Long id;
    @ApiModelProperty("内容")
    private String articleContent;
    @ApiModelProperty("封面图")
    private String articleCover;
    @ApiModelProperty("标题")
    private String articleTitle;
    @ApiModelProperty("摘要 如果该字段为空，默认取文章的前500个字符作为摘要")
    private String articleAbstract;
    @ApiModelProperty("最新的评论id")
    private Long latestReplyId;
    @ApiModelProperty("评论数量")
    private Integer replyTimes;
    @ApiModelProperty("收藏数量")
    private Integer favTimes;
    @ApiModelProperty("是否被收藏 false-未收藏 true-已收藏")
    private Boolean favStatus = Boolean.FALSE;
    @ApiModelProperty("类型 1-文章 2-视频")
    private ArticleTypeEnum type;
    @ApiModelProperty("视频地址")
    private String url;
    @ApiModelProperty("发表时间")
    private LocalDateTime createTime;

    @ApiModelProperty("作者id")
    private Long userId;
    @ApiModelProperty("作者账号")
    private String username;
    @ApiModelProperty("性别：0-男性，1-女性")
    private UserGenderEnum userGender;
    @ApiModelProperty("用户头像")
    private String userAvatar;
}
