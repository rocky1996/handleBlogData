package fb.history.mapreduce;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import common.bean.Constance;
import common.util.CommonRule;
import common.util.PhoneUtil;
import fb.history.bean.FbHistoryLog;
import fb.history.bean.FbHistoryResultLog;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalTime;
import java.util.UUID;

public class FBHistoryMapper extends Mapper<LongWritable, Text, Text, Text> {

    private String line;
    private Text outputKey = new Text();
    private Text outputValue = new Text();
    private static final Gson gson = new Gson();
    private String platform;
    private String data_source;
    private static final String FB_INFO = "fb_Info_";
    private static final String PHOTO_SUF = "_photo.jpg";
    private static final String DEFAULT_USER_URL_DOMAIN = "https://www.facebook.com/profile.php?id=";
    private PrintWriter errorWriter;

    @Override
    protected void setup(Mapper<LongWritable, Text, Text, Text>.Context context) throws IOException, InterruptedException {
        Configuration conf = context.getConfiguration();
        FileSplit inputSplit = (FileSplit) context.getInputSplit();
        platform = conf.get("platform");
        data_source = inputSplit.getPath().getName();
        String date = conf.get("date");
        String outputPath = "output/" + platform + "/history/error_log/" + date;
        File errorPath = new File(outputPath);
        if (!errorPath.exists()) {
            errorPath.mkdirs();
        }
        errorWriter = new PrintWriter(outputPath + "/error_log");
    }

    @Override
    protected void map(LongWritable key, Text value, Context context)
            throws IOException, InterruptedException {
        line = value.toString();
        FbHistoryLog log;
        try {
            log = gson.fromJson(line, FbHistoryLog.class);
        } catch (JsonSyntaxException e) {
            errorWriter.write("JsonSyntaxException : " + line);
            e.printStackTrace();
            return;
        }
        FbHistoryResultLog resultLog = getInsResultLog(log);
        outputKey.set(resultLog.getUser_id());
        outputValue.set(gson.toJson(resultLog));
        context.write(outputKey, outputValue);
    }

    private FbHistoryResultLog getInsResultLog(FbHistoryLog log) {
        FbHistoryResultLog resultLog = new FbHistoryResultLog();

        resultLog.setUuid(UUID.randomUUID().toString());
        resultLog.setPlatform(platform);
        resultLog.setData_source(data_source);
        resultLog.setCreate_time(LocalTime.now().toString());
        resultLog.setImportance("0");
        resultLog.setRemark(Constance.STRING_DEFAULT);

        resultLog.setSource_id(String.valueOf(log.getUserId()));
        resultLog.setUser_id(log.getUserId());
        resultLog.setScreen_name(log.getScreenName());
        resultLog.setUse_name(log.getName());
        resultLog.setUser_url(DEFAULT_USER_URL_DOMAIN + log.getUserId());
        resultLog.setUser_avatar(log.getUserAvatar());
        resultLog.setLocal_photo_url(FB_INFO + resultLog.getUser_id() + PHOTO_SUF);
        resultLog.setGender(CommonRule.getGender(log.getGender()));
        resultLog.setCountry(log.getCountry());
        resultLog.setCity(log.getCity());
        resultLog.setUser_type(log.getUserType());
        resultLog.setVerified(String.valueOf(log.getVerified()));
        resultLog.setFollowers_count(log.getFlwCnt());
        resultLog.setFriend_count(log.getFrdCnt());
        try {
            resultLog.setMobile((String) PhoneUtil.getPhoneNumberInfo(log.getPhoneNum()).get("formatNumber"));
        } catch (Exception e) {
            errorWriter.write("Phone Parse Exception : " + line);
            e.printStackTrace();
            resultLog.setMobile(log.getPhoneNum());
        }
        resultLog.setEmail(log.getEmail());
        resultLog.setLanguage(log.getLanguageCode());
        resultLog.setWorks(log.getWorks());
        resultLog.setLocation(log.getLocation());
        resultLog.setMarriage(log.getMarriage());
        resultLog.setHome_town(log.getHometown());

        //unique field
        resultLog.setUser_systent_name(log.getNickName());
        resultLog.setRegistration_date(log.getRegist());
        resultLog.setUser_birthday(log.getBirthday());
        resultLog.setUser_classify(log.getUserTag());
        resultLog.setVerified_reason(log.getVerifiedReason());

        return resultLog;
    }

    @Override
    protected void cleanup(Mapper<LongWritable, Text, Text, Text>.Context context) throws IOException, InterruptedException {
        errorWriter.close();
    }
}
