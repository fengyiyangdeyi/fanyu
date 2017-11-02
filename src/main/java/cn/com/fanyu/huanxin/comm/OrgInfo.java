package cn.com.fanyu.huanxin.comm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by easemob on 2017/3/31.
 */
@Component
public class OrgInfo {

    public static String ORG_NAME;
    public static String APP_NAME;
    public static final Logger logger = LoggerFactory.getLogger(OrgInfo.class);

    private static String configFile;

    @Value("${configFile}")
    public void setConfigFile(String configFile) {
        InputStream inputStream = OrgInfo.class.getClassLoader().getResourceAsStream(configFile);
        Properties prop = new Properties();
        try {
            prop.load(inputStream);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
        ORG_NAME = prop.getProperty("ORG_NAME");
        APP_NAME = prop.getProperty("APP_NAME");
    }

    static {

    }
}
