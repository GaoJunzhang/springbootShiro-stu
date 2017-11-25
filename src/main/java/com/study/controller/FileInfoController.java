package com.study.controller;

import com.github.pagehelper.PageInfo;
import com.study.config.OSSConfigure;
import com.study.model.FileInfo;
import com.study.model.FileRole;
import com.study.model.UserFile;
import com.study.service.FileInfoService;
import com.study.service.FileRoleService;
import com.study.service.UserService;
import com.study.util.OSSManageUtil;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by 张高俊 on 2017/10/10.
 */
@RestController
@RequestMapping("/fileinfos")
public class FileInfoController {

    @Resource
    private FileInfoService fileInfoService;

    @Resource
    private FileRoleService fileRoleService;

    @Resource
    private UserService userService;

    @RequestMapping
    public Map<String,Object> getAll(FileInfo fileInfo, String draw,
                                     @RequestParam(required = false, defaultValue = "1") int start,
                                     @RequestParam(required = false, defaultValue = "10") int length){
        Map<String,Object> map = new HashMap<>();
        PageInfo<FileInfo> pageInfo = fileInfoService.selectByPage(fileInfo, start, length);
        System.out.println("pageInfo.getTotal():"+pageInfo.getTotal());
        map.put("draw",draw);
        map.put("recordsTotal",pageInfo.getTotal());
        map.put("recordsFiltered",pageInfo.getTotal());
        map.put("data", pageInfo.getList());
        return map;
    }

    @RequestMapping(value = "/getAllUserFile")
    public Map<String,Object> getAllUserfile( String draw,
                                     @RequestParam(required = false) String username,
                                     @RequestParam(required = false, defaultValue = "1") int start,
                                     @RequestParam(required = false, defaultValue = "10") int length){
        Map<String,Object> map = new HashMap<>();
        PageInfo<FileInfo> pageInfo = fileInfoService.selectPageByUsername(username, start, length);
        System.out.println("pageInfo.getTotal():"+pageInfo.getTotal());
        map.put("draw",draw);
        map.put("recordsTotal",pageInfo.getTotal());
        map.put("recordsFiltered",pageInfo.getTotal());
        map.put("data", pageInfo.getList());
        return map;
    }

    @RequestMapping(value = "/delete")
    public String delete(@RequestParam() String[] ids,@RequestParam() String[] filesrcs,@RequestParam() String[] imgsrcs){

        try{
            OSSConfigure ossConfigure = new OSSConfigure();
            for(int i=0;i<ids.length;i++){
                if(filesrcs[i].length()>ossConfigure.getAccessUrl().length()+1){
                    OSSManageUtil.deleteFile(ossConfigure,filesrcs[i].replace(ossConfigure.getAccessUrl()+"/",""));
                }
                if(imgsrcs.length>0){
                    if(imgsrcs[i].length()>ossConfigure.getAccessUrl().length()+1){
                        OSSManageUtil.deleteFile(ossConfigure,imgsrcs[i].replace(ossConfigure.getAccessUrl()+"/",""));
                    }
                }
                System.out.println(filesrcs[i].replace(ossConfigure.getAccessUrl()+"/",""));
                 fileInfoService.delFile(Integer.parseInt(ids[i]));
            }
            return "success";
        }catch (Exception e){
            e.printStackTrace();
            return "fail";
        }
    }

    /**
     * 保存用户角色
     * @param fileRole 文件角色
     *  	  此处获取的参数的角色id是以 “,” 分隔的字符串
     * @return
     */
    @RequestMapping("/saveFileRoles")
    public String saveFileRoles(FileRole fileRole){
        if(StringUtils.isEmpty(fileRole.getFileid()))
            return "error";
        try {
            fileRoleService.addFileRole(fileRole);
            return "success";
        } catch (Exception e) {
            e.printStackTrace();
            return "fail";
        }
    }
    @RequestMapping(value = "/addUserFile",method = RequestMethod.POST)
    public String adduserFile(@RequestParam(value = "userId")String userId,@RequestParam(value = "fileids")String[] fileids,RedirectAttributes model){

        UserFile  userFile = null;
        System.out.println("用户id"+userId);
        int flg = 0;
        System.out.println(fileids.toString());
        for (int i=0;i<fileids.length;i++){
            userFile = new UserFile();
            userFile.setUserId(Integer.parseInt(userId));
            userFile.setFileId(Integer.parseInt(fileids[i]));
            flg = userService.addUserFile(userFile);
        }
        if(flg==1){
            return "success";
        }
        model.addFlashAttribute("userid",userId);
        model.addFlashAttribute("data","success");
        return "error";
    }

}
