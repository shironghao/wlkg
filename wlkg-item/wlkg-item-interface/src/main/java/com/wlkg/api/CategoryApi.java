package com.wlkg.api;

import com.wlkg.pojo.Category;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface CategoryApi {

    @GetMapping("/category/list/ids")
    List<Category> queryCategoryByIds(@RequestParam("ids") List<Long> ids);
}
