package week06.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringReader;
import java.net.Socket;

import javax.xml.parsers.*;

import org.jdom2.Document;
import org.xml.sax.InputSource;

import week06.AtmException;
import week06.util.LoginRequest;
import week06.xml.XmlUtility;

/**
 * @author Laura Stever
 *
 */
public class AtmEndPointClient
{
	/**
	 * Entry point for the class. This example puts a lot of code in the main.
	 * Suggest refactoring if you use this as a starting point and NOT having it
	 * in main.
	 * 
	 * @param args
	 *            command line arguments
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException
	{
		new AtmEndPointClient().run();
	}
	
	public void run() throws IOException
	{
		initializeClient();
		startSession();
	}
          //my version of sendMessage- Need to complete             XXXXXXXXXXXXXXXXXXXXXXXXXXXXX
	public void sendMessageNew(BufferedReader in, PrintWriter out)
			throws IOException
	{
		//in has plain text of the LogInRequest object contents
		//use XmlUtility xmlToObject to create a LogInRequest object
		
		//send it WHERE??
		
		//get a LoginResponse object from WHERE??
		
		//use XmlUtility objectToXML to serialize the LoginResponse object 
		
		//send the serialized LoginResponse to out	
		
	}
	
	private static Document convertStringToXMLDocument(String xmlString)
    {
        //Parser that produces DOM object trees from XML content
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
         
        //API to obtain DOM Document instance
        DocumentBuilder builder = null;
        try
        {
            //Create DocumentBuilder with default configuration
            builder = factory.newDocumentBuilder();
             
            //Parse the content to Document object
            Document doc = (Document)builder.parse(new InputSource(new StringReader(xmlString)));
            return doc;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }
	
	
	public void sendMessage(BufferedReader in, PrintWriter out)
			throws IOException
	{		
		String msgToSend = "";
		String serverResponse = "";
		String msgResponse = "";
		while(true)
		{
			//no user prompt -- message should already be coming in 
			//System.out.print("Enter your message: ");
			
			msgToSend = m_stdIn.readLine(); //this is the xml of the LoginRequest
			
			if( msgToSend.equals(""))
			{
				break; // all done
			}
		
			
			try
			{
				LoginRequest req = (LoginRequest)XmlUtility.xmlToObject(convertStringToXMLDocument(msgToSend));
			}
			catch(AtmException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			//WHERE we need to somehow send the LoginRequest to OUT (atm /server)
			
			out.println("MSG " + msgToSend);

			//So that the loginResponse would be in
			msgResponse = in.readLine(); // response to our message
			if( msgResponse.contains("SERVER"))
			{
				// unexpected server response
				log("Unexpecteed Server response: " + serverResponse);
			}
			else
			{
				serverResponse = in.readLine(); // server status response
				log("Message response: " + msgResponse);
				log("Server response: " + serverResponse);
				if(!"SERVER: OK".equals(serverResponse))
				{
					log("Error processing message: " + serverResponse);				
				}
			}
		}
	}

	/**
	 * Read in a nickname from stdin and attempt to authenticate with the server
	 * by sending a NICK command to @out. If the response from @in is not equal
	 * to "OK" go back and read a nickname again
	 * 
	 * @param in
	 *            BufferedReader reference
	 * @param out
	 *            The output stream.
	 */
	private String getNick(BufferedReader in, PrintWriter out)
			throws IOException
	{
		log("getNick");
		boolean run = true;

		System.out.print("Enter your nick: ");
		String msg = m_stdIn.readLine();

		if(msg.equals(""))
		{
			run = false;
		}

		out.println("NICK " + msg);
		String serverResponse = in.readLine();
		if("SERVER: OK".equals(serverResponse))
		{
			return msg;
		}

		System.out.println(serverResponse);
		if(run)
		{
			return getNick(in, out);
		}
		else
		{
			sendMessage(in, out);
		}
		
		return "";
	}

	private void initializeClient()
	{
		try
		{
			log("initializeClient");
			m_server = new Socket(m_host, m_port);

			m_stdIn = new BufferedReader(new InputStreamReader(System.in));

			/* obtain an output stream to the server... */
			m_writer = new PrintWriter(m_server.getOutputStream(), true);

			/* ... and an input stream */
			m_reader = new BufferedReader(new InputStreamReader(
					m_server.getInputStream()));

			//m_nick = getNick(m_reader, m_writer);
		}
		catch(IOException ex)
		{
			System.err.println(ex);
			System.exit(1);
		}
	}

	private void startSession() throws IOException
	{
		log("enter startSession");
		//m_nick = 
		//this doesn't go anymore -- getNick(m_reader, m_writer);

		sendMessage(m_reader, m_writer);
	
		log("exit startSession");
	}
	
	private void log(String msg)
	{
		System.out.println(msg);
	}
	

	// Private data
	private static int m_port = 8001; /* port to connect to */
	private static String m_host = "localhost"; /* host to connect to */

	private static BufferedReader m_stdIn;

	//private static String m_nick;
	private static Socket m_server = null;

	private static BufferedReader m_reader;
	private static PrintWriter m_writer;
}

