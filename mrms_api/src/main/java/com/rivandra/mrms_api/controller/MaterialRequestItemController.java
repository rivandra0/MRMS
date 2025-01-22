package com.rivandra.mrms_api.controller;

import org.springframework.web.bind.annotation.RestController;

import com.rivandra.mrms_api.service.JwtService;
import com.rivandra.mrms_api.service.MaterialRequestItemService;

import jakarta.servlet.http.HttpServletRequest;
import model.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
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
    public CommonDTO insertRequestItem(@RequestBody MaterialRequestItem matReqItem, HttpServletRequest request) {
        try {
            String userId = _jwtService.extractClaimSubject(request);

            _materialRequestItemService.insertRequestItem(userId, matReqItem);
    
            CommonDTO dto = new CommonDTO();
            dto.setMessage("successfully insert item");
            dto.setStatus("success");
            return dto;
        }
        catch(Throwable ex) {
            throw ex;
        }
        
    }

    @PutMapping("/admin/material-request-item")
    public CommonDTO updateRequestItemForAdmin(@RequestBody MaterialRequestItem matReqItem) {
        try {
            _materialRequestItemService.updateRequestItem("ALL", matReqItem);

            CommonDTO dto = new CommonDTO();
            dto.setMessage("successfully update item");
            dto.setStatus("success");
            return dto;
            
        } catch(Throwable ex) {
            throw ex;
        }
        
    }

    @PutMapping("/user/material-request-item")
    public CommonDTO updateRequestItemForUser(@RequestBody MaterialRequestItem matReqItem, HttpServletRequest request) {
        try{
            String userId = _jwtService.extractClaimSubject(request);

            _materialRequestItemService.updateRequestItem(userId, matReqItem);
    
            CommonDTO dto = new CommonDTO();
            dto.setMessage("successfully update item");
            dto.setStatus("success");
            return dto;
        }
        catch(Throwable ex) {
            throw ex;
        }

    }    

    @DeleteMapping("/user/material-request-item")
    public CommonDTO deleteRequestItem(@RequestParam String requestId, String itemId, HttpServletRequest request) {
        try{
            String userId = _jwtService.extractClaimSubject(request);

            _materialRequestItemService.deleteRequestItem(userId, Integer.parseInt(requestId), Integer.parseInt(itemId));
            
            CommonDTO dto = new CommonDTO();
            dto.setMessage("successfully delete item");
            dto.setStatus("success");
            return dto;
        }
        catch(Throwable ex) {
            throw ex;
        }
        
    }
    
}
