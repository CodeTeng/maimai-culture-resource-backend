package com.teng.mai.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.teng.mai.common.BaseResponse;
import com.teng.mai.common.ErrorCode;
import com.teng.mai.common.ResultUtils;
import com.teng.mai.common.page.PageVO;
import com.teng.mai.exception.ThrowUtils;
import com.teng.mai.model.dto.ArticleSearchDTO;
import com.teng.mai.model.entity.Article;
import com.teng.mai.model.entity.ArticleFav;
import com.teng.mai.model.vo.ArticleVO;
import com.teng.mai.service.ArticleFavService;
import com.teng.mai.service.ArticleService;
import com.teng.mai.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @description:
 * @author: ~Teng~
 * @date: 2024/3/22 23:33
 */
@RestController
@RequestMapping("/articles")
@Api(tags = "文化资源管理模块")
public class ArticleController {
    @Resource
    private ArticleService articleService;
    @Resource
    private UserService userService;
    @Resource
    private ArticleFavService articleFavService;

    @GetMapping("/searchArticle")
    @ApiOperation("根据条件查询文章")
    public BaseResponse<List<ArticleVO>> searchArticle(@Validated ArticleSearchDTO articleSearchDTO) {
        ThrowUtils.throwIf(articleSearchDTO == null, ErrorCode.PARAMS_ERROR);
        List<ArticleVO> articlePageVO = articleService.searchArticle(articleSearchDTO);
        return ResultUtils.success(articlePageVO);
    }

    @GetMapping("/getArticleDetail/{id}")
    @ApiOperation("浏览文章")
    public BaseResponse<ArticleVO> getArticleDetail(@PathVariable("id") Long id) {
        ThrowUtils.throwIf(id == null || id <= 0, ErrorCode.PARAMS_ERROR);
        ArticleVO articleVO = articleService.getArticleDetail(id);
        return ResultUtils.success(articleVO);
    }

    @GetMapping("/favOrCancelFav/{id}")
    @ApiOperation("收藏或取消收藏文章")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "文章id", required = true, paramType = "path", dataType = "Long", example = "1"),
            @ApiImplicitParam(name = "type", value = "0-收藏 1-取消收藏", required = true, paramType = "query", dataType = "Long")
    })
    public BaseResponse<Boolean> favOrCancelFav(@PathVariable("id") Long id, @RequestParam("type") Long type) {
        ThrowUtils.throwIf(id == null || id <= 0, ErrorCode.PARAMS_ERROR);
        articleService.favOrCancelFav(id, type);
        return ResultUtils.success(true);
    }

    @GetMapping("/getMyFavArticle")
    @ApiOperation("查看我收藏的文章")
    public BaseResponse<List<ArticleVO>> getMyFavArticle(@Validated ArticleSearchDTO articleSearchDTO) {
        ThrowUtils.throwIf(articleSearchDTO == null, ErrorCode.PARAMS_ERROR);
        List<ArticleVO> articleVOPageVO = articleService.getMyFavArticle(articleSearchDTO);
        return ResultUtils.success(articleVOPageVO);
    }

    @GetMapping("/myFavArticleCount")
    @ApiOperation("查看我收藏的文章数量")
    public BaseResponse<Long> myArticleCount() {
        Long userId = userService.getCurrentUser().getId();
        long count = articleFavService.count(new LambdaQueryWrapper<ArticleFav>().eq(ArticleFav::getUserId, userId));
        return ResultUtils.success(count);
    }
}
