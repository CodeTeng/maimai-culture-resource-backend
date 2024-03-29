package com.teng.mai.controller;

import com.teng.mai.common.BaseResponse;
import com.teng.mai.common.ErrorCode;
import com.teng.mai.common.ResultUtils;
import com.teng.mai.exception.BusinessException;
import com.teng.mai.exception.ThrowUtils;
import com.teng.mai.model.dto.FriendAddDTO;
import com.teng.mai.model.vo.FriendsRecordVO;
import com.teng.mai.service.FriendsService;
import com.teng.mai.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;

/**
 * @description:
 * @author: ~Teng~
 * @date: 2024/3/23 21:56
 */
@RestController
@RequestMapping("/friends")
@Api(tags = "好友管理模块")
public class FriendsController {
    @Resource
    private FriendsService friendsService;
    @Resource
    private UserService userService;

    @PostMapping("/add")
    @ApiOperation("发送好友申请")
    public BaseResponse<Boolean> addFriendRecords(@RequestBody @Validated FriendAddDTO friendAddDTO) {
        ThrowUtils.throwIf(friendAddDTO == null, ErrorCode.PARAMS_ERROR);
        if (StringUtils.isNotBlank(friendAddDTO.getRemark()) && friendAddDTO.getRemark().length() > 35) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "申请备注最多35个字符");
        }
        Long fromId = userService.getCurrentUser().getId();
        friendsService.addFriendRecords(fromId, friendAddDTO);
        return ResultUtils.success(true);
    }

    @GetMapping("/getMyFriends")
    @ApiOperation("查看我的好友")
    public BaseResponse<List<FriendsRecordVO>> getMyFriends() {
        List<FriendsRecordVO> list = friendsService.getMyFriends();
        return ResultUtils.success(list);
    }

    @GetMapping("/getFromList")
    @ApiOperation("查看我发送的好友申请")
    public BaseResponse<List<FriendsRecordVO>> getFromList() {
        Long userId = userService.getCurrentUser().getId();
        List<FriendsRecordVO> fromList = friendsService.getFromList(userId);
        return ResultUtils.success(fromList);
    }

    @GetMapping("/getReceiveList")
    @ApiOperation("查看我收到的好友申请")
    public BaseResponse<List<FriendsRecordVO>> getReceiveList() {
        Long userId = userService.getCurrentUser().getId();
        List<FriendsRecordVO> receiveList = friendsService.getReceiveList(userId);
        return ResultUtils.success(receiveList);
    }

    @GetMapping("/getNoReadCount")
    @ApiOperation("查看我的未读消息")
    public BaseResponse<Long> getNoReadCount() {
        Long userId = userService.getCurrentUser().getId();
        Long count = friendsService.getNoReadCount(userId);
        return ResultUtils.success(count);
    }

    @PostMapping("/agree/{fromId}")
    @ApiOperation("同意好友申请")
    public BaseResponse<Boolean> agreeToApply(@PathVariable("fromId") Long fromId) {
        ThrowUtils.throwIf(fromId == null || fromId <= 0, ErrorCode.PARAMS_ERROR);
        Long userId = userService.getCurrentUser().getId();
        friendsService.agreeToApply(userId, fromId);
        return ResultUtils.success(true);
    }

    @PostMapping("/canceledApply/{id}")
    @ApiOperation("撤销申请")
    public BaseResponse<Boolean> canceledApply(@PathVariable("id") Long id) {
        ThrowUtils.throwIf(id == null || id <= 0, ErrorCode.PARAMS_ERROR);
        friendsService.canceledApply(id);
        return ResultUtils.success(true);
    }

    @GetMapping("/read")
    @ApiOperation("已读(全部已读)")
    public BaseResponse<Boolean> toRead(@RequestParam(required = false, value = "ids") Set<Long> ids) {
        ThrowUtils.throwIf(CollectionUtils.isEmpty(ids), ErrorCode.PARAMS_ERROR);
        friendsService.toRead(ids);
        return ResultUtils.success(true);
    }

    @DeleteMapping("/deleteFriend/{id}")
    @ApiOperation("删除好友")
    public BaseResponse<Boolean> deleteFriend(@PathVariable("id") Long id) {
        ThrowUtils.throwIf(id == null || id <= 0, ErrorCode.PARAMS_ERROR);
        friendsService.deleteFriend(id);
        return ResultUtils.success(true);
    }
}
