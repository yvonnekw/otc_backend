package com.otc.backend.client;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "OTC-SERVICE", url = "${otc.url}")

public interface OtcClient {

    

    
}
