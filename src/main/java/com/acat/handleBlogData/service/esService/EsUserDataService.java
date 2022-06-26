package com.acat.handleBlogData.service.esService;

import com.acat.handleBlogData.domain.esDb.*;
import com.acat.handleBlogData.enums.MediaSourceEnum;
import com.acat.handleBlogData.service.esService.repository.*;
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
public class EsUserDataService {

    @Resource
    private TwitterRepository twitterRepository;
    @Resource
    private InstagramRepository instagramRepository;
    @Resource
    private FbImplRepository fbImplRepository;
    @Resource
    private FbHistoryRepository fbHistoryRepository;
    @Resource
    private FqImplRepository fqImplRepository;
    @Resource
    private FqHistoryRepository fqHistoryRepository;

    /**
     *
     * @param file
     * @param mediaSourceEnum
     * @return
     */
    @Transactional
    public boolean insertEsData(File file, MediaSourceEnum mediaSourceEnum) {
        try {
            switch (mediaSourceEnum) {
                case TWITTER:
                    List<TwitterUserData> twitterUserDataList = (List<TwitterUserData>) ReaderFileUtil.readFile(file, MediaSourceEnum.TWITTER);
                    if (!CollectionUtils.isEmpty(twitterUserDataList)) {
                        twitterRepository.saveAll(twitterUserDataList);
                    }
                    break;
                case INSTAGRAM:
                    List<InstagramUserData> instagramUserDataList = (List<InstagramUserData>) ReaderFileUtil.readFile(file, MediaSourceEnum.INSTAGRAM);
                    if (!CollectionUtils.isEmpty(instagramUserDataList)) {
                        instagramRepository.saveAll(instagramUserDataList);
                    }
                    break;
                case FB_IMPL:
                    List<FbUserImplData> fbUserImplDataList = (List<FbUserImplData>) ReaderFileUtil.readFile(file, MediaSourceEnum.FB_IMPL);
                    if (!CollectionUtils.isEmpty(fbUserImplDataList)) {
                        fbImplRepository.saveAll(fbUserImplDataList);
                    }
                    break;
                case FB_HISTORY:
                    List<FbUserHistoryData> fbUserHistoryDataList = (List<FbUserHistoryData>) ReaderFileUtil.readFile(file, MediaSourceEnum.FB_HISTORY);
                    if (!CollectionUtils.isEmpty(fbUserHistoryDataList)) {
                        fbHistoryRepository.saveAll(fbUserHistoryDataList);
                    }
                    break;
                case FQ_IMPL:
                    List<FqUserImplData> fqUserImplDataList = (List<FqUserImplData>) ReaderFileUtil.readFile(file, MediaSourceEnum.FQ_IMPL);
                    if (!CollectionUtils.isEmpty(fqUserImplDataList)) {
                        fqImplRepository.saveAll(fqUserImplDataList);
                    }
                    break;
                case FQ_HISTORY:
                    List<FqUserHistoryData> fqUserHistoryData = (List<FqUserHistoryData>) ReaderFileUtil.readFile(file, MediaSourceEnum.FQ_HISTORY);
                    if (!CollectionUtils.isEmpty(fqUserHistoryData)) {
                        fqHistoryRepository.saveAll(fqUserHistoryData);
                    }
                    break;
                default:
                    break;
            }
            return true;
        }catch (Exception e) {
            log.error("TwitterService.insertEsData has error:{}",e.getMessage());
        }
        return false;
    }
}
