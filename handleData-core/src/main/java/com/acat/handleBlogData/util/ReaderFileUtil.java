package com.acat.handleBlogData.util;

import com.acat.handleBlogData.domain.*;
import com.acat.handleBlogData.domain.esEntityV2.*;
import com.acat.handleBlogData.enums.MediaSourceEnum;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.List;
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
     * @param isNewVersion
     * @return
     */
    public static Object readMultipartFileFile(MultipartFile multipartFile, MediaSourceEnum mediaSourceEnum, boolean isNewVersion) {

        List<Object> objList = Lists.newLinkedList();
        try {
            InputStreamReader isr = new InputStreamReader(multipartFile.getInputStream());
            BufferedReader bf = new BufferedReader(isr);
            String textLine;
            while ((textLine = bf.readLine()) != null) {
                if (StringUtils.isNotBlank(textLine)) {
                    switch (mediaSourceEnum) {
                        case TWITTER:
                            if (isNewVersion) {
                                TwitterUserData_v2 twitterUserData_v2 = JacksonUtil.strToBean(textLine, TwitterUserData_v2.class);
                                objList.add(twitterUserData_v2);
                            }else {
                                TwitterUserData twitterUserData = JacksonUtil.strToBean(textLine, TwitterUserData.class);
                                objList.add(twitterUserData);
                            }
                            break;
                        case FB_IMPL:
                            if (isNewVersion) {
                                FbUserData_v2 fbUserData_v2 = JacksonUtil.strToBean(textLine, FbUserData_v2.class);
                                objList.add(fbUserData_v2);
                            }else {
                                FbUserImplData fbUserImplData = JacksonUtil.strToBean(textLine, FbUserImplData.class);
                                objList.add(fbUserImplData);
                            }
                            break;
                        case FB_HISTORY:
                            if (isNewVersion) {
                                FbUserData_v2 fbUserData_v2 = JacksonUtil.strToBean(textLine, FbUserData_v2.class);
                                objList.add(fbUserData_v2);
                            }else {
                                FbUserHistoryData fbUserHistoryData = JacksonUtil.strToBean(textLine, FbUserHistoryData.class);
                                objList.add(fbUserHistoryData);
                            }
                            break;
                        case FQ_IMPL:
                            if (isNewVersion) {
                                FqUserData_v2 fqUserData_v2 = JacksonUtil.strToBean(textLine, FqUserData_v2.class);
                                objList.add(fqUserData_v2);
                            }else {
                                FqUserImplData fqUserImplData = JacksonUtil.strToBean(textLine, FqUserImplData.class);
                                objList.add(fqUserImplData);
                            }
                            break;
                        case FQ_HISTORY:
                            if (isNewVersion) {
                                FqUserData_v2 fqUserData_v2 = JacksonUtil.strToBean(textLine, FqUserData_v2.class);
                                objList.add(fqUserData_v2);
                            }else {
                                FqUserHistoryData fqUserHistoryData = JacksonUtil.strToBean(textLine, FqUserHistoryData.class);
                                objList.add(fqUserHistoryData);
                            }
                            break;
                        case INSTAGRAM:
                            if (isNewVersion) {
                                InstagramUserData_v2 instagramUserData_v2 = JacksonUtil.strToBean(textLine, InstagramUserData_v2.class);
                                objList.add(instagramUserData_v2);
                            }else {
                                InstagramUserData instagramUserData = JacksonUtil.strToBean(textLine, InstagramUserData.class);
                                objList.add(instagramUserData);
                            }
                            break;
                        case LINKEDIN_IMPL:
                            if (isNewVersion) {
                                LinkUserData_v2 linkUserData_v2 = JacksonUtil.strToBean(textLine, LinkUserData_v2.class);
                                objList.add(linkUserData_v2);
                            }else {
                                LinkUserImplData linkUserImplData = JacksonUtil.strToBean(textLine, LinkUserImplData.class);
                                objList.add(linkUserImplData);
                            }
                            break;
                        case LINKEDIN_HISTORY:
                            if (isNewVersion) {
                                LinkUserData_v2 linkUserData_v2 = JacksonUtil.strToBean(textLine, LinkUserData_v2.class);
                                objList.add(linkUserData_v2);
                            }else {
                                LInkUserHistoryData lInkUserHistoryData = JacksonUtil.strToBean(textLine, LInkUserHistoryData.class);
                                objList.add(lInkUserHistoryData);
                            }
                            break;
                        case LINKEDIN_BUSINESS:
                            if (isNewVersion) {
                                LinkBusinessUserData_v2 linkBusinessUserData_v2 = JacksonUtil.strToBean(textLine, LinkBusinessUserData_v2.class);
                                objList.add(linkBusinessUserData_v2);
                            }else {
                                LinkBusinessUserData linkBusinessUserData = JacksonUtil.strToBean(textLine, LinkBusinessUserData.class);
                                objList.add(linkBusinessUserData);
                            }
                            break;
                        case LINKEDIN_SCHOOL:
                            if (isNewVersion) {
                                log.info(textLine);
                                LinkSchoolUserData_v2 linkSchoolUserData_v2 = JacksonUtil.strToBean(textLine, LinkSchoolUserData_v2.class);
                                objList.add(linkSchoolUserData_v2);
                            }else {
                                LinkSchoolUserData linkSchoolUserData = JacksonUtil.strToBean(textLine, LinkSchoolUserData.class);
                                objList.add(linkSchoolUserData);
                            }

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
