package fq.imp.mapreduce;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import common.bean.Constance;
import common.util.CommonRule;
import fq.imp.bean.FqLog;
import fq.imp.bean.FqResultLog;
import is.bean.InsLog;
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

public class FQMapper extends Mapper<LongWritable, Text, Text, Text> {

    private String line;
    private Text outputKey = new Text();
    private Text outputValue = new Text();
    private static final Gson gson = new Gson();
    private String platform;
    private String data_source;
    private static final String FQ_INFO = "fq_Info_";
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
        FqLog log;
        try {
            log = gson.fromJson(line, FqLog.class);
        } catch (JsonSyntaxException e) {
            errorWriter.write("JsonSyntaxException : " + line);
            e.printStackTrace();
            return;
        }
        FqResultLog resultLog = getInsResultLog(log);
        outputKey.set(resultLog.getUser_id());
        outputValue.set(gson.toJson(resultLog));
        context.write(outputKey, outputValue);
    }

    private FqResultLog getInsResultLog(FqLog log) {
        FqResultLog resultLog = new FqResultLog();

        resultLog.setUuid(UUID.randomUUID().toString());
        resultLog.setPlatform(platform);
        resultLog.setData_source(data_source);
        resultLog.setCreate_time(LocalTime.now().toString());
        resultLog.setImportance("0");
        resultLog.setRemark(Constance.STRING_DEFAULT);

        resultLog.setSource_id(String.valueOf(log.getId()));
        resultLog.setUser_id(log.getUser_id());
        resultLog.setScreen_name(log.getNickName());
        resultLog.setUse_name(log.getFirstName() + " "  + log.getLastName());
        resultLog.setUser_url(log.getCanonicalUrl());
        resultLog.setUser_avatar(log.getPhoto_url());
        resultLog.setLocal_photo_url(FQ_INFO + resultLog.getUser_id() + PHOTO_SUF);
        resultLog.setGender(CommonRule.getGender(log.getGender()));
        resultLog.setCountry(log.getCountryCode());
        resultLog.setCity(log.getHomeCity());
        resultLog.setUser_type(log.getUserType());
        resultLog.setVerified("");
        resultLog.setFollowers_count(log.getFollower_count());
        resultLog.setFriend_count(log.getFollowing_count());
        resultLog.setPost_count(log.getTips_count());
        resultLog.setLike_count("0");
        resultLog.setSource_create_time(log.getCrawlTime());
        resultLog.setUser_summary(log.getBio());

        //unique field
        resultLog.setContact(log.getContact());
        resultLog.setAlg_remark(log.getAlg_remark());
        resultLog.setAlg_result(log.getAlgResult());
        resultLog.setCheckins_count(log.getCheckins_count());
        resultLog.setCountry_region(log.getCountryRegion());
        resultLog.setFirst_name(log.getFirstName());
        resultLog.setHome_city(String.valueOf(log.getHomeCity()));
        resultLog.setLast_name(log.getLastName());
        resultLog.setLenses(log.getLenses());
        resultLog.setLists(log.getLists());
        resultLog.setLists_count(log.getLists_count());
        resultLog.setLocal_user_head_url(log.getLocalUserHeadUrl());
        resultLog.setMedia_title(log.getMediaTitle());
        resultLog.setMedia_type_embeded(log.getMediaTypeEmbeded());
        resultLog.setMedia_url(log.getMediaUrl());
        resultLog.setMedia_url_name(log.getMediaUrlName());
        resultLog.setPerson(log.getPerson());
        resultLog.setPhoto(log.getPhoto());
        resultLog.setPhoto_prefix(String.valueOf(log.getPhoto_prefix()));
        resultLog.setPhoto_suffix(log.getPhoto_suffix());
        resultLog.setReal_name(log.getReal_name());
        resultLog.setRecent_list(log.getRecentlist());
        resultLog.setSend_state(log.getSendState());
        resultLog.setSent(log.getSent());
        resultLog.setSent_num(log.getSentNum());
        resultLog.setTodo(log.getTodo());
        resultLog.setTop_tips(log.getToptips());
        resultLog.setVisibility(log.getVisibility());
        resultLog.setVpers(log.getVpers());
        resultLog.setW3_fb_url(log.getW3FbUrl());
        resultLog.setSource_key(String.valueOf(log.getSource_key()));
        resultLog.setSource_venue(log.getSource_venue());
        resultLog.setSource_venue_id(log.getSource_venue_id());
        resultLog.setFriends(log.getFriends());

        return resultLog;
    }

    @Override
    protected void cleanup(Mapper<LongWritable, Text, Text, Text>.Context context) throws IOException, InterruptedException {
        errorWriter.close();
    }
}
