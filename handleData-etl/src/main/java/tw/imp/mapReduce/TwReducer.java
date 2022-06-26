package tw.imp.mapReduce;

import com.google.gson.Gson;
import common.bean.Constance;
import common.util.CommonRule;
import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.output.MultipleOutputs;
import tw.imp.bean.TwResultLog;

import java.io.IOException;

public class TwReducer extends Reducer<Text, Text, Text, NullWritable> {

    private static final Gson gson = new Gson();
    private MultipleOutputs<Text, NullWritable> multipleOutputs;
    private Text result = new Text();

    @Override
    protected void setup(Reducer<Text, Text, Text, NullWritable>.Context context) throws IOException, InterruptedException {
        multipleOutputs = new MultipleOutputs<>(context);
    }

    @Override
    protected void reduce(Text key, Iterable<Text> values,Context context)
            throws IOException, InterruptedException {
        for (Text value : values){
            TwResultLog log = gson.fromJson(value.toString(), TwResultLog.class);
            String language_type = log.getLanguage();
            if (StringUtils.isBlank(language_type)) {
                language_type = CommonRule.getLanguageType(log.getUser_summary() + log.getUse_name());
            }
            log.setLanguage_type(language_type);
            log.setLanguage(language_type);

            if (StringUtils.isBlank(log.getCountry()) && Constance.languageInfo.get(language_type) != null) {
                log.setCountry(Constance.languageInfo.get(language_type).getCountry());
            }
            result.set(gson.toJson(log));
            multipleOutputs.write(result, NullWritable.get(),language_type);
            return;
        }
    }

    @Override
    protected void cleanup(Reducer<Text, Text, Text, NullWritable>.Context context) throws IOException, InterruptedException {
        multipleOutputs.close();
    }
}
