package org.platform.vehicle.util;

import java.io.IOException;
import java.util.function.BiConsumer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.bulk.BackoffPolicy;
import org.elasticsearch.action.bulk.BulkProcessor;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Author gejiawei
 * @Date 2023/11/10 13:51
 */
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class EsHelper {

    private final RestHighLevelClient restHighLevelClient;


    public void createIndex(String indexName) throws IOException {
        if (doesIndexExist(indexName)) {
            return;
        }
        this.createIndex(indexName, "_doc", 3, 1, getBalanceAccountEsMapping());
    }

    public void createIndex(String idx, String typ, Integer shardingCount, Integer copyCount,
            String mapping) {
        try {
            CreateIndexRequest request = new CreateIndexRequest(idx);
            request.settings(Settings.builder()
                    // 分片数量
                    .put("index.number_of_shards", shardingCount)
                    // 副本分片数量
                    .put("index.number_of_replicas", copyCount));
            if (mapping != null) {
                request.mapping(typ, mapping, XContentType.JSON);
            }
            CreateIndexResponse createIndexResponse = restHighLevelClient.indices()
                    .create(request, RequestOptions.DEFAULT);
            boolean acknowledged = createIndexResponse.isAcknowledged();
            log.info("创建索引【" + idx + "】" + (acknowledged ? "成功" : "失败"));
        } catch (IOException e) {
            log.error("创建索引【" + idx + "】" + "出现异常", e);
            throw new RuntimeException(e);
        }
    }

    public boolean doesIndexExist(String indexName) throws IOException {
        GetIndexRequest request = new GetIndexRequest(indexName);
        return restHighLevelClient.indices().exists(request, RequestOptions.DEFAULT);
    }

    public static String getBalanceAccountEsMapping() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        sb.append("\"_doc\": {");
        sb.append("\"properties\": {");
        sb.append("\"createTime\": {");
        sb.append("\"type\": \"date\",");
        sb.append("\"format\": \"yyyy-MM-dd HH:mm:ss||yyyy-MM-dd||epoch_millis\"");
        sb.append("},");
        sb.append("\"deviceTime\": {");
        sb.append("\"type\": \"date\",");
        sb.append("\"format\": \"yyyy-MM-dd HH:mm:ss||yyyy-MM-dd||epoch_millis\"");
        sb.append("}");
        sb.append("}}}");
        return sb.toString();
    }

    public BulkProcessor bulkProcessor() {
        BiConsumer<BulkRequest, ActionListener<BulkResponse>> bulkConsumer =
                (request, bulkListener) -> restHighLevelClient.bulkAsync(request,
                        RequestOptions.DEFAULT, bulkListener);
        return BulkProcessor.builder(bulkConsumer, new BulkProcessor.Listener() {
                    @Override
                    public void beforeBulk(long executionId, BulkRequest request) {
                    }

                    @Override
                    public void afterBulk(long executionId, BulkRequest request, BulkResponse response) {
                        if (response.hasFailures()) {
                            log.info("写入ES 失败" + response.buildFailureMessage());
                        }
                    }

                    @Override
                    public void afterBulk(long executionId, BulkRequest request, Throwable failure) {
                        log.error("写入ES 重新消费" + failure);
                    }
                })
                //  达到刷新的条数
                .setBulkActions(100)
                // 达到刷新的大小
//                .setBulkSize(new ByteSizeValue(5, ByteSizeUnit.MB))
                // 固定刷新的时间频率
                .setFlushInterval(TimeValue.timeValueSeconds(10))
                // 固定刷新的时间频率
                .setConcurrentRequests(1)
                // 重试补偿策略
                .setBackoffPolicy(
                        BackoffPolicy.exponentialBackoff(TimeValue.timeValueMillis(1000), 3))
                .build();
    }
}
