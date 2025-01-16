package com.rivandra.mrms_api.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import model.*;

import com.rivandra.mrms_api.mapper.MaterialRequestItemMapper;


@Service
public class MaterialRequestItemService {
    @Autowired
    private MaterialRequestItemMapper _materialRequestItemMapper;

    //#region request-item

    public void insertRequestItem(String userId, MaterialRequestItem reqObj) {
        _materialRequestItemMapper.insertOne(reqObj);
    }
    public void updateRequestItem(String userId, MaterialRequestItem reqObj) {
        _materialRequestItemMapper.updateOne(reqObj);
    }
    public void deleteRequestItem(String userId, Integer requestId, Integer itemId ) {
        _materialRequestItemMapper.deleteOne(userId, requestId, itemId);
    }
    //#endregion
}
