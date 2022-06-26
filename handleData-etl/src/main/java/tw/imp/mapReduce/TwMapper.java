package tw.imp.mapReduce;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import common.bean.Constance;
import common.util.CommonRule;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;
import tw.imp.bean.TwLog;
import tw.imp.bean.TwResultLog;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalTime;
import java.util.UUID;

public class TwMapper extends Mapper<LongWritable, Text, Text, Text> {

    private String line;
    private Text outputKey = new Text();
    private Text outputValue = new Text();
    private static final Gson gson = new Gson();
    private String platform;
    private String data_source;
    private static final String TW_INFO = "tw_Info_";
    private static final String PHOTO_SUF = "_photo.jpg";
    private PrintWriter errorWriter;

    @Override
    protected void setup(Mapper<LongWritable, Text, Text, Text>.Context context) throws IOException, InterruptedException {
        Configuration conf = context.getConfiguration();
        FileSplit inputSplit = (FileSplit) context.getInputSplit();
        platform = conf.get("platform");
        data_source = inputSplit.getPath().getName();
        String date = conf.get("date");
        String outputPath = "output/" + platform + "/imp/error_log/" + date;
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
        TwLog log;
        try {
            log = gson.fromJson(line, TwLog.class);
        } catch (JsonSyntaxException e) {
            errorWriter.write("JsonSyntaxException : " + line);
            e.printStackTrace();
            return;
        }
        TwResultLog resultLog = getTwResultLog(log);
        outputKey.set(resultLog.getUser_id());
        outputValue.set(gson.toJson(resultLog));
        context.write(outputKey, outputValue);
    }

    private TwResultLog getTwResultLog(TwLog log) {
        TwResultLog resultLog = new TwResultLog();

        resultLog.setUuid(UUID.randomUUID().toString());
        resultLog.setPlatform(platform);
        resultLog.setData_source(data_source);
        resultLog.setCreate_time(LocalTime.now().toString());
        resultLog.setImportance("0");
        resultLog.setRemark(Constance.STRING_DEFAULT);

        resultLog.setSource_id(log.getMd5id());
        resultLog.setUser_id(log.getBlogger_id());
        resultLog.setScreen_name(log.getScreen_name());
        resultLog.setUse_name(log.getFullname());
        resultLog.setUser_url(log.getUser_url());
        resultLog.setUser_avatar(log.getUseravatar());
        resultLog.setLocal_photo_url(TW_INFO + resultLog.getUser_id() + PHOTO_SUF);
        resultLog.setGender(CommonRule.getGender(log.getGender()));
        resultLog.setCountry(log.getCountry());
        resultLog.setCity(log.getUser_addr());
        resultLog.setUser_type(log.getUserType());
        resultLog.setVerified(log.getVerified());
        resultLog.setFollowers_count(log.getFollowers());
        resultLog.setFriend_count(String.valueOf(log.getFollowing()));
        resultLog.setPost_count(String.valueOf(log.getTweets()));
        resultLog.setLike_count(String.valueOf(log.getLikes()));
        resultLog.setSource_create_time(log.getInput_time());
        resultLog.setEmail(log.getEmail());
        resultLog.setLanguage(log.getLang());
        resultLog.setUser_summary(log.getUserflag());

        //unique field
        resultLog.setUser_web_url(log.getUser_web_url());
        resultLog.setBorn_time(log.getBorn_time());
        resultLog.setRegistered_time(log.getRegistered_time());
        resultLog.setBkgd_url(String.valueOf(log.getBkgdurl()));
        resultLog.setListed(log.getListed());
        resultLog.setMoments(log.getMoments());
        resultLog.setProtect(log.getProtect());
        resultLog.setTf_effective(log.getTf_effective());
        resultLog.setTime_zone(log.getTime_zone());
        resultLog.setCom_from(log.getComfrom());
        resultLog.setDiff_time(log.getDiff_time());

        return resultLog;
    }

    @Override
    protected void cleanup(Mapper<LongWritable, Text, Text, Text>.Context context) throws IOException, InterruptedException {
        errorWriter.close();
    }
}
