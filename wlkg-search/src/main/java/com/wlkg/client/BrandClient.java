package com.wlkg.client;

import com.wlkg.api.BrandApi;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient("item-service")
public interface BrandClient extends BrandApi {


}
