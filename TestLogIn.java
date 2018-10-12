package week06;

import static org.junit.Assert.*;

import org.junit.Test;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

import org.jdom2.Document;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.XMLOutputter;

import test.AbstractJUnitBase;

import week06.core.AtmObject;
import week06.util.LoginRequest;
import week06.util.LoginResponse;
import week06.xml.XmlUtility;

public class TestLogIn extends AbstractJUnitBase
{
	/**
	 * Default constructor
	 */
	public TestLogIn()
	{		
	}

	@Test
	public void testGenerateObjectFromXmlLoginResponse()
	{
		try
		{
			LoginResponse refRequest = new LoginResponse(true, 100100L);
			
			String testResponse = "<login-response version=\"1.0\" logged-in=\"true\" session-id=\"100100\" />";
			java.io.StringReader reader = new StringReader(testResponse);

			org.jdom2.input.SAXBuilder builder = new SAXBuilder();
			Document dom = builder.build(reader);
			
			AtmObject request = XmlUtility.xmlToObject(dom);
			
			if( !refRequest.equals(request))
			{
				String msg = "LoginRequest objects don't match";
				trace(msg);
				fail(msg);
			}
		}
		catch(IOException ex)
		{
			String msg = " Failed: testGenerateObjectFromXmlLoginResponse " + ex.getMessage();
			trace(msg);
			fail(msg);
		}
		catch(JDOMException ex)
		{
			String msg = " Failed: testGenerateObjectFromXmlLoginResponse " + ex.getMessage();
			trace(msg);
			fail(msg);
		}
		catch(AtmException ex)
		{
			String msg = " Failed: testGenerateObjectFromXmlLoginResponse " + ex.getMessage();
			trace(msg);
			fail(msg);
		}
	}

	@Test
	public void testGenerateObjectFromXmlLoginRequest()
	{
		try
		{
			LoginRequest refRequest = new LoginRequest(1234, 100100L);
			
			String testRequest = "<login-request version=\"1.0\" pin=\"1234\" account-id=\"100100\" />";
			java.io.StringReader reader = new StringReader(testRequest);

			org.jdom2.input.SAXBuilder builder = new SAXBuilder();
			Document dom = builder.build(reader);
			
			AtmObject request = XmlUtility.xmlToObject(dom);
			
			if( !refRequest.equals(request))
			{
				String msg = "LoginRequest objects don't match";
				trace(msg);
				fail(msg);
			}
		}
		catch(IOException ex)
		{
			String msg = "Failed: testGenerateObjectFromXmlLoginRequest " + ex.getMessage();
			trace(msg);
			fail(msg);
		}
		catch(JDOMException ex)
		{
			String msg = "Failed: testGenerateObjectFromXmlLoginRequest " + ex.getMessage();
			trace(msg);
			fail(msg);
		}
		catch(AtmException ex)
		{
			String msg = "Failed: testGenerateObjectFromXmlLoginRequest " + ex.getMessage();
			trace(msg);
			fail(msg);
		}

	}

	@SuppressWarnings("unused")
	@Test
	public void tesGenerateXmlFromLoginRequest()
	{
		trace("  tesGenerateXmlFromLoginRequest");
		boolean result = true;

		try
		{
			AtmObject logRequest = new LoginRequest(1234, 100100L);

			if(logRequest == null)
			{
				String msg = "Failed to create LoginRequest ";
				trace(msg);
				fail(msg);
			}

			Document dom = XmlUtility.objectToXml(logRequest);

			String xml = dumpDocument(dom);
			if(xml.equals(""))
			{
				String msg = "Failed to generate XML output ";
				trace(msg);
				fail(msg);
			}

			trace("Generated XML \n" + xml);
		}
		catch(Exception ex)
		{
			String msg = "Failed to generate XML output " + ex.getMessage();
			trace(msg);
			fail(msg);
		}
	}

	private String dumpDocument(Document dom)
	{
		XMLOutputter outputter = new XMLOutputter();
		java.io.StringWriter writer = new StringWriter();
		try
		{
			outputter.output(dom, writer);
		}
		catch(IOException ex)
		{
			String msg = "Failed to dump XML output " + ex.getMessage();
			trace(msg);
			fail(msg);
			ex.printStackTrace();
		}

		return writer.toString();
	}

	private boolean testLogRequestCreation()
	{
		trace("  testLogRequestCreation");
		boolean result = true;

		try
		{
			@SuppressWarnings("unused")
			AtmObject logRequest = new LoginRequest(1234, 100100L);
		}
		catch(Exception ex)
		{
			String msg = "Failed to create " + ex.getMessage();
			trace(msg);
			fail(msg);
		}

		return result;
	}

}
