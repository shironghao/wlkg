package com.wlkg.controller;

import com.wlkg.pojo.Brand;
import com.wlkg.pojo.Category;
import com.wlkg.pojo.PageResult;
import com.wlkg.service.BrandService;
import com.wlkg.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class BrandController {
    @Autowired
    private BrandService brandService;

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/brand/page")
    public ResponseEntity<PageResult<Brand>> getBrand(@RequestParam(value = "page", defaultValue = "1") Integer page,
                                                      @RequestParam(value = "rows", defaultValue = "5") Integer rows,
                                                      @RequestParam(value = "sortBy", required = false) String sortBy,
                                                      @RequestParam(value = "desc", defaultValue = "false") Boolean desc,
                                                      @RequestParam(value = "key", required = false) String key
    ) {
       PageResult<Brand> result= brandService.getBrand(page,rows,sortBy,desc,key);
       return ResponseEntity.ok(result);
    }
    @PostMapping("/brand")
    public ResponseEntity<Boolean> add( Brand brand, @RequestParam("cids") List<Long> cids){

        brandService.insert(brand,cids);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/brand")
    public ResponseEntity<Boolean> update(Brand brand,@RequestParam("cids") List<Long> cids){
        brandService.update(brand,cids);
        return ResponseEntity.ok(true);
    }


    //删除
    @DeleteMapping("/brand/delete")
    public boolean delete(@RequestParam("id")long id){
        brandService.delete(id);
        return true;
    }



    //回显
    @GetMapping("/category/bid/{bid}")
    public ResponseEntity<List<Category>> queryByBrandId(@PathVariable("bid") Long bid) {
        System.out.println(bid);
        List<Category> list = categoryService.queryByBrandId(bid);
        System.out.println(list);
        if (list == null || list.size() < 1) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(list);
    }

    /**
     * 4.根据id查询品牌
     * @param id
     * @return
     */
    @GetMapping("/brand/{id}")
    public ResponseEntity<Brand> queryBrandById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(brandService.queryById(id));
    }





}


