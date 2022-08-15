package com.acat.handleBlogData.config;

import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Configuration
@Component
public class EsConfig {

    @Value("${spring.elasticsearch.rest.uris}")
    private String hostList;

    /**
     * 高版本客户端
     * @return
     */
    @Bean
    public RestHighLevelClient restHighLevelClient() {
        // 解析 hostList 配置信息。假如以后有多个，则需要用 ， 分开
        String[] split = hostList.split(",");
        // 创建 HttpHost 数组，其中存放es主机和端口的配置信息
        HttpHost[] httpHostArray = new HttpHost[split.length];
        for (int i = 0; i < split.length; i++) {
            String item = split[i];
            httpHostArray[i] = new HttpHost(item.split(":")[0], Integer.parseInt(item.split(":")[1]), "http");
        }

//        RestClient
//                .builder(httpHostArray)
//                .setRequestConfigCallback(new RestClientBuilder.RequestConfigCallback() {
//                    // 该方法接收一个RequestConfig.Builder对象，对该对象进行修改后然后返回。
//                    @Override
//                    public RequestConfig.Builder customizeRequestConfig(
//                            RequestConfig.Builder requestConfigBuilder) {
//                        return requestConfigBuilder.setConnectTimeout(5000 * 1000) // 连接超时（默认为1秒）
//                                .setSocketTimeout(6000 * 1000);// 套接字超时（默认为30秒）//更改客户端的超时限制默认30秒现在改为100*1000分钟
//                    }
//                });
        return new RestHighLevelClient(RestClient
                .builder(httpHostArray)
                .setRequestConfigCallback(new RestClientBuilder.RequestConfigCallback() {
                    // 该方法接收一个RequestConfig.Builder对象，对该对象进行修改后然后返回。
                    @Override
                    public RequestConfig.Builder customizeRequestConfig(
                            RequestConfig.Builder requestConfigBuilder) {
                        return requestConfigBuilder
                                .setConnectTimeout(30000) // 连接超时（默认为1秒）
                                .setSocketTimeout(90000)
                                .setConnectionRequestTimeout(90000); // 套接字超时（默认为30秒）//更改客户端的超时限制默认30秒现在改为100*1000分钟
                    }
                }));
    }
}
