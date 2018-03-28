
package net.codetojoy.monitor; 

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import net.codetojoy.common.*;

import java.util.*;
import java.util.concurrent.*;

import java.rmi.registry.*;

class Task implements Runnable {
    private ApplicationContext context;
 
    private static final String PING_SERVICE = "pingService";
    private static final String PONG_SERVICE = "pongService";

    private final Set ALL_ENTRIES = new HashSet<>();

    Task(ApplicationContext context) {
        this.context = context; 

        ALL_ENTRIES.add(PING_SERVICE);
        ALL_ENTRIES.add(PONG_SERVICE);
    }

    private void healthCheck(String serviceName) {
        try {
            String result = "N/A";

            if (serviceName.equals(PING_SERVICE)) {
                PingService pingService = (PingService) context.getBean(serviceName);
                long healthCheckResult = pingService.healthCheck();
                result = new Date(healthCheckResult).toString();
            } else if (serviceName.equals(PONG_SERVICE)) {
                PongService pongService = (PongService) context.getBean(serviceName);
                long healthCheckResult = pongService.healthCheck();
                result = new Date(healthCheckResult).toString();
            } 

            System.out.println("TRACER from " + serviceName + " : " + result);
        } catch(Exception ex) {
            System.err.println("TRACER caught ex: " + ex.getMessage());
        }
    }

    @Override
    public void run() {
        try {
            System.out.println("--------------------------");
            System.out.println("\n\nTRACER " + new Date() + " checking...");
            Registry registry = LocateRegistry.getRegistry("127.0.0.1", 2020);
            String[] entries = registry.list();

            if (entries != null) {
                Set<String> refSet = new HashSet(ALL_ENTRIES); 
                Set<String> entrySet = new HashSet<String>(Arrays.asList(entries));
                refSet.removeAll(entrySet);

                if (! refSet.isEmpty()) {
                    for (String entry : refSet) {
                        System.out.println("\nTRACER found entry: " + entry); 
                        healthCheck(entry);
                    }
                }
            } else {
                System.out.println("TRACER no entries found");
            } 
        } catch (Exception ex) {
            System.err.println("TRACER caught exception: " + ex.getMessage());
        }
    }
}

public class Monitor {
    private ApplicationContext context;
    private ScheduledExecutorService scheduledPool = Executors.newScheduledThreadPool(1);

    Monitor(ApplicationContext context) {
        this.context = context;
    }

    public void start() {
        final int delayInSeconds = 4;
        scheduledPool.scheduleAtFixedRate(new Task(context), delayInSeconds, delayInSeconds, TimeUnit.SECONDS);
    }
    
    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("server_config.xml");
        Monitor monitor = new Monitor(context);
        monitor.start();

        while (true) {
            try {  Thread.sleep(30*1000); } catch (Exception ex) {}
        }
    }
}
