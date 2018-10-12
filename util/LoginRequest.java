package week06.util;

import week06.core.AtmObject;

/**
 * Encapsulates the login request information
 * @author scottl
 *
 */
public class LoginRequest extends AtmObject
{
	/**
	 * Parameterized Constructor
	 * 
	 * @param pin User provided pin
	 * @param accountId - account id (checking/savings etc)
	 */
	public LoginRequest(int pin, long accountId)
	{
		m_pin = pin;
		m_accountId = accountId;
	}

	/**
	 * Gets the pin
	 * @return the pin
	 */
	public int getPin()
	{
		return m_pin;
	}
	
	/**
	 * Sets the user pin
	 * 
	 * @param pin the pin to set
	 */
	public void setPin(int pin)
	{
		this.m_pin = pin;
	}

	/**
	 * Get the account ID
	 * @return the account ID
	 */
	public long getAccountId()
	{
		return m_accountId;
	}

	/**
	 * Set the account ID
	 * 
	 * @param id the account id to set
	 */
	public void setAccountId(long id)
	{
		this.m_accountId = id;
	}

	/**
	 * Overrides the Object.equals to compare two LoginRequest references.
	 * The are equal if the attributes of the User instance are equal.
	 * 
	 * @param obj The object on the right-hand side to evaluate
	 * @return true if match, otherwise false
	 */
	@Override
	public boolean equals(Object obj)
	{
		boolean result = false;
		if( obj instanceof LoginRequest)
		{
			LoginRequest rhs = (LoginRequest)obj;
			if( this.getPin() == rhs.getPin() &&
				this.getAccountId() == rhs.getAccountId())
			{
				result = true;
			}
		}
		
		return result;
	}
	
	private int m_pin;
	private long m_accountId; 
}
