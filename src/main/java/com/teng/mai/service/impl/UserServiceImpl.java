package com.teng.mai.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.teng.mai.common.ErrorCode;
import com.teng.mai.common.page.PageVO;
import com.teng.mai.constant.UserConstant;
import com.teng.mai.exception.BusinessException;
import com.teng.mai.exception.ThrowUtils;
import com.teng.mai.mapper.FriendsMapper;
import com.teng.mai.mapper.UserFollowMapper;
import com.teng.mai.model.dto.*;
import com.teng.mai.model.entity.Friends;
import com.teng.mai.model.entity.User;
import com.teng.mai.model.entity.UserFollow;
import com.teng.mai.model.enums.FriendsStatusEnum;
import com.teng.mai.model.enums.UserRoleEnum;
import com.teng.mai.model.enums.UserStatusEnum;
import com.teng.mai.model.vo.UserVO;
import com.teng.mai.service.UserService;
import com.teng.mai.mapper.UserMapper;
import com.teng.mai.utils.BeanCopyUtils;
import com.teng.mai.utils.CollUtils;
import com.teng.mai.utils.JwtUtils;
import com.teng.mai.utils.UserThreadLocalUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * @author teng
 * @description 针对表【user(用户表)】的数据库操作Service实现
 * @createDate 2024-03-22 21:41:08
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
        implements UserService {
    @Resource
    private UserFollowMapper userFollowMapper;
    @Resource
    private FriendsMapper friendsMapper;

    @Override
    public void userRegister(UserRegisterDTO userRegisterDTO) {
        String username = userRegisterDTO.getUsername();
        String userPassword = userRegisterDTO.getUserPassword();
        synchronized (username.intern()) {
            // 1. 账户不能重复
            Long count = lambdaQuery().eq(User::getUsername, username).count();
            if (count > 0) {
                throw new BusinessException(ErrorCode.OPERATION_ERROR, "该用户已存在，请更改用户账号");
            }
            // 2. 加密
            userPassword = DigestUtils.md5DigestAsHex((UserConstant.SALT + userPassword).getBytes(StandardCharsets.UTF_8));
            // 3. 插入数据
            User user = new User();
            user.setUsername(username);
            user.setUserPassword(userPassword);
            user.setUserAvatar(UserConstant.DEFAULT_USER_AVATAR);
            boolean result = save(user);
            ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR, "注册失败，数据库错误");
        }
    }

    @Override
    public UserVO userLogin(UserLoginDTO userLoginDTO) {
        // 1. 查询用户是否存在
        String userPassword = userLoginDTO.getUserPassword();
        userPassword = DigestUtils.md5DigestAsHex((UserConstant.SALT + userPassword).getBytes(StandardCharsets.UTF_8));
        String username = userLoginDTO.getUsername();
        User user = lambdaQuery().eq(User::getUsername, username).eq(User::getUserPassword, userPassword).one();
        if (user == null) {
            log.error("用户登录失败，用户名或密码错误");
            throw new BusinessException(ErrorCode.PASSWORD_ERROR);
        }
        // 2. 判断用户是否已被禁用
        String userRole = user.getUserRole();
        String[] userRoleNames = StringUtils.split(userRole, ",");
        if (user.getUserStatus() == UserStatusEnum.DISABLED || ArrayUtils.contains(userRoleNames, UserRoleEnum.BAN.getValue())) {
            throw new BusinessException(ErrorCode.ACCOUNT_LOCKOUT);
        }
        // 3. 生成 token
        String token = JwtUtils.getToken(user.getId());
        // 4. 用户脱敏
        UserVO userVO = BeanCopyUtils.copyObject(user, UserVO.class);
        userVO.setAccessToken(token);
        return userVO;
    }

    @Override
    public UserVO getCurrentUser() {
        Long userId = UserThreadLocalUtils.getUserId();
        if (userId == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }
        User user = getById(userId);
        if (user == null) {
            throw new BusinessException(ErrorCode.USER_NOT_EXISTS);
        }
        if (user.getUserStatus() == UserStatusEnum.DISABLED || user.getUserRole().contains(UserRoleEnum.BAN.getValue())) {
            throw new BusinessException(ErrorCode.ACCOUNT_LOCKOUT);
        }
        return BeanCopyUtils.copyObject(user, UserVO.class);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void folOrCancelFol(Long id, Long type) {
        // 1. 获取当前登录用户
        UserVO currentUser = getCurrentUser();
        Long userId = currentUser.getId();
        if (userId.equals(id)) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "自己不能关注自己");
        }
        // 2. 判断是否已经关注或没有关注
        Long count = userFollowMapper.selectCount(new LambdaQueryWrapper<UserFollow>()
                .eq(UserFollow::getUserId, userId)
                .eq(UserFollow::getUserFollowId, id));
        if (type.equals(0L)) {
            if (count > 0) {
                throw new BusinessException(ErrorCode.OPERATION_ERROR, "你已关注过该用户");
            }
            // 3. 进行关注
            UserFollow userFollow = new UserFollow();
            userFollow.setUserId(userId);
            userFollow.setUserFollowId(id);
            userFollowMapper.insert(userFollow);
            // 4. 判断对方是否已经关注自己
            count = userFollowMapper.selectCount(new LambdaQueryWrapper<UserFollow>()
                    .eq(UserFollow::getUserId, id)
                    .eq(UserFollow::getUserFollowId, userId));
            if (count > 0) {
                // 对方已经关注我了 添加为朋友
                Friends friends = new Friends();
                friends.setFromId(id);
                friends.setReceiveId(userId);
                friends.setIsRead(1);
                friends.setStatus(FriendsStatusEnum.PASSED);
                friendsMapper.insert(friends);
            }
        } else if (type.equals(1L)) {
            if (count <= 0) {
                throw new BusinessException(ErrorCode.OPERATION_ERROR, "你还未关注该用户，请先关注");
            }
            // 3. 判断对方是否已经关注了我
            count = userFollowMapper.selectCount(new LambdaQueryWrapper<UserFollow>()
                    .eq(UserFollow::getUserId, id)
                    .eq(UserFollow::getUserFollowId, userId));
            if (count > 0) {
                // 对方之前关注了我 取消朋友关系
                friendsMapper.delete(new LambdaQueryWrapper<Friends>()
                        .eq(Friends::getFromId, id)
                        .eq(Friends::getReceiveId, userId));
            }
            // 4. 取消关注
            userFollowMapper.delete(new LambdaQueryWrapper<UserFollow>()
                    .eq(UserFollow::getUserId, userId)
                    .eq(UserFollow::getUserFollowId, id));
        } else {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
    }

    @Override
    public PageVO<UserVO> getMyFolUser(FolSearchDTO folSearchDTO) {
        // 1. 获取当前用户
        UserVO currentUser = getCurrentUser();
        Long userId = currentUser.getId();
        // 2. 查询我关注的用户
        List<UserFollow> userFollowList = userFollowMapper.selectList(new LambdaQueryWrapper<UserFollow>()
                .eq(UserFollow::getUserId, userId));
        if (CollUtils.isEmpty(userFollowList)) {
            return PageVO.empty(new Page<>());
        }
        List<Long> userFollowIdList = userFollowList.stream().map(UserFollow::getUserFollowId).toList();
        // 3. 分页查询我关注的用户
        String keyword = folSearchDTO.getKeyword();
        Page<User> userPage = lambdaQuery()
                .in(User::getId, userFollowIdList)
                .like(StringUtils.isNotBlank(keyword), User::getUsername, keyword)
                .page(folSearchDTO.toMpPageDefaultSortByCreateTimeDesc());
        List<User> userList = userPage.getRecords();
        if (CollUtils.isEmpty(userList)) {
            return PageVO.empty(userPage);
        }
        List<UserVO> userVOList = userList.stream().map(user -> {
            UserVO userVO = BeanCopyUtils.copyObject(user, UserVO.class);
            userVO.setFollowed(true);
            return userVO;
        }).toList();
        return PageVO.of(userPage, userVOList);
    }

    @Override
    public void updateMyInfo(UserUpdateDTO userUpdateDTO) {
        Long userId = getCurrentUser().getId();
        Long count = lambdaQuery()
                .ne(User::getId, userId)
                .eq(User::getUsername, userUpdateDTO.getUsername())
                .count();
        if (count > 0) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "账号重复，请修改账号名称");
        }
        User user = new User();
        BeanUtils.copyProperties(userUpdateDTO, user);
        user.setId(userId);
        boolean result = updateById(user);
        ThrowUtils.throwIf(!result, ErrorCode.DB_UPDATE_EXCEPTION);
    }

    @Override
    public void updateUserMyPwd(UserUpdateMyPwdDTO userUpdateMyPwdDTO) {
        Long userId = getCurrentUser().getId();
        String oldPassword = userUpdateMyPwdDTO.getOldPassword();
        String userPassword = userUpdateMyPwdDTO.getUserPassword();
        User user = getById(userId);
        if (user == null) {
            throw new BusinessException(ErrorCode.USER_NOT_EXISTS);
        }
        String oldEntryPwd = DigestUtils.md5DigestAsHex((UserConstant.SALT + oldPassword).getBytes(StandardCharsets.UTF_8));
        if (!oldEntryPwd.equals(user.getUserPassword())) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "用户原始密码错误");
        }
        userPassword = DigestUtils.md5DigestAsHex((UserConstant.SALT + userPassword).getBytes(StandardCharsets.UTF_8));
        User newUser = new User();
        newUser.setId(userId);
        newUser.setUserPassword(userPassword);
        boolean result = updateById(newUser);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
    }

    @Override
    public Long favMyCount(Long type) {
        Long id = getCurrentUser().getId();
        if (type.equals(0L)) {
            // 我关注的
            return userFollowMapper.selectCount(new LambdaQueryWrapper<UserFollow>().eq(UserFollow::getUserId, id));
        } else if (type.equals(1L)) {
            return userFollowMapper.selectCount(new LambdaQueryWrapper<UserFollow>().eq(UserFollow::getUserFollowId, id));
        } else {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
    }

    @Override
    public PageVO<UserVO> getFolMyUser(FolSearchDTO folSearchDTO) {
        // 1. 获取当前用户
        Long userId = getCurrentUser().getId();
        // 2. 查询关注我的用户
        List<UserFollow> userFollowList = userFollowMapper.selectList(new LambdaQueryWrapper<UserFollow>()
                .eq(UserFollow::getUserFollowId, userId));
        if (CollUtils.isEmpty(userFollowList)) {
            return PageVO.empty(new Page<>());
        }
        List<Long> userIdList = userFollowList.stream().map(UserFollow::getUserId).toList();
        // 3. 分页查询关注我的用户
        String keyword = folSearchDTO.getKeyword();
        Page<User> userPage = lambdaQuery()
                .in(User::getId, userIdList)
                .like(StringUtils.isNotBlank(keyword), User::getUsername, keyword)
                .page(folSearchDTO.toMpPageDefaultSortByCreateTimeDesc());
        List<User> userList = userPage.getRecords();
        if (CollUtils.isEmpty(userList)) {
            return PageVO.empty(userPage);
        }
        // 4. 我关注的用户
        List<UserFollow> users = userFollowMapper.selectList(new LambdaQueryWrapper<UserFollow>()
                .eq(UserFollow::getUserId, userId));
        List<Long> userIds = new ArrayList<>();
        if (CollUtils.isNotEmpty(users)) {
            userIds = users.stream().map(UserFollow::getUserFollowId).toList();
        }
        List<Long> finalUserIds = userIds;
        List<UserVO> userVOList = userList.stream().map(user -> {
            UserVO userVO = BeanCopyUtils.copyObject(user, UserVO.class);
            // 查询我是否关注了它
            userVO.setFollowed(finalUserIds.contains(user.getId()));
            return userVO;
        }).toList();
        return PageVO.of(userPage, userVOList);
    }
}




