package com.study.controller;

import com.aliyun.oss.model.OSSObjectSummary;
import com.github.pagehelper.PageInfo;
import com.study.config.OSSConfigure;
import com.study.model.FileInfo;
import com.study.model.FileRole;
import com.study.model.UserFile;
import com.study.service.FileInfoService;
import com.study.service.FileRoleService;
import com.study.service.UserService;
import com.study.util.OSSManageUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
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

    @Value(value = "accessUrl")
    private String accessUrl;

    @RequestMapping
    public Map<String, Object> getAll(FileInfo fileInfo, String draw,
                                      @RequestParam(required = false, defaultValue = "1") int start,
                                      @RequestParam(required = false, defaultValue = "10") int length) {
        Map<String, Object> map = new HashMap<>();
        PageInfo<FileInfo> pageInfo = fileInfoService.selectByPage(fileInfo, start, length);
        System.out.println("pageInfo.getTotal():" + pageInfo.getTotal());
        map.put("draw", draw);
        map.put("recordsTotal", pageInfo.getTotal());
        map.put("recordsFiltered", pageInfo.getTotal());
        map.put("data", pageInfo.getList());
        return map;
    }

    @RequestMapping(value = "/getAllUserFile")
    public Map<String, Object> getAllUserfile(String draw,
                                              @RequestParam(required = false) String username,
                                              @RequestParam(required = false, defaultValue = "1") int start,
                                              @RequestParam(required = false, defaultValue = "10") int length) {
        Map<String, Object> map = new HashMap<>();
        PageInfo<FileInfo> pageInfo = fileInfoService.selectPageByUsername(username, start, length);
        System.out.println("pageInfo.getTotal():" + pageInfo.getTotal());
        map.put("draw", draw);
        map.put("recordsTotal", pageInfo.getTotal());
        map.put("recordsFiltered", pageInfo.getTotal());
        map.put("data", pageInfo.getList());
        return map;
    }

    @RequestMapping(value = "/delete")
    public String delete(@RequestParam() String[] ids, @RequestParam() String[] filesrcs, @RequestParam() String[] imgsrcs) {

        try {
            OSSConfigure ossConfigure = new OSSConfigure();
            for (int i = 0; i < ids.length; i++) {
                if (filesrcs[i].length() > ossConfigure.getAccessUrl().length() + 1) {
                    OSSManageUtil.deleteFile(ossConfigure, filesrcs[i].replace(ossConfigure.getAccessUrl() + "/", ""));
                }
                if (imgsrcs.length > 0) {
                    if (imgsrcs[i].length() > ossConfigure.getAccessUrl().length() + 1) {
                        OSSManageUtil.deleteFile(ossConfigure, imgsrcs[i].replace(ossConfigure.getAccessUrl() + "/", ""));
                    }
                }
                System.out.println(filesrcs[i].replace(ossConfigure.getAccessUrl() + "/", ""));
                fileInfoService.delFile(Integer.parseInt(ids[i]));
            }
            return "success";
        } catch (Exception e) {
            e.printStackTrace();
            return "fail";
        }
    }

    /**
     * 保存用户角色
     *
     * @param fileRole 文件角色
     *                 此处获取的参数的角色id是以 “,” 分隔的字符串
     * @return
     */
    @RequestMapping("/saveFileRoles")
    public String saveFileRoles(FileRole fileRole) {
        if (StringUtils.isEmpty(fileRole.getFileid()))
            return "error";
        try {
            fileRoleService.addFileRole(fileRole);
            return "success";
        } catch (Exception e) {
            e.printStackTrace();
            return "fail";
        }
    }

    @RequestMapping(value = "/addUserFile", method = RequestMethod.POST)
    public String adduserFile(@RequestParam(value = "userId") String userId, @RequestParam(value = "fileids") String[] fileids, RedirectAttributes model) {

        UserFile userFile = null;
        System.out.println("用户id" + userId);
        int flg = 0;
        System.out.println(fileids.toString());
        for (int i = 0; i < fileids.length; i++) {
            userFile = new UserFile();
            userFile.setUserId(Integer.parseInt(userId));
            userFile.setFileId(Integer.parseInt(fileids[i]));
            flg = userService.addUserFile(userFile);
        }
        if (flg == 1) {
            return "success";
        }
        model.addFlashAttribute("userid", userId);
        model.addFlashAttribute("data", "success");
        return "error";
    }

    @GetMapping(value = "/getOssFiles")
    public Map<String, Object> getOssFiles() {
        Map<String, Object> map = new HashMap<>();
        OSSManageUtil ossManageUtil = new OSSManageUtil();
        try {
            List<OSSObjectSummary> ossObjectSummaries = ossManageUtil.getOSSobjectSummary("vtu");
            /*PageBean pageBean = new PageBean(ossObjectSummaries.size());
            pageBean.setCurPage(10);
            int pagesize = pageBean.getPageSize();
            //获得分页数据在list集合中的索引
            int firstIndex = (10 - 1) * pagesize;
            int toIndex = 10 * pagesize;
            if (toIndex > ossObjectSummaries.size()) {
                toIndex = ossObjectSummaries.size();
            }
            if (firstIndex > toIndex) {
                firstIndex = 0;
                pageBean.setCurPage(1);
            }*//*
            //截取数据集合，获得分页数据
            List<OSSObjectSummary> courseList=ossObjectSummaries.subList(firstIndex, toIndex);
            map.put("ossFiles", courseList);*/
            return map;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
