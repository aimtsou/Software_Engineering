package gov.nist.sip.proxy.strategy.io;

import gov.nist.sip.proxy.Billing;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class BillingWritingStrategy implements IOStrategy {
	String caller;
	File xmlFile;
	double charge;
	
	public BillingWritingStrategy(String caller, File xmlFile, double charge) {
		this.caller = caller;
		this.xmlFile = xmlFile;
		this.charge = charge;
	}

	@Override
	public void performIOOperation() {

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

		try {
			// Read and parse the xml file
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document document = builder.parse(xmlFile);

			// Get all the user nodes
			NodeList nl = document.getElementsByTagName("user");
			int i = 0;
			int ind = -1;
			String txt = null;
			if (nl != null && nl.getLength() > 0) {

				while (i < nl.getLength()) {

					Element el = (Element) nl.item(i);
					txt = el.getFirstChild().getNodeValue();

					// Whne you find the Caller, save his index
					if (txt.trim().equalsIgnoreCase(caller)) {
						ind = i;
						i++;
					} else
						i++;
				}

				// If the Caller has already a balance
				if ((ind >= 0) && (ind < nl.getLength())) {
					// Read the bill node
					Element ch1 = (Element) nl.item(ind);
					NodeList dm = ch1.getElementsByTagName("bill");

					if (dm != null && dm.getLength() > 0) {

						txt = dm.item(0).getFirstChild().getNodeValue();
						double aDouble = Double.parseDouble(txt);
						// Add the new charge to his existing balance
						aDouble += charge;
						String aString = Double.toString(aDouble);
						Node ch2 = dm.item(0).getFirstChild();
						ch2.setNodeValue(aString);

						// Save the xml file
						TransformerFactory transformerFactory = TransformerFactory
								.newInstance();
						try {
							Transformer transformer = transformerFactory
									.newTransformer();
							transformer.setOutputProperty(OutputKeys.INDENT,
									"yes");
							DOMSource source = new DOMSource(document);
							StreamResult result = new StreamResult(xmlFile);
							transformer.transform(source, result);

						} catch (TransformerException e) {
							e.printStackTrace();
						}

					}
					// If this is his first call
					else {
						// Create a bill node
						Element em = document.createElement("bill");
						// Set his balance to the new charge
						em.appendChild(document.createTextNode(Double
								.toString(charge)));
						ch1.appendChild(em);

						// Save the xml file
						TransformerFactory transformerFactory = TransformerFactory
								.newInstance();
						try {
							Transformer transformer = transformerFactory
									.newTransformer();
							transformer.setOutputProperty(OutputKeys.INDENT,
									"yes");
							DOMSource source = new DOMSource(document);
							StreamResult result = new StreamResult(xmlFile);
							transformer.transform(source, result);
						} catch (TransformerException e) {
							e.printStackTrace();
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
