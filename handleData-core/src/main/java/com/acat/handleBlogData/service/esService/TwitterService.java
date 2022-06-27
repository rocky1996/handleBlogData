package com.acat.handleBlogData.service.esService;

import com.acat.handleBlogData.domain.esDb.TwitterUserData;
import com.acat.handleBlogData.enums.MediaSourceEnum;
import com.acat.handleBlogData.service.esService.repository.TwitterRepository;
import com.acat.handleBlogData.util.ReaderFileUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.io.File;
import java.util.List;

@Service
@Slf4j
public class TwitterService {

    @Resource
    private TwitterRepository twitterRepository;

    /**
     *
     * @param file
     * @param mediaSourceEnum
     * @return
     */
    @Transactional
    public boolean insertEsData(File file, MediaSourceEnum mediaSourceEnum) {
        try {
            List<TwitterUserData> twitterUserDataList = (List<TwitterUserData>) ReaderFileUtil.readFile(file, MediaSourceEnum.TWITTER);
            if (!CollectionUtils.isEmpty(twitterUserDataList)) {
                twitterRepository.saveAll(twitterUserDataList);
            }
            return true;
        }catch (Exception e) {
            log.error("TwitterService.insertEsData has error:{}",e.getMessage());
        }
        return false;
    }
}
