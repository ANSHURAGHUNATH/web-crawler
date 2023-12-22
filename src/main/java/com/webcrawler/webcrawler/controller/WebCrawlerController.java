package com.webcrawler.webcrawler.controller;

import com.webcrawler.webcrawler.model.WebCrawlCompletionStatus;
import com.webcrawler.webcrawler.model.WebCrawlerRequest;
import com.webcrawler.webcrawler.model.WebCrawlerResponse;
import com.webcrawler.webcrawler.service.WebCrawlerService;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.interceptor.AbstractCacheResolver;
import org.springframework.cache.support.AbstractCacheManager;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@RequestMapping(value = "/crawl")
public class WebCrawlerController {
    private final WebCrawlerService webCrawlerService;
    @Autowired
    public WebCrawlerController(WebCrawlerService webCrawlerService) {
        this.webCrawlerService = webCrawlerService;
    }
    @RequestMapping(method = RequestMethod.POST, path="/begin")
    public ResponseEntity<WebCrawlerResponse> startWebCrawlProcess(@RequestBody WebCrawlerRequest webCrawlerRequest) {
        WebCrawlerResponse webCrawlerResponse = webCrawlerService.startWebCrawl(webCrawlerRequest.getRequestedURL());
        return new ResponseEntity<>(webCrawlerResponse, HttpStatus.OK);
    }
    @RequestMapping(method = RequestMethod.GET, path="/{requestToken}")
    public ResponseEntity<WebCrawlerResponse> getWebCrawlerResponse(@PathVariable("requestToken") String requestToken) {
        WebCrawlerResponse webCrawlerResponse = webCrawlerService.getCrawledURLs(requestToken);
        return new ResponseEntity<>(webCrawlerResponse, Objects.isNull(webCrawlerResponse.getErrorMessage())?HttpStatus.OK:HttpStatus.NOT_FOUND);
    }
}
