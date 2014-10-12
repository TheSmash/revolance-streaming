package com.smash.revolance.camcast;

import com.hazelcast.config.Config;
import com.hazelcast.core.Cluster;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;

import java.util.UUID;

/**
 * Created by ebour on 11/10/14.
 */
public class StreamingClient
{
    private HazelcastInstance cluster;

    public StreamingClient()
    {
        // Create the hazelcast server instance
        final Config cfg = new Config();
        cfg.setInstanceName(UUID.randomUUID().toString());

        cluster = Hazelcast.newHazelcastInstance(cfg);
    }

    public Cluster getCluster()
    {
        return cluster.getCluster();
    }

}
