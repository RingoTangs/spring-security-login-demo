package com.ymy.boot.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ymy.boot.entity.PersistentLogin;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author Ringo
 * @date 2021/5/16 21:22
 */
public interface PersistentLoginMapper extends BaseMapper<PersistentLogin> {

    /**
     * 查询 persistent_logins 表中过期的登录记录
     *
     * @param seconds 过期时间(单位: 秒)
     * @return
     */
    List<String> queryExpireIn(@Param("seconds") int seconds);
}
