package com.crawler.bdzp.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.crawler.bdzp.entity.AbstractCompany;
import com.crawler.bdzp.entity.AppointCompanyDetail;
import com.crawler.bdzp.entity.BaiduzhaopinCompanyDetail;
import com.crawler.bdzp.repository.AppointCompanyDetailRepo;
import com.crawler.bdzp.repository.BaiduzhaopinCompanyDetailRepo;
import org.apache.log4j.Logger;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created with IDEA
 * User: vector
 * Data: 2017/11/20
 * Time: 10:32
 * Description:
 */
@Service
public class CrawlerService {

    private Logger logger = Logger.getLogger(CrawlerService.class);

    private static int PAGE_TOTAL = 38;

    @Value("${address.list}")
    private String addressString;

    @Autowired
    private BaiduzhaopinCompanyDetailRepo detailRepo;

    @Autowired
    private AppointCompanyDetailRepo appointCompanyDetailRepo;


    public void crawlerTargetCompany() throws IOException {
        List<String> addressList = Arrays.asList(addressString.split(","));
        for (String string : addressList) {
            try {
                System.out.println(string);
                Document parse = Jsoup.parse(new URL("http://zhaopin.baidu.com/quanzhi?query=" + string), 5000);
                Element select = parse.select("#feed-list > a:nth-child(1)").get(0);
                String attr = select.select("a").attr("href");
                Document parse1 = Jsoup.parse(new URL("http://zhaopin.baidu.com/" + attr), 5000);

                Element comDetail = parse1.select("body > div.main > div.page > div.clearfix > div.right.width-right > div.rightbar > div > div.details").get(0);
                Elements p = comDetail.select("p");
                AppointCompanyDetail appointCompanyDetail = new AppointCompanyDetail();
                appointCompanyDetail.setName(p.first().text());
                buildCompanyAttr(p, appointCompanyDetail);
                System.out.println(appointCompanyDetail);
//                appointCompanyDetailRepo.save(appointCompanyDetail);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }

    private void buildCompanyAttr(Elements p, AbstractCompany abstractCompany) {
        for (Element item : p) {
            String txt = item.text();
            if (txt.contains("公司性质：")) {
                abstractCompany.setCompanyNature(txt.replace("公司性质：", ""));
            }
            if (txt.contains("公司规模：")) {
                abstractCompany.setCompanyScale(txt.replace("公司规模：", ""));
            }
            if (txt.contains("公司类型：")) {
                abstractCompany.setCompanyCategory(txt.replace("公司类型：", ""));
            }
            if (txt.contains("工作地点：")) {
                abstractCompany.setCompanyAddress(txt.replace("工作地点：", "").replace("[查看地图]", ""));
            }
            if (txt.contains("联系方式：")) {
                abstractCompany.setCompanyContact(txt.replace("联系方式：", ""));
            }
        }
    }

    /**
     * 公司列表
     *
     * @throws IOException
     */
    public void crawlerCompanyList() throws IOException {
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        List<String> addressList = Arrays.asList(addressString.split(","));
        List[] lists = splitList(addressList, 10);

        for (List list : lists) {
            executorService.execute(() -> {
                try {
                    crawlerByAddressList(list);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
        executorService.shutdown();

    }

    private void crawlerByAddressList(List<String> addressList) throws IOException {
        for (String city : addressList) {
            Document parse = Jsoup.parse(new URL("http://zhaopin.baidu.com/quanzhi?city=" + city), 5000);
            List<String> disList = new ArrayList<String>();
            Elements districts = parse.select("#filter-box > div.filter-district > dl > dd > a");
            for (Element district : districts) {
                String txt = district.text();
                if (!txt.contains("不限")) {
                    disList.add(txt);
                }
            }

            for (String item : disList) {
                // 默认先访问第一页的数据，判断总共多少条记录
//                String url = "http://zhaopin.baidu.com/api/quanzhiasync?sort_type=1&city=" + city + "&district=" + item + "&detailmode=close&rn=20&pn=0";

                for (int i = 1; i <= PAGE_TOTAL; i++) {
                    try {

                        String url = "http://zhaopin.baidu.com/api/quanzhiasync?sort_type=1&city=" + city + "&district=" + item + "&detailmode=close&rn=20&pn=" + 2 * (i - 1) * 10;

                        Connection.Response resp = Jsoup.connect(url)
                                .header("Accept", "*/*")
                                .header("Accept-Encoding", "gzip, deflate")
                                .header("Accept-Language", "zh-CN,zh;q=0.8,en-US;q=0.5,en;q=0.3")
                                .header("Content-Type", "application/json;charset=UTF-8")
                                .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.90 Safari/537.36")
                                .timeout(2000).ignoreContentType(true).execute();
                        JSONObject jsonObject = JSON.parseObject(resp.body());

                        // 判断多少页
                        if (i == 1) {
                            String pageSizeString = jsonObject.getJSONObject("data").getJSONObject("main").getJSONObject("data").getString("listNum");
                            PAGE_TOTAL = Integer.parseInt(pageSizeString) / 20;
                            if (PAGE_TOTAL == 0) {
                                break;
                            }

                        }
                        JSONArray jsonArray = jsonObject.getJSONObject("data").getJSONObject("main").getJSONObject("data").getJSONArray("disp_data");
                        for (int j = 0; j < jsonArray.size(); j++) {
                            try {
                                JSONObject data = jsonArray.getJSONObject(j);
                                String id = data.getString("@id");
                                crawlerCompanySummary(city, id);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }
            logger.info(city + "抓取完毕");
        }
    }


    /**
     * 详情页
     *
     * @throws IOException
     */
    private void crawlerCompanySummary(String city, String urlId) throws IOException, InterruptedException {
        String formatUrl = String.format("http://zhaopin.baidu.com/szzw?detailidx=0&city=%s&id=%s", URLEncoder.encode(city, "utf-8"), URLEncoder.encode(urlId, "utf-8"));
        Document parse = Jsoup.parse(new URL(formatUrl), 5000);
        Element comDetail = parse.select("body > div.main > div.page > div.clearfix > div.right.width-right > div.rightbar > div > div.details").get(0);
        Elements p = comDetail.select("p");
        BaiduzhaopinCompanyDetail baiduzhaopinCompanyDetail = new BaiduzhaopinCompanyDetail();
        baiduzhaopinCompanyDetail.setName(p.first().text());
        baiduzhaopinCompanyDetail.setUrlId(urlId);
        buildCompanyAttr(p, baiduzhaopinCompanyDetail);
        System.out.println(baiduzhaopinCompanyDetail);
        save(baiduzhaopinCompanyDetail);
        Random random = new Random();
        Thread.sleep(random.nextInt(1000));
    }

    private void save(BaiduzhaopinCompanyDetail baiduzhaopinCompanyDetail) {
        System.out.println(JSON.toJSONString(baiduzhaopinCompanyDetail));
//        if (detailRepo.findCountByUrlId(baiduzhaopinCompanyDetail.getUrlId()) <= 0) {
//            detailRepo.save(baiduzhaopinCompanyDetail);
//        }
    }


    public static List[] splitList(List list, int pageSize) {
        int total = list.size();
        //总页数
        int pageCount = total % pageSize == 0 ? total / pageSize : total / pageSize + 1;
        List[] result = new List[pageCount];
        for (int i = 0; i < pageCount; i++) {
            int start = i * pageSize;
            //最后一条可能超出总数
            int end = start + pageSize > total ? total : start + pageSize;
            List subList = list.subList(start, end);
            result[i] = subList;
        }
        return result;
    }

}
