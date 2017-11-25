package com.study.service;

import com.github.pagehelper.PageInfo;
import com.study.model.User;
import com.study.model.UserFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * Created by yangqj on 2017/4/21.
 */
public interface UserService extends IService<User>{
    PageInfo<User> selectByPage(User user, int start, int length);

    User selectByUsername(String username);

    User selectByUserId(Integer userId);

    void delUser(Integer userid);

    void updateEquipmentNoByUsername(User user);

    public String batchImport(String fileName, MultipartFile mfile);

    public int addUserFile(UserFile userFile);

    public int deleteUserFile(int userId,int[] fileId);

    List<User> findAll();

}
