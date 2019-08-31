package com.mars.cloud.core.util;

import com.alibaba.fastjson.JSONObject;
import com.mars.core.util.ConfigUtil;

/**
 * cloud模块配置文件管理
 */
public class CloudConfigUtil {

    /**
     * 获取cloud配置文件
     *
     * @return 配置
     * @throws Exception 异常
     */
    public static JSONObject getCloudConfig() throws Exception {
        try {
            JSONObject config = ConfigUtil.getConfig();
            JSONObject cloudConfig = config.getJSONObject("cloud");
            return cloudConfig;
        } catch (Exception e) {
            throw new Exception("获取cloud配置失败", e);
        }
    }

    /**
     * 获取cloud配置文件
     *
     * @param key 配置文件key
     * @return 配置
     * @throws Exception 异常
     */
    public static Object getCloudConfig(String key) throws Exception {
        try {
            JSONObject config = getCloudConfig();
            return config.get(key);
        } catch (Exception e) {
            throw new Exception("获取cloud配置失败", e);
        }
    }

    /**
     * 获取cloud配置文件中的服务name
     *
     * @return 配置
     * @throws Exception 异常
     */
    public static String getCloudName() throws Exception {
        try {
            JSONObject cloudConfig = getCloudConfig();
            String cloudName = cloudConfig.getString("name");
            return cloudName;
        } catch (Exception e) {
            throw new Exception("获取cloud配置中的服务name失败", e);
        }
    }
}
