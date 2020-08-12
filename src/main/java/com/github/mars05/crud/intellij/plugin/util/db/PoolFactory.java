package com.github.mars05.crud.intellij.plugin.util.db;

import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.DefaultPooledObject;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


/**
 * RabbitMq生产者的连接池工厂
 *
 * @author xiaoyu
 * @see PooledConnection
 * @see PooledObjectFactory
 * @see PoolConfig
 * @since 1.0.0
 */
class PoolFactory implements PooledObjectFactory<PooledConnection> {

    private ConnectionConfig config;

    public PoolFactory(ConnectionConfig config) {
        this.config = config;
    }

    private Connection getConnection() {
        try {
            return DriverManager.getConnection("jdbc:mysql://" + config.getHost() + ":" + config.getPort() + "/?useUnicode=true&characterEncoding=UTF-8&zeroDateTimeBehavior=convertToNull&useSSL=false", config.getProps());
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    private Connection getDbConnection(String database) {
        try {
            return DriverManager.getConnection("jdbc:mysql://" + config.getHost() + ":" + config.getPort() + "/" + database + "?useUnicode=true&characterEncoding=UTF-8&zeroDateTimeBehavior=convertToNull&useSSL=false", config.getProps());
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    private void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
            }
        }
    }

    @Override
    public PooledObject<PooledConnection> makeObject() throws Exception {
        Connection connection = null;
        // connectionFactory.newConnection();
        PooledConnection pooledConnection = new PooledConnection(connection);
        return new DefaultPooledObject<>(pooledConnection);
    }

    @Override
    public void destroyObject(PooledObject<PooledConnection> p) {
        try {
            PooledConnection connection = p.getObject();
            connection.disconnect();
        } catch (Exception ignored) {
        }
    }

    @Override
    public boolean validateObject(PooledObject<PooledConnection> p) {
        try {
            return p.getObject().isValid(5);
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public void activateObject(PooledObject<PooledConnection> p) throws Exception {

    }

    @Override
    public void passivateObject(PooledObject<PooledConnection> p) throws Exception {

    }
}
