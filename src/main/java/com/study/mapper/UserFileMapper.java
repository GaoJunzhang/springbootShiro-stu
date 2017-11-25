package com.study.mapper;

import com.study.model.UserFile;
import com.study.util.MyMapper;
import org.apache.ibatis.annotations.Param;

public interface UserFileMapper extends MyMapper<UserFile> {
    public int addUserFile(int userid,int fileid);

    public int deleteUserFile(@Param("userId") int userId,@Param("fileId") int[] fileId);
}