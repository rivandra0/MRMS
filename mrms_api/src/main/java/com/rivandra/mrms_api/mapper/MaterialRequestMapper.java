package com.rivandra.mrms_api.mapper;

import org.apache.ibatis.annotations.*;
import java.util.List;
import model.MaterialRequest;

@Mapper
public interface MaterialRequestMapper {

    @Select("""
        select 
            request_id as requestId, 
            status as status, 
            submit_by as submitBy, 
            submit_date as submitDate, 
            approved_by as approvedBy, 
            approved_date as approvedDate, 
            rejected_by as rejectedBy, 
            rejected_date as rejectedDate,
            remark as remark
        from material_requests
        where
            request_id=#{ requestId }
            AND (submit_by=#{ userId } or #{ userId }='ALL')
    """)
    MaterialRequest getOne(String userId, Integer requestId);


    @Select("""
        select 
            request_id as requestId, 
            status as status, 
            submit_by as submitBy, 
            submit_date as submitDate, 
            approved_by as approvedBy, 
            approved_date as approvedDate, 
            rejected_by as rejectedBy, 
            rejected_date as rejectedDate,
            remark as remark
        from material_requests a
        left join users b on a.submit_by = b.user_id
        where
            (submit_by=#{ userId } or #{ userId }='ALL')
            and (status=#{ status } or #{ status }='ALL' )
			and submit_date >= cast(#{ dateFrom } as date) and submit_date <= cast(#{dateTo} as date) + INTERVAL '1 day'
        order by request_id desc
    """)
    List<MaterialRequest> getMany( 
        @Param("userId") String userId, 
        @Param("status") String status, 
        @Param("dateFrom") String dateFrom, 
        @Param("dateTo") String dateTo);

    @Insert("""
        INSERT INTO material_requests (status, submit_by, submit_date)
        VALUES ('PENDING_APPROVAL', #{ matReq.submitBy }, CURRENT_TIMESTAMP)
    """)
    @Options(useGeneratedKeys = true, keyProperty = "requestId") // Simple key handling
    void insertOne(@Param("matReq") MaterialRequest matReq);


    @Update("""
        update material_request
            set 
                status = #{ materialRequest.status }, 
                approved_by=#{ materialRequest.approvedBy },  
                approved_date=#{ materialRequest.approvedDate },
                rejected_by=#{ materialRequest.rejectedBy },  
                rejected_date=#{ materialRequest.rejectedDate }
                remark=#{ materialRequest.remark }
        where request_id=#{ materialRequest.requestId }
    """)
    void updateOne(
        @Param("userId") String userId, 
        @Param("materialRequest") MaterialRequest materialRequest);

    @Update("""
        update material_requests
        set 
            status = 'APPROVED', 
            approved_by = #{userId},  
            approved_date = CURRENT_TIMESTAMP
        where request_id = #{requestId}
    """)
    void approveOne(
        @Param("userId") String userId, 
        @Param("requestId") Integer requestId
    );

    @Update("""
        update material_requests
        set 
            status = 'REJECTED', 
            rejected_by = #{userId},  
            rejected_date = CURRENT_TIMESTAMP,
            remark = #{remark}
        where request_id = #{requestId}
    """)
    void rejectOne(
        @Param("userId") String userId, 
        @Param("requestId") Integer requestId,
        @Param("remark") String remark
    );

    @Delete("""
        DELETE FROM material_requests
        WHERE 
            request_id = #{requestId} 
            AND (submit_by = #{userId} OR #{userId} = 'ALL')
    """)
    void deleteOne(
        @Param("userId") String userId, 
        @Param("requestId") Integer requestId);
    

}
