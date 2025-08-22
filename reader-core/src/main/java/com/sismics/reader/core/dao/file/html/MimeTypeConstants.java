package com.sismics.reader.core.dao.file.html;

import com.google.common.collect.ImmutableMap;

public class MimeTypeConstants {
    /**
     * Authorized MIME types and corresponding file extensions for favicons.
     */
    private static final ImmutableMap<String, String> FAVICON_MIME_TYPE_MAP = new ImmutableMap.Builder<String, String>()
            .put("image/bmp", ".bmp")
            .put("image/gif", ".gif")
            .put("image/jpeg", ".jpg")
            .put("image/png", ".png")
            .put("image/x-icon", ".ico")
            .put("image/vnd.microsoft.icon", ".ico")
            .build();

            public static final String IMAGE_X_ICON = "image/x-icon";

            public static final String IMAGE_PNG = "image/png";
        
            public static final String IMAGE_JPEG = "image/jpeg";
        
            public static final String IMAGE_GIF = "image/gif";
        
            public static final String APPLICATION_ZIP = "application/zip";

    public static String getExtensionForMimeType(String mimeType) {
        return FAVICON_MIME_TYPE_MAP.get(mimeType);
    }
}