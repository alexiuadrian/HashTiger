package com.adialexiu.hashtiger;

public class Credential {
    private String Id;
    private String serviceName;
    private String userName;
    private String password;
    private String credential_owner;

    public Credential(String id, String serviceName, String userName, String password, String credential_owner) {
        Id = id;
        this.serviceName = serviceName;
        this.userName = userName;
        this.password = password;
        this.credential_owner = credential_owner;
    }

    public void setId(String id) {
        Id = id;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setCredential_owner(String credential_owner) {
        this.credential_owner = credential_owner;
    }

    public String getId() {
        return Id;
    }

    public String getServiceName() {
        return serviceName;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public String getCredential_owner() {
        return credential_owner;
    }


}
