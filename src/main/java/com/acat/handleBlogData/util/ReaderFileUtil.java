package com.acat.handleBlogData.util;

import lombok.extern.slf4j.Slf4j;

import java.io.*;

@Slf4j
public class ReaderFileUtil {

    /**
     * 读取本地文件
     * @param filePath
     * @return
     */
    public static String readFile(String filePath) {
        try {
            File targetFile = new File(filePath);
            InputStreamReader rdCto = new InputStreamReader(new FileInputStream(targetFile));
            BufferedReader bfReader = new BufferedReader(rdCto);
            String textLine = null;
            while ((textLine = bfReader.readLine()) != null) {
                return textLine;
            }
        } catch (IOException e) {
            log.info("ReaderFileUtil.readFile has error:{}",e.getMessage());
        }
        return null;
    }
}
