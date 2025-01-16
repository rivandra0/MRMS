package com.rivandra.mrms_api.mapper;

import org.apache.ibatis.annotations.*;
import java.util.List;
import model.MaterialRequestItem;

@Mapper
public interface MaterialRequestItemMapper {

    @Select("SELECT * FROM material_request_item WHERE request_id = #{requestId}")
    List<MaterialRequestItem> getManyByRequestId(String requestId);

    @Select("SELECT * FROM material_request_item WHERE request_id = #{requestId}")
    List<MaterialRequestItem> getManyByDate(String userId, String status, String dateFrom, String dateTo);

}
