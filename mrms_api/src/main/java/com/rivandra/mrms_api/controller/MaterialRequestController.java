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

        try {
            return _materialRequestService.getRequests("ALL", status, dateFrom, dateTo);

        } catch(Throwable ex) {
            throw ex;
        }
    }

    @GetMapping("/user/material-requests")
    public List<MaterialRequest> getRequestsForUser(
        @RequestParam String status, 
        @RequestParam String dateFrom, 
        @RequestParam String dateTo, 
        HttpServletRequest request) {
        try{

            String userId = _jwtRequestService.extractClaimSubject(request);
        
            return _materialRequestService.getRequests(userId, status, dateFrom, dateTo);
        } catch(Throwable ex) {
            throw ex;
        }
        
    }


    @GetMapping("/admin/material-request")
    public MaterialRequest getRequestForAdmin(@RequestParam String requestId, HttpServletRequest request) {
        try{
            return _materialRequestService.getRequest("ALL", Integer.valueOf(requestId));
        } catch (Throwable ex) {
            throw ex;
        }
    }

    @GetMapping("/user/material-request")
    public MaterialRequest getRequestForUser(@RequestParam String requestId, HttpServletRequest request) {
        try{
            String userId = _jwtRequestService.extractClaimSubject(request);

            return _materialRequestService.getRequest(userId, Integer.valueOf(requestId));
        }
        catch(Throwable ex) {
            throw ex;
        }
        
    }



    @PostMapping("/user/material-request")
    public CommonDTO insertOneMaterialRequest(@RequestBody MaterialRequest matReq, HttpServletRequest request) {
        try {
            String userId = _jwtRequestService.extractClaimSubject(request);

            MaterialRequest matReqRes = _materialRequestService.insertRequest(userId, matReq);
    
            CommonDTO dto = new CommonDTO();
            dto.setMessage("successfully insert request");
            dto.setData(matReqRes);
            dto.setStatus("success");
            return dto;
        }
        catch(Throwable ex) {
            throw ex;
        }
        
    }


    
    @PutMapping("/admin/material-request")
    public CommonDTO updateRequestForAdmin(@RequestBody MaterialRequest matReq, HttpServletRequest request) {

        _materialRequestService.updateRequest("ALL", matReq);

        CommonDTO dto = new CommonDTO();
        dto.setMessage("successfully modify request");
        dto.setStatus("success");
        return dto;
    }

    @GetMapping("/admin/material-request/approve")
    public CommonDTO approveRequestForAdmin(@RequestParam String requestId, HttpServletRequest request) {
        try {
            String userId = _jwtRequestService.extractClaimSubject(request);
            _materialRequestService.approveRequest(userId, Integer.parseInt(requestId));
    
            CommonDTO dto = new CommonDTO();
            dto.setMessage("successfully approve request");
            dto.setStatus("success");
            return dto;
        } catch (Throwable ex) {
            throw ex;
        }
        
    }

    @GetMapping("/admin/material-request/reject")
    public CommonDTO rejectRequestForAdmin(@RequestParam String requestId, @RequestParam String remark, HttpServletRequest request) {
        try {
            String userId = _jwtRequestService.extractClaimSubject(request);
            _materialRequestService.rejectRequest(userId, Integer.parseInt(requestId), remark);
    
            CommonDTO dto = new CommonDTO();
            dto.setMessage("rejected request");
            dto.setStatus("success");
            return dto;
        } catch(Throwable ex) {
            throw ex;
        }
        
    }


    @PutMapping("/user/material-request")
    public String updateRequestForUser(@RequestBody MaterialRequest matReq, HttpServletRequest request) {
        String userId = _jwtRequestService.extractClaimSubject(request);

        _materialRequestService.updateRequest(userId, matReq);

        return "successfully update material request";
    }

    
    @DeleteMapping("/user/material-request")
    public CommonDTO deleteOneMaterialRequest(@RequestParam String requestId, HttpServletRequest request) {
        try {
            String userId = _jwtRequestService.extractClaimSubject(request);

            _materialRequestService.deleteRequest(userId, Integer.parseInt(requestId));
    
            CommonDTO dto = new CommonDTO();
            dto.setMessage("successfully delete request");
            dto.setStatus("success");
            return dto;

        } catch(Throwable ex) {
            throw ex;
        }
        
    }


}
