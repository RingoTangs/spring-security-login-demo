package com.ymy.boot.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 自动登录-信息表
 *
 * @author Ringo
 * @date 2021/5/16 21:46
 */
@TableName("persistent_logins")
@Data
public class PersistentLogin {

    @TableId
    private String series;

}
