package com.epoint.cache.mapper;

import com.epoint.cache.entity.Admin;
import org.apache.ibatis.annotations.Param;

public interface AdminMapper {
    //@Param 参数代表在对应的xml 文件中该方法的属性
    int deleteByPrimaryKey(@Param("adminid") Integer adminid);

    int insert(Admin record);

    int insertSelective(Admin record);

    Admin selectByPrimaryKey(Integer adminid);

    int updateByPrimaryKeySelective(Admin record);

    int updateByPrimaryKey(Admin record);
}