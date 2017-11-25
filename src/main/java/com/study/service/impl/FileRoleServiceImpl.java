package com.study.service.impl;

import com.study.model.FileRole;
import com.study.service.FileRoleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by 张高俊 on 2017/10/21.
 */
@Service("fileRoleService")
public class FileRoleServiceImpl extends BaseService<FileRole> implements FileRoleService{

    @Override
    @Transactional(propagation= Propagation.REQUIRED,readOnly=false,rollbackFor={Exception.class})
    public void addFileRole(FileRole fileRole){

        //删除
        /*Example example = new Example(FileRole.class);
        Example.Criteria criteria = example.createCriteria();
        criteria
        criteria.andEqualTo("fileid",userRole.getUserid());
        mapper.deleteByExample(example);*/
        //添加
        String[] fileids = fileRole.getFileid().split(",");
        String[] roleids = fileRole.getRoleid().split(",");
        for (String roleId : roleids) {
            for(String fileId:fileids){
                FileRole fR = new FileRole();
                fR.setFileid(fileId);
                fR.setRoleid(roleId);
                mapper.insert(fR);
            }
        }
    }
}
