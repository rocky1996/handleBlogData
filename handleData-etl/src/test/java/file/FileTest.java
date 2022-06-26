package file;

import common.bean.Language;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class FileTest {

    @Test
    public void testGetProperties() throws IOException {
        Properties properties = new Properties();
        BufferedReader bufferedReader = new BufferedReader(new FileReader("src/conf/config.properties"));
        properties.load(bufferedReader);
        System.out.println(properties.getProperty("translate_url"));
    }

    @Test
    public void testGetlangauge() throws IOException {
        Map<String,Language> languageInfo = new HashMap<>();
        String line;
        try (BufferedReader reader = new BufferedReader(
                new FileReader(System.getProperty("user.dir") + File.separator + "conf/language.csv"))){
            while ((line = reader.readLine()) != null) {
                String[] values = line.split(",");
                if (values.length != 3 ) {
                    throw new IOException("language conf format error!");
                }
                languageInfo.put(values[0],new Language(values[0], values[1], values[2]));
            }
        }

        System.out.println(languageInfo.get("zh-TW").getCountry());
    }
}
