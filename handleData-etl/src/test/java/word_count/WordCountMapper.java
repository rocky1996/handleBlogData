package word_count;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

public class WordCountMapper extends Mapper<LongWritable, Text, Text, IntWritable> {

    private String line;
    private Text outputKey = new Text();
    private IntWritable outputValue = new IntWritable(1);


    @Override
    protected void map(LongWritable key1, Text value1, Context context)
            throws IOException, InterruptedException {

        //数据： I like MapReduce
        line = value1.toString();

        //分词：按空格来分词
        String[] words = line.split(" ");

        //输出 k2    v2
        for(String w:words){
            outputKey.set(w);
            context.write(outputKey, outputValue);
        }
    }

}
