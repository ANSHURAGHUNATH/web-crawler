package com.webcrawler.webcrawler.service;

import com.webcrawler.webcrawler.config.CacheConfig;
import com.webcrawler.webcrawler.model.WebCrawlCompletionStatus;
import com.webcrawler.webcrawler.model.WebCrawlerResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class WebCrawlerServiceImpl implements WebCrawlerService{
    @Autowired
    private CacheManager cacheManager;
    public static Map<String, WebCrawlerResponse> availableURLsMap = new HashMap<>();
    ExecutorService threadPool = Executors.newFixedThreadPool(5);
    @Override
    @CachePut(value = "cacheName",key="#requestedURL")
    public WebCrawlerResponse startWebCrawl(String requestedURL) {
        String requestID;
        Cache crawlerResponse = cacheManager.getCache("cacheName");
        WebCrawlerResponse response = crawlerResponse.get(requestedURL,WebCrawlerResponse.class);
        if(!Objects.isNull(response) && WebCrawlCompletionStatus.COMPLETED.equals(response.getCrawlCompletionStatus())) {
            System.out.println("Crawled URLs present in cache");
            return response;
        }
        while(true) {
            requestID = UUID.randomUUID().toString();
            if(!availableURLsMap.containsKey(requestID))
                break;
        }

        WebCrawlerResponse webCrawlerResponse = new WebCrawlerResponse(requestedURL,requestID);
        threadPool.submit(new WebCrawlerProcessor(webCrawlerResponse));
        availableURLsMap.put(webCrawlerResponse.getRequestToken(),webCrawlerResponse);
        return webCrawlerResponse;
    }

    @Override
    public WebCrawlerResponse getCrawledURLs(String requestToken) {
        if(availableURLsMap.containsKey(requestToken)){
            return availableURLsMap.get(requestToken);
        }
        else {
            WebCrawlerResponse response = new WebCrawlerResponse(null,requestToken);
            response.setCrawlCompletionStatus(WebCrawlCompletionStatus.NOT_AVAILABLE);
            response.setErrorMessage("No Data Found");
            return response;
        }
    }
}
