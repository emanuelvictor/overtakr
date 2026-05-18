package com.emanuelvictor.accessmanager.domain.model;

import lombok.Getter;
// TODO chamar de authorization
@Getter
public class Authorization {

    private String id;
    private String principalName;
    private String sid;
    private String accessTokenValue;

    private Authorization(String id, String principalName, String sid, String accessTokenValue) {
        this.id = id;
        this.principalName = principalName;
        this.sid = sid;
        this.accessTokenValue = accessTokenValue;
    }

    private static Authorization create(String id, String principalName, String sid, String accessTokenValue) {
        return new Authorization(id, principalName, sid, accessTokenValue);
    }

    public static Authorization rehydrate(String id, String principalName, String sid, String accessTokenValue) {
        return new Authorization(id, principalName, sid, accessTokenValue);
    }
}
