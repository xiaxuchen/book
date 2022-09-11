package org.originit.analyze.util;

import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;


public class ResourceUtil {

    public static final String CLASS_PATH_PREFIX = "classpath:";

    public static final String FILE_PREFIX = "file:";

    /**
     * 加载匹配路径的文件
     * @param pattern 路径模式
     * @return 资源
     */
    public static Resource[] loadResources(String pattern){
        try {
            if (isClassPath(pattern)) {
                return new PathMatchingResourcePatternResolver().getResources(pattern);
            } else {
                return new PathMatchingResourcePatternResolver().getResources(FILE_PREFIX + pattern);
            }
        }catch (Exception e) {
            return new Resource[0];
        }

    }

    public static boolean isClassPath(String path) {
        return path.startsWith(CLASS_PATH_PREFIX) || path.startsWith(PathMatchingResourcePatternResolver.CLASSPATH_URL_PREFIX);
    }
}
