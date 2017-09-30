package com.lesso.mapper.manager;

import com.lesso.pojo.TenantInfo;
import org.apache.ibatis.annotations.Param;

public interface TenantInfoMapper {

    public TenantInfo getTenantByName(String tenantAccount);

    public TenantInfo getTenantByPhone(String phone);

    public TenantInfo getTenantByIdOrName(@Param("tenantId")Integer tenantId,@Param("tenantAccount") String tenantAccount);

    public int updateTenantStatus(TenantInfo tenantInfo);


}
