package com.github.mars05.crud.intellij.plugin.util.db;

import org.apache.commons.pool2.impl.GenericObjectPool;

import java.sql.Connection;

/**
 * @author xiaoyu
 */
public class ConnectionFactory {
    private GenericObjectPool<PooledConnection> connectionsPool;

    public ConnectionFactory() {
        connectionsPool = new GenericObjectPool<>(new PoolFactory(null), new PoolConfig());
    }

    public Connection newConnection() {
        try {
            PooledConnection connection = connectionsPool.borrowObject();
            connection.setConnectionsPool(connectionsPool);
            return connection;
        } catch (Exception e) {
            throw new IllegalStateException("Could not get a connection from the pool", e);
        }
    }
}
