package org.originit.analyze.config;

import lombok.extern.slf4j.Slf4j;
import org.originit.analyze.util.ResourceUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author xxc
 */
@Configuration
@Slf4j
public class WebConfig  implements WebMvcConfigurer {

    @Value("${outside.resource.path}")
    private String path;

    @Value("${outside.resource.prefix}")
    private String resourcePrefix;

    /**
     * 访问外部文件配置，访问D盘下文件
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String curPath = path;
        //配置server虚拟路径，handler为jsp中访问的目录，locations为image相对应的本地路径
        if (!ResourceUtil.isClassPath(path)) {
            curPath = ResourceUtil.FILE_PREFIX + path;
        }
        log.info("【配置映射】pattern:{},path:{}",resourcePrefix,curPath);
        registry.addResourceHandler(resourcePrefix +"/**").addResourceLocations(curPath + "/");
    }

}
