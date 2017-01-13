package com.automatium.system.http.response;

import com.automatium.system.CommandOutput;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Gurusharan on 11-12-2016.
 */
public class JsonResponseFormatter implements OutputResponseFormatter {

    private static final String OUTPUT = "output";
    private static final String ERROR = "error";
    private static final String EXITCODE = "exitcode";

    public String getResponseFromOutput(CommandOutput output) {
        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put(OUTPUT, output.getOut());
        responseMap.put(ERROR, output.getErr());
        responseMap.put(EXITCODE, output.getExitCode());

        return new JSONObject(responseMap).toJSONString();
    }

    public CommandOutput getOutputFromResponse(String response) {
        try {
            JSONObject responseMap = (JSONObject) (new JSONParser().parse(response));
            return new CommandOutput(responseMap.get(OUTPUT).toString(), responseMap.get(ERROR).toString(), new Integer(responseMap.get(EXITCODE).toString()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
}
