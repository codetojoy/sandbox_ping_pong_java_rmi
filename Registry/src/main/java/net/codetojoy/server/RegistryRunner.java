
package net.codetojoy.server; 

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import net.codetojoy.common.util.Utils;

public class RegistryRunner {
    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("server_config.xml");

        context.getBean("myRegistry");
        System.out.println("\n\nTRACER: Registry ready !");

        while (true) {
            System.out.println("TRACER registry ...");
            try { Thread.sleep(2*1000); } catch (Exception ex) {}
        }
    }
}
