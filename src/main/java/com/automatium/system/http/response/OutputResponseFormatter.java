package com.automatium.system.http.response;

import com.automatium.system.CommandOutput;

/**
 * Created by Gurusharan on 11-12-2016.
 *
 * An interface for converting {@link CommandOutput} obect
 * into a {@link String} and vice versa.
 *
 * @see JsonResponseFormatter
 * @see XMLResponseFormatter
 */
public interface OutputResponseFormatter {
    String getResponseFromOutput(CommandOutput output);
    CommandOutput getOutputFromResponse(String response);
}
