package com.shadowingszy.notebook.mapper;

import com.shadowingszy.notebook.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Repository
@Mapper
public interface UserMapper {
    /**
     * 根据用户名和密码查询某个用户
     *
     * @param username 用户名
     * @param password 密码
     * @param userType 账号类型
     * @return 用户
     */
    ArrayList<User> queryUserByUserInformation(String username, String password, String userType);

    /**
     * 根据用户ID查询某个用户
     *
     * @param userId 用户ID
     * @return 用户
     */
    ArrayList<User> queryUserByUserId(int userId);

    /**
     * 根据用户名查询用户数量，注册时用
     *
     * @param username 用户名
     * @return 数量数量
     */
    int queryUserCountByUsername(String username);

    /**
     * 向数据库中插入一个用户
     *
     * @param user 用户
     * @return 插入是否成功
     */
    int insertUser(User user);

    /**
     * 修改某个用户的用户名或密码
     *
     * @param userId   用户ID
     * @param username 用户名
     * @param password 密码
     * @return 修改是否成功
     */
    int updateUser(int userId, String username, String password);
}
