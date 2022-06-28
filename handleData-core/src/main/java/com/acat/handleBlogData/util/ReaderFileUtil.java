package com.acat.handleBlogData.util;

import com.acat.handleBlogData.domain.esDb.*;
import com.acat.handleBlogData.enums.MediaSourceEnum;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.List;

@Slf4j
public class ReaderFileUtil {

    /**
     * 读取本地文件
     * @param targetFile
     * @return
     */
    public static Object readFile(File targetFile, MediaSourceEnum mediaSourceEnum) {

        List<Object> objList = Lists.newLinkedList();
        try {
            InputStreamReader rdCto = new InputStreamReader(new FileInputStream(targetFile));
            BufferedReader bfReader = new BufferedReader(rdCto);
            String textLine = null;
            while ((textLine = bfReader.readLine()) != null) {
                if (StringUtils.isNotBlank(textLine)) {

                    switch (mediaSourceEnum) {
                        case TWITTER:
                            TwitterUserData twitterUserData = JacksonUtil.strToBean(textLine, TwitterUserData.class);
                            objList.add(twitterUserData);
                            break;
                        case FB_IMPL:
                            break;
                        case FB_HISTORY:
                            break;
                        case FQ_IMPL:
                            break;
                        case FQ_HISTORY:
                            break;
                        case INSTAGRAM:
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
            log.info("ReaderFileUtil.readFile has error:{}",e.getMessage());
        }
        return objList;
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
}
