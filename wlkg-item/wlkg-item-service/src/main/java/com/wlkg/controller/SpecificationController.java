package com.wlkg.controller;

import com.wlkg.pojo.SpecGroup;
import com.wlkg.pojo.SpecParam;
import com.wlkg.service.SpecificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class SpecificationController {

    @Autowired
    private SpecificationService specificationService;

    @GetMapping("/spec/groups/{cid}")
    public ResponseEntity<List<SpecGroup>> querySpecGroups(@PathVariable("cid") Long cid) {
        List<SpecGroup> list = this.specificationService.querySpecGroups(cid);
        if (list == null || list.size() == 0) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(list);
    }

    @PostMapping("/spec/group")
    public ResponseEntity<Boolean> add(@RequestBody SpecGroup specGroup) {
        specificationService.add(specGroup);
        return ResponseEntity.ok(true);

    }

    @PutMapping("/spec/group")
    public ResponseEntity<Boolean> update(@RequestBody SpecGroup specGroup) {
        specificationService.update(specGroup);
        return ResponseEntity.ok(true);

    }


    @DeleteMapping("/spec/group/{id}")
    public ResponseEntity<Boolean> delete(@PathVariable("id") Long id) {
        specificationService.delete(id);

        return ResponseEntity.ok(true);
    }

    @GetMapping("/spec/params")
    public ResponseEntity<List<SpecParam>> querySpecParam(@RequestParam(value = "gid", required = false) Long gid,
                                                          @RequestParam(value = "cid", required = false) Long cid,
                                                          @RequestParam(value = "searching", required = false) Boolean searching,
                                                          @RequestParam(value = "generic", required = false) Boolean generic
    ) {
        List<SpecParam> list = this.specificationService.querySpecParams(gid, cid, searching, generic);
        return ResponseEntity.ok(list);

    }


    // 查询规格参数组，及组内参数
    @GetMapping("/group")
    public ResponseEntity<List<SpecGroup>> querySpecsByCid(@RequestParam("cid") Long cid) {
        List<SpecGroup> list = this.specificationService.querySpecsByCid(cid);
        return ResponseEntity.ok(list);

    }
}