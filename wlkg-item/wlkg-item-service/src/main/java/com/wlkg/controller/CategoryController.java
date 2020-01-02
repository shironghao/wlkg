package com.wlkg.controller;

import com.wlkg.pojo.Category;
import com.wlkg.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private CategoryService service;

    @GetMapping("/list")
    public ResponseEntity<List<Category>> getCategory(@RequestParam("pid") Long pid){
        List<Category> list = service.getCategory(pid);
//        System.out.println(list);
        return ResponseEntity.ok(list);
    }
    @PostMapping("/add")
    public Category add(@RequestBody Category category){

        service.addCategory(category);
        return category;

    }
    @PutMapping("/edit")
    public String edit(@RequestBody Category category){
        System.out.println(category);
        boolean flag = service.edit(category);
        if (flag){
            return "修改成功";
        }else {
            return "修改失败";
        }

    }




    @DeleteMapping("/delete")
    public ResponseEntity<Boolean> delete( Long id){
        System.out.println(id);
             service.delete(id);
            return ResponseEntity.ok(true);
    }
    /**
     * 根据Id查询商品分类
     * @param ids
     * @return
     */
    @GetMapping("list/ids")
    public ResponseEntity<List<Category>> queryCategoryByIds(@RequestParam("ids")List<Long> ids){
        return ResponseEntity.ok(service.queryByIds(ids));
    }




}
