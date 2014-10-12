package com.smash.revolance.camcast;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;

import java.util.Map;

/**
 * Created by ebour on 11/10/14.
 */
public class Main
{
    public static void main(String[] args)
    {



        // Create the hazelcast client instance
        final ClientConfig clientConfig = new ClientConfig();
        clientConfig.addAddress("127.0.0.1:5701");

        HazelcastInstance client = HazelcastClient.newHazelcastClient(clientConfig);

        IMap map = client.getMap("customers");
        System.out.println("Map Size:" + map.size());
    }

}
