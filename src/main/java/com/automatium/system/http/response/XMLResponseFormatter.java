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
 */
public class XMLResponseFormatter implements OutputResponseFormatter {
    private static final String ROOTTAG = "commandoutput";
    private static final String OUTPUT = "output";
    private static final String ERROR = "error";
    private static final String EXITCODE = "exitcode";

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
