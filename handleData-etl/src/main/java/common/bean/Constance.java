package common.bean;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class Constance {

    public static final String DEFAULT_CONFIG_PATH = "conf/config.properties";
    public static final Properties properties = new Properties();
    public static final String TRANSLATE_URL = "translate_url";

    public static final String LANGAUGE_CONFIG_PATH = "conf/language.csv";
    public static final Map<String, Language> languageInfo = new HashMap<>();

    public static final String STRING_DEFAULT = "";
    public static final String DEFAULT_LANGUAGE = "en";
    public static final String TW = "TW";
    public static final String IS = "IS";
    public static final String FQ = "FQ";
    public static final String FB = "FB";
    public static final String LI = "LI";


    public static void init() throws IOException {
        try (BufferedReader bufferedReader = new BufferedReader(
                new FileReader(System.getProperty("user.dir") + File.separator + DEFAULT_CONFIG_PATH))) {
            properties.load(bufferedReader);
        }
        String line;
        try (BufferedReader reader = new BufferedReader(
                new FileReader(System.getProperty("user.dir") + File.separator + LANGAUGE_CONFIG_PATH))){
            while ((line = reader.readLine()) != null) {
                String[] values = line.split(",");
                if (values.length != 3 ) {
                    throw new IOException("language conf format error! " + line );
                }
                languageInfo.put(values[0],new Language(values[0], values[1], values[2]));
            }
        }
    }
}
