package com.hpb.bc.entity.cache;

import com.hpb.bc.common.SpringBootContext;
import com.hpb.bc.entity.BaseEntity;
import com.hpb.bc.entity.ParamInfo;
import io.hpb.web3.protocol.admin.Admin;

public class CacheSession extends BaseEntity {
    private static final long serialVersionUID = 1851251217990041420L;
    private Admin admin;
    private String sessionId;
    private ParamInfo paramInfo;

    public ParamInfo getParamInfo() {
        return paramInfo;
    }

    public void setParamInfo(ParamInfo paramInfo) {
        this.paramInfo = paramInfo;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public Admin getAdmin() {
        if (this.admin == null) {
            return SpringBootContext.getBean("admin", Admin.class);
        }
        return admin;
    }

    public void setAdmin(Admin admin) {
        this.admin = admin;
    }
}