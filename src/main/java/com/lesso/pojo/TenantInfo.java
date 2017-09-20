package com.lesso.pojo;

public class TenantInfo {

    public int id;

    public String tenantAccount;

    public String tenantPassword;

    public String serverIp;

    public String servertPort;

    public String dbName;

    public String dbPassword;

    public String companyName;

    public int status;

    public int created;

    public int updated;

    public String phoneNo;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTenantAccount() {
        return tenantAccount;
    }

    public void setTenantAccount(String tenantAccount) {
        this.tenantAccount = tenantAccount;
    }

    public String getTenantPassword() {
        return tenantPassword;
    }

    public void setTenantPassword(String tenantPassword) {
        this.tenantPassword = tenantPassword;
    }

    public String getServerIp() {
        return serverIp;
    }

    public void setServerIp(String serverIp) {
        this.serverIp = serverIp;
    }

    public String getServertPort() {
        return servertPort;
    }

    public void setServertPort(String servertPort) {
        this.servertPort = servertPort;
    }

    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    public String getDbPassword() {
        return dbPassword;
    }

    public void setDbPassword(String dbPassword) {
        this.dbPassword = dbPassword;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getCreated() {
        return created;
    }

    public void setCreated(int created) {
        this.created = created;
    }

    public int getUpdated() {
        return updated;
    }

    public void setUpdated(int updated) {
        this.updated = updated;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }
}
