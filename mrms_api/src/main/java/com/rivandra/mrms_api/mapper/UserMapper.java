package com.rivandra.mrms_api.mapper;

import org.apache.ibatis.annotations.*;
import model.*;

@Mapper
public interface UserMapper {

    @Select("""
        SELECT user_id, email, role, pwd, created_at 
        FROM users 
        WHERE user_id=#{ userId } and pwd = crypt( #{ password } , pwd);;
    """)
    User getOne(String userId, String password);

}
