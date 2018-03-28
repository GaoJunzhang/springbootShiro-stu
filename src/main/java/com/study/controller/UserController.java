package com.study.controller;

import com.github.pagehelper.PageInfo;
import com.study.model.User;
import com.study.model.UserRole;
import com.study.service.UserRoleService;
import com.study.service.UserService;
import com.study.util.ExcelUtil;
import com.study.util.PasswordHelper;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by yangqj on 2017/4/22.
 */
@RestController
@RequestMapping("/users")
public class UserController {
    @Resource
    private UserService userService;
    @Resource
    private UserRoleService userRoleService;

    @RequestMapping
    public Map<String, Object> getAll(User user, String draw,
                                      @RequestParam(required = false, defaultValue = "1") int start,
                                      @RequestParam(required = false, defaultValue = "10") int length) {
        Map<String, Object> map = new HashMap<>();
        PageInfo<User> pageInfo = userService.selectByPage(user, start, length);
        System.out.println("pageInfo.getTotal():" + pageInfo.getTotal());
        map.put("draw", draw);
        map.put("recordsTotal", pageInfo.getTotal());
        map.put("recordsFiltered", pageInfo.getTotal());
        map.put("data", pageInfo.getList());
        return map;
    }


    /**
     * 保存用户角色
     *
     * @param userRole 用户角色
     *                 此处获取的参数的角色id是以 “,” 分隔的字符串
     * @return
     */
    @RequestMapping("/saveUserRoles")
    public String saveUserRoles(UserRole userRole) {
        if (StringUtils.isEmpty(userRole.getUserid()))
            return "error";
        try {
            userRoleService.addUserRole(userRole);
            return "success";
        } catch (Exception e) {
            e.printStackTrace();
            return "fail";
        }
    }

    @RequestMapping(value = "/add")
    public String add(User user) {
        User u = userService.selectByUsername(user.getUsername());
        if (u != null)
            return "error";
        try {
            user.setEnable(1);
            PasswordHelper passwordHelper = new PasswordHelper();
            passwordHelper.encryptPassword(user);
            userService.save(user);
            return "success";
        } catch (Exception e) {
            e.printStackTrace();
            return "fail";
        }
    }

    @RequestMapping(value = "/delete")
    public String delete(Integer id) {
        try {
            userService.delUser(id);
            return "success";
        } catch (Exception e) {
            e.printStackTrace();
            return "fail";
        }
    }

    @RequestMapping(value = "/deleteUserFile")
    public String deleteUserFile( int userId,@RequestParam(name = "fileId") int[] fileId){
        try {
            userService.deleteUserFile(userId,fileId);
            return "success";
        } catch (Exception e) {
            e.printStackTrace();
            return "fail";
        }
    }

    @RequestMapping(value = "/resetPassword")
    public String resetPassword(Integer id) {
        User user = userService.selectByKey(id);
        if (user == null)
            return "error";
        try {
            user.setEnable(1);
            user.setPassword("888888");
            PasswordHelper passwordHelper = new PasswordHelper();
            passwordHelper.encryptPassword(user);
            userService.updateEquipmentNoByUsername(user);
            return "success";
        } catch (Exception e) {
            e.printStackTrace();
            return "fail";
        }
    }
    @RequestMapping("/downloadUsers")
    public void downloadUsers(HttpServletResponse response)throws IOException {
        String[] headers = { "账号", "真实姓名","手机号码", "学校","用户类型","关联设备","是否启用"};
        String fileName = "用户明细"+System.currentTimeMillis()+".xlsx"; //文件名
        String sheetName = "用户明细";//sheet名
        List<User> list = userService.findAll();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String [][]values = new String[list.size()][];
        for(int i=0;i<list.size();i++){
            values[i] = new String[headers.length];
            //将对象内容转换成string
            User user = list.get(i);
            values[i][0] = user.getUsername()+"";
            values[i][1] = user.getRealname()+"";
            values[i][2] = user.getMobile()+"";
            values[i][3] = user.getOrganization()+"";
            if ("0".equals(user.getLevel())){
                values[i][4] = "学生";
            }else if("1".equals(user.getLevel())){
                values[i][4] = "教师";
            }else {
                values[i][4] = "普通用户";
            }
            values[i][5] = user.getEquipmentno()+"";
            if ("0".equals(user.getEnable())){
                values[i][6] = "禁用";
            }else {
                values[i][6] = "可用";
            }
        }
//        HSSFWorkbook wb = ExcelUtil.getHSSFWorkbook(sheetName, headers, values, null);
        SXSSFWorkbook wb = ExcelUtil.getSXSSFWorkbook(sheetName,headers,values,null);
        try {
            this.setResponseHeader(response, fileName);
            OutputStream os = response.getOutputStream();
            wb.write(os);
            os.flush();
            os.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void setResponseHeader(HttpServletResponse response, String fileName) {
        try {
            try {
                fileName = new String(fileName.getBytes(),"ISO8859-1");
            } catch (UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            response.setContentType("application/octet-stream;charset=ISO8859-1");
            response.setHeader("Content-Disposition", "attachment;filename="+ fileName);
            response.addHeader("Pargam", "no-cache");
            response.addHeader("Cache-Control", "no-cache");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @RequestMapping(value = "/editPwd",method = RequestMethod.POST)
    public String editPwd(HttpSession session,@RequestParam(value = "currentPwd")String currentPwd, @RequestParam(value = "newPwd")String newPwd){
        try {
            PasswordHelper passwordHelper = new PasswordHelper();
            User user = userService.selectByKey(session.getAttribute("userSessionId"));
            User user1 = new User();
            user1.setPassword(currentPwd);
            user1.setUsername(user.getUsername());
            String Pwd =passwordHelper.getPassword(user1);
            if (!Pwd.equals(user.getPassword())){
                return "pwdError";
            }
            user.setPassword(newPwd);
            passwordHelper.encryptPassword(user);
            userService.updateEquipmentNoByUsername(user);
            return "success";
        } catch (Exception e) {
            e.printStackTrace();
            return "fail";
        }
    }

}
