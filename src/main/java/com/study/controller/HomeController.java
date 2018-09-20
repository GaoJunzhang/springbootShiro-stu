package com.study.controller;

import com.aliyun.oss.model.OSSObjectSummary;
import com.study.model.User;
import com.study.util.OSSManageUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by yangqj on 2017/4/21.
 */
@Controller
public class HomeController {
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login() {
        return "login";
    }

    @Value("${accessUrl}")
    private String accessUrl;

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String login(HttpServletRequest request, User user, Model model) {
        if (StringUtils.isEmpty(user.getUsername()) || StringUtils.isEmpty(user.getPassword())) {
            request.setAttribute("msg", "用户名或密码不能为空！");
            return "login";
        }
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(user.getUsername(), user.getPassword());
        try {
            subject.login(token);
            subject.getSession().setTimeout(1800000);
            return "redirect:fileInfo";
        } catch (LockedAccountException lae) {
            token.clear();
            request.setAttribute("msg", "用户已经被锁定不能登录，请与管理员联系！");
            return "login";
        } catch (AuthenticationException e) {
            token.clear();
            request.setAttribute("msg", "用户或密码不正确！");
            return "login";
        }
    }

    @RequestMapping(value = {"/usersPage", ""})
    public String usersPage() {
        return "user/users";
    }

    @RequestMapping("/rolesPage")
    public String rolesPage() {
        return "role/roles";
    }

    @RequestMapping("/resourcesPage")
    public String resourcesPage() {
        return "resources/resources";
    }

    @RequestMapping("/403")
    public String forbidden() {
        return "403";
    }

    @RequestMapping("/fileInfo")
    public String filesourcePage() {
        return "fileInfo/fileInfo";
    }

    @RequestMapping(value = "/addfileView")
    public String addfileView(Model model) {

        OSSManageUtil ossManageUtil = new OSSManageUtil();
        List<OSSObjectSummary> objectSummaries = null;
        try {
            objectSummaries = ossManageUtil.getOSSobjectSummary("vstu");
            List<Map> listApp = new ArrayList<>();
            List<Map> listVideo = new ArrayList<>();
            List<Map> listImage = new ArrayList<>();
            List<Map> listPreviewVideo = new ArrayList<>();
            String strTemp = "";
            for (int i = 0; i < objectSummaries.size(); i++) {
                strTemp = objectSummaries.get(i).getKey();
                if (strTemp.contains("lefile")&&strTemp.contains(".")) {
                    Map map = new HashMap();
                    map.put("address", accessUrl + "/" + strTemp);
                    map.put("name", strTemp.replace("lefile/", ""));
                    if (strTemp.contains(".apk")) {
                        listApp.add(map);
                    }else {
                        listVideo.add(map);
                    }
                }
                if (strTemp.contains("imgfile")&&strTemp.contains(".")) {
                    Map map = new HashMap();
                    map.put("address", accessUrl + "/" + strTemp);
                    map.put("name", strTemp.replace("imgfile/", ""));
                    listImage.add(map);
                }
                if (strTemp.contains("prefile")&&strTemp.contains(".")) {
                    Map map = new HashMap();
                    map.put("address", accessUrl + "/" + strTemp);
                    map.put("name", strTemp.replace("prefile/", ""));
                    listPreviewVideo.add(map);
                }
            }
            model.addAttribute("fileListApp", listApp);
            model.addAttribute("fileListVideo", listVideo);
            model.addAttribute("filelistImage", listImage);
            model.addAttribute("filePreVideo",listPreviewVideo);
//            return "fileSources/fileSources";
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "upload/uploadfile";
    }

    @RequestMapping(value = "/editfileView")
    public String editfileView(Model model) {

        OSSManageUtil ossManageUtil = new OSSManageUtil();
        List<OSSObjectSummary> objectSummaries = null;
        try {
            objectSummaries = ossManageUtil.getOSSobjectSummary("vstu");
            List<Map> listApp = new ArrayList<>();
            List<Map> listVideo = new ArrayList<>();
            List<Map> listImage = new ArrayList<>();
            List<Map> listPreviewVideo = new ArrayList<>();
            String strTemp = "";
            for (int i = 0; i < objectSummaries.size(); i++) {
                strTemp = objectSummaries.get(i).getKey();
                if (strTemp.contains("lefile")&&strTemp.contains(".")) {
                    Map map = new HashMap();
                    map.put("address", accessUrl + "/" + strTemp);
                    map.put("name", strTemp.replace("lefile/", ""));
                    if (strTemp.contains(".apk")) {
                        listApp.add(map);
                    }else {
                        listVideo.add(map);
                    }
                }
                if (strTemp.contains("imgfile")&&strTemp.contains(".")) {
                    Map map = new HashMap();
                    map.put("address", accessUrl + "/" + strTemp);
                    map.put("name", strTemp.replace("imgfile/", ""));
                    listImage.add(map);
                }
                if (strTemp.contains("prefile")&&strTemp.contains(".")) {
                    Map map = new HashMap();
                    map.put("address", accessUrl + "/" + strTemp);
                    map.put("name", strTemp.replace("prefile/", ""));
                    listPreviewVideo.add(map);
                }
            }
            model.addAttribute("fileListApp", listApp);
            model.addAttribute("fileListVideo", listVideo);
            model.addAttribute("filelistImage", listImage);
            model.addAttribute("filePreVideo",listPreviewVideo);
//            return "fileSources/fileSources";
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "fileInfo/fileInfoEdit";
    }

    @RequestMapping(value = "/viewUserFile")
    public String addUserFile() {
        return "user/fileInfo";
    }

    @RequestMapping(value = "/userfileinfo")
    public String userfileinfo() {
        return "user/userfileinfo";
    }

    @RequestMapping(value = "/fileSources")
    public String fileSources() {
        return "fileSources//fileSources";
    }

}
