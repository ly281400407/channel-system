package com.lesso.mapper.manager;

import com.lesso.pojo.TenantInfo;

public interface TenantInfoMapper {

    public TenantInfo getTenantByName(String tenantAccount);

    public TenantInfo getTenantByPhone(String phone);

    public TenantInfo getTenantByIdOrName(Integer tenantId,String tenantAccount);

    public int updateTenantStatus(TenantInfo tenantInfo);


}
