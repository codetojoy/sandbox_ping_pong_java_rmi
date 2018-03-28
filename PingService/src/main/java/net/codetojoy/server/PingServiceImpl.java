
package net.codetojoy.server; 

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import net.codetojoy.common.*;
import net.codetojoy.common.rmi.*;

public class PingServiceImpl implements PingService, ApplicationContextAware {
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
    public PongService getPongService() { 
        PongService pongService = null;

        try {
            System.out.println("TRACER getPongService cp 0");
            pongService = (PongService) safeGetBean(context, "pongService", "PING");
            System.out.println("TRACER getPongService cp 1");
        } catch (Exception ex) {
            System.err.println("TRACER caught exception: " + ex.getMessage());
        }

        return pongService; 
    }


    @Override
    public long healthCheck() {
        return System.currentTimeMillis();
    } 

    @Override
    public Ball ping(Ball ball) {
        Ball result = ball;

        if (! ball.isMaxedOut()) {
            String message = "PING #: " + (ball.getNumHits() + 1);
            System.out.println("TRACER " + message);

            Ball newBall = ball.hit(message);
            PongService pongService = getPongService();

            if (pongService != null) {
                result = pongService.pong(newBall);
            } else {
                System.err.println("TRACER no pong service, so sequence will end here");
            }
        } else {
            System.out.println("TRACER halting sequence. numHits: " + ball.getNumHits());
            System.out.println("TRACER ball: " + ball.toString());
        }
        
        return result;
    }

    public static void main(String[] args) {
        System.out.println("\n\nTRACER: PingService starting up...");

        ApplicationContext context = new ClassPathXmlApplicationContext("server_config.xml");

        context.getBean("pingServiceExporter");

        System.out.println("\n\nTRACER: PingService ready !");
    }
}
