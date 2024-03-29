package com.teng.mai.service;

import com.teng.mai.common.page.PageVO;
import com.teng.mai.model.dto.FriendAddDTO;
import com.teng.mai.model.dto.FriendSearchDTO;
import com.teng.mai.model.entity.Friends;
import com.baomidou.mybatisplus.extension.service.IService;
import com.teng.mai.model.vo.FriendsRecordVO;

import java.util.List;
import java.util.Set;

/**
 * @author teng
 * @description 针对表【friends(好友申请管理表)】的数据库操作Service
 * @createDate 2024-03-23 21:55:22
 */
public interface FriendsService extends IService<Friends> {

    /**
     * 发送好友申请
     */
    void addFriendRecords(Long fromId, FriendAddDTO friendAddDTO);

    /**
     * 同意好友申请
     */
    void agreeToApply(Long userId, Long fromId);

    /**
     * 撤销申请
     */
    void canceledApply(Long id);

    /**
     * 已读(全部已读)
     */
    void toRead(Set<Long> ids);

    /**
     * 查看我收到的好友申请
     */
    List<FriendsRecordVO> getReceiveList(Long userId);

    /**
     * 查看我发送的好友申请
     */
    List<FriendsRecordVO> getFromList(Long userId);

    /**
     * 查看我的未读消息
     */
    Long getNoReadCount(Long userId);

    /**
     * 判断是否是好友
     */
    Boolean isFriends(Long fromId, Long toId);

    /**
     * 分页查看我的好友
     */
    List<FriendsRecordVO> getMyFriends();

    /**
     * 删除好友
     */
    void deleteFriend(Long id);
}
