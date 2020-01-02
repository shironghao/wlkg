package com.wlkg.controller;

import com.wlkg.pojo.SpecParam;
import com.wlkg.service.SpecificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class QuerySpecParamCtroller {
    @Autowired
    private SpecificationService specificationService;


    //添加
    @PostMapping("/spec/param")
    public ResponseEntity<Boolean> add(@RequestBody SpecParam specParam){

        specificationService.addParam(specParam);
        return ResponseEntity.ok(true);
    }


    //修改
    @PutMapping("/spec/param")
    public ResponseEntity<Boolean> update(@RequestBody SpecParam specParam){

        specificationService.updateParam(specParam);
        return ResponseEntity.ok(true);
    }


    //删除
    @DeleteMapping("/spec/param/{id}")
    public ResponseEntity<Boolean> deleteParam(@PathVariable("id")Long id){
        specificationService.deleteParam(id);
        return ResponseEntity.ok(true);
    }


}
