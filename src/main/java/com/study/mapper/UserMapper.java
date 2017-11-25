package com.study.mapper;

import com.study.model.User;
import com.study.model.UserFile;
import com.study.util.MyMapper;

public interface UserMapper extends MyMapper<User> {

    public int addUserFile(UserFile userFile);
}