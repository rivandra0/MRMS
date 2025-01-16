package com.rivandra.mrms_api.controller;

import org.springframework.web.bind.annotation.RestController;

import com.rivandra.mrms_api.service.MaterialRequestService;

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
    
    //✅
    @GetMapping("/material-requests")
    public List<MaterialRequest> getManyMaterialRequest(@RequestParam String status, @RequestParam String dateFrom, @RequestParam String dateTo) {
        String userId = "prd1";

        return _materialRequestService.getRequests(userId, status, dateFrom, dateTo);
    }

    //✅
    @GetMapping("/material-request")
    public MaterialRequest getOneMaterialRequest(@RequestParam String requestId) {
        String userId = "prd1";

        return _materialRequestService.getRequest(userId, Integer.valueOf(requestId));
    }

    //✅
    @PostMapping("/material-request")
    public String insertOneMaterialRequest(@RequestBody MaterialRequest matReq) {
        String userId = "prd1";

        _materialRequestService.insertRequest(userId, matReq);

        return "successfully insert material";
    }

    //✅
    @PutMapping("/material-request")
    public String updateOneMaterialRequest(@RequestBody MaterialRequest matReq) {
        String userId = "ALL";

        _materialRequestService.updateRequest(userId, matReq);

        return "successfully update material request";
    }

    //✅
    @DeleteMapping("/material-request")
    public String deleteOneMaterialRequest(@RequestParam String requestId) {
        String userId = "ALL";


        _materialRequestService.deleteRequest(userId, Integer.parseInt(requestId));

        return "successfully delete material request";
    }


}
