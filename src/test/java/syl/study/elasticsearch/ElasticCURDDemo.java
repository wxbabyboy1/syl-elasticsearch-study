package syl.study.elasticsearch;

import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.junit.Test;
import syl.study.elasticsearch.client.ESUtil;
import syl.study.elasticsearch.model.Member;
import syl.study.utils.FastJsonUtil;

import java.net.UnknownHostException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * 该实例是以elasticSearch官方的java客户端TransportClient 做的演示
 * 包括： 1.获取连接
 *        2.添加索引
 *        3.根据主键ID获取索引
 *        4.根据其他字段检索索引
 *        5.更新索引
 *        6.删除索引
 * Created by shiyanlei on 2016/10/12.
 * ES Version ： 2.4.0
 */
public class ElasticCURDDemo extends BaseElasticSearchTest {



    /**
     * 添加索引
     * @throws UnknownHostException
     */
    @Test
    public void addIndex() throws UnknownHostException {
        //插入数据到elasticsearch
        Member member = new Member();
        member.setAge(14);
        member.setBirthday(LocalDateTime.now());
        member.setName("zhangsan");
        member.setId(2);
        member.setUserId(123456);
        member.setPrice(12.4);
//        member.setPric(12.5f);
        ESUtil.addIndex(member);
    }


    /**
     * 根据其他字段检索索引
     * @throws UnknownHostException
     */
    @Test
    public void getIndex() throws UnknownHostException {
        TermQueryBuilder query = QueryBuilders.termQuery("name", "1");
        SearchResponse response = client.prepareSearch("customer")
                .setTypes("custom")
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                .setQuery(query)
                .execute().actionGet();
        System.out.println(response.toString());
        client.close();

    }

    /**
     * 根据主键ID获取索引
     * @throws UnknownHostException
     */
    @Test
    public void getIndexById() throws UnknownHostException {
        GetResponse response = client.prepareGet("customer", "custom", "1").get();

        System.out.println(FastJsonUtil.bean2Json(response));
        client.close();
    }

    /**
     * 更新索引
     * @throws UnknownHostException
     */
    @Test
    public void updateIndex() throws UnknownHostException {
        Map<String,Object> customer = new HashMap<>();
        customer.put("id","2");
        customer.put("age","13");
        customer.put("name",null);
        UpdateResponse response = client.prepareUpdate("customer", "custom", "1").setDoc(customer).get();
        System.out.println(FastJsonUtil.bean2Json(response));
        client.close();
    }

    /**
     * 删除索引
     * @throws UnknownHostException
     */
    @Test
    public void deleteIndex() throws UnknownHostException {
        DeleteResponse response = client.prepareDelete("customer", "custom", "1").get();
        System.out.println(response.toString());
        client.close();
    }


    public static void main(String[] args) {
        String s = String.format("%016d", 24242342l);
        System.out.println(s);
    }
}
