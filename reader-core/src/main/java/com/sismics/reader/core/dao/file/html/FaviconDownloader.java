package com.sismics.reader.core.dao.file.html;

import com.google.common.collect.ImmutableList;
import com.google.common.io.ByteStreams;
import com.google.common.io.Files;
import com.sismics.reader.core.util.http.ReaderHttpClient;
import com.sismics.util.mime.MimeTypeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.text.MessageFormat;
import java.util.Iterator;
import java.util.List;

public class FaviconDownloader {
    private static final Logger log = LoggerFactory.getLogger(FaviconDownloader.class);
    
    private static final List<String> COMMON_FAVICON_NAMES = ImmutableList.of(
            "favicon.png", "favicon.gif", "favicon.ico", "favicon.jpg", "favicon.jpeg");

    public String downloadFaviconFromPage(String pageUrl, String directory, String fileName) {
        // Try to extract the favicon URL from the page
        String faviconUrl = extractFaviconFromPage(pageUrl);
        
        // Attempt to download favicon from extracted URL
        String localFilename = null;
        if (faviconUrl != null) {
            localFilename = downloadFavicon(faviconUrl, directory, fileName);
        }

        // Try common favicon locations if not found
        if (localFilename == null) {
            localFilename = tryCommonFaviconLocations(pageUrl, directory, fileName);
        }
        
        logDownloadResult(localFilename, fileName, pageUrl);
        return localFilename;
    }

    private String extractFaviconFromPage(String pageUrl) {
        try {
            final FaviconExtractor extractor = new FaviconExtractor(pageUrl);
            new ReaderHttpClient() {
                @Override
                public Void process(InputStream is) throws Exception {
                    extractor.readPage(is);
                    return null;
                }
            }.open(new URL(pageUrl));
            return extractor.getFavicon();
        } catch (Exception e) {
            log.error("Error extracting icon from feed HTML page", e);
            return null;
        }
    }

    private String tryCommonFaviconLocations(String pageUrl, String directory, String fileName) {
        String localFilename = null;
        Iterator<String> iterator = COMMON_FAVICON_NAMES.iterator(); 
        while (localFilename == null && iterator.hasNext()) {
            String filename = iterator.next();
            String faviconUrl = FaviconUrlBuilder.buildFaviconUrl(pageUrl, filename);
            localFilename = downloadFavicon(faviconUrl, directory, fileName);
        }
        return localFilename;
    }

    protected String downloadFavicon(final String faviconUrl, final String directory, final String fileName) {
        try {
            ReaderHttpClient<String> client = new ReaderHttpClient<String>() {
                @Override
                public String process(InputStream is) throws Exception {
                    return handleFaviconDownload(is, directory, fileName);
                }
            };
            client.setTimeout(2000);
            return client.open(new URL(faviconUrl));
        } catch (Exception e) {
            if (log.isInfoEnabled()) {
                log.info(MessageFormat.format("Error downloading favicon at URL {0}", faviconUrl), e);
            }
            return null;
        }
    }

    private String handleFaviconDownload(InputStream is, String directory, String fileName) throws Exception {
        File localFile = null;
        try {
            localFile = File.createTempFile("reader_favicon", ".ico");
            if (ByteStreams.copy(is, new FileOutputStream(localFile)) > 0) {
                String type = MimeTypeUtil.guessMimeType(localFile);
                if (type != null) {
                    String extension = MimeTypeConstants.getExtensionForMimeType(type);
                    if (extension != null) {
                        File outputFile = new File(directory + File.separator + fileName + extension);
                        Files.copy(localFile, new FileOutputStream(outputFile));
                        return outputFile.getPath();
                    }
                }
            }
        } catch (FileNotFoundException e) {
            log.info("Favicon file not found");
        } finally {
            cleanupTempFile(localFile);
        }
        return null;
    }

    private void cleanupTempFile(File localFile) {
        if (localFile != null) {
            try {
                localFile.delete();
            } catch (Exception e) {
                // NOP
            }
        }
    }

    private void logDownloadResult(String localFilename, String fileName, String pageUrl) {
        if (log.isInfoEnabled()) {
            if (localFilename != null) {
                log.info(MessageFormat.format("Favicon successfully downloaded to {0}", localFilename));
            } else {
                log.info(MessageFormat.format("Cannot find a valid favicon for feed {0} at page {1} or at the domain root", fileName, pageUrl));
            }
        }
    }
}