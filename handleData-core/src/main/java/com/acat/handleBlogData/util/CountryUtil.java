package com.acat.handleBlogData.util;

import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

public class CountryUtil {

    public static Map<String, String> countryMap = new HashMap<>();
    static {
        countryMap.put("TW", "中国台湾");
        countryMap.put("MX", "墨西哥");
        countryMap.put("US", "美国");
        countryMap.put("TH", "泰国");
        countryMap.put("USA|美国", "美国");
        countryMap.put("IN", "印度");
        countryMap.put("Spain", "西班牙");
        countryMap.put("Panama", "巴拿马");
        countryMap.put("Philippines", "菲律宾");
        countryMap.put("Bosnia and Herzegovina", "波斯尼亚和黑塞哥维那");
        countryMap.put("Malaysla", "马来西亚");
        countryMap.put("NorWay", "挪威");
        countryMap.put("AU", "澳大利亚");
        countryMap.put("DE", "德国");
        countryMap.put("CN", "中国");
        countryMap.put("JP", "日本");
        countryMap.put("Botswana", "博茨瓦纳");
        countryMap.put("Vietnam", "越南");
        countryMap.put("Bulgaria", "保加利亚");
        countryMap.put("Germany", "德国");
        countryMap.put("Greece", "希腊");
        countryMap.put("ES", "西班牙");
        countryMap.put("HK", "中国香港");
        countryMap.put("CA", "加拿大");
        countryMap.put("IT", "意大利");
        countryMap.put("MY", "马来西亚");
        countryMap.put("IE", "爱尔兰");
        countryMap.put("SG", "新加坡");
        countryMap.put("TR", "土耳其");
        countryMap.put("PH", "菲律宾");
        countryMap.put("GB", "英国");
        countryMap.put("KH", "柬埔寨");
        countryMap.put("FR", "法国");
        countryMap.put("SE", "瑞典");
        countryMap.put("LU", "卢森堡");
        countryMap.put("CH", "瑞士");
        countryMap.put("BE", "比利时");
        countryMap.put("NL", "荷兰");
        countryMap.put("GE", "格鲁吉亚");
        countryMap.put("AE", "阿拉伯联合酋长国");
        countryMap.put("SA", "沙特阿拉伯王国");
        countryMap.put("BH", "巴林");
        countryMap.put("IL", "以色列");
        countryMap.put("PL", "波兰");
        countryMap.put("PK", "巴基斯坦");
        countryMap.put("EG", "埃及");
        countryMap.put("LB", "黎巴嫩");
        countryMap.put("ZA", "南非");
        countryMap.put("DK", "丹麦");
        countryMap.put("JO", "约旦");
        countryMap.put("QA", "卡塔尔");
        countryMap.put("KR", "韩国");
        countryMap.put("GH", "加纳");
        countryMap.put("BR", "巴西");
        countryMap.put("NP", "尼泊尔");
        countryMap.put("RU", "俄罗斯");
        countryMap.put("BD", "孟加拉国");
        countryMap.put("PE", "秘鲁");
        countryMap.put("GR", "希腊");
        countryMap.put("VN", "越南");
        countryMap.put("KW", "科威特");
        countryMap.put("LV", "拉脱维亚");
        countryMap.put("KE", "肯尼亚");
        countryMap.put("AZ", "阿塞拜疆");
        countryMap.put("ID", "印度尼西亚");
        countryMap.put("MA", "摩洛哥");
        countryMap.put("EC", "厄瓜多尔");
        countryMap.put("MX", "墨西哥");
        countryMap.put("CZ", "捷克");
        countryMap.put("AL", "阿尔巴尼亚");
        countryMap.put("FI", "芬兰");
        countryMap.put("JE", "泽西岛");
        countryMap.put("NZ", "新西兰");
        countryMap.put("OO", "阿尔巴尼亚");
        countryMap.put("LK", "斯里兰卡");
        countryMap.put("RW", "卢旺达");
        countryMap.put("NG", "尼日利亚");
        countryMap.put("BY", "白俄罗斯");
        countryMap.put("AF", "阿富汗");
        countryMap.put("TN", "突尼斯");
        countryMap.put("RO", "罗马尼亚");
        countryMap.put("NO", "挪威");
        countryMap.put("UA", "乌克兰");
        countryMap.put("AT", "奥地利");
        countryMap.put("BO", "玻利维亚");
        countryMap.put("CO", "哥伦比亚");
        countryMap.put("CL", "智利");
        countryMap.put("VE", "委内瑞拉");
        countryMap.put("MG", "马达加斯加");
        countryMap.put("SN", "塞内加尔");
        countryMap.put("PT", "葡萄牙");
        countryMap.put("HR", "克罗地亚");
    }

    public static String getCountryName(String countryKey) {
//        if (StringUtils.isBlank(countryKey)) {
//            return "";
//        }
        if (ReaderFileUtil.isChinese(countryKey)) {
            return countryKey;
        }
        String name = countryMap.get(countryKey);
        if (StringUtils.isNotBlank(name)) {
            return name;
        }else {
            String upperName = countryMap.get(countryKey.toUpperCase());
            if (StringUtils.isNotBlank(upperName)) {
                return upperName;
            }
        }
        return "";
    }
}
