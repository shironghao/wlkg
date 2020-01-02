package com.wlkg.client;

import com.wlkg.api.SpecificationApi;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(value = "item-service")
public interface SpecificationClient  extends SpecificationApi {
}
