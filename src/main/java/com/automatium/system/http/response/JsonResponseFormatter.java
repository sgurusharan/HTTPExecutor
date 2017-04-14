package com.automatium.system.http.response;

import com.automatium.system.CommandOutput;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Gurusharan on 11-12-2016.
 *
 * An implementation of {@link OutputResponseFormatter}
 * for converting a {@link CommandOutput} object into a
 * JSON {@link String} and vice versa.
 *
 * @see OutputResponseFormatter
 */
public class JsonResponseFormatter implements OutputResponseFormatter {

    private static final String OUTPUT = "output";
    private static final String ERROR = "error";
    private static final String EXITCODE = "exitcode";

    /**
     * Convert a {@link CommandOutput} object into a JSON
     * {@link String}. The JSON keys are 'output', containing
     * the output stream of the command, 'error', containing
     * the error stream of the command, and 'exitcode',
     * containing the exit code of the command.
     *
     * Thus the returned format is:
     * {
     *     "output":"Output stream in the CommandObject",
     *     "error":"Error stream in the CommandObject",
     *     "exitcode":Exit code in the CommandObject
     * }
     *
     * @param output - a {@link CommandOutput} object
     * @return a JSON {@link String}
     */
    @Override
    public String getResponseFromOutput(CommandOutput output) {
        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put(OUTPUT, output.getOut());
        responseMap.put(ERROR, output.getErr());
        responseMap.put(EXITCODE, output.getExitCode());

        return new JSONObject(responseMap).toJSONString();
    }

    /**
     * Convert a JSON {@link String} into a {@link CommandOutput}
     * object. The JSON string must contain the keys 'output',
     * 'error' and 'exitcode' (which should be an integer) at its
     * root. These correspond to the output stream, error stream and
     * the exit code of the {@link CommandOutput} object.
     *
     * @param response a JSON {@link String}
     * @return a {@link CommandOutput} object or null if JSON string is not valid
     */
    @Override
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
