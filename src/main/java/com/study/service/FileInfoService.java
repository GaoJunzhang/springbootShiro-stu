package com.study.service;

import com.github.pagehelper.PageInfo;
import com.study.model.FileInfo;

import java.util.List;

/**
 * Created by 张高俊 on 2017/10/10.
 */
public interface FileInfoService extends IService<FileInfo> {
    PageInfo<FileInfo> selectByPage(FileInfo fileInfo, int start, int length);

    PageInfo<FileInfo> selectPageByUsername(String username, int start, int length);

    FileInfo selectByFilename(String filename);

    void delFile(Integer fileid);

    FileInfo selectByUsername(String username);

    List<FileInfo> loadFileResources(String username);

    FileInfo selectById(int id);

    List<FileInfo> findAll();


}
