
package net.codetojoy.common.rmi;

import java.rmi.registry.*;

public class RegistryReader {
    private final static String DEFAULT_HOST = "127.0.0.1";
    private final static int DEFAULT_PORT = 2020;

    public String[] readRegistry() {
        return readRegistry(DEFAULT_HOST, DEFAULT_PORT);
    }

    public String[] readRegistry(String host, int port) {
        String[] results = null;

        try {
            Registry registry = LocateRegistry.getRegistry(host, port);
            
            if (registry != null) {
                results = registry.list();
            }
        } catch (Exception ex) {
            System.err.println("TRACER exception ! " + ex.getMessage());
        }

        return results;
    }
}
