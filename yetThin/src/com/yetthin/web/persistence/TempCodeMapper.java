package com.yetthin.web.persistence;

import com.yetthin.web.domain.TempCode;

public interface TempCodeMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(TempCode record);

    int insertSelective(TempCode record);

    TempCode selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TempCode record);

    int updateByPrimaryKey(TempCode record);
}