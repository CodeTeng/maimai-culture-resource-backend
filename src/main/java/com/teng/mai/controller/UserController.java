package com.teng.mai.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.teng.mai.common.BaseResponse;
import com.teng.mai.common.ErrorCode;
import com.teng.mai.common.ResultUtils;
import com.teng.mai.common.page.PageVO;
import com.teng.mai.exception.BusinessException;
import com.teng.mai.exception.ThrowUtils;
import com.teng.mai.model.dto.*;
import com.teng.mai.model.entity.*;
import com.teng.mai.model.enums.FriendsStatusEnum;
import com.teng.mai.model.vo.UserVO;
import com.teng.mai.service.*;
import com.teng.mai.utils.BeanCopyUtils;
import com.teng.mai.utils.CollUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;

/**
 * @description:
 * @author: ~Teng~
 * @date: 2024/3/22 21:47
 */
@RestController
@RequestMapping("/users")
@Api(tags = "用户管理模块")
@Slf4j
public class UserController {
    @Resource
    private UserService userService;
    @Resource
    private UserFollowService userFollowService;
    @Resource
    private ArticleService articleService;
    @Resource
    private FriendsService friendsService;
    @Resource
    private CommentService commentService;

    @PostMapping("/register")
    @ApiOperation("用户注册")
    public BaseResponse<Boolean> userRegister(@RequestBody @Validated UserRegisterDTO userRegisterDTO) {
        ThrowUtils.throwIf(userRegisterDTO == null, ErrorCode.PARAMS_ERROR);
        // 密码和校验密码相同
        if (!userRegisterDTO.getUserPassword().equals(userRegisterDTO.getCheckPassword())) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "两次输入的密码不一致");
        }
        userService.userRegister(userRegisterDTO);
        return ResultUtils.success(true);
    }

    @PostMapping("/login")
    @ApiOperation("用户登录")
    public BaseResponse<UserVO> userLogin(@RequestBody @Validated UserLoginDTO userLoginDTO) {
        ThrowUtils.throwIf(userLoginDTO == null, ErrorCode.PARAMS_ERROR);
        UserVO userVO = userService.userLogin(userLoginDTO);
        return ResultUtils.success(userVO);
    }

    @GetMapping("/current")
    @ApiOperation("获取当前登录用户")
    public BaseResponse<UserVO> getCurrentUser() {
        UserVO currentUser = userService.getCurrentUser();
        return ResultUtils.success(currentUser);
    }

    @PutMapping("/updateMyInfo")
    @ApiOperation("修改我的个人信息")
    public BaseResponse<Boolean> updateMyInfo(@RequestBody @Validated UserUpdateDTO userUpdateDTO) {
        ThrowUtils.throwIf(userUpdateDTO == null, ErrorCode.PARAMS_ERROR);
        userService.updateMyInfo(userUpdateDTO);
        return ResultUtils.success(true);
    }

    @PutMapping("/update/myPwd")
    @ApiOperation("当前用户个人修改密码")
    public BaseResponse<Boolean> updateUserMyPwd(@RequestBody @Validated UserUpdateMyPwdDTO userUpdateMyPwdDTO) {
        ThrowUtils.throwIf(userUpdateMyPwdDTO == null, ErrorCode.PARAMS_ERROR);
        if (!userUpdateMyPwdDTO.getUserPassword().equals(userUpdateMyPwdDTO.getCheckPassword())) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "两次输入的密码不一致");
        }
        userService.updateUserMyPwd(userUpdateMyPwdDTO);
        return ResultUtils.success(true);
    }

    @GetMapping("/getUserDetail/{id}")
    @ApiOperation("查看用户")
    public BaseResponse<UserVO> getUserDetail(@PathVariable("id") Long id) {
        ThrowUtils.throwIf(id == null || id <= 0, ErrorCode.PARAMS_ERROR);
        User user = userService.getById(id);
        if (user == null) {
            throw new BusinessException(ErrorCode.USER_NOT_EXISTS);
        }
        UserVO userVO = BeanCopyUtils.copyObject(user, UserVO.class);
        UserVO currentUser = userService.getCurrentUser();
        long count = userFollowService.count(new LambdaQueryWrapper<UserFollow>()
                .eq(UserFollow::getUserId, currentUser.getId())
                .eq(UserFollow::getUserFollowId, id));
        if (count > 0) {
            userVO.setFollowed(true);
        }
        return ResultUtils.success(userVO);
    }

    @GetMapping("/folOrCancelFol/{id}")
    @ApiOperation("关注或取消关注用户")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "用户id", required = true, paramType = "path", dataType = "Long"),
            @ApiImplicitParam(name = "type", value = "0-关注 1-取消关注", required = true, paramType = "query", dataType = "Long")
    })
    public BaseResponse<Boolean> folOrCancelFol(@PathVariable("id") Long id, @RequestParam("type") Long type) {
        ThrowUtils.throwIf(id == null || id <= 0, ErrorCode.PARAMS_ERROR);
        userService.folOrCancelFol(id, type);
        return ResultUtils.success(true);
    }

    @PostMapping("/getMyFolUser")
    @ApiOperation("分页查看我关注的用户")
    public BaseResponse<PageVO<UserVO>> getMyFolUser(@RequestBody @Validated FolSearchDTO folSearchDTO) {
        ThrowUtils.throwIf(folSearchDTO == null, ErrorCode.PARAMS_ERROR);
        PageVO<UserVO> pageVO = userService.getMyFolUser(folSearchDTO);
        return ResultUtils.success(pageVO);
    }

    @PostMapping("/getFolMyUser")
    @ApiOperation("分页查看关注我的用户")
    public BaseResponse<PageVO<UserVO>> getFolMyUser(@RequestBody @Validated FolSearchDTO folSearchDTO) {
        ThrowUtils.throwIf(folSearchDTO == null, ErrorCode.PARAMS_ERROR);
        PageVO<UserVO> pageVO = userService.getFolMyUser(folSearchDTO);
        return ResultUtils.success(pageVO);
    }

    @GetMapping("/myFavCount")
    @ApiOperation("关注人员数量")
    @ApiImplicitParam(name = "type", value = "0-我关注的人员数量 1-别人关注我的人员数量", required = true, paramType = "query", dataType = "Long")
    public BaseResponse<Long> myFavCount(Long type) {
        ThrowUtils.throwIf(type == null, ErrorCode.PARAMS_ERROR);
        Long count = userService.favMyCount(type);
        return ResultUtils.success(count);
    }

    @GetMapping("/myFriendsCount")
    @ApiOperation("我的好友数量")
    public BaseResponse<Long> myFriendsCount() {
        Long userId = userService.getCurrentUser().getId();
        List<Friends> list1 = friendsService.list(new LambdaQueryWrapper<Friends>()
                .eq(Friends::getStatus, FriendsStatusEnum.PASSED)
                .eq(Friends::getFromId, userId));
        List<Friends> list2 = friendsService.list(new LambdaQueryWrapper<Friends>()
                .eq(Friends::getStatus, FriendsStatusEnum.PASSED)
                .eq(Friends::getReceiveId, userId));
        Set<Friends> friendsSet = CollUtils.unionDistinct(list1, list2);
        return ResultUtils.success((long) friendsSet.size());
    }

    @GetMapping("/getMyCommentCount")
    @ApiOperation("查看我评论的数量")
    public BaseResponse<Long> getMyCommentCount() {
        UserVO user = userService.getCurrentUser();
        Long userId = user.getId();
        long count = commentService.count(new LambdaQueryWrapper<Comment>().eq(Comment::getUserId, userId));
        return ResultUtils.success(count);
    }
}
