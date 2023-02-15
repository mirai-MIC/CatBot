package org.Simbot.utils.Properties;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

/**
 * @author mirai
 * @version 1.0
 * @packAge: org.Simbot.utils.Properties
 * @date 2022/12/8 16:27
 */

public class properties {
    public String getProperties(String Path, String Key) throws IOException {
        var properties = new Properties();
        properties.load(new FileReader(Path));
        return properties.getProperty(Key);
    }
}
