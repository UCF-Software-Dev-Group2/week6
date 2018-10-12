package week06.util;

import week06.core.AtmObject;

/**
 * Encapsulates the login request information
 * @author scottl
 *
 */
public class LoginResponse extends AtmObject
{
	/**
	 * Constructor
	 * @param loggedIn true if logged in successfully, false otherwise
	 * @param sessionId - SessionId token)
	 */
	public LoginResponse(boolean loggedIn, long sessionId)
	{
		m_loggedIn = loggedIn;
		m_sessionId = sessionId;
	}

	/**
	 * Retrieve the pin
	 * @return the pin
	 */
	public Boolean getLoggedIn()
	{
		return m_loggedIn;
	}
	
	/**
	 * Get the session id
	 * @return the session ID
	 */
	public long getSessionId()
	{
		return m_sessionId;
	}

	/**
	 * Overrides the Object.equals to compare two LoginResponse references.
	 * The are equal if the attributes of the User instance are equal.
	 * 
	 * @param obj The object on the right-hand side to evaluate
	 * @return true if match, otherwise false
	 */
	@Override
	public boolean equals(Object obj)
	{
		boolean result = false;
		if( obj instanceof LoginResponse)
		{
			LoginResponse rhs = (LoginResponse)obj;
			
			if( this.getLoggedIn() == rhs.getLoggedIn() &&
				this.getSessionId() == rhs.getSessionId())
			{
				result = true;
			}
		}
		
		return result;
	}

	private Boolean m_loggedIn;
	private long m_sessionId; 
}
