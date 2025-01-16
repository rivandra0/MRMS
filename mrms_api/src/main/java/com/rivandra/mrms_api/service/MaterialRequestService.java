package com.rivandra.mrms_api.service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import model.*;

import com.rivandra.mrms_api.mapper.MaterialRequestItemMapper;
import com.rivandra.mrms_api.mapper.MaterialRequestMapper;


@Service
public class MaterialRequestService {
    @Autowired
    private MaterialRequestMapper _materialRequestMapper;
    @Autowired
    private MaterialRequestItemMapper _materialRequestItemMapper;

    public List<MaterialRequest> getManyMaterialRequest(String userId, String status, String dateFrom, String dateTo) {
        List<MaterialRequest> reqs = _materialRequestMapper.getMany(userId, status, dateFrom, dateTo);
        List<MaterialRequestItem> items = _materialRequestItemMapper.getManyByDate(userId, status, dateFrom, dateTo);
        
        // Group items by requestId
        Map<String, List<MaterialRequestItem>> itemsByRequestId = items.stream()
            .collect(Collectors.groupingBy(MaterialRequestItem::getRequestId));

        // Populate items in each MaterialRequest
        reqs.forEach(req -> req.setItems(itemsByRequestId.getOrDefault(req.getRequestId(), Collections.emptyList())));

        return reqs;
    }

    public MaterialRequest getOneMaterialRequest (String userId, String requestId) {
        MaterialRequest reqObj = _materialRequestMapper.getOne(userId, requestId);
        reqObj.setItems(_materialRequestItemMapper.getManyByRequestId(requestId));

        return reqObj;
    }

    public void updateOneMaterialRequest(String userId, MaterialRequest reqObj) {
        _materialRequestMapper.updateOne(userId, reqObj);
    }

    public void insertOneMaterialRequest(String userId,MaterialRequest reqObj) {
        _materialRequestMapper.insertOne(userId, reqObj);
    }
}
