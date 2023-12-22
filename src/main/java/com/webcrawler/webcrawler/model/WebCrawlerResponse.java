package com.webcrawler.webcrawler.model;

import java.util.ArrayList;
import java.util.List;

public class WebCrawlerResponse {
    private final String requestedURL;
    private final String requestToken;
    private WebCrawlCompletionStatus crawlCompletionStatus;
    private List<String> availableURLs;
    private String ErrorMessage;

    public String getErrorMessage() {
        return ErrorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        ErrorMessage = errorMessage;
    }

    public String getRequestedURL() {
        return requestedURL;
    }

    public String getRequestToken() {
        return requestToken;
    }

    public WebCrawlCompletionStatus getCrawlCompletionStatus() {
        return crawlCompletionStatus;
    }

    public void setCrawlCompletionStatus(WebCrawlCompletionStatus crawlCompletionStatus) {
        this.crawlCompletionStatus = crawlCompletionStatus;
    }

    public List<String> getAvailableURLs() {
        return availableURLs;
    }

    public void setAvailableURLs(List<String> availableURLs) {
        this.availableURLs = availableURLs;
    }

    public void addURLs(String url) {
        this.availableURLs.add(url);
    }

    public WebCrawlerResponse(String requestedURL, String requestToken) {
        this.requestedURL = requestedURL;
        this.requestToken = requestToken;
        this.crawlCompletionStatus = WebCrawlCompletionStatus.IN_PROGRESS;
        this.availableURLs = new ArrayList<>();
    }
}
