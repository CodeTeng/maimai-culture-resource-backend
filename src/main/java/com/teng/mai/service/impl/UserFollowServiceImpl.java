package com.teng.mai.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.teng.mai.model.entity.UserFollow;
import com.teng.mai.service.UserFollowService;
import com.teng.mai.mapper.UserFollowMapper;
import org.springframework.stereotype.Service;

/**
* @author teng
* @description 针对表【user_follow(用户关注表)】的数据库操作Service实现
* @createDate 2024-03-23 18:58:08
*/
@Service
public class UserFollowServiceImpl extends ServiceImpl<UserFollowMapper, UserFollow>
    implements UserFollowService{

}




