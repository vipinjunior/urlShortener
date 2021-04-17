package com.vipin.manager;

import com.vipin.resources.UrlCreationRequest;

public interface UrlShortenerManager {
    String createLongUrl(UrlCreationRequest urlCreationRequest);
    String getLongUrl(String encryptedId) throws Exception;
}
