package com.teng.mai.job;

import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import com.teng.mai.model.entity.Friends;
import com.teng.mai.model.enums.FriendsStatusEnum;
import com.teng.mai.service.FriendsService;
import com.teng.mai.utils.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

/**
 * @description:
 * @author: ~Teng~
 * @date: 2024/3/24 0:28
 */
@Component
@Slf4j
public class TimedJobs {
    @Resource
    private FriendsService friendsService;

    /**
     * 每隔半个小时执行一次
     */
    @Scheduled(cron = "0 0/30 * * * ?")
    public void updateFriendsStatusJob() {
        log.info("更新好友申请过期状态");
        List<Friends> friendsList = friendsService.list();
        friendsList.forEach(friends -> {
            if (DateUtils.between(LocalDateTime.now(), friends.getCreateTime(), ChronoUnit.DAYS) >= 3 && friends.getStatus() != FriendsStatusEnum.EXPIRED) {
                // 更新至已过期
                friends.setStatus(FriendsStatusEnum.EXPIRED);
                friendsService.updateById(friends);
            }
        });
    }
}
