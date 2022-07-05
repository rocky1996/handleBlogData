package com.acat.handleBlogData.util;

import com.acat.handleBlogData.domain.*;
import com.acat.handleBlogData.enums.MediaSourceEnum;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

@Slf4j
public class ReaderFileUtil {

    public static Map<String, String> countryMap = new HashMap<>();
    static {
        countryMap.put("TW", "中国");
        countryMap.put("MX", "墨西哥");
        countryMap.put("US", "美国");
        countryMap.put("TH", "泰国");
        countryMap.put("USA|美国", "美国");
        countryMap.put("IN", "印度");
        countryMap.put("Spain", "西班牙");
        countryMap.put("Philippines", "菲律宾");
        countryMap.put("Bosnia and Herzegovina", "波斯尼亚和黑塞哥维那");
        countryMap.put("Malaysla", "马来西亚");
    }

    /**
     * 读取本地文件
     * @param multipartFile
     * @return
     */
    public static List<String> readFile(MultipartFile multipartFile) {

        List<String> fieldList = Lists.newLinkedList();
        try {
            InputStreamReader isr = new InputStreamReader(multipartFile.getInputStream());
            BufferedReader bf = new BufferedReader(isr);
            String textLine = null;
            while ((textLine = bf.readLine()) != null) {
                if (StringUtils.isNotBlank(textLine)) {
                    fieldList.add(textLine);
                }
            }
        } catch (IOException e) {
            log.info("ReaderFileUtil.readFile has error:{}",e.getMessage());
        }
        return fieldList;
    }

    /**
     * 读取本地文件
     * @param multipartFile
     * @param mediaSourceEnum
     * @return
     */
    public static Object readMultipartFileFile(MultipartFile multipartFile, MediaSourceEnum mediaSourceEnum) {

        List<Object> objList = Lists.newLinkedList();
        try {
            InputStreamReader isr = new InputStreamReader(multipartFile.getInputStream());
            BufferedReader bf = new BufferedReader(isr);
            String textLine;
            while ((textLine = bf.readLine()) != null) {
                if (StringUtils.isNotBlank(textLine)) {
                    switch (mediaSourceEnum) {
                        case TWITTER:
                            TwitterUserData twitterUserData = JacksonUtil.strToBean(textLine, TwitterUserData.class);
                            objList.add(twitterUserData);
                            break;
                        case FB_IMPL:
                            FbUserImplData fbUserImplData = JacksonUtil.strToBean(textLine, FbUserImplData.class);
                            objList.add(fbUserImplData);
                            break;
                        case FB_HISTORY:
                            FbUserHistoryData fbUserHistoryData = JacksonUtil.strToBean(textLine, FbUserHistoryData.class);
                            objList.add(fbUserHistoryData);
                            break;
                        case FQ_IMPL:
                            FqUserImplData fqUserImplData = JacksonUtil.strToBean(textLine, FqUserImplData.class);
                            objList.add(fqUserImplData);
                            break;
                        case FQ_HISTORY:
                            FqUserHistoryData fqUserHistoryData = JacksonUtil.strToBean(textLine, FqUserHistoryData.class);
                            objList.add(fqUserHistoryData);
                            break;
                        case INSTAGRAM:
                            InstagramUserData instagramUserData = JacksonUtil.strToBean(textLine, InstagramUserData.class);
                            objList.add(instagramUserData);
                            break;
                        case LINKEDIN_IMPL:
                            break;
                        case LINKEDIN_HISTORY:
                            break;
                        case LINKEDIN_BUSINESS:
                            break;
                        case LINKEDIN_SCHOOL:
                            break;
                        default:
                            break;
                    }
                }
            }
        } catch (IOException e) {
            log.info("ReaderFileUtil.readMultipartFileFile has error:{}",e.getMessage());
        }
        return objList;
    }

    public static boolean isChinese(String value) {
        Pattern pattern = Pattern.compile("[\u0391-\uFFE5]+$");
        return pattern.matcher(value).matches();
    }

    public static String countryMap(String countryKey) {
        return countryMap.get(countryKey) == null ? "" : countryMap.get(countryKey);
    }
}
