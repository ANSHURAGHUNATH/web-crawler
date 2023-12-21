package com.webcrawler.webcrawler.service;

import com.webcrawler.webcrawler.model.WebCrawlCompletionStatus;
import com.webcrawler.webcrawler.model.WebCrawlerResponse;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Callable;

public class WebCrawlerProcessor implements Callable<WebCrawlerResponse> {

    private final WebCrawlerResponse webCrawlerResponse;
    @Value("${max.depth.units}")
    private int maxDepth;

    public WebCrawlerProcessor(WebCrawlerResponse webCrawlerResponse) {
        this.webCrawlerResponse = webCrawlerResponse;
        this.maxDepth = 3;
    }

    @Override
    public WebCrawlerResponse call() throws Exception {
        Set<String> visitedUrls = new HashSet();
        processCrawl(webCrawlerResponse,webCrawlerResponse.getRequestedURL(),visitedUrls,0);
        webCrawlerResponse.setCrawlCompletionStatus(WebCrawlCompletionStatus.COMPLETED);
        return webCrawlerResponse;
    }

    private void processCrawl(WebCrawlerResponse webCrawlerResponse, String requestedURL, Set<String> visitedUrls, int depth) {
        if(visitedUrls.contains(requestedURL) || depth>maxDepth) {
            return;
        }
        visitedUrls.add(requestedURL);
        webCrawlerResponse.addURLs(requestedURL);
        try {
            Document document = Jsoup.connect(webCrawlerResponse.getRequestedURL()).get();
            Elements links = document.select("a[href]");
            for(Element link: links) {
                String absoluteURL = link.absUrl("href");
                if(!visitedUrls.contains(absoluteURL) && absoluteURL.startsWith("https://")) {
                    processCrawl(webCrawlerResponse,absoluteURL,visitedUrls,depth+1);
                }
            }
        } catch(IOException exception){
            System.out.println("Failed to crawl URL" + requestedURL);
        }
    }
}
