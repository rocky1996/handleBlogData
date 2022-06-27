package com.acat.handleBlogData.service.esService;

import com.acat.handleBlogData.domain.esDb.*;
import com.acat.handleBlogData.enums.MediaSourceEnum;
import com.acat.handleBlogData.service.esService.repository.*;
import com.acat.handleBlogData.util.ReaderFileUtil;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.List;

@Service
@Slf4j
public class EsServiceImpl {

    @Resource
    private TwitterRepository twitterRepository;
    @Resource
    private InstagramRepository instagramRepository;
//    @Resource
//    private FbImplRepository fbImplRepository;
    @Resource
    private FbHistoryRepository fbHistoryRepository;
    @Resource
    private FqImplRepository fqImplRepository;
    @Resource
    private FqHistoryRepository fqHistoryRepository;
    //标准桶大小
    private static final Integer LIMIT_SIZE = 100;

    /**
     *
     * @param file
     * @param mediaSourceEnum
     * @return
     */
    @Transactional
    public boolean insertEsData(MultipartFile file, MediaSourceEnum mediaSourceEnum) {
        if (file == null) {
            return false;
        }
        try {
            switch (mediaSourceEnum) {
                case TWITTER:
                    List<TwitterUserData> twitterUserDataList = (List<TwitterUserData>) ReaderFileUtil.readMultipartFileFile(file, MediaSourceEnum.TWITTER);
                    if (!CollectionUtils.isEmpty(twitterUserDataList)) {
                        Lists.partition(twitterUserDataList, LIMIT_SIZE).forEach(twitter -> twitterRepository.saveAll(twitter));
                    }
                    break;
                case INSTAGRAM:
                    List<InstagramUserData> instagramUserDataList = (List<InstagramUserData>) ReaderFileUtil.readMultipartFileFile(file, MediaSourceEnum.INSTAGRAM);
                    if (!CollectionUtils.isEmpty(instagramUserDataList)) {
                        Lists.partition(instagramUserDataList, LIMIT_SIZE).forEach(instagram -> instagramRepository.saveAll(instagram));
                    }
                    break;
//                case FB_IMPL:
//                    List<FbUserImplData> fbUserImplDataList = (List<FbUserImplData>) ReaderFileUtil.readMultipartFileFile(file, MediaSourceEnum.FB_IMPL);
//                    if (!CollectionUtils.isEmpty(fbUserImplDataList)) {
//                        Lists.partition(fbUserImplDataList, LIMIT_SIZE).forEach(fbImpl -> fbImplRepository.saveAll(fbImpl));
//                    }
//                    break;
                case FB_HISTORY:
                    List<FbUserHistoryData> fbUserHistoryDataList = (List<FbUserHistoryData>) ReaderFileUtil.readMultipartFileFile(file, MediaSourceEnum.FB_HISTORY);
                    if (!CollectionUtils.isEmpty(fbUserHistoryDataList)) {
                        Lists.partition(fbUserHistoryDataList, LIMIT_SIZE).forEach(fbHistory -> fbHistoryRepository.saveAll(fbHistory));
                    }
                    break;
                case FQ_IMPL:
                    List<FqUserImplData> fqUserImplDataList = (List<FqUserImplData>) ReaderFileUtil.readMultipartFileFile(file, MediaSourceEnum.FQ_IMPL);
                    if (!CollectionUtils.isEmpty(fqUserImplDataList)) {
                        Lists.partition(fqUserImplDataList, LIMIT_SIZE).forEach(fqImpl -> fqImplRepository.saveAll(fqImpl));
                    }
                    break;
                case FQ_HISTORY:
                    List<FqUserHistoryData> fqUserHistoryData = (List<FqUserHistoryData>) ReaderFileUtil.readMultipartFileFile(file, MediaSourceEnum.FQ_HISTORY);
                    if (!CollectionUtils.isEmpty(fqUserHistoryData)) {
                        Lists.partition(fqUserHistoryData, LIMIT_SIZE).forEach(fqHistory -> fqHistoryRepository.saveAll(fqHistory));
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
