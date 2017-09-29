package com.lesso.mapper.manager;

import com.lesso.pojo.Msg;
import com.lesso.pojo.TenantInfo;

/**
 * Created by czx on 2017/9/28.
 */
public interface MsgMapper {
    public int insertMsg(Msg Mmg);

    public int getMsg(int id);

}
