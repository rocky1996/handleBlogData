package com.acat.handleBlogData.util;

import com.acat.handleBlogData.domain.esDb.TwitterUserData;
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
     *
     * @param multipartFile to File
     * @return
     */
    public static File transferToFile(MultipartFile multipartFile) {
//        选择用缓冲区来实现这个转换即使用java 创建的临时文件 使用 MultipartFile.transferto()方法 。
        File file = null;
        try {
            String originalFilename = multipartFile.getOriginalFilename();
            String[] filename = originalFilename.split("\\.");
            file=File.createTempFile(filename[0], filename[1]);
            multipartFile.transferTo(file);
            file.deleteOnExit();
        } catch (IOException e) {
            log.info("ReaderFileUtil.transferToFile has error:{}",e.getMessage());
        }
        return file;
    }
}
