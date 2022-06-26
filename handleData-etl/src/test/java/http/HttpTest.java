package http;

import com.google.gson.Gson;
import common.util.HttpClientUtil;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.junit.Test;
import tw.imp.bean.TwLog;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HttpTest {

    private static final Gson gson = new Gson();

    @Test
    public void testHttp() throws UnsupportedEncodingException {
        String url = "http://121.196.186.253:14024/detect";
        String line = "{\"id\": 17687, \"md5id\": \"699352cb9aa35b7a9dff5c079c3df247\", \"input_time\": \"4/12/2020 06:24:14+08\", \"user_url\": \"https://twitter.com/nocci_95\", \"user_web_url\": \"https://peing.net/2bb477f5b2b5779\", \"screen_name\": \"nocci_95\", \"blogger_id\": \"995319257914195969\", \"fullname\": \"noci\", \"user_addr\": \"質問箱→\", \"born_time\": \"\", \"registered_time\": \"2018-05-12 15:06:17\", \"useravatar\": \"https://pbs.twimg.com/profile_images/1330047343148855296/Mzb9l4lS_normal.jpg\", \"useravatar_md5\": null, \"useravatar_state\": null, \"bkgdurl\": \"https://pbs.twimg.com/profile_banners/995319257914195969/1605942973\", \"bkgdurl_md5\": null, \"bkgdurl_state\": null, \"userflag\": \"主に古性のち @nocci_84 のひとりごとと心惹かれたお店や場所と旅の話。大事なコミュニティ @dotcolony の事もこっちで投稿してます。ときどき旅人ふたりでRadiotalk中 → https://t.co/8HTcliN4zR\", \"tweets\": 1021, \"following\": 220, \"followers\": \"4344\", \"likes\": 1755, \"listed\": \"44\", \"moments\": \"242\", \"verified\": \"0\", \"protected\": \"0\", \"tf_effective\": \"0\", \"everyday_tweets\": 0, \"update_clr_time\": \"30/11/2020 16:21:05.496262+08\", \"spider_task_id\": \"4f1150072e47d82a1876bf30c48dc0812831b134031e29b7f6ae45d89214bb2e\", \"is_full\": \"1\", \"source_tag\": \"6178642\", \"comfrom\": \"forwardingrelationship\", \"remarks\": \"1130\", \"reserved1\": null, \"reserved2\": null, \"reserved3\": null, \"spidertype\": \"2\", \"lang\": \"\", \"email\": \"\", \"time_zone\": \"\", \"level\": null, \"diff_time\": \"12/5/2018 23:06:17+08\", \"country\": null, \"relation\": null}\n";
        TwLog log = gson.fromJson(line, TwLog.class);
        String text = log.getUserflag() + log.getFullname();
        Map<String,String> params = new HashMap<>();
//        params.put("q",URLEncoder.encode(text, "utf-8"));
        params.put("q",URLEncoder.encode("世昌  谢", "utf-8"));
        String result = HttpClientUtil.doHttpGet(url, params, new HashMap<>());
        System.out.println(result);
    }
}
