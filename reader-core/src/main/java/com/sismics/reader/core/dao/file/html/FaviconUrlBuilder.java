package com.sismics.reader.core.dao.file.html;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.MessageFormat;

public class FaviconUrlBuilder {
    private static final Logger log = LoggerFactory.getLogger(FaviconUrlBuilder.class);

    /**
     * Constructs a favicon URL from a feed path and filename.
     */
    public static String buildFaviconUrl(String pageUrl, String fileName) {
        if (pageUrl != null) {
            try {
                URL url = new URL(pageUrl);
                return new URL(url.getProtocol(), url.getHost(), url.getPort(), "/" + fileName).toString();
            } catch (MalformedURLException e) {
                if (log.isErrorEnabled()) {
                    log.error(MessageFormat.format("Error building favicon URL from the page URL {0} with filename {1}", pageUrl, fileName));
                }
            }
        }
        return null;
    }
}