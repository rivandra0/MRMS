package com.rivandra.mrms_api.mapper;

import org.apache.ibatis.annotations.*;
import model.MaterialRequest;

@Mapper
public interface AppUserMapper {

    @Select("SELECT * FROM material_request WHERE request_id = #{requestId}")
    MaterialRequest getOne(String requestId);

}
