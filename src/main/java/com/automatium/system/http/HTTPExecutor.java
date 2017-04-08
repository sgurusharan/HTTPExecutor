package com.automatium.system.http;

import com.automatium.system.http.listener.HTTPExecutionListener;

/**
 * Created by Gurusharan on 27-12-2016.
 */
public class HTTPExecutor {

    public static void main(String[] args) throws InterruptedException {

        if (args.length > 0 && args[0].equalsIgnoreCase("help")) {
            printHelpAndExit();
        }

        String portString = System.getProperty("port", "0");

        int port = Integer.parseInt(portString);

        HTTPExecutionListener listener = new HTTPExecutionListener(port);
        listener.start();

        while (listener.isRunning()) {
            Thread.sleep(1000L);
        }
    }

    private static void printHelpAndExit() {
        System.out.println("Start HTTP executor server in a random available port:" +
                "\n\tjava -jar HTTPExecutor.jar");
        System.out.println("Start HTTP executor server in a given port:" +
                "\n\tjava -jar -Dport=5555 HTTPExecutor.jar");
        System.exit(0);
    }
}
