package com.study.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.study.mapper.UserFileMapper;
import com.study.mapper.UserMapper;
import com.study.mapper.UserRoleMapper;
import com.study.model.User;
import com.study.model.UserFile;
import com.study.model.UserRole;
import com.study.service.UserService;
import com.study.util.ExcelImportUtils;
import com.study.util.PasswordHelper;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.util.StringUtil;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by yangqj on 2017/4/21.
 */
@Service("userService")
public class UserServiceImpl extends BaseService<User> implements UserService {
    @Resource
    private UserRoleMapper userRoleMapper;

    @Resource
    private UserMapper userMapper;

    @Resource
    private UserFileMapper userFileMapper;

    @Value("${sys.file.savepath}")
    private String path;

    @Override
    public PageInfo<User> selectByPage(User user, int start, int length) {
        int page = start / length + 1;
        Example example = new Example(User.class);
        Example.Criteria criteria = example.createCriteria();
        if (StringUtil.isNotEmpty(user.getUsername())) {
            criteria.andLike("username", "%" + user.getUsername() + "%");
        }
        if (StringUtil.isNotEmpty(user.getRealname())) {
            criteria.andLike("realname", "%" + user.getRealname() + "%");
        }
        if (user.getEnable() != null) {
            criteria.andEqualTo("enable", user.getEnable());
        }
        //分页查询
        PageHelper.startPage(page, length);
        List<User> userList = selectByExample(example);
        return new PageInfo<>(userList);
    }

    @Override
    public User selectByUsername(String username) {
        Example example = new Example(User.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("username", username);
        List<User> userList = selectByExample(example);
        if (userList.size() > 0) {
            return userList.get(0);
        }
        return null;
    }

    @Override
    public User selectByUserId(Integer userId) {
        Example example = new Example(User.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("id", userId);
        List<User> userList = selectByExample(example);
        if (userList.size() > 0) {
            return userList.get(0);
        }
        return null;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = {Exception.class})
    public void delUser(Integer userid) {
        //删除用户表
        mapper.deleteByPrimaryKey(userid);
        //删除用户角色表
        Example example = new Example(UserRole.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("userid", userid);
        userRoleMapper.deleteByExample(example);
    }

    @Override
    public void updateEquipmentNoByUsername(User user) {
        mapper.updateByPrimaryKey(user);
//        userMapper.updateEquipmentNoByUsername(equipmentno,username);
    }

    public String batchImport(String fileName, MultipartFile mfile) {
        File uploadDir = new File(path);
        //创建一个目录 （它的路径名由当前 File 对象指定，包括任一必须的父路径。）
        if (!uploadDir.exists()) uploadDir.mkdirs();
        //新建一个文件
        File tempFile = new File(path + "//fileupload//" + new Date().getTime() + ".xlsx");
        //初始化输入流
        InputStream is = null;
        try {
            //将上传的文件写入新建的文件中
            mfile.transferTo(tempFile);

            //根据新建的文件实例化输入流
            is = new FileInputStream(tempFile);

            //根据版本选择创建Workbook的方式
            Workbook wb = null;
            //根据文件名判断文件是2003版本还是2007版本
            if (ExcelImportUtils.isExcel2007(fileName)) {
                wb = new XSSFWorkbook(is);
            } else {
                wb = new HSSFWorkbook(is);
            }
            //根据excel里面的内容读取知识库信息
            return readExcelValue(wb, tempFile);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    is = null;
                    e.printStackTrace();
                }
            }
        }
        return "导入出错！请检查数据格式！";
    }

    /**
     * 解析Excel里面的数据
     *
     * @param wb
     * @return
     */
    private String readExcelValue(Workbook wb, File tempFile) {

        PasswordHelper passwordHelper = new PasswordHelper();
        //错误信息接收器
        String errorMsg = "";
        //得到第一个shell
        Sheet sheet = wb.getSheetAt(0);
        //得到Excel的行数
        int totalRows = sheet.getPhysicalNumberOfRows();
        //总列数
        int totalCells = 0;
        //得到Excel的列数(前提是有行数)，从第二行算起
        if (totalRows >= 2 && sheet.getRow(1) != null) {
            totalCells = sheet.getRow(1).getPhysicalNumberOfCells();
        }
        List<User> userBaseList = new ArrayList<User>();
        User tempUserKB;

        String br = "<br/>";

        //循环Excel行数,从第二行开始。标题不入库
        for (int r = 1; r < totalRows; r++) {
            String rowMessage = "";
            Row row = sheet.getRow(r);
            if (row == null) {
                errorMsg += br + "第" + (r + 1) + "行数据有问题，请仔细检查！";
                continue;
            }
            tempUserKB = new User();

            String username = "";
            String password = "";
            String realname = "";
            String level = "";
            String equipmentno = "";
            String category = "";
            String mac = "";
            String mobile = "";
            String organization = "";

            //循环Excel的列
            for (int c = 0; c < totalCells; c++) {
                Cell cell = row.getCell(c);
                if (null != cell) {
                    if (c == 0) {
                        username = cell.getStringCellValue();
                        if (StringUtils.isEmpty(username)) {
                            rowMessage += "用户名不能为空；";
                        } else if (username.length() > 60) {
                            rowMessage += "用户名字数不能超过60；";
                        }
                        tempUserKB.setUsername(username);
                    } else if (c == 1) {
                        password = cell.getStringCellValue();
                        if (StringUtils.isEmpty(password)) {
                            rowMessage += "密码不能为空；";
                        } else if (password.length() > 100) {
                            rowMessage += "密码的字数不能超过100；";
                        }
                    }else if(c == 2){
                        realname = cell.getStringCellValue();
                    }else if (c == 3){
                        organization = cell.getStringCellValue();
                    }else if(c == 4){
                        level = cell.getStringCellValue();
                        if ("学生".equals(level.trim())){
                            level = "0";
                        }
                        if ("老师".equals(level.trim())){
                            level = "1";
                        }
                        if ("普通用户".equals(level.trim())){
                            level = "2";
                        }
                    }else if (c == 5){
                        equipmentno = cell.getStringCellValue();
                    }else if(c == 6){
                        category = cell.getStringCellValue();
                        if ("vive".equals(category.trim())){
                            category = "1";
                        }
                        if ("一体机".equals(category.trim())){
                            category = "2";
                        }
                    }else if(c == 7){
                        mac = cell.getStringCellValue();
                    }else if(c == 8){
                        mobile = cell.getStringCellValue();
                        User user = new User();
                        user.setUsername(username);
                        user.setPassword(password);
                        tempUserKB.setPassword(passwordHelper.getPassword(user));
                        tempUserKB.setRealname(realname);
                        tempUserKB.setOrganization(organization);
                        tempUserKB.setLevel(level);
                        tempUserKB.setEquipmentno(equipmentno);
                        tempUserKB.setCategory(category);
                        tempUserKB.setMobile(mobile);
                        tempUserKB.setMac(mac);
                    }
                } else {
                    rowMessage += "第" + (c + 1) + "列数据有问题，请仔细检查；";
                }
            }
            tempUserKB.setEnable(1);
            //拼接每行的错误提示
            if (!StringUtils.isEmpty(rowMessage)) {
                errorMsg += br + "第" + (r + 1) + "行，" + rowMessage;
            } else {
                userBaseList.add(tempUserKB);
            }
        }

        //删除上传的临时文件
        if (tempFile.exists()) {
            tempFile.delete();
        }

        //全部验证通过才导入到数据库
        if (StringUtils.isEmpty(errorMsg)) {
            for (User userinfo : userBaseList) {
                this.save(userinfo);
            }
            errorMsg = "导入成功，共" + userBaseList.size() + "条数据！";
        }
        return errorMsg;
    }

    @Override
    public int addUserFile(UserFile userFile) {

        return userMapper.addUserFile(userFile);
    }

    @Override
    public int deleteUserFile(int userId, int[] fileId) {
        return userFileMapper.deleteUserFile(userId,fileId);
    }

    @Override
    public List<User> findAll(){
        Example example = new Example(User.class);
        return selectByExample(example);
    }
}
