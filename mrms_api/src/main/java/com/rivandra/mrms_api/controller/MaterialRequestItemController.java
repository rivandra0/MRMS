package com.rivandra.mrms_api.controller;

import org.springframework.web.bind.annotation.RestController;

import com.rivandra.mrms_api.service.JwtService;
import com.rivandra.mrms_api.service.MaterialRequestItemService;

import jakarta.servlet.http.HttpServletRequest;
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

    @Autowired
    private JwtService _jwtService;


    @PostMapping("/user/material-request-item")
    public String insertRequestItem(@RequestBody MaterialRequestItem matReqItem, HttpServletRequest request) {
        String userId = _jwtService.extractClaimSubject(request);

        _materialRequestItemService.insertRequestItem(userId, matReqItem);

        return "successfully insert item";
    }

    @PutMapping("/admin/material-request-item")
    public String updateRequestItemForUser(@RequestBody MaterialRequestItem matReqItem) {
        _materialRequestItemService.updateRequestItem("ALL", matReqItem);

        return "successfully update item";
    }

    @PutMapping("/user/material-request-item")
    public String updateRequestItemForAdmin(@RequestBody MaterialRequestItem matReqItem, HttpServletRequest request) {
        String userId = _jwtService.extractClaimSubject(request);

        _materialRequestItemService.updateRequestItem(userId, matReqItem);

        return "successfully update item";
    }    

    @DeleteMapping("/user/material-request-item")
    public String deleteRequestItem(@RequestParam String requestId, String itemId, HttpServletRequest request) {
        String userId = _jwtService.extractClaimSubject(request);

        _materialRequestItemService.deleteRequestItem(userId, Integer.parseInt(requestId), Integer.parseInt(itemId));

        return "successfully delete item";
    }
    
}
