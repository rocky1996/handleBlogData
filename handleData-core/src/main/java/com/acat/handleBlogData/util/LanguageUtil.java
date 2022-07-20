//package com.acat.handleBlogData.util;
//
//import java.util.HashMap;
//import java.util.Map;
//
//public class LanguageUtil {
//
//    public static Map<String, String> languageMap = new HashMap<>();
//    static {
//        languageMap.put("aa", "阿法尔语");
//        languageMap.put("fr", "法语");
//        languageMap.put("li", "林堡语");
//        languageMap.put("se", "北萨米语");
//    }
//    /**
//     * aa	阿法尔语	fr	法语	li	林堡语	se	北萨米语
//     * ab	阿布哈兹语	fy	弗里西亚语	ln	林加拉语	sg	桑戈语
//     * ae	阿维斯陀语	ga	爱尔兰语	lo	老挝语	sh	塞尔维亚-克罗地亚语
//     * af	南非语	gd	苏格兰盖尔语	lt	立陶宛语	si	僧加罗语
//     * ak	阿坎语	gl	加利西亚语	lu	卢巴语	sk	斯洛伐克语
//     * am	阿姆哈拉语	gn	瓜拉尼语	lv	拉脱维亚语	sl	斯洛文尼亚语
//     * an	阿拉贡语	gu	古吉拉特语	mg	马达加斯加语	sm	萨摩亚语
//     * ar	阿拉伯语	gv	马恩岛语	mh	马绍尔语	sn	绍纳语
//     * as	阿萨姆语	ha	豪萨语	mi	毛利语	so	索马里语
//     * av	阿瓦尔语	he	希伯来语	mk	马其顿语	sq	阿尔巴尼亚语
//     * ay	艾马拉语	hi	印地语	ml	马拉亚拉姆语	sr	塞尔维亚语
//     * az	阿塞拜疆语	ho	希里莫图语	mn	蒙古语	ss	斯瓦特语
//     * ba	巴什基尔语	hr	克罗地亚语	mo	摩尔达维亚语	st	南索托语
//     * be	白俄罗斯语	ht	海地克里奥尔语	mr	马拉提语	su	巽他语
//     * bg	保加利亚语	hu	匈牙利语	ms	马来语	sv	瑞典语
//     * bh	比哈尔语	hy	亚美尼亚语	mt	马耳他语	sw	斯瓦希里语
//     * bi	比斯拉马语	hz	赫雷罗语	my	缅甸语	ta	泰米尔语
//     * bm	班巴拉语	ia	国际语A	na	瑙鲁语	te	泰卢固语
//     * bn	孟加拉语	id	印尼语	nb	书面挪威语	tg	塔吉克斯坦语
//     * bo	藏语	ie	国际语E	nd	北恩德贝勒语	th	泰语
//     * br	布列塔尼语	ig	伊博语	ne	尼泊尔语	ti	提格里尼亚语
//     * bs	波斯尼亚语	ii	四川彝语（诺苏语）	ng	恩敦加语	tk	土库曼语
//     * ca	加泰隆语	ik	依努庇克语	nl	荷兰语	tl	他加禄语
//     * ce	车臣语	io	伊多语	nn	新挪威语	tn	塞茨瓦纳语
//     * ch	查莫罗语	is	冰岛语	no	挪威语	to	汤加语
//     * co	科西嘉语	it	意大利语	nr	南恩德贝勒语	tr	土耳其语
//     * cr	克里语	iu	因纽特语	nv	纳瓦霍语	ts	宗加语
//     * cs	捷克语	ja	日语	ny	尼扬贾语	tt	塔塔尔语
//     * cu	古教会斯拉夫语	jv	爪哇语	oc	奥克语	tw	特威语
//     * cv	楚瓦什语	ka	格鲁吉亚语	oj	奥吉布瓦语	ty	塔希提语
//     * cy	威尔士语	kg	刚果语	om	奥洛莫语	ug	维吾尔语
//     * da	丹麦语	ki	基库尤语	or	奥利亚语	uk	乌克兰语
//     * de	德语	kj	宽亚玛语	os	奥塞梯语	ur	乌尔都语
//     * dv	迪维希语	kk	哈萨克语	pa	旁遮普语	uz	乌兹别克语
//     * dz	不丹语	kl	格陵兰语	pi	巴利语	ve	文达语
//     * ee	埃维语	km	高棉语	pl	波兰语	vi	越南语
//     * el	现代希腊语	kn	卡纳达语	ps	普什图语	vo	沃拉普克语
//     * en	英语	ko	朝鲜语、韩语	pt	葡萄牙语	wa	沃伦语
//     * eo	世界语	kr	卡努里语	qu	凯楚亚语	wo	沃洛夫语
//     * es	西班牙语	ks	克什米尔语	rm	罗曼什语	xh	科萨语
//     * et	爱沙尼亚语	ku	库尔德语	rn	基隆迪语	yi	依地语
//     * eu	巴斯克语	kv	科米语	ro	罗马尼亚语	yo	约鲁巴语
//     * fa	波斯语	kw	康沃尔语	ru	俄语	za	壮语
//     * ff	富拉语	ky	吉尔吉斯语	rw	卢旺达语	zh	中文、汉语
//     * fi	芬兰语	la	拉丁语	sa	梵语	zu	祖鲁语
//     * fj	斐济语	lb	卢森堡语	sc	萨丁尼亚语
//     * fo	法罗语	lg	卢干达语	sd	信德语
//     * 自RFC 3066出版后，ISO 639-1 新增了以下语言：
//     * ISO 639-1	ISO 639-2	名称	更改日期	更改类型	曾用代码
//     * io	ido	伊多语	2002年1月15日	新增	art
//     * wa	wln	瓦龙语	2002年1月29日	新增	roa
//     * li	lim	林堡语	2002年8月2日	新增	gem
//     * ii	iii	四川省彝语（诺苏语）	2002年10月14日	新增
//     * an	arg	阿拉贡语	2002年12月23日	新增	roa
//     * ht	hat	海地克里奥尔语	2003年2月26日	新增	cpf
//     */
//}
