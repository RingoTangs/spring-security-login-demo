package com.ymy.boot.code.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Ringo
 * @date 2021/5/11 12:57
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "validate.code.image")
public class ImageCodeProperties {

    private boolean enabled = true;

}
