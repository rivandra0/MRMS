package com.rivandra.mrms_api.controller;

import org.springframework.web.bind.annotation.RestController;

import com.rivandra.mrms_api.service.JwtService;
import com.rivandra.mrms_api.service.MaterialRequestService;

import jakarta.servlet.http.HttpServletRequest;
import model.*;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;


@RestController
public class MaterialRequestController {

    @Autowired
    private MaterialRequestService _materialRequestService;

    @Autowired
    private JwtService _jwtRequestService;
    
    @GetMapping("/admin/material-requests")
    public List<MaterialRequest> getRequestsForAdmin(
        @RequestParam String status, 
        @RequestParam String dateFrom, 
        @RequestParam String dateTo, 
        HttpServletRequest request) {
        
        return _materialRequestService.getRequests("ALL", status, dateFrom, dateTo);
    }

    @GetMapping("/user/material-requests")
    public List<MaterialRequest> getRequestsForUser(
        @RequestParam String status, 
        @RequestParam String dateFrom, 
        @RequestParam String dateTo, 
        HttpServletRequest request) {
            
        String userId = _jwtRequestService.extractClaimSubject(request);
        
        return _materialRequestService.getRequests(userId, status, dateFrom, dateTo);
    }



    @GetMapping("/admin/material-request")
    public MaterialRequest getRequestForAdmin(@RequestParam String requestId, HttpServletRequest request) {

        return _materialRequestService.getRequest("ALL", Integer.valueOf(requestId));
    }

    @GetMapping("/user/material-request")
    public MaterialRequest getRequestForUser(@RequestParam String requestId, HttpServletRequest request) {
        String userId = _jwtRequestService.extractClaimSubject(request);

        return _materialRequestService.getRequest(userId, Integer.valueOf(requestId));
    }



    @PostMapping("/user/material-request")
    public String insertOneMaterialRequest(@RequestBody MaterialRequest matReq, HttpServletRequest request) {
        String userId = _jwtRequestService.extractClaimSubject(request);

        _materialRequestService.insertRequest(userId, matReq);

        return "successfully insert material";
    }


    
    @PutMapping("/admin/material-request")
    public String updateRequestForAdmin(@RequestBody MaterialRequest matReq, HttpServletRequest request) {

        _materialRequestService.updateRequest("ALL", matReq);

        return "successfully update material request";
    }

    @PutMapping("/user/material-request")
    public String updateRequestForUser(@RequestBody MaterialRequest matReq, HttpServletRequest request) {
        String userId = _jwtRequestService.extractClaimSubject(request);

        _materialRequestService.updateRequest(userId, matReq);

        return "successfully update material request";
    }


    
    @DeleteMapping("/material-request")
    public String deleteOneMaterialRequest(@RequestParam String requestId, HttpServletRequest request) {
        String userId = _jwtRequestService.extractClaimSubject(request);

        _materialRequestService.deleteRequest(userId, Integer.parseInt(requestId));

        return "successfully delete material request";
    }


}
