package com.emanuelvictor.accessmanager.domain.model;

import lombok.Getter;

@Getter
public class Session {

    private String id;
    private String principalName;
    private String sid;
    private String accessTokenValue;

    private Session(String id, String principalName, String sid, String accessTokenValue) {
        this.id = id;
        this.principalName = principalName;
        this.sid = sid;
        this.accessTokenValue = accessTokenValue;
    }

    private static Session create(String id, String principalName, String sid, String accessTokenValue) {
        return new Session(id, principalName, sid, accessTokenValue);
    }

    public static Session rehydrate(String id, String principalName, String sid, String accessTokenValue) {
        return new Session(id, principalName, sid, accessTokenValue);
    }
}
