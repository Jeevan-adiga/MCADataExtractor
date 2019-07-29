package auto.framework.xml;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.MimeHeaders;
import javax.xml.soap.SOAPConnection;
import javax.xml.soap.SOAPConnectionFactory;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import auto.framework.ReportLog;

public class SOAP {

	//	private SOAPMessage storeRequest;
	private static Document xmlStream;

	public Document getXmlStream() {
		return xmlStream;
	}

	public RequestXML request() throws Exception{
		return new RequestXML();
	}

	public SOAP(final String source) throws Exception{
		initializeTemplate(source);
	}

	private void initializeTemplate(final String source) throws Exception{
		File fpath;
		DocumentBuilderFactory factory = null;
		DocumentBuilder builder = null;
		FileInputStream  fis = null;
		fpath = new File(source);
		fis = new FileInputStream(fpath);
		factory = DocumentBuilderFactory.newInstance();
		builder = factory.newDocumentBuilder();
		xmlStream  = builder.parse(fis);
	}

	public Node selectNode(final String expression) throws Exception{
		final XPath xPath =  XPathFactory.newInstance().newXPath();
		final Node node = (Node) xPath.compile("//*[name()='"+expression+"']").evaluate(xmlStream, XPathConstants.NODE);
		return node;
	}

	public NodeList selectNodeList(final String expression) throws Exception{
		return xmlStream.getElementsByTagName(expression);
	}

	public class RequestXML{

		private final SOAPMessage msg;

		public RequestXML() throws Exception{
			msg = toSOAPMessage();
		}

		@SuppressWarnings("unused")
		public SOAPMessage submitRequest(final String strEndpoint) throws Exception, SOAPException {
			final SOAPConnectionFactory soapConnectionFactory = SOAPConnectionFactory.newInstance();
			final SOAPConnection soapConnection = soapConnectionFactory.createConnection();
			ReportLog.addInfo("Request : "+getSOAPMessageAsString(msg));
			return soapConnection.call(msg, strEndpoint);
		}

		private SOAPMessage toSOAPMessage() throws Exception {
			final MessageFactory mfactory = MessageFactory.newInstance();
			return mfactory.createMessage(new MimeHeaders(), toInputStream());
		}

		public SOAPMessage getRequestMessage() throws Exception {
			return msg;
		}

		private InputStream toInputStream() throws TransformerConfigurationException, TransformerException, TransformerFactoryConfigurationError {
			final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			final Source xmlSource = new DOMSource(xmlStream);
			final Result outputTarget = new StreamResult(outputStream);
			TransformerFactory.newInstance().newTransformer().transform(xmlSource, outputTarget);
			return new ByteArrayInputStream(outputStream.toByteArray());
		}

	}

	public String getSOAPMessageAsString(final SOAPMessage soapMessage) {
		try {

			final TransformerFactory tff = TransformerFactory.newInstance();
			final Transformer tf = tff.newTransformer();

			// Set formatting

			tf.setOutputProperty(OutputKeys.INDENT, "yes");
			tf.setOutputProperty("{http://xml.apache.org/xslt}indent-amount",
					"2");

			final Source sc = soapMessage.getSOAPPart().getContent();

			final ByteArrayOutputStream streamOut = new ByteArrayOutputStream();
			final StreamResult result = new StreamResult(streamOut);
			tf.transform(sc, result);

			final String strMessage = streamOut.toString();
			return strMessage;
		} catch (final Exception e) {
			System.out.println("Exception in getSOAPMessageAsString "
					+ e.getMessage());
			return null;
		}

	}

	/**
	 * @summary Takes an xpath and return the value found
	 * @param xpath String: xpath to evaluate
	 * @throws XPathExpressionException Could not match xPath during evaluation
	 * @throws RuntimeException Could not match xPath to a node, element or attribute
	 */
	public String getValueByXpath(final Document doc, final String xpath) {

		final XPathFactory xPathFactory = XPathFactory.newInstance();
		final XPath xPath = xPathFactory.newXPath();
		XPathExpression expr;
		NodeList nList = null;

		//Evaluate the xpath
		try {
			expr = xPath.compile(xpath);
			nList = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
		} catch (final XPathExpressionException xpe) {
			throw new RuntimeException("Xpath evaluation failed with xpath [ " + xpath + " ] ", xpe.getCause());
		}

		//Ensure an element was found, if not then throw error and fail
		if (nList.item(0) == null) {
			throw new RuntimeException("No xpath was found with the path [ " + xpath + " ] ");
		}

		//If no errors, then return the value found
		return nList.item(0).getTextContent();
	}

	/**
	 * convert soapmessage to Document object
	 * @param soapXML
	 * @return
	 */
	public Document makeXMLDocument(final SOAPMessage soapXML) {

		Document doc = null;

		final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		try {
			soapXML.writeTo(outputStream);
			final DocumentBuilderFactory factory = DocumentBuilderFactory
					.newInstance();
			factory.setNamespaceAware(false);
			factory.setIgnoringElementContentWhitespace(true);
			final DocumentBuilder builder = factory.newDocumentBuilder();
			doc = builder.parse(new ByteArrayInputStream(outputStream.toByteArray()));
		} catch (SOAPException | IOException e) {
			e.printStackTrace();
		} catch (final ParserConfigurationException e) {
			e.printStackTrace();
		} catch (final SAXException e) {
			e.printStackTrace();
		}

		return doc;
	}
}
