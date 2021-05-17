package com.ymy.boot.task;

import com.ymy.boot.mapper.PersistentLoginMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

import java.util.List;

import static com.ymy.boot.constant.AuthConstant.COOKIE_EXPIRE_IN;

/**
 * 定时任务需要开启 @EnableScheduling
 *
 * @author Ringo
 * @date 2021/5/16 21:38
 */
@Slf4j
@Component
public class RememberMeTask {

    @Resource
    private PersistentLoginMapper persistentLoginMapper;

    /**
     * 删除过期的自动登录记录
     * 每天的凌晨4点更新记录
     */
    @Scheduled(cron = "0 0 4 * * 0-7")
    public void deleteExpiredAutoLoginRecord() {
        log.info("删除自动登录过期记录....");
        List<String> expired = persistentLoginMapper.queryExpireIn(COOKIE_EXPIRE_IN);
        if (expired != null && expired.size() > 0) {
            persistentLoginMapper.deleteBatchIds(expired);
        }
    }

}
