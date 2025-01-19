package com.rivandra.mrms_api.mapper;

import org.apache.ibatis.annotations.*;
import java.util.List;
import model.MaterialRequestItem;

@Mapper
public interface MaterialRequestItemMapper {

    @Select("""
        select 
            request_id as requestId, 
            item_id as itemId, 
            material_name as materialName, 
            requested_quantity as requestedQuantity, 
            usage_description as usageDescription
        from material_request_items where request_id=#{requestId};
    """)
    List<MaterialRequestItem> getManyByRequestId(@Param("requestId") Integer requestId);

    @Select("""
        select 
            a.item_id as itemId,
            a.request_id as requestId,
            a.material_name as itemId,
            a.item_id as itemId,
            a.requested_quantity as requestedQuantity,
            a.usage_description as usageDescription
        from material_request_items a
		left join (
			select request_id, status, submit_by from material_requests
			where 
				(submit_by=#{ userId } or #{ userId }='ALL' ) 
				and (status=#{ status } or #{ status }='ALL' )
				and submit_date >= cast(#{ dateFrom } as date) and submit_date <= cast(#{ dateTo } as date)
		) b on a.request_id=b.request_id
		where b.request_id is not null
    """)
    List<MaterialRequestItem> getManyByDate(
        @Param("userId") String userId, 
        @Param("status") String status, 
        @Param("dateFrom") String dateFrom, 
        @Param("dateTo") String dateTo);


    @Update("""
        update material_request_items 
        set material_name = #{ matReqItem.materialName }, requested_quantity=#{ matReqItem.requestedQuantity }, usage_description=#{ matReqItem.usageDescription } 
        where   
            request_id= #{ matReqItem.requestId } and item_id= #{ matReqItem.itemId} 
            and (
                exists (select * from material_requests where submit_by=#{userId} and request_id=#{ matReqItem.requestId })
                or #{userId}='ALL'
            )
    """)
    void updateOne(@Param("userId") String userId, @Param("matReqItem") MaterialRequestItem matReqItem);

    @Insert("""
        INSERT INTO material_request_items (request_id, material_name, requested_quantity, usage_description)
        VALUES
            (#{ matReqItem.requestId }, #{ matReqItem.materialName }, #{ matReqItem.requestedQuantity }, #{ matReqItem.usageDescription })
    """)
    void insertOne(@Param("matReqItem") MaterialRequestItem matReqItem);

    @Delete("""
        DELETE FROM material_request_items 
        WHERE 
            EXISTS (
                SELECT submit_by 
                FROM material_requests 
                WHERE request_id = #{requestId} 
                  AND submit_by = #{userId}
            )
            AND request_id = #{requestId} 
            AND item_id = #{itemId}    
    """)
    void deleteOne(
        @Param("userId") String userId, 
        @Param("requestId") Integer requestId,
        @Param("itemId") Integer itemId
    );

}
