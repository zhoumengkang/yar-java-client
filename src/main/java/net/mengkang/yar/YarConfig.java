package net.mengkang.yar;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

/**
 * Created by zhoumengkang on 12/12/15.
 */
public class YarConfig {

    private static Properties properties = new Properties();
    private static final String configName = "/yar.properties";
    private static YarConfig instance;

    /**
     * 构造方法 加载配置文件
     */
    private YarConfig() {
        try {
            properties.load(new InputStreamReader(getClass().getResourceAsStream(configName),"UTF-8"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取单例对象
     * @return EmpConfig
     */
    private synchronized static YarConfig getInstance() {
        if (null == instance) {
            instance = new YarConfig();
        }
        return instance;
    }

    /**
     * 将属性值获取为int型
     * @param str 属性名
     * @return
     */
    public static int getInt( String str){
        try {
            if (null == instance) {
                getInstance();
            }
            return Integer.parseInt(properties.getProperty( str ));
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 将属性值获取为String型
     * @param str 属性名
     * @return
     */
    public static String getString( String str){
        try {
            if (null == instance) {
                getInstance();
            }
            return properties.getProperty(str);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 将属性值获取为boolean型
     * @param str 属性名
     * @return
     */
    public static boolean getBoolean( String str){
        try {
            if (null == instance) {
                getInstance();
            }
            return Boolean.parseBoolean(properties.getProperty(str));
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
