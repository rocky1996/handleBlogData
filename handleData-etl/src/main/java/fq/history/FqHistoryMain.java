package fq.history;

import common.bean.Constance;
import common.util.DateUtil;
import fq.history.mapreduce.FQHistoryMapper;
import fq.history.mapreduce.FQHistoryReducer;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.LazyOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

public class FqHistoryMain extends Configured implements Tool {

    @Override
    public int run(String[] args) throws Exception {

        String date = DateUtil.getDate();
        if (args.length >= 1) {
            date = args[0];
        }

        Configuration conf = getConf();
        conf.set("platform", Constance.FQ);
        conf.set("date",date);
        Job job = Job.getInstance(conf);
        job.setJarByClass(FqHistoryMain.class);

        job.setMapperClass(FQHistoryMapper.class);
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(Text.class);

        job.setReducerClass(FQHistoryReducer.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(NullWritable.class);

        LazyOutputFormat.setOutputFormatClass(job, TextOutputFormat.class);
        String input = "input/" + Constance.FQ + "/history/";
        String output = "output/" + Constance.FQ + "/history/" + date;
        //4.指定job的输入和输出
        FileInputFormat.setInputPaths(job, new Path(input));
        FileOutputFormat.setOutputPath(job, new Path(output));

        //5.执行job
        return job.waitForCompletion(true) ? 0 : 1;
    }

    public static void main(String args[]) throws Exception {
        Constance.init();
        System.exit(ToolRunner.run(new FqHistoryMain(), args));
    }
}
