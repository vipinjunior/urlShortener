package com.vipin.manager;

import com.vipin.entity.ClientConfiguration;
import com.vipin.entity.LongUrl;
import com.vipin.repository.ClientRepository;
import com.vipin.repository.LongUrlRepository;
import com.vipin.resources.UrlCreationRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.function.LongUnaryOperator;

@Service
public class UrlShortenerManagerImpl implements  UrlShortenerManager{
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private LongUrlRepository longUrlRepository;
    @Override
    public String createLongUrl(UrlCreationRequest urlCreationRequest) {
        LongUrl longUrl=LongUrl.builder()
                .url(urlCreationRequest.getLongUrl())
                .createdAt(LocalDateTime.now())
                .build();
        ClientConfiguration clientConfiguration= clientRepository
                .findByHostName(urlCreationRequest.getHostName()).orElse(null);
        if(clientConfiguration==null){
            clientConfiguration=ClientConfiguration.builder()
                    .hostName(urlCreationRequest.getHostName())
                    .longUrls(new ArrayList<>())
                    .build();
        }
        //check if the long url already esists
        for (LongUrl url : clientConfiguration.getLongUrls()){
            if(url.getUrl().equals(urlCreationRequest.getLongUrl())){
                return buildShortUrl(clientConfiguration.getHostName(),url.getId());
            }
        }

        clientConfiguration.getLongUrls().add(longUrl);
        ClientConfiguration savedClientConfiguration=
        clientRepository.save(clientConfiguration);
        Long id=null;
        for(LongUrl url: savedClientConfiguration.getLongUrls()){
            if(url.getUrl().equals(urlCreationRequest.getLongUrl())){
                id=url.getId();
            }
        }
        return buildShortUrl(clientConfiguration.getHostName(),id);
    }
    private String buildShortUrl(String hostName, Long longUrlId){
        return "http://".concat(hostName).concat("/").concat(encryptId((longUrlId)));
    }

    String encryptId(Long id){
        String encryptedString= Base64.getEncoder().encodeToString(String.valueOf(id).getBytes());
        return encryptedString;
    }
    @Override
    public String getLongUrl(String encryptedId) throws Exception {
        Long id=decryptId(encryptedId);
        LongUrl longUrl=longUrlRepository.findById(id)
                .orElseThrow(()->new Exception("url is not Present"));

        return longUrl.getUrl();
    }
    Long decryptId(String encryptId) throws UnsupportedEncodingException {
        String decryptedString=new String(Base64.getDecoder().decode(encryptId),"UTF-8");
        return Long.parseLong(decryptedString);
    }
}
