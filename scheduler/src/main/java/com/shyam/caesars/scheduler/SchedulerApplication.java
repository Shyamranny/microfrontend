package com.shyam.caesars.scheduler;

import com.shyam.caesard.rest.client.RestClientApplication;
import com.shyam.caesars.elasticsearch.ElasticSearchApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableScheduling;

/* shyam created on 7/7/20 */
@SpringBootApplication
@EnableScheduling
@Import({RestClientApplication.class, ElasticSearchApplication.class})
public class SchedulerApplication {
    public static void main(String[] args) {
        SpringApplication.run(SchedulerApplication.class, args);
    }
}
