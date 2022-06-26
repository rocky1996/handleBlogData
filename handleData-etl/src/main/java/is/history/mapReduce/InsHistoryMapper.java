package is.history.mapReduce;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import common.bean.Constance;
import is.bean.InsHistoryLog;
import is.bean.InsResultLog;
import org.apache.commons.lang.StringUtils;
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

public class InsHistoryMapper extends Mapper<LongWritable, Text, Text, Text> {

    private String line;
    private Text outputKey = new Text();
    private Text outputValue = new Text();
    private static final Gson gson = new Gson();
    private String platform;
    private String data_source;
    private static final String IS_INFO = "in_Info_";
    private static final String PHOTO_SUF = "_photo.jpg";
    private static final String DEFAULT_USER_URL_DOMAIN = "https://www.instagram.com/";
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
        InsHistoryLog log;
        try {
            log = gson.fromJson(line, InsHistoryLog.class);
        } catch (JsonSyntaxException e) {
            errorWriter.write("JsonSyntaxException : " + line);
            e.printStackTrace();
            return;
        }
        InsResultLog resultLog = getInsResultLog(log);
        outputKey.set(resultLog.getUser_id());
        outputValue.set(gson.toJson(resultLog));
        context.write(outputKey, outputValue);
    }

    private InsResultLog getInsResultLog(InsHistoryLog log) {
        InsResultLog resultLog = new InsResultLog();

        resultLog.setUuid(UUID.randomUUID().toString());
        resultLog.setPlatform(platform);
        resultLog.setData_source(data_source);
        resultLog.setCreate_time(LocalTime.now().toString());
        resultLog.setImportance("0");
        resultLog.setRemark(Constance.STRING_DEFAULT);

        resultLog.setSource_id(log.getAccount_id());
        resultLog.setUser_id(log.getAccount_id());
        resultLog.setScreen_name(log.getUsername());
        resultLog.setUse_name(log.getFull_name());
        if (StringUtils.isNotBlank(log.getUsername())) {
            resultLog.setUser_url(DEFAULT_USER_URL_DOMAIN + log.getUsername());
        } else {
            resultLog.setUser_url(DEFAULT_USER_URL_DOMAIN + log.getAccount_id());
        }
        resultLog.setUser_avatar(log.getProfile_pic_url());
        resultLog.setLocal_photo_url(IS_INFO + resultLog.getUser_id() + PHOTO_SUF);
        resultLog.setGender("0");
        resultLog.setCountry(log.getCountry());
        resultLog.setCity(log.getCity());
        resultLog.setUser_type(String.valueOf(log.getIs_private()));
        resultLog.setVerified(String.valueOf(log.getIs_verified()));
        resultLog.setFollowers_count(String.valueOf(log.getFollow_count()));
        resultLog.setFriend_count(String.valueOf(log.getFollow_count()));
        resultLog.setPost_count(String.valueOf(log.getMedia_count()));
        resultLog.setLike_count("0");
        resultLog.setSource_create_time(log.getFeach_time());
        resultLog.setUser_summary(log.getBiography());

        //unique field
        resultLog.setExternal_url(log.getExternal_url());
        resultLog.setFetch_day(log.getFeach_day());
        resultLog.setProfile_pic_url_oss(String.valueOf(log.getProfile_pic_url()));

        return resultLog;
    }

    @Override
    protected void cleanup(Mapper<LongWritable, Text, Text, Text>.Context context) throws IOException, InterruptedException {
        errorWriter.close();
    }
}
