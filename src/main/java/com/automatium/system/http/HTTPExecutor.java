package com.automatium.system.http;

import com.automatium.system.http.listener.HTTPExecutionListener;

/**
 * Created by Gurusharan on 27-12-2016.
 */
public class HTTPExecutor {

    public static void main(String[] args) throws InterruptedException {
        String portString = System.getProperty("port", "0");

        int port = Integer.parseInt(portString);

        HTTPExecutionListener listener = new HTTPExecutionListener(port);
        listener.start();

        while (listener.isRunning()) {
            Thread.sleep(1000L);
        }
    }
}
