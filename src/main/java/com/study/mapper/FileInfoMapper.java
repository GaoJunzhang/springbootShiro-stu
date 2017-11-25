package com.study.mapper;

import com.study.model.FileInfo;
import com.study.util.MyMapper;

import java.util.List;

public interface FileInfoMapper extends MyMapper<FileInfo> {
//loadFileResources
public List<FileInfo> loadFileResources(String username);
}