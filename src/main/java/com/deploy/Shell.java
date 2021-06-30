package com.deploy;

/**
 * 服务器基本信息
 *
 * @author seven
 * @date 2021/1/15
 */
public class Shell {

    public static final int CONNECT_TIMEOUT = 30 * 1000;

    private String host = "47.100.125.75";
    private int port = 22;
    private String username = "root";
    private String password = "Root.com";
    private String identity = "~/.ssh/id_rsa";
    private String passphrase = "";

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }

    public String getPassphrase() {
        return passphrase;
    }

    public void setPassphrase(String passphrase) {
        this.passphrase = passphrase;
    }
}
