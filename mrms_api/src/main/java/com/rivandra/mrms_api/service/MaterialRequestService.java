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

    //#region request

    public List<MaterialRequest> getRequests(String userId, String status, String dateFrom, String dateTo) {
        List<MaterialRequest> reqs = _materialRequestMapper.getMany(userId, status, dateFrom, dateTo);
        List<MaterialRequestItem> items = _materialRequestItemMapper.getManyByDate(userId, status, dateFrom, dateTo);
        
        // Group items by requestId
        Map<Integer, List<MaterialRequestItem>> itemsByRequestId = items.stream()
            .collect(Collectors.groupingBy(MaterialRequestItem::getRequestId));

        // Populate items in each MaterialRequest
        reqs.forEach(req -> req.setItems(itemsByRequestId.getOrDefault(req.getRequestId(), Collections.emptyList())));

        return reqs;
    }

    public MaterialRequest getRequest (String userId, Integer requestId) {
        MaterialRequest reqObj = _materialRequestMapper.getOne(userId, requestId);
        reqObj.setItems(_materialRequestItemMapper.getManyByRequestId(requestId));

        return reqObj;
    }

    public void insertRequest(String userId, MaterialRequest reqObj) {
        reqObj.setSubmitBy(userId);
        _materialRequestMapper.insertOne(reqObj);

        for (MaterialRequestItem item : reqObj.getItems()) {
            item.setRequestId(reqObj.getRequestId());
            _materialRequestItemMapper.insertOne(item);
        };
    }

    public void updateRequest(String userId, MaterialRequest reqObj) {
        Integer requestId = reqObj.getRequestId();

        for (MaterialRequestItem item : reqObj.getItems()) {
            item.setRequestId(requestId);
            _materialRequestItemMapper.updateOne(userId, item);
        };

    }
    
    public void deleteRequest(String userId, int requestId) {
        _materialRequestMapper.deleteOne(userId, requestId);

    }

    //#endregion

}
