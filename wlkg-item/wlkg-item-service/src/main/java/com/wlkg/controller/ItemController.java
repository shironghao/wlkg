package com.wlkg.controller;

import com.wlkg.pojo.Item;
import com.wlkg.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ItemController {
    @Autowired
    private ItemService service;
    @PostMapping("/add")
    public ResponseEntity<Item>saveItem(Item item){
        if (item.getPrice()==null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        item = service.saveItem(item);
        return ResponseEntity.status(HttpStatus.CREATED).body(item);
    }
}
