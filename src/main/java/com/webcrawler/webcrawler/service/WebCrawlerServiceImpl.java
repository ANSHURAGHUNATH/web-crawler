package com.webcrawler.webcrawler.service;

import com.webcrawler.webcrawler.model.WebCrawlerResponse;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class WebCrawlerServiceImpl implements WebCrawlerService{
    public static Map<String, WebCrawlerResponse> availableURLsMap = new HashMap<>();
    ExecutorService threadPool = Executors.newFixedThreadPool(5);
    @Override
    @Caching(
            put= { @CachePut(value="cacheName", key="#requestedURL") },
            cacheable = { @Cacheable(value ="cacheName", key="#requestedURL")}
    )
    public WebCrawlerResponse startWebCrawl(String requestedURL) {
        String requestID;
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
        return availableURLsMap.get(requestToken);
    }
}
