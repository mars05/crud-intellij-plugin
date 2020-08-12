package com.github.mars05.crud.intellij.plugin.util.db;

import java.util.Properties;

/**
 * @author yu.xiao
 * @date 2020-08-12 11:30
 */
public class ConnectionConfig {

    private String host;
    private Integer port;
    private String username;
    private String password;
    private Properties props;

    public ConnectionConfig(String host, Integer port, String username, String password) {
        this.host = host;
        this.port = port;
        this.username = username;
        this.password = password;

        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
        props = new Properties();
        props.put("user", this.username);
        props.put("password", this.password);
        props.setProperty("remarks", "true");
        props.put("useInformationSchema", "true");
    }

    public String getHost() {
        return host;
    }

    public Integer getPort() {
        return port;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public Properties getProps() {
        return props;
    }
}
