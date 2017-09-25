package com.lesso.mapper.user;

import com.lesso.pojo.User;

import java.util.List;
import java.util.Map;

public interface UserMapper {

    public User get(Integer id);

    public List<User> getList(User user);

    public int insertUser(User user);

    public int insertUserByTableIndex(User user);

    public User getUserInfo(User user);

    public int executeCreateQuery(Map<String, Object> map);


    public List<User> getUserByTableIndex(User user);




/*    public User find(Integer id);

    public User update(User user);

    public User delete(Integer id);*/

}
