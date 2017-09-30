package com.lesso.pojo;

import java.util.Date;

/**
 * Created by czx on 2017/9/20.
 */
public class ServerInfo {
    private Integer id;
    private String serverIp;
    private Integer serverPort;
    private Integer tenantMaxcount;
    private Integer tenantCurcount;
    private Integer status;
    private String createman;
    private Date created;
    private Date updated;
    private String modifyman;
    private String managerUser;
    private String managerPassword;
    private Integer delflag;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getServerIp() {
        return serverIp;
    }

    public void setServerIp(String serverIp) {
        this.serverIp = serverIp;
    }


    public Integer getServerPort() {
        return serverPort;
    }

    public void setServerPort(Integer serverPort) {
        this.serverPort = serverPort;
    }

    public Integer getTenantMaxcount() {
        return tenantMaxcount;
    }

    public void setTenantMaxcount(Integer tenantMaxcount) {
        this.tenantMaxcount = tenantMaxcount;
    }

    public Integer getTenantCurcount() {
        return tenantCurcount;
    }

    public void setTenantCurcount(Integer tenantCurcount) {
        this.tenantCurcount = tenantCurcount;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getCreateman() {
        return createman;
    }

    public void setCreateman(String createman) {
        this.createman = createman;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }

    public String getModifyman() {
        return modifyman;
    }

    public void setModifyman(String modifyman) {
        this.modifyman = modifyman;
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

    public Integer getDelflag() {
        return delflag;
    }

    public void setDelflag(Integer delflag) {
        this.delflag = delflag;
    }
}
