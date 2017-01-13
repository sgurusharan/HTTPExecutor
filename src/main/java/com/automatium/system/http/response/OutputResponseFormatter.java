package com.automatium.system.http.response;

import com.automatium.system.CommandOutput;

/**
 * Created by Gurusharan on 11-12-2016.
 */
public interface OutputResponseFormatter {
    String getResponseFromOutput(CommandOutput output);
    CommandOutput getOutputFromResponse(String response);
}
