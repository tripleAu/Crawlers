# Crawler

LZ会把平时跟爬虫相关的代码整合在此，方便以后重复造轮子


### crawler-zhilian
智联职位抓取
简单的尝试

添加了ip代理池，智联抓了四天，没有任何异常，以防万一，添加IP代理池来抓取

### quick-csdn2md
CSDN文章转批量转MD的代码，还需要改动才能使用

### Simulation-baidu-search
模拟百度搜索，得到百度的返回结果，可以作为一些爬虫的seed
需要注意的是

```java
searchResult(item, item + " site:cn.linkedin.com", 1);
```
注意搜索时候的 url

### crawler-inno-tree
因果树 相关抓取，本想使用webmagic抓的，后来发现还没有httpclient容易控制，遂采用httpclient4.3.2抓取，注意版本号不同使用的方法也不同

### linkedin crawler
需要领英爬虫的私聊我~~~