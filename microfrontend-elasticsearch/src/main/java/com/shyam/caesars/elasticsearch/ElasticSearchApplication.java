package com.shyam.caesars.elasticsearch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

/* shyam created on 7/7/20 */
@SpringBootApplication
@EnableElasticsearchRepositories(basePackages = "com.shyam.caesars.elasticsearch.repo")
public class ElasticSearchApplication {
    public static void main(String[] args) {
        SpringApplication.run(ElasticSearchApplication.class, args);
    }
}
