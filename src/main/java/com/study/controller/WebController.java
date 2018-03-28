package com.study.controller;

import com.study.model.FileInfo;
import com.study.service.FileInfoService;
import com.study.service.UserService;
import com.study.util.VTools;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 2018/3/28.
 */
@RestController
@RequestMapping(value = "/api/web")
public class WebController {
    @Resource
    private FileInfoService fileInfoService;

    @Resource
    private UserService userService;

    @RequestMapping(value = "/getUserFiles",method = RequestMethod.POST)
    public List<FileInfo> getUserFiles(@Param("useraccout") String useraccout){
        List list = new ArrayList();
        list.add("用户名不能为空");
        if(VTools.StringIsNullOrSpace(useraccout)){
            return list;
        }
        return fileInfoService.loadFileResources(useraccout);
    }
}
