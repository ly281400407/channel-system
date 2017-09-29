package com.lesso.pojo;

/**
 * Created by czx on 2017/9/20.
 */
public class ServerInfo {
    private int id;
    private String serverIp;
    private int servertPort;
    private int tenant_maxcount;
    private int tenant_curcount;
    private int status;
    private int created;
    private String modifyman;
    private String managerUser;
    private String managerPassword;
    private int updated;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getServerIp() {
        return serverIp;
    }

    public void setServerIp(String serverIp) {
        this.serverIp = serverIp;
    }

    public int getServertPort() {
        return servertPort;
    }

    public void setServertPort(int servertPort) {
        this.servertPort = servertPort;
    }

    public int getTenant_maxcount() {
        return tenant_maxcount;
    }

    public void setTenant_maxcount(int tenant_maxcount) {
        this.tenant_maxcount = tenant_maxcount;
    }

    public int getTenant_curcount() {
        return tenant_curcount;
    }

    public void setTenant_curcount(int tenant_curcount) {
        this.tenant_curcount = tenant_curcount;
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

    public String getModifyman() {
        return modifyman;
    }

    public void setModifyman(String modifyman) {
        this.modifyman = modifyman;
    }

    public int getUpdated() {
        return updated;
    }

    public void setUpdated(int updated) {
        this.updated = updated;
    }

    public String getManagerUser() {
        return managerUser;
    }

    public void setManagerUser(String managerUser) {
        this.managerUser = managerUser;
    }

    public String getManagerPassword() {
        return managerPassword;
    }

    public void setManagerPassword(String managerPassword) {
        this.managerPassword = managerPassword;
    }
}
