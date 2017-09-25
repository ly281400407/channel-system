package com.lesso.mapper.manager;

import com.lesso.pojo.Finance;

import java.util.List;
import java.util.Map;

public interface FinanceMapper {

    public int executeCreateQuery(Map<String, Object> map);
    public int insertFinanceByTableIndex(Finance finance);
    public List<Finance> getFinanceByTableIndex(Finance finance);




/*    public User find(Integer id);

    public User update(User user);

    public User delete(Integer id);*/

}
