package com.webcrawler.webcrawler.service;

import com.webcrawler.webcrawler.model.WebCrawlerRequest;
import com.webcrawler.webcrawler.model.WebCrawlerResponse;

public interface WebCrawlerService {
    public WebCrawlerResponse startWebCrawl(String requestedURL);
    public WebCrawlerResponse getCrawledURLs(String requestToken);
}
