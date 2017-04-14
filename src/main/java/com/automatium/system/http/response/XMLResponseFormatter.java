package com.automatium.system.http.response;

import com.automatium.system.CommandOutput;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringWriter;

/**
 * Created by Gurusharan on 11-12-2016.
 *
 * An implementation of {@link OutputResponseFormatter}
 * for converting a {@link CommandOutput} object into an
 * XML {@link String} and vice versa.
 *
 * @see OutputResponseFormatter
 */
public class XMLResponseFormatter implements OutputResponseFormatter {
    private static final String ROOTTAG = "commandoutput";
    private static final String OUTPUT = "output";
    private static final String ERROR = "error";
    private static final String EXITCODE = "exitcode";

    /**
     * Convert a {@link CommandOutput} object into an XML
     * {@link String}. The XML root is 'commandoutput' and
     * it contains the tags: 'output', containing the
     * output stream of the command, 'error', containing
     * the error stream of the command, and 'exitcode',
     * containing the exit code of the command.
     *
     * Thus the returned format is:
     * <commandoutput>
     *     <output>Output stream in the CommandOutput</output>
     *     <error>Error stream in the CommandOutput</error>
     *     <exitcode>Exit code in the CommandOutput</exitcode>
     * </commandoutput>
     *
     * @param output - a {@link CommandOutput} object
     * @return an XML {@link String}
     */
    @Override
    public String getResponseFromOutput(CommandOutput output) {
        try {
            Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
            Element rootElement = document.createElement(ROOTTAG);

            Element outputElement = document.createElement(OUTPUT);
            outputElement.appendChild(document.createTextNode(output.getOut()));

            Element errorElement = document.createElement(ERROR);
            errorElement.appendChild(document.createTextNode(output.getErr()));

            Element exitCodeElement = document.createElement(EXITCODE);
            exitCodeElement.appendChild(document.createTextNode(Integer.toString(output.getExitCode())));

            rootElement.appendChild(outputElement);
            rootElement.appendChild(errorElement);
            rootElement.appendChild(exitCodeElement);
            document.appendChild(rootElement);

            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer = tf.newTransformer();
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            StringWriter writer = new StringWriter();
            transformer.transform(new DOMSource(document), new StreamResult(writer));

            return writer.getBuffer().toString();

        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Convert an XML {@link String} into a {@link CommandOutput}
     * object. The XML string must contain the root element
     * 'commandoutput' with the attributes: 'output', 'error'
     * and 'exitcode' (which should be an integer). These
     * correspond to the output stream, error stream and the
     * exit code of the {@link CommandOutput} object respectively.
     *
     * @param response an XML {@link String}
     * @return a {@link CommandOutput} object or null if XML string is not valid
     */
    @Override
    public CommandOutput getOutputFromResponse(String response) {
        try {
            Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new ByteArrayInputStream(response.getBytes("UTF-8")));
            String output = document.getElementsByTagName(OUTPUT).item(0).getTextContent();
            String error = document.getElementsByTagName(ERROR).item(0).getTextContent();
            int exitcode = new Integer(document.getElementsByTagName(EXITCODE).item(0).getTextContent());
            return new CommandOutput(output, error, exitcode);
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }

        return null;
    }
}
