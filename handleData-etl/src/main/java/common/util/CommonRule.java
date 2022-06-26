package common.util;

import com.google.gson.Gson;
import common.bean.Constance;
import common.bean.TranslateResponse;
import org.apache.commons.lang.StringUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class CommonRule {

    private static final Gson gson = new Gson();
    private static final Set<String> normalNameSet = new HashSet<String>() {
        {
            add("zh");
            add("zh-Hant");
            add("ja");
            add("ko");
        }
    };
    private static final Set<String> reverseNameSet = new HashSet<String>() {
        {
            add("en");
        }
    };

    public static String getGender (String sourceGender) {
        if ("男".equals(sourceGender)) {
            return "-1";
        } else if ("女".equals(sourceGender)) {
            return "1";
        } else {
            return "0";
        }
    }

    public static String getLanguageType (String text) throws UnsupportedEncodingException {
        if (StringUtils.isBlank(text)) {
            return Constance.DEFAULT_LANGUAGE;
        }
        String url = Constance.properties.getProperty(Constance.TRANSLATE_URL);
        Map<String, String> params = new HashMap<>();
        params.put("q", URLEncoder.encode(text, "utf-8"));
        String response = HttpClientUtil.doHttpGet(url, params, new HashMap<>());
        TranslateResponse result = gson.fromJson(response, TranslateResponse.class);
        return result.getResponseData().getLanguage();
    }

    public static String getUseNameByLanguageType(String use_name) throws UnsupportedEncodingException {
        String[] values = use_name.split("  ");
        if (values.length >= 2) {
            String language_type = CommonRule.getLanguageType(use_name);
            StringBuilder name = new StringBuilder();
            if (normalNameSet.contains(language_type)) {
                for (String value : values) {
                    name.append(value);
                }
                return name.toString();
            } else if (reverseNameSet.contains(language_type)) {
                for (int i = 1;i < values.length;i++) {
                    name.append(values[i]).append("  ");
                }
                name.append(values[0]);
                return name.toString();
            }
        }

        return use_name;
    }
}
