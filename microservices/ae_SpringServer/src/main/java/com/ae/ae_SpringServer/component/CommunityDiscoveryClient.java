package com.ae.ae_SpringServer.component;

import org.json.simple.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class CommunityDiscoveryClient {
    private final RestTemplate restTemplate;

    public CommunityDiscoveryClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    private HttpEntity<?> apiClientHttpEntity(String appType, String params) {

        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.set("Content-Type", "application/" + appType);

        if ("".equals(params))
            return new HttpEntity<Object>(requestHeaders);
        else
            return new HttpEntity<Object>(params, requestHeaders);
    }

    public String updateNickname(String userIdx, String nickname) {
        String baseUrl = "http://ae-communityServer/eureka/";

        JSONObject parameter = new JSONObject();
        parameter.put("nickname", nickname);

        HttpEntity<?> requestEntity = apiClientHttpEntity("json", parameter.toJSONString());

        ResponseEntity<String> responseEntity = restTemplate.exchange(baseUrl + userIdx,
                HttpMethod.POST, requestEntity, (Class<String>) null);

        return responseEntity.getBody();
    }
}
