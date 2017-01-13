package com.automatium.system.http.listener;

import com.automatium.system.CommandOutput;
import com.automatium.system.SystemCommand;
import com.automatium.system.http.response.JsonResponseFormatter;
import com.automatium.system.http.response.OutputResponseFormatter;
import com.automatium.system.http.response.XMLResponseFormatter;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.URLDecoder;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Gurusharan on 11-12-2016.
 */
public class HTTPExecutionListener implements HttpHandler {

    private HttpServer server;
    private boolean running = false;

    public HTTPExecutionListener(int port) {
        try {
            server = HttpServer.create(new InetSocketAddress(port), 0);
            server.createContext("/execute", this);
            server.setExecutor(null);
        } catch (IOException e) {
            System.err.println("HTTPExecutor: Unable to create HTTP listener. Exiting...");
            e.printStackTrace();
            System.exit(1);
        }
    }

    public HTTPExecutionListener() {
        this(0);
    }

    public boolean isRunning() {
        return running;
    }

    public void start() {
        server.start();

        String finalLocalURL = String.format("http://localhost:%d/execute?cmd=url_encoded_command&format=json_or_xml", server.getAddress().getPort());
        String finalSubnetURL = String.format("http://%s:%d/execute?cmd=url_encoded_command&format=json_or_xml", "ip_address_of_local_host", server.getAddress().getPort());
        try {
            finalSubnetURL = String.format("http://%s:%d/execute?cmd=url_encoded_command", InetAddress.getLocalHost().getHostAddress(), server.getAddress().getPort());
        } catch (UnknownHostException e) {
            System.err.println("HTTPExecutor: Unable to find localhost IP address (are you on a network?)");
            e.printStackTrace();
        }
        System.out.println("HTTPExecutor: Server started and is listening on port " + server.getAddress().getPort() + ". Details are as below:");
        System.out.println("\tUse '" + finalLocalURL + "' to send execution requests from this machine.");
        System.out.println("\tUse '" + finalSubnetURL + "' to send execution requests from another machine on the same network.");
        System.out.println("\tTo stop the server send cmd=stop");
        running = true;
    }

    public void stop() {
        server.stop(0);
        System.out.println("HTTPExecutor: Server has stopped.");
        running = false;
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        Map<String, String> parameters = queryToMap(httpExchange.getRequestURI().getRawQuery());
        String command = parameters.get("cmd");
        String format = parameters.get("format");
        boolean shouldStop = false;
        OutputResponseFormatter responseFormatter = null;

        if (format == null) {
            System.out.println("HTTPExecutor: Defaulting 'format' to 'json'");
            format = "json";
        }

        if (!(format.equalsIgnoreCase("json") || format.equalsIgnoreCase("xml"))) {
            System.out.println("HTTPExecutor: Unknown format - defaulting 'format' to 'json'");
            format = "json";
        }

        if (format.equalsIgnoreCase("json")) {
            responseFormatter = new JsonResponseFormatter();
            httpExchange.getResponseHeaders().set("content-type", "text/json");
        }

        if (format.equalsIgnoreCase("xml")) {
            responseFormatter = new XMLResponseFormatter();
            httpExchange.getResponseHeaders().set("content-type", "text/xml");
        }

        if (command == null) {
            System.out.println("HTTPExecutor: 'cmd' not found.");
            command = "echo 'No cmd found' 1>&2 ";
        }

        System.out.println("HTTPExecutor: Request from " + httpExchange.getRemoteAddress().getAddress());
        System.out.println("\tformat: " + format);
        System.out.println("\tcmd:\n#--Start--#");
        System.out.println(command);
        System.out.println("#--End--#");

        if (command.equalsIgnoreCase("stop")) {
            command = "echo Stop requested";
            shouldStop = true;
        }

        CommandOutput output = SystemCommand.execute(command);
        String responseString = responseFormatter.getResponseFromOutput(output);

        httpExchange.sendResponseHeaders(200, responseString.length());
        OutputStream stream = httpExchange.getResponseBody();
        stream.write(responseString.getBytes());
        stream.close();

        if (shouldStop) {
            stop();
        }
    }

    private Map<String, String> queryToMap(String rawQuery) throws UnsupportedEncodingException {
        Map<String, String> result = new HashMap<String, String>();
        for (String param : rawQuery.split("&")) {
            String[] pair = param.split("=");
            if (pair.length>1) {
                result.put(pair[0], URLDecoder.decode(pair[1], "UTF-8"));
            }else{
                result.put(pair[0], "");
            }
        }
        return result;
    }
}
