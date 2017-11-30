package com.crawler.bdzp.sechedule;

import com.crawler.bdzp.service.CrawlerService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * Created with IDEA
 * User: vector
 * Data: 2017/11/30
 * Time: 14:30
 * Description:
 */
@Component
public class CrawlerJob {
    @Resource
    private CrawlerService crawlerService;

    @Scheduled(cron = "0 0 1 * * ?")// 每天的凌晨一点执行
    public void run(){
        try {
            crawlerService.crawlerCompanyList();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
