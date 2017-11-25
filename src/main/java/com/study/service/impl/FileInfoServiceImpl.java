package com.study.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.study.mapper.FileInfoMapper;
import com.study.model.FileInfo;
import com.study.service.FileInfoService;
import com.study.util.VTools;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.util.StringUtil;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by 张高俊 on 2017/10/10.
 */
@Service("fileInfoService")
public class FileInfoServiceImpl extends BaseService<FileInfo> implements FileInfoService{

    @Resource
    private FileInfoMapper fileInfoMapper;

    @Override
    public PageInfo<FileInfo> selectByPage(FileInfo fileInfo, int start, int length){

        int page = start/length+1;
        Example example = new Example(FileInfo.class);
        Example.Criteria criteria = example.createCriteria();
        if (StringUtil.isNotEmpty(fileInfo.getFilename())) {
            criteria.andLike("filename", "%" + fileInfo.getFilename() + "%");
        }
        if (fileInfo.getFiletype() != null&&fileInfo.getFiletype()!="") {
            criteria.andEqualTo("filetype", fileInfo.getFiletype());
        }
        if (!VTools.StringIsNullOrSpace(fileInfo.getEnable())) {
            criteria.andEqualTo("enable", fileInfo.getEnable());
        }
        //分页查询
        PageHelper.startPage(page, length);
        List<FileInfo> fileInfoListList = selectByExample(example);
        return new PageInfo<>(fileInfoListList);
    }

    @Override
    public PageInfo<FileInfo> selectPageByUsername(String username, int start, int length){
        int page = start/length+1;
        //分页查询
        PageHelper.startPage(page, length);
        List<FileInfo> userFileListList = fileInfoMapper.loadFileResources(username);
        return new PageInfo<>(userFileListList);
    }

    @Override
    public FileInfo selectByFilename(String filename){
        Example example = new Example(FileInfo.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("filename",filename);
        List<FileInfo> fileInfoList = selectByExample(example);
        if(fileInfoList.size()>0){
            return fileInfoList.get(0);
        }
        return null;
    }

    @Override
    public FileInfo selectByUsername(String username){
        Example example = new Example(FileInfo.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("username",username);
        List<FileInfo> fileInfoList = selectByExample(example);
        if(fileInfoList.size()>0){
            return fileInfoList.get(0);
        }
        return null;
    }

    @Override
    @Transactional(propagation= Propagation.REQUIRED,readOnly=false,rollbackFor={Exception.class})
    public void delFile(Integer fileid){

        //删除资源
        mapper.deleteByPrimaryKey(fileid);
        //TODO 删除资源角色表
       /* Example example = new Example(UserRole.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("userid",userid);
        userRoleMapper.deleteByExample(example);*/
    }
    @Override
    public List<FileInfo> loadFileResources(String username){
        return fileInfoMapper.loadFileResources(username);
    }

    @Override
    public FileInfo selectById(int id){
        Example example = new Example(FileInfo.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("id",id);
        List<FileInfo> fileInfoList = selectByExample(example);
        if(fileInfoList.size()>0){
            return fileInfoList.get(0);
        }
        return null;
    }

    @Override
    public List<FileInfo> findAll(){
        Example example = new Example(FileInfo.class);
        return selectByExample(example);
    }
}
