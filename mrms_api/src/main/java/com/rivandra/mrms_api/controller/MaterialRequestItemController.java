package com.rivandra.mrms_api.controller;

import org.springframework.web.bind.annotation.RestController;

import com.rivandra.mrms_api.service.MaterialRequestItemService;

import model.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PutMapping;


@RestController
public class MaterialRequestItemController {

    @Autowired
    private MaterialRequestItemService _materialRequestItemService;

    //✅
    @PostMapping("/material-request-item")
    public String insertOneMaterialRequestItem(@RequestBody MaterialRequestItem matReqItem) {
        String userId = "ALL";

        _materialRequestItemService.insertRequestItem(userId, matReqItem);

        return "successfully insert item";
    }

    //✅
    @PutMapping("/material-request-item")
    public String updateOneMaterialRequestItem(@RequestBody MaterialRequestItem matReqItem) {
        String userId = "ALL";

        _materialRequestItemService.updateRequestItem(userId, matReqItem);

        return "successfully update item";
    }

    //✅
    @DeleteMapping("/material-request-item")
    public String deleteOneMaterialRequestItem(@RequestParam String requestId, String itemId) {
        String userId = "prd1";

        _materialRequestItemService.deleteRequestItem(userId, Integer.parseInt(requestId), Integer.parseInt(itemId));

        return "successfully delete item";
    }
    
}
