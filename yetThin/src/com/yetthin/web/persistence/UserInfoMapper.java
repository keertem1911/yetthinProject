package com.yetthin.web.persistence;

import java.util.List;

import com.yetthin.web.domain.UserInfo;

public interface UserInfoMapper {
    int deleteByPrimaryKey(Integer userId);

    int insert(UserInfo record);

    int insertSelective(UserInfo record);

    UserInfo selectByPrimaryKey(Integer userId);

    int updateByPrimaryKeySelective(UserInfo record);

    int updateByPrimaryKeyWithBLOBs(UserInfo record);

    int updateByPrimaryKey(UserInfo record);
    

    UserInfo selectByPhoneNumAndPassWord(UserInfo record);
    List<UserInfo> getAllUser();
    UserInfo selectByPhoneNum(String phoneNum);
    UserInfo selectByEmail(String email);
    
    List<UserInfo> lookIdeaText();
}