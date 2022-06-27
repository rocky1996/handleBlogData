package fb.imp.mapreduce;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import common.bean.Constance;
import common.util.CommonRule;
import fb.imp.bean.FbLog;
import fb.imp.bean.FbResultLog;
import fq.imp.bean.FqLog;
import fq.imp.bean.FqResultLog;
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

public class FBMapper extends Mapper<LongWritable, Text, Text, Text> {

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
        FbLog log;
        try {
            log = gson.fromJson(line, FbLog.class);
        } catch (JsonSyntaxException e) {
            errorWriter.write("JsonSyntaxException : " + line);
            e.printStackTrace();
            return;
        }
        FbResultLog resultLog = getInsResultLog(log);
        outputKey.set(resultLog.getUser_id());
        outputValue.set(gson.toJson(resultLog));
        context.write(outputKey, outputValue);
    }

    private FbResultLog getInsResultLog(FbLog log) {
        FbResultLog resultLog = new FbResultLog();

        resultLog.setUuid(UUID.randomUUID().toString());
        resultLog.setPlatform(platform);
        resultLog.setData_source(data_source);
        resultLog.setCreate_time(LocalTime.now().toString());
        resultLog.setImportance("0");
        resultLog.setRemark(Constance.STRING_DEFAULT);

        resultLog.setSource_id(String.valueOf(log.getId()));
        resultLog.setUser_id(log.getUserId());
        resultLog.setScreen_name(log.getScreenName());
        resultLog.setUse_name(log.getUserName());
        resultLog.setUser_url(DEFAULT_USER_URL_DOMAIN + log.getId());
        resultLog.setUser_avatar(log.getUserHeadUrl());
        resultLog.setLocal_photo_url(FB_INFO + resultLog.getUser_id() + PHOTO_SUF);
        resultLog.setGender(CommonRule.getGender(log.getUserGender()));
        if (StringUtils.isNotBlank(log.getCountry())) {
            resultLog.setCountry(log.getCountry());
        } else {
            resultLog.setCountry(log.getCountryRegion());
        }
        resultLog.setCity(log.getCountryRegionCity()); //TODO
        resultLog.setUser_type(log.getUserType());
        resultLog.setVerified(log.getIsAttestation());
        resultLog.setFollowers_count(log.getFollowerNumber());
        resultLog.setFriend_count("0");
        resultLog.setPost_count("0");
        resultLog.setLike_count(log.getLikeNumber());
        resultLog.setSource_create_time(log.getCreateTime());
        resultLog.setMobile(log.getUserPhone());
        resultLog.setEmail(log.getContactAndBasicInfo());
        resultLog.setName_userd_before(log.getNameUsedBefore());
        resultLog.setLanguage(log.getUserLanguage());
        resultLog.setUser_religion(log.getUserReligion());
        resultLog.setWorks(log.getWorkMessage());
        resultLog.setLocation(log.getCurrentLocation());
        resultLog.setMarriage(log.getSignificantOther());
        resultLog.setHome_town(log.getHometownMessage());
        resultLog.setUser_summary(log.getUserSummary());

        //unique field
        resultLog.setUser_political_views(log.getUserPoliticalViews());
        resultLog.setUser_systent_name(log.getUserSystentName());
        resultLog.setW3_fb_url(log.getW3FbUrl());
        resultLog.setInstitution_id(log.getInstitutionId());
        resultLog.setIs_community_page(log.getIsCommunityPage());
        resultLog.setCommunication_philosophy(log.getCommunicationPhilosophy());
        resultLog.setHave_product(log.getHaveProduct());
        resultLog.setExchange_number(log.getExchangeNumber());
        resultLog.setVisit_number(log.getVisitNumber());
        resultLog.setFirst_name(log.getFirstName());
        resultLog.setLast_name(log.getLastName());
        resultLog.setTeach_message(log.getTeachMessage());
        resultLog.setAcquisition_time(log.getAcquisitionTime());
        resultLog.setAffective_state(log.getAffectiveState());
        resultLog.setBackground_picture_url(log.getBackgroundPictureUrl());
        resultLog.setBusiness_story(log.getBusinessStory());
        resultLog.setClassify_message(log.getClassifyMessage());
        resultLog.setCom_from(log.getComFrom());
        resultLog.setDm_tag1(log.getDm_tag1());
        resultLog.setCompany_profile(log.getCompanyProfile());
        resultLog.setCountry_region(log.getCountryRegion());
        resultLog.setCountry_region_city(log.getCountryRegionCity());
        resultLog.setDetailed_summary(log.getDetailedSummary());
        resultLog.setFavorite_quotes(log.getFavoriteQuotes());
        resultLog.setFound(log.getFound());
        resultLog.setGender_orientation(log.getGenderOrientation());
        resultLog.setGo_through(log.getGoThrough());
        resultLog.setLike_number_int(log.getLikeNumberInt());
        resultLog.setLocal_profile_pic_background_url(log.getLocalProfilePicBackgroundUrl());
        resultLog.setMedia_title(log.getMediaTitle());
        resultLog.setMedia_type_embeded(log.getMediaTypeEmbeded());
        resultLog.setMedia_url(log.getMediaUrl());
        resultLog.setMedia_url_name(log.getMediaUrlName());
        resultLog.setOpening_hours(log.getOpeningHours());
        resultLog.setPersonal_web_url(log.getPersonalWebUrl());
        resultLog.setPhoto_album_url(log.getPhotoAlbumUrl());
        resultLog.setPhoto_wall(log.getPhotoWall());
        resultLog.setPosition_message(log.getPositionMessage());
        resultLog.setRegister_number(log.getRegisterNumber());
        resultLog.setRegistration_date(log.getRegistrationDate());
        resultLog.setRelated_home_page(log.getRelatedHomepage());
        resultLog.setShop_content(log.getShopContent());
        resultLog.setFamily_and_relation_ships(log.getFamilyAndRelationships());
        resultLog.setSkill(log.getSkill());
        resultLog.setUser_birthday(log.getUserBirthday());
        resultLog.setUser_classify(log.getUserClassify());
        resultLog.setUser_description(log.getUserDescription());
        resultLog.setUser_facebook_url(log.getUserFacebookUrl());

        return resultLog;
    }

    @Override
    protected void cleanup(Mapper<LongWritable, Text, Text, Text>.Context context) throws IOException, InterruptedException {
        errorWriter.close();
    }
}
