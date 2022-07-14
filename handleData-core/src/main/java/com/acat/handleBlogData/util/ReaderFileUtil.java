package com.acat.handleBlogData.util;

import com.acat.handleBlogData.domain.*;
import com.acat.handleBlogData.enums.MediaSourceEnum;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.elasticsearch.common.Numbers;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

@Slf4j
public class ReaderFileUtil {

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
                            LinkUserImplData linkUserImplData = JacksonUtil.strToBean(textLine, LinkUserImplData.class);
                            objList.add(linkUserImplData);
                            break;
                        case LINKEDIN_HISTORY:
                            LInkUserHistoryData lInkUserHistoryData = JacksonUtil.strToBean(textLine, LInkUserHistoryData.class);
                            objList.add(lInkUserHistoryData);
                            break;
                        case LINKEDIN_BUSINESS:
                            LinkBusinessUserData linkBusinessUserData = JacksonUtil.strToBean(textLine, LinkBusinessUserData.class);
                            objList.add(linkBusinessUserData);
                            break;
                        case LINKEDIN_SCHOOL:
                            LinkSchoolUserData linkSchoolUserData = JacksonUtil.strToBean(textLine, LinkSchoolUserData.class);
                            objList.add(linkSchoolUserData);
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

    public static boolean isNumber(String value) {
//        if (NumberUtils.isNumber(value)) {
//            return true;
//        }
//        return false;
        return NumberUtils.isNumber(value) ? true : false;
    }

    /**
     * 判断均大写
     * @param word
     * @return
     */
    public static boolean isAcronym(String word, boolean isLarge) {
        for(int i = 0; i < word.length(); i++) {
            char c = word.charAt(i);
            if (isLarge) {
                if (Character.isLowerCase(c)) {
                    return false;
                }
            }else {
                if (Character.isUpperCase(c)) {
                    return false;
                }
            }
        }
        return true;
    }

    public static void main(String[] args) {
        System.out.println(isAcronym("abc", false));
    }

//    public static void main(String[] args) {
//        System.out.println(isNumber("123ed"));
//    }
}
