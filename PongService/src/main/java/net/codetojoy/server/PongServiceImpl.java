
package net.codetojoy.server; 

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import net.codetojoy.common.*;
import net.codetojoy.common.rmi.*;

public class PongServiceImpl implements PongService, ApplicationContextAware {
    private ApplicationContext context = null;

    private Object safeGetBean(ApplicationContext context, String beanName, String logMsg) {
        Object result = null;

        try {
            result = context.getBean(beanName);
        } catch(Exception ex) {
            System.err.println("ERROR caught exception trying to get: " + beanName + " msg: " + logMsg);
        }
        
        return result;
    }

    @Override
    public void setApplicationContext(ApplicationContext context) throws BeansException {
        System.out.println("TRACER sAC hello");
        this.context = context;
    }

    // use delayed getter to break chicken-and-egg
    public PingService getPingService() { 
        PingService pingService = null;

        try {
            System.out.println("TRACER getPingService cp 0");
            pingService = (PingService) safeGetBean(context, "pingService", "PONG");
            System.out.println("TRACER getPingService cp 1");
        } catch (Exception ex) {
            System.err.println("TRACER caught exception: " + ex.getMessage());
        }

        return pingService; 
    }

    @Override
    public long healthCheck() {
        return System.currentTimeMillis();
    } 

    @Override
    public Ball pong(Ball ball) {
        Ball result = ball;

        if (! ball.isMaxedOut()) {
            String message = "PONG #: " + (ball.getNumHits() + 1);
            System.out.println("TRACER " + message);

            Ball newBall = ball.hit(message);
            PingService pingService = getPingService();

            if (pingService != null) {
                result = pingService.ping(newBall);
            } else {
                System.err.println("TRACER no ping service so sequence stops here");
            }
        } else {
            System.out.println("TRACER halting sequence. numHits: " + ball.getNumHits());
            System.out.println("TRACER ball: " + ball.toString());
        }
        
        return result;
    }

    public static void main(String[] args) {
        System.out.println("\n\nTRACER: PongService starting up...");

        ApplicationContext context = new ClassPathXmlApplicationContext("server_config.xml");

        context.getBean("pongServiceExporter");

        System.out.println("\n\nTRACER: PongService ready !");
    }
}
