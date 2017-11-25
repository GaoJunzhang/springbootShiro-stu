package com.study.controller;

import com.study.model.FileInfo;
import com.study.model.User;
import com.study.service.FileInfoService;
import com.study.service.UserService;
import com.study.util.PasswordHelper;
import com.study.util.VTools;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by 张高俊 on 2017/10/22.
 */
@RestController
@RequestMapping(value = "/fileInterface")
public class FileInterfaceController {

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

    @RequestMapping(value = "/loginVerifyAndUpdateequipmentNo",method = RequestMethod.POST)
    public String loginVerifyAndUpdateequipmentNo(@Param("equipmentno")String equipmentno, @Param("uname")String uname,@Param("pwd")String pwd){

        if (VTools.StringIsNullOrSpace(uname)){
            return "账号不能为空";
        }
        if (VTools.StringIsNullOrSpace(pwd)){
            return "密码不能为空";
        }
        if (VTools.StringIsNullOrSpace(equipmentno)){
            return "设备号不能为空";
        }
        User user = userService.selectByUsername(uname);
        if(user==null){
            return "账号不存在";
        }
        PasswordHelper passwordHelper = new PasswordHelper();
        String pwdMd5 = passwordHelper.getPassword(pwd);
        if(!pwdMd5.equals(user.getPassword())){
            return "密码错误";
        }
        try {
            user.setEquipmentno(equipmentno);
            userService.updateEquipmentNoByUsername(user);
            return "success";
        }catch (Exception e){
            e.printStackTrace();
            return "faile";
        }

    }

}
