package com.acat.handleBlogData.service.esService;

import com.acat.handleBlogData.domain.esDb.*;
import com.acat.handleBlogData.enums.MediaSourceEnum;
import com.acat.handleBlogData.service.emailService.SendEmailServiceImpl;
import com.acat.handleBlogData.service.emailService.vo.SendEmailReq;
import com.acat.handleBlogData.service.esService.repository.*;
import com.acat.handleBlogData.util.ReaderFileUtil;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class EsServiceImpl {

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
    @Resource
    private SendEmailServiceImpl sendEmailService;
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
                        List<TwitterUserData> dataList = (List<TwitterUserData>) twitterRepository.saveAll(twitterUserDataList);
                        if (CollectionUtils.isEmpty(dataList)) {
                            sendEmailService.sendSimpleEmail(covBean(MediaSourceEnum.TWITTER));
                            return false;
                        }
                    }
                    break;
                case INSTAGRAM:
                    List<InstagramUserData> instagramUserDataList = (List<InstagramUserData>) ReaderFileUtil.readMultipartFileFile(file, MediaSourceEnum.INSTAGRAM);
                    if (!CollectionUtils.isEmpty(instagramUserDataList)) {
                        List<InstagramUserData> dataList = (List<InstagramUserData>) instagramRepository.saveAll(instagramUserDataList);
                        if (CollectionUtils.isEmpty(dataList)) {
                            sendEmailService.sendSimpleEmail(covBean(MediaSourceEnum.INSTAGRAM));
                            return false;
                        }
                    }
                    break;
                case FB_IMPL:
                    List<FbUserImplData> fbUserImplDataList = (List<FbUserImplData>) ReaderFileUtil.readMultipartFileFile(file, MediaSourceEnum.FB_IMPL);
                    if (!CollectionUtils.isEmpty(fbUserImplDataList)) {
                        List<FbUserImplData> dataList = (List<FbUserImplData>) fbImplRepository.saveAll(fbUserImplDataList);
                        if (CollectionUtils.isEmpty(dataList)) {
                            sendEmailService.sendSimpleEmail(covBean(MediaSourceEnum.FB_IMPL));
                            return false;
                        }                    }
                    break;
                case FB_HISTORY:
                    List<FbUserHistoryData> fbUserHistoryDataList = (List<FbUserHistoryData>) ReaderFileUtil.readMultipartFileFile(file, MediaSourceEnum.FB_HISTORY);
                    if (!CollectionUtils.isEmpty(fbUserHistoryDataList)) {
                        List<FbUserHistoryData> dataList = (List<FbUserHistoryData>) fbHistoryRepository.saveAll(fbUserHistoryDataList);
                        if (CollectionUtils.isEmpty(dataList)) {
                            sendEmailService.sendSimpleEmail(covBean(MediaSourceEnum.FB_HISTORY));
                            return false;
                        }
                    }
                    break;
                case FQ_IMPL:
                    List<FqUserImplData> fqUserImplDataList = (List<FqUserImplData>) ReaderFileUtil.readMultipartFileFile(file, MediaSourceEnum.FQ_IMPL);
                    if (!CollectionUtils.isEmpty(fqUserImplDataList)) {
                        List<FqUserImplData> dataList = (List<FqUserImplData>) fqImplRepository.saveAll(fqUserImplDataList);
                        if (CollectionUtils.isEmpty(dataList)) {
                            sendEmailService.sendSimpleEmail(covBean(MediaSourceEnum.FQ_IMPL));
                            return false;
                        }
                    }
                    break;
                case FQ_HISTORY:
                    List<FqUserHistoryData> fqUserHistoryData = (List<FqUserHistoryData>) ReaderFileUtil.readMultipartFileFile(file, MediaSourceEnum.FQ_HISTORY);
                    if (!CollectionUtils.isEmpty(fqUserHistoryData)) {
                        List<FqUserHistoryData> dataList = (List<FqUserHistoryData>) fqHistoryRepository.saveAll(fqUserHistoryData);
                        if (CollectionUtils.isEmpty(dataList)) {
                            sendEmailService.sendSimpleEmail(covBean(MediaSourceEnum.FQ_HISTORY));
                            return false;
                        }
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

    /**
     * 组装
     * @param mediaSourceEnum
     * @return
     */
    private SendEmailReq covBean(MediaSourceEnum mediaSourceEnum) {
        SendEmailReq emailReq = SendEmailReq
                .builder()
                .subject("落es库失败")
                .content("您好,系统于北京时间" + new Date() + "入" + mediaSourceEnum.name() + "类型数据时失败,请联系rd紧急排查,谢谢！！！")
                .build();
        return emailReq;
    }
}
