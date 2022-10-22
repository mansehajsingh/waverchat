package com.waverchat.api.v1.authentication;

import com.waverchat.api.v1.ProgramArgs;
import com.waverchat.api.v1.util.AuthenticatedEndpoint;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class AuthenticationConfig {

    private Document document;

    public AuthenticationConfig() {
        File authConfXML = new File(ProgramArgs.getAuthenticationConfigPath());
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder docBuilder = dbf.newDocumentBuilder();
            Document doc = docBuilder.parse(authConfXML);
            doc.getDocumentElement().normalize();
            this.document = doc;
        } catch (Exception e) {}
    }

    public List<AuthenticatedEndpoint> getAuthenticatedEndpointsFromConfig() {
        NodeList v1Nodes = this.document.getElementsByTagName("v1").item(0).getChildNodes();
        NodeList endpointNodes = null;

        for (int i = 0; i < v1Nodes.getLength(); i++) {
            if (v1Nodes.item(i).getNodeName().equals("authenticatedEndpoints")) {
                endpointNodes = v1Nodes.item(i).getChildNodes();
                break;
            }
        }

        List<AuthenticatedEndpoint> authenticatedEndpoints = new ArrayList<>();

        for (int i = 0; i < endpointNodes.getLength(); i++) {
            if (!endpointNodes.item(i).getNodeName().equals("endpoint")) continue;

            NodeList currEpData = endpointNodes.item(i).getChildNodes();
            String method = null;
            String uri = null;

            for (int j = 0; j < currEpData.getLength(); j++) {
                if (currEpData.item(j).getNodeName().equals("method")) method = currEpData.item(j).getTextContent();
                if (currEpData.item(j).getNodeName().equals("uri")) uri = currEpData.item(j).getTextContent();
                if (uri != null && method != null) break;
            }

            authenticatedEndpoints.add(new AuthenticatedEndpoint(method, uri));
        }

        return authenticatedEndpoints;
    }

}
