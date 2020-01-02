package com.wlkg.api;

import com.wlkg.pojo.SpecGroup;
import com.wlkg.pojo.SpecParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface SpecificationApi {

    @GetMapping("/spec/params")
    List<SpecParam> querySpecParam(@RequestParam(value="gid", required = false) Long gid,
                                          @RequestParam(value="cid", required = false) Long cid,
                                          @RequestParam(value="searching", required = false) Boolean searching,
                                          @RequestParam(value="generic", required = false) Boolean generic
    );

    @GetMapping("/group")
    List<SpecGroup> querySpecsByCid(@RequestParam("cid") Long cid);
}
