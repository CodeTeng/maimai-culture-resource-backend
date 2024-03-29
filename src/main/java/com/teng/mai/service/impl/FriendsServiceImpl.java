package com.teng.mai.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.teng.mai.common.ErrorCode;
import com.teng.mai.exception.BusinessException;
import com.teng.mai.mapper.UserFollowMapper;
import com.teng.mai.mapper.UserMapper;
import com.teng.mai.model.dto.FriendAddDTO;
import com.teng.mai.model.entity.Friends;
import com.teng.mai.model.entity.User;
import com.teng.mai.model.entity.UserFollow;
import com.teng.mai.model.enums.FriendsStatusEnum;
import com.teng.mai.model.vo.FriendsRecordVO;
import com.teng.mai.model.vo.UserVO;
import com.teng.mai.service.FriendsService;
import com.teng.mai.mapper.FriendsMapper;
import com.teng.mai.service.UserService;
import com.teng.mai.utils.BeanCopyUtils;
import com.teng.mai.utils.CollUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;


/**
 * @author teng
 * @description 针对表【friends(好友申请管理表)】的数据库操作Service实现
 * @createDate 2024-03-23 21:55:22
 */
@Service
public class FriendsServiceImpl extends ServiceImpl<FriendsMapper, Friends>
        implements FriendsService {
    @Resource
    private UserMapper userMapper;
    @Resource
    private UserFollowMapper userFollowMapper;
    @Resource
    private UserService userService;

    @Override
    public void addFriendRecords(Long fromId, FriendAddDTO friendAddDTO) {
        // 1. 判断用户是否存在
        String username = friendAddDTO.getUsername();
        User user = userService.getOne(new LambdaQueryWrapper<User>().eq(User::getUsername, username));
        if (user == null) {
            throw new BusinessException(ErrorCode.USER_NOT_EXISTS);
        }
        // 2. 不能添加自己为好友
        if (fromId.equals(user.getId())) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "不能添加自己为好友");
        }
        // 3. 一个人只能申请一次 不能重复申请
        Long count = lambdaQuery()
                .eq(Friends::getReceiveId, user.getId())
                .eq(Friends::getFromId, fromId)
                .in(Friends::getStatus, FriendsStatusEnum.NOT_PASSED, FriendsStatusEnum.EXPIRED).count();
        if (count > 0) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "只能申请加好友一次");
        }
        // 4. 不能重复添加好友
        count = lambdaQuery()
                .eq(Friends::getReceiveId, user.getId())
                .eq(Friends::getFromId, fromId)
                .eq(Friends::getStatus, FriendsStatusEnum.PASSED).count();
        if (count > 0) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "你们已经是好友了");
        }
        // 5. 发起好友申请
        Friends friends = new Friends();
        friends.setFromId(fromId);
        friends.setReceiveId(user.getId());
        if (StringUtils.isBlank(friendAddDTO.getRemark())) {
            friends.setRemark("我是" + userMapper.selectById(fromId).getUsername());
        } else {
            friends.setRemark(friendAddDTO.getRemark());
        }
        this.save(friends);

//        // 1. 不能添加自己
//        if (fromId.equals(friendAddDTO.getReceiveId())) {
//            throw new BusinessException(ErrorCode.OPERATION_ERROR, "不能添加自己为好友");
//        }
//        // 2. 一个人只能申请一次 不能重复申请
//        Long count = lambdaQuery()
//                .eq(Friends::getReceiveId, receiveId)
//                .eq(Friends::getFromId, fromId)
//                .in(Friends::getStatus, FriendsStatusEnum.NOT_PASSED, FriendsStatusEnum.EXPIRED).count();
//        if (count > 0) {
//            throw new BusinessException(ErrorCode.OPERATION_ERROR, "只能申请加好友一次");
//        }
//        // 2. 不能重复添加好友
//        count = lambdaQuery()
//                .eq(Friends::getReceiveId, receiveId)
//                .eq(Friends::getFromId, fromId)
//                .eq(Friends::getStatus, FriendsStatusEnum.PASSED).count();
//        if (count > 0) {
//            throw new BusinessException(ErrorCode.OPERATION_ERROR, "你们已经是好友了");
//        }
//        Friends friends = new Friends();
//        friends.setFromId(fromId);
//        friends.setReceiveId(receiveId);
//        if (StringUtils.isBlank(friendAddDTO.getRemark())) {
//            friends.setRemark("我是" + userMapper.selectById(fromId).getUsername());
//        } else {
//            friends.setRemark(friendAddDTO.getRemark());
//        }
//        this.save(friends);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void agreeToApply(Long receiveId, Long fromId) {
        // 1. 根据receiveId查询申请记录
        LambdaQueryWrapper<Friends> friendsLambdaQueryWrapper = new LambdaQueryWrapper<>();
        friendsLambdaQueryWrapper.eq(Friends::getReceiveId, receiveId);
        friendsLambdaQueryWrapper.eq(Friends::getFromId, fromId);
        friendsLambdaQueryWrapper.eq(Friends::getStatus, FriendsStatusEnum.NOT_PASSED);
        Friends friends = this.getOne(friendsLambdaQueryWrapper);
        // 2. 修改状态 更新
        friends.setStatus(FriendsStatusEnum.PASSED);
        this.updateById(friends);
        // 3. 添加为互相关注
        UserFollow userFollow1 = new UserFollow();
        userFollow1.setUserId(receiveId);
        userFollow1.setUserFollowId(fromId);
        userFollowMapper.insert(userFollow1);
        UserFollow userFollow2 = new UserFollow();
        userFollow2.setUserId(fromId);
        userFollow2.setUserFollowId(receiveId);
        userFollowMapper.insert(userFollow2);
    }

    @Override
    public void canceledApply(Long id) {
        Friends friend = this.getById(id);
        if (friend.getStatus() != FriendsStatusEnum.NOT_PASSED) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "该申请已过期或已通过");
        }
        friend.setStatus(FriendsStatusEnum.RESCINDED);
        this.updateById(friend);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void toRead(Set<Long> ids) {
        for (Long id : ids) {
            Friends friend = this.getById(id);
            if (friend != null) {
                if (friend.getStatus() == FriendsStatusEnum.NOT_PASSED && friend.getIsRead() == 0) {
                    friend.setIsRead(1);
                    this.updateById(friend);
                }
            }
        }
    }

    @Override
    public List<FriendsRecordVO> getReceiveList(Long userId) {
        // 1. 查看我收到的好友申请
        List<Friends> friendsList = lambdaQuery()
                .eq(Friends::getReceiveId, userId)
                .ne(Friends::getStatus, FriendsStatusEnum.RESCINDED)
                .list();
        if (CollUtils.isEmpty(friendsList)) {
            return new ArrayList<>();
        }
        // 2. 转VO
        List<Long> fromIdList = friendsList.stream().map(Friends::getFromId).distinct().toList();
        List<User> userList = userMapper.selectBatchIds(fromIdList);
        Map<Long, User> userMap = userList.stream().collect(Collectors.toMap(User::getId, u -> u));
        return friendsList.stream().map(friends -> {
            FriendsRecordVO friendsRecordVO = BeanCopyUtils.copyObject(friends, FriendsRecordVO.class);
            User user = userMap.get(friends.getFromId());
            if (user != null) {
                UserVO userVO = BeanCopyUtils.copyObject(user, UserVO.class);
                if (friends.getStatus() == FriendsStatusEnum.PASSED) {
                    userVO.setFollowed(true);
                }
                friendsRecordVO.setApplyUser(userVO);
            }
            return friendsRecordVO;
        }).toList();
    }

    @Override
    public List<FriendsRecordVO> getFromList(Long userId) {
        // 1. 查看我发送的好友申请
        List<Friends> friendsList = lambdaQuery()
                .eq(Friends::getFromId, userId)
                .list();
        if (CollUtils.isEmpty(friendsList)) {
            return new ArrayList<>();
        }
        // 2. 转VO
        List<Long> receiveList = friendsList.stream().map(Friends::getReceiveId).distinct().toList();
        List<User> userList = userMapper.selectBatchIds(receiveList);
        Map<Long, User> userMap = userList.stream().collect(Collectors.toMap(User::getId, u -> u));
        return friendsList.stream().map(friends -> {
            FriendsRecordVO friendsRecordVO = BeanCopyUtils.copyObject(friends, FriendsRecordVO.class);
            User user = userMap.get(friends.getReceiveId());
            if (user != null) {
                UserVO userVO = BeanCopyUtils.copyObject(user, UserVO.class);
                if (friends.getStatus() == FriendsStatusEnum.PASSED) {
                    userVO.setFollowed(true);
                }
                friendsRecordVO.setApplyUser(userVO);
            }
            return friendsRecordVO;
        }).toList();
    }

    @Override
    public Long getNoReadCount(Long userId) {
        return lambdaQuery()
                .eq(Friends::getReceiveId, userId)
                .eq(Friends::getStatus, FriendsStatusEnum.NOT_PASSED)
                .eq(Friends::getIsRead, 0)
                .count();
    }

    @Override
    public Boolean isFriends(Long fromId, Long toId) {
        long count = this.count(new LambdaQueryWrapper<Friends>()
                .eq(Friends::getFromId, fromId).eq(Friends::getReceiveId, toId)
                .eq(Friends::getStatus, FriendsStatusEnum.PASSED));
        if (count > 0) return true;
        count = this.count(new LambdaQueryWrapper<Friends>()
                .eq(Friends::getFromId, toId).eq(Friends::getReceiveId, fromId)
                .eq(Friends::getStatus, FriendsStatusEnum.PASSED));
        return count > 0;
    }

    @Override
    public List<FriendsRecordVO> getMyFriends() {
        // 1. 查看我的好友
        Long userId = userService.getCurrentUser().getId();
        List<Friends> list1 = lambdaQuery().eq(Friends::getStatus, FriendsStatusEnum.PASSED)
                .eq(Friends::getFromId, userId)
                .list();
        List<Friends> list2 = lambdaQuery().eq(Friends::getStatus, FriendsStatusEnum.PASSED)
                .eq(Friends::getReceiveId, userId)
                .list();
        Set<Friends> friendsSet = CollUtils.unionDistinct(list1, list2);
        if (CollUtils.isEmpty(friendsSet)) {
            return new ArrayList<>();
        }
        // 2. 转 VO
        List<Long> idList1 = friendsSet.stream().map(Friends::getFromId).distinct().filter(f -> !f.equals(userId)).toList();
        List<Long> idList2 = friendsSet.stream().map(Friends::getReceiveId).distinct().filter(f -> !f.equals(userId)).toList();
        HashSet<Long> userIdList = new HashSet<>(CollUtils.unionDistinct(idList1, idList2));
        List<User> userList = userMapper.selectBatchIds(userIdList);
        Map<Long, User> userMap = userList.stream().collect(Collectors.toMap(User::getId, u -> u));
        return friendsSet.stream().map(friends -> {
            FriendsRecordVO friendsRecordVO = BeanCopyUtils.copyObject(friends, FriendsRecordVO.class);
            User user = userMap.get(friends.getFromId());
            if (user != null) {
                UserVO userVO = BeanCopyUtils.copyObject(user, UserVO.class);
                userVO.setFollowed(true);
                friendsRecordVO.setApplyUser(userVO);
            }
            user = userMap.get(friends.getReceiveId());
            if (user != null) {
                UserVO userVO = BeanCopyUtils.copyObject(user, UserVO.class);
                userVO.setFollowed(true);
                friendsRecordVO.setApplyUser(userVO);
            }
            return friendsRecordVO;
        }).toList();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteFriend(Long id) {
        // 1. 删除好友表
        Friends friends = getById(id);
        if (friends == null) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "你们已不是好友");
        }
        removeById(id);
        // 2. 双方都删除互关
        Long fromId = friends.getFromId();
        Long receiveId = friends.getReceiveId();
        userFollowMapper.delete(new LambdaQueryWrapper<UserFollow>()
                .eq(UserFollow::getUserId, fromId)
                .eq(UserFollow::getUserFollowId, receiveId));
        userFollowMapper.delete(new LambdaQueryWrapper<UserFollow>()
                .eq(UserFollow::getUserId, receiveId)
                .eq(UserFollow::getUserFollowId, fromId));
    }
}




