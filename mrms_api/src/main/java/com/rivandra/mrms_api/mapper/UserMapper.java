package com.rivandra.mrms_api.mapper;

import org.apache.ibatis.annotations.*;
import model.*;

@Mapper
public interface UserMapper {

    @Select("""
        SELECT user_id as userId, email, role, pwd, created_at as createdAt 
        FROM users 
        WHERE user_id=#{ userId } and pwd = crypt( #{ password } , pwd);
    """)
    User getOneWithPassword(String userId, String password);

    @Select("""
        SELECT 
            user_id as userId, 
            email, 
            role, 
            pwd, 
            created_at as createdAt 
        FROM users 
        WHERE user_id=#{ userId }
    """)
    User getOneWithId(String userId);

}
