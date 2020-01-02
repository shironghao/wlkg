package com.wlkg.client;

import com.wlkg.api.CategoryApi;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient("item-service")
public interface CategoryClient extends CategoryApi {
}
