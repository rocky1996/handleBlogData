package com.acat.handleBlogData.outerService.outerInterface;

import com.acat.handleBlogData.outerService.outerConstants.OutUrlConstants;
import com.acat.handleBlogData.util.JacksonUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StopWatch;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;

@Service
@Slf4j
@Component
public class TranslateOuterServiceImpl {

    @Resource
    private RestTemplate restTemplate;

    private static String ENGLISH_PARAM_KEY = "srcl";
    private static String CHINESE_PARAM_KEY = "tgtl";
    private static String CHINESE_PARAM_VALUE = "nzh";
    private static String TEXT_KEY = "text";

    public String getTranslateValue(String tranLanguageType, String textValue) {

        try {
            if (StringUtils.isBlank(textValue)) {
                log.info("TranslateOuterServiceImpl.getTranslateValue,textValue is null");
                return null;
            }

            StopWatch stopWatch = new StopWatch();
            stopWatch.start();
            HttpHeaders httpHeader = new HttpHeaders();
            httpHeader.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
            paramMap.add(ENGLISH_PARAM_KEY, "n"+tranLanguageType);
            paramMap.add(CHINESE_PARAM_KEY, CHINESE_PARAM_VALUE);
            paramMap.add(TEXT_KEY, textValue);
            HttpEntity<MultiValueMap<String, Object>> requestParam = new HttpEntity<>(paramMap, httpHeader);
            ResponseEntity<String> outerResp = restTemplate.postForEntity(OutUrlConstants.TRANSLATE_URL, requestParam, String.class);
            stopWatch.stop();
            log.info("获取中译语音翻译数据,requestParam:{},outerResp:{},time:{}", JacksonUtil.beanToStr(requestParam), JacksonUtil.beanToStr(outerResp), stopWatch.getTotalTimeMillis());

            if (HttpStatus.OK != outerResp.getStatusCode()) {
                log.info("TranslateOuterServiceImpl.getTranslateValue,translate failed!!!");
                return null;
            }
            return JacksonUtil.beanToStr(outerResp.getBody());
        }catch (Exception e) {
            log.error("TranslateOuterServiceImpl.getTranslateValue has error",e.getMessage());
            return null;
        }
    }


    /**
     * aa	阿法尔语	fr	法语	li	林堡语	se
     * ab	阿布哈兹语	fy	弗里西亚语	ln	林加拉语	sg
     * ae	阿维斯陀语	ga	爱尔兰语	lo	老挝语	sh
     * af	南非语	gd	苏格兰盖尔语	lt	立陶宛语	si
     * ak	阿坎语	gl	加利西亚语	lu	卢巴语	sk
     * am	阿姆哈拉语	gn	瓜拉尼语	lv	拉脱维亚语	sl
     * an	阿拉贡语	gu	古吉拉特语	mg	马达加斯加语	sm
     * ar	阿拉伯语	gv	马恩岛语	mh	马绍尔语	sn
     * as	阿萨姆语	ha	豪萨语	mi	毛利语	so
     * av	阿瓦尔语	he	希伯来语	mk	马其顿语	sq
     * ay	艾马拉语	hi	印地语	ml	马拉亚拉姆语	sr
     * az	阿塞拜疆语	ho	希里莫图语	mn	蒙古语	ss
     * ba	巴什基尔语	hr	克罗地亚语	mo	摩尔达维亚语	st
     * be	白俄罗斯语	ht	海地克里奥尔语	mr	马拉提语	su
     * bg	保加利亚语	hu	匈牙利语	ms	马来语	sv
     * bh	比哈尔语	hy	亚美尼亚语	mt	马耳他语	sw
     * bi	比斯拉马语	hz	赫雷罗语	my	缅甸语	ta
     * bm	班巴拉语	ia	国际语A	na	瑙鲁语	te
     * bn	孟加拉语	id	印尼语	nb	书面挪威语	tg
     * bo	藏语	ie	国际语E	nd	北恩德贝勒语	th
     * br	布列塔尼语	ig	伊博语	ne	尼泊尔语	ti
     * bs	波斯尼亚语	ii	四川彝语（诺苏语）	ng	恩敦加语	tk
     * ca	加泰隆语	ik	依努庇克语	nl	荷兰语	tl
     * ce	车臣语	io	伊多语	nn	新挪威语	tn
     * ch	查莫罗语	is	冰岛语	no	挪威语	to
     * co	科西嘉语	it	意大利语	nr	南恩德贝勒语	tr
     * cr	克里语	iu	因纽特语	nv	纳瓦霍语	ts
     * cs	捷克语	ja	日语	ny	尼扬贾语	tt
     * cu	古教会斯拉夫语	jv	爪哇语	oc	奥克语	tw
     * cv	楚瓦什语	ka	格鲁吉亚语	oj	奥吉布瓦语	ty
     * cy	威尔士语	kg	刚果语	om	奥洛莫语	ug
     * da	丹麦语	ki	基库尤语	or	奥利亚语	uk
     * de	德语	kj	宽亚玛语	os	奥塞梯语	ur
     * dv	迪维希语	kk	哈萨克语	pa	旁遮普语	uz
     * dz	不丹语	kl	格陵兰语	pi	巴利语	ve
     * ee	埃维语	km	高棉语	pl	波兰语	vi
     * el	现代希腊语	kn	卡纳达语	ps	普什图语	vo
     * en	英语	ko	朝鲜语、韩语	pt	葡萄牙语	wa
     * eo	世界语	kr	卡努里语	qu	凯楚亚语	wo
     * es	西班牙语	ks	克什米尔语	rm	罗曼什语	xh
     * et	爱沙尼亚语	ku	库尔德语	rn	基隆迪语	yi
     * eu	巴斯克语	kv	科米语	ro	罗马尼亚语	yo
     * fa	波斯语	kw	康沃尔语	ru	俄语	za
     * ff	富拉语	ky	吉尔吉斯语	rw	卢旺达语	zh
     * fi	芬兰语	la	拉丁语	sa	梵语	zu
     * fj	斐济语	lb	卢森堡语	sc	萨丁尼亚语
     * fo	法罗语	lg	卢干达语	sd	信德语
     */
}
