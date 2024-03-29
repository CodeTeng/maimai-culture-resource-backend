package com.teng.mai.controller;

import com.teng.mai.common.BaseResponse;
import com.teng.mai.common.ResultUtils;
import com.teng.mai.model.entity.Tag;
import com.teng.mai.service.TagService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @description:
 * @author: ~Teng~
 * @date: 2024/3/28 14:37
 */
@RestController
@RequestMapping("/tags")
@Api(tags = "标签管理模块")
public class TagController {
    @Resource
    private TagService tagService;

    @GetMapping("/getTagList")
    @ApiOperation("获取所有标签")
    public BaseResponse<List<Tag>> getTagList() {
        return ResultUtils.success(tagService.list());
    }
}
