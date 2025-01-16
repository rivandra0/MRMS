package com.rivandra.mrms_api.controller;

import org.springframework.web.bind.annotation.RestController;

import com.rivandra.mrms_api.service.MaterialRequestService;

import model.MaterialRequest;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;




@RestController
public class MaterialRequestController {

    @Autowired
    private MaterialRequestService _materialRequestService;

    @GetMapping("/material-requests")
    public List<MaterialRequest> getManyMaterialRequest(@RequestParam String status, @RequestParam String dateFrom, @RequestParam String dateTo) {
        String userId = "ini diambil dari jwtcookie";

        return _materialRequestService.getManyMaterialRequest(userId, status, dateFrom, dateTo);
    }

    @GetMapping("/material-request")
    public MaterialRequest getOneMaterialRequest(@RequestParam String requestId) {
        String userId = "ini diambil dari jwtcookie";
  
        return _materialRequestService.getOneMaterialRequest(userId, requestId);
    }

    @PostMapping("/material-request")
    public String insertOneMaterialRequest(@RequestBody MaterialRequest matReq) {
        String userId = "ini diambil dari jwtcookie";
        _materialRequestService.insertOneMaterialRequest(userId, matReq);

        return "successfully insert material";
    }

    @PutMapping("/material-request")
    public String updateOneMaterialRequest(@RequestBody MaterialRequest matReq) {
        String userId = "ini diambil dari jwtcookie";
        _materialRequestService.updateOneMaterialRequest(userId, matReq);

        return "successfully update material";
    }
    
}
