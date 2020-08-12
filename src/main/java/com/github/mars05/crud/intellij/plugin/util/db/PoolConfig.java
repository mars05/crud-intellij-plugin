package com.github.mars05.crud.intellij.plugin.util.db;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

/**
 * PoolConfig
 *
 * @author xiaoyu
 */
class PoolConfig extends GenericObjectPoolConfig<PooledConnection> {
    public PoolConfig() {
        setTestWhileIdle(true);
        setMinEvictableIdleTimeMillis(60000);
        setTimeBetweenEvictionRunsMillis(30000);
        setNumTestsPerEvictionRun(-1);
    }
}
