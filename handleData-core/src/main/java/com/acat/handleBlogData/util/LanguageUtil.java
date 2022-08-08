package com.acat.handleBlogData.util;

import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

public class LanguageUtil {

    public static Map<String, String> languageMap = new HashMap<>();
    static {
        languageMap.put("aa", "阿法尔语");
        languageMap.put("fr", "法语");
        languageMap.put("li", "林堡语");
        languageMap.put("se", "北萨米语");

        languageMap.put("ab", "阿布哈兹语");
        languageMap.put("fy", "弗里西亚语");
        languageMap.put("ln", "林加拉语");
        languageMap.put("sg", "桑戈语");

        languageMap.put("ae", "阿维斯陀语");
        languageMap.put("ga", "爱尔兰语");
        languageMap.put("lo", "老挝语");
        languageMap.put("sh", "塞尔维亚-克罗地亚语");

        languageMap.put("af", "南非语");
        languageMap.put("gd", "苏格兰盖尔语");
        languageMap.put("lt", "立陶宛语");
        languageMap.put("si", "僧加罗语");

        languageMap.put("ak", "阿坎语");
        languageMap.put("gl", "加利西亚语");
        languageMap.put("lu", "卢巴语");
        languageMap.put("sk", "斯洛伐克语");

        languageMap.put("am", "阿姆哈拉语");
        languageMap.put("gn", "瓜拉尼语");
        languageMap.put("lv", "拉脱维亚语");
        languageMap.put("sl", "斯洛文尼亚语");

        languageMap.put("an", "阿拉贡语");
        languageMap.put("gu", "古吉拉特语");
        languageMap.put("mg", "马达加斯加语");
        languageMap.put("sm", "萨摩亚语");

        languageMap.put("ar", "阿拉伯语");
        languageMap.put("gv", "马恩岛语");
        languageMap.put("mh", "马绍尔语");
        languageMap.put("sn", "绍纳语");

        languageMap.put("as", "阿萨姆语");
        languageMap.put("ha", "豪萨语");
        languageMap.put("mi", "毛利语");
        languageMap.put("so", "索马里语");

        languageMap.put("av", "阿瓦尔语");
        languageMap.put("he", "希伯来语");
        languageMap.put("mk", "马其顿语");
        languageMap.put("sq", "阿尔巴尼亚语");

        languageMap.put("ay", "艾马拉语");
        languageMap.put("hi", "印地语");
        languageMap.put("ml", "马拉亚拉姆语");
        languageMap.put("sr", "塞尔维亚语");

        languageMap.put("az", "阿塞拜疆语");
        languageMap.put("ho", "希里莫图语");
        languageMap.put("mn", "蒙古语");
        languageMap.put("ss", "斯瓦特语");

        languageMap.put("ba", "巴什基尔语");
        languageMap.put("hr", "克罗地亚语");
        languageMap.put("mo", "摩尔达维亚语");
        languageMap.put("st", "南索托语");

        languageMap.put("be", "白俄罗斯语");
        languageMap.put("ht", "海地克里奥尔语");
        languageMap.put("mr", "马拉提语");
        languageMap.put("su", "巽他语");

        languageMap.put("bg", "保加利亚语");
        languageMap.put("hu", "匈牙利语");
        languageMap.put("ms", "马来语");
        languageMap.put("sv", "瑞典语");

        languageMap.put("bh", "比哈尔语");
        languageMap.put("hy", "亚美尼亚语");
        languageMap.put("mt", "马耳他语");
        languageMap.put("sw", "斯瓦希里语");

        languageMap.put("bi", "比斯拉马语");
        languageMap.put("hz", "赫雷罗语");
        languageMap.put("my", "缅甸语");
        languageMap.put("ta", "泰米尔语");

        languageMap.put("bm", "班巴拉语");
        languageMap.put("ia", "国际语A");
        languageMap.put("na", "瑙鲁语");
        languageMap.put("te", "泰卢固语");

        languageMap.put("bn", "孟加拉语");
        languageMap.put("id", "印尼语");
        languageMap.put("nb", "书面挪威语");
        languageMap.put("tg", "塔吉克斯坦语");

        languageMap.put("bo", "藏语");
        languageMap.put("ie", "国际语E");
        languageMap.put("nd", "北恩德贝勒语");
        languageMap.put("th", "泰语");

        languageMap.put("br", "布列塔尼语");
        languageMap.put("ig", "伊博语");
        languageMap.put("ne", "尼泊尔语");
        languageMap.put("ti", "提格里尼亚语");

        languageMap.put("bs", "波斯尼亚语");
        languageMap.put("ii", "四川彝语（诺苏语）");
        languageMap.put("ng", "恩敦加语");
        languageMap.put("tk", "土库曼语");

        languageMap.put("ca", "加泰隆语");
        languageMap.put("ik", "依努庇克语");
        languageMap.put("nl", "荷兰语");
        languageMap.put("tl", "他加禄语");

        languageMap.put("ce", "车臣语");
        languageMap.put("io", "伊多语");
        languageMap.put("nn", "新挪威语");
        languageMap.put("tn", "塞茨瓦纳语");

        languageMap.put("ch", "查莫罗语");
        languageMap.put("is", "冰岛语");
        languageMap.put("no", "挪威语");
        languageMap.put("to", "汤加语");

        languageMap.put("co", "科西嘉语");
        languageMap.put("it", "意大利语");
        languageMap.put("nr", "南恩德贝勒语");
        languageMap.put("tr", "土耳其语");

        languageMap.put("cr", "克里语");
        languageMap.put("iu", "因纽特语");
        languageMap.put("nv", "纳瓦霍语");
        languageMap.put("ts", "宗加语");

        languageMap.put("cs", "捷克语");
        languageMap.put("ja", "日语");
        languageMap.put("ny", "尼扬贾语");
        languageMap.put("tt", "塔塔尔语");

        languageMap.put("cu", "古教会斯拉夫语");
        languageMap.put("jv", "爪哇语");
        languageMap.put("oc", "奥克语");
        languageMap.put("tw", "特威语");

        languageMap.put("cv", "楚瓦什语");
        languageMap.put("ka", "格鲁吉亚语");
        languageMap.put("oj", "奥吉布瓦语");
        languageMap.put("ty", "塔希提语");

        languageMap.put("cy", "威尔士语");
        languageMap.put("kg", "刚果语");
        languageMap.put("om", "奥洛莫语");
        languageMap.put("ug", "维吾尔语");

        languageMap.put("da", "丹麦语");
        languageMap.put("ki", "基库尤语");
        languageMap.put("or", "奥利亚语");
        languageMap.put("uk", "乌克兰语");

        languageMap.put("de", "德语");
        languageMap.put("kj", "宽亚玛语");
        languageMap.put("os", "奥塞梯语");
        languageMap.put("ur", "乌尔都语");

        languageMap.put("dv", "迪维希语");
        languageMap.put("kk", "哈萨克语");
        languageMap.put("pa", "旁遮普语");
        languageMap.put("uz", "乌兹别克语");

        languageMap.put("dz", "不丹语");
        languageMap.put("kl", "格陵兰语");
        languageMap.put("pi", "巴利语");
        languageMap.put("ve", "文达语");

        languageMap.put("ee", "埃维语");
        languageMap.put("km", "高棉语");
        languageMap.put("pl", "波兰语");
        languageMap.put("vi", "越南语");

        languageMap.put("el", "现代希腊语");
        languageMap.put("kn", "卡纳达语");
        languageMap.put("ps", "普什图语");
        languageMap.put("vo", "沃拉普克语");

        languageMap.put("en", "英语");
        languageMap.put("ko", "朝鲜语、韩语");
        languageMap.put("pt", "葡萄牙语");
        languageMap.put("wa", "沃伦语");

        languageMap.put("eo", "世界语");
        languageMap.put("kr", "卡努里语");
        languageMap.put("qu", "凯楚亚语");
        languageMap.put("wo", "沃洛夫语");

        languageMap.put("es", "西班牙语");
        languageMap.put("ks", "克什米尔语");
        languageMap.put("rm", "罗曼什语");
        languageMap.put("xh", "科萨语");

        languageMap.put("et", "爱沙尼亚语");
        languageMap.put("ku", "库尔德语");
        languageMap.put("rn", "基隆迪语");
        languageMap.put("yi", "依地语");

        languageMap.put("eu", "巴斯克语");
        languageMap.put("kv", "科米语");
        languageMap.put("ro", "罗马尼亚语");
        languageMap.put("yo", "约鲁巴语");

        languageMap.put("fa", "波斯语");
        languageMap.put("kw", "康沃尔语");
        languageMap.put("ru", "俄语");
        languageMap.put("za", "壮语");

        languageMap.put("ff", "富拉语");
        languageMap.put("ky", "吉尔吉斯语");
        languageMap.put("rw", "卢旺达语");
        languageMap.put("zh", "汉语");
        languageMap.put("zh-cn", "汉语");

        languageMap.put("fi", "芬兰语");
        languageMap.put("la", "拉丁语");
        languageMap.put("sa", "梵语");
        languageMap.put("zu", "祖鲁语");

        languageMap.put("fj", "斐济语");
        languageMap.put("lb", "卢森堡语");
        languageMap.put("sc", "萨丁尼亚语");

        languageMap.put("fo", "法罗语");
        languageMap.put("lg", "卢干达语");
        languageMap.put("sd", "信德语");
    }

    public static String getLanguageName(String LangName) {
        if (ReaderFileUtil.isChinese(LangName)) {
            return LangName;
        }
        String name = languageMap.get(LangName);
        if (StringUtils.isNotBlank(name)) {
            return name;
        }else {
            String lowerName = languageMap.get(LangName.toLowerCase());
            if (StringUtils.isNotBlank(lowerName)) {
                return lowerName;
            }
        }
        return LangName;
    }
}
