package com.crawler.bdzp;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created with IDEA
 * User: vector
 * Data: 2017/11/30
 * Time: 14:42
 * Description:
 */
public class TestMain {
    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        Random random = new Random();
        for(int i=0;i<100;i++) {
            executorService.execute(()-> {
                System.out.println(random.nextInt(1000));
            });
        }
        executorService.shutdown();
    }
}
