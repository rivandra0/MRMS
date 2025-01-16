package com.rivandra.mrms_api.mapper;

import org.apache.ibatis.annotations.*;
import java.util.List;
import model.MaterialRequest;

@Mapper
public interface MaterialRequestMapper {

    @Select("SELECT * FROM material_request WHERE request_id = #{requestId}")
    MaterialRequest getOne(String userId, String requestId);

    @Select("SELECT * FROM material_request_item WHERE request_id = #{requestId}")
    List<MaterialRequest> getMany(String userId, String status, String dateFrom, String dateTo);

    @Insert("""
        INSERT INTO material_request (request_id, status, submit_by, submit_date)
        VALUES (#{requestId}, #{status}, #{submitBy}, #{submitDate})
    """)
    void insertOne(String userId, MaterialRequest materialRequest);

    @Update("""
        INSERT INTO material_request (request_id, status, submit_by, submit_date)
        VALUES (#{requestId}, #{status}, #{submitBy}, #{submitDate})
    """)
    void updateOne(String userId, MaterialRequest materialRequest);

}
