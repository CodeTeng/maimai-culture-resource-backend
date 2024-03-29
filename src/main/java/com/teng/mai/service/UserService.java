package com.teng.mai.service;

import com.teng.mai.common.page.PageVO;
import com.teng.mai.model.dto.*;
import com.teng.mai.model.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.teng.mai.model.vo.UserVO;

import javax.servlet.http.HttpServletRequest;

/**
 * @author teng
 * @description 针对表【user(用户表)】的数据库操作Service
 * @createDate 2024-03-22 21:41:08
 */
public interface UserService extends IService<User> {

    /**
     * 用户注册
     */
    void userRegister(UserRegisterDTO userRegisterDTO);

    /**
     * 用户登录
     */
    UserVO userLogin(UserLoginDTO userLoginDTO);

    /**
     * 获取当前登录用户
     */
    UserVO getCurrentUser();

    /**
     * 关注或取消关注用户
     */
    void folOrCancelFol(Long id, Long type);

    /**
     * 分页查看我关注的用户
     */
    PageVO<UserVO> getMyFolUser(FolSearchDTO folSearchDTO);

    /**
     * 修改我的个人信息
     */
    void updateMyInfo(UserUpdateDTO userUpdateDTO);

    /**
     * 当前用户个人修改密码
     */
    void updateUserMyPwd(UserUpdateMyPwdDTO userUpdateMyPwdDTO);

    /**
     * 关注人员数量
     */
    Long favMyCount(Long type);

    /**
     * 分页查看关注我的用户
     */
    PageVO<UserVO> getFolMyUser(FolSearchDTO folSearchDTO);
}
