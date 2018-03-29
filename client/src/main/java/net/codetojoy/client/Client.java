
package net.codetojoy.client;

import org.springframework.context.*;
import org.springframework.context.support.*;

import net.codetojoy.common.*;

import net.codetojoy.common.rmi.RegistryReader;

public class Client {
    private static final String PING = "i";
    private static final String PONG = "o";
    private static final String REGISTRY = "r";

    private static final String PING_SERVICE = "pingService";
    private static final String PONG_SERVICE = "pongService";

    private ApplicationContext context = null;

    public Client(ApplicationContext context) {
        this.context = context;
    }

    public PingService getPingService() {
        return (PingService) context.getBean(PING_SERVICE);
    }

    public PongService getPongService() {
        return (PongService) context.getBean(PONG_SERVICE);
    }

    private void processCommand() {
        Prompt prompt = new Prompt();
        String input = prompt.getInput("\n\ncmd: [I=ping, O=pong, R=registry, Q=quit] ?", PING, PONG, REGISTRY);

        if (input.equalsIgnoreCase(PING)) {
            String name = prompt.getInput("enter a name: "); 
            Ball ball = new Ball(name);
            ball = getPingService().ping(ball);
            System.out.println("\nOK! result: \n\n" + ball.toString());
        } else if (input.equalsIgnoreCase(PONG)) {
            String name = prompt.getInput("enter a name: "); 
            Ball ball = new Ball(name);
            ball = getPongService().pong(ball);
            System.out.println("result: \n" + ball.toString());
        } else if (input.equalsIgnoreCase(REGISTRY)) {
            String[] results = new RegistryReader().readRegistry();
            System.out.println("TRACER registry results: ");
            if (results != null) {
                for (String result : results) {
                    System.out.println("TRACER reg: " + result);
                }
            }
        } 
    }

    public void inputLoop() {
        while (true) {
            try {
                processCommand();
            } catch(Exception ex) {
                System.err.println("\nTRACER command failed! check if the service is running \n");
            }
        }
    }

    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("client_config.xml"); 
        Client client = new Client(context);
        client.inputLoop();
    }
}
