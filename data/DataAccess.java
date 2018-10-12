package week06.data;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Calendar;
import java.util.ArrayList;
import java.util.List;

import week04.app.Account;
import week04.app.User;

import java.sql.SQLException;

/**
 * Provides the interface to the data store For this implementation that is a
 * MySql database
 * 
 * @author Scott LaChance
 */
public class DataAccess
{
	/**
	 * Default Constructor
	 */
	private DataAccess()
	{
		this("root", "root");
	}

	/**
	 * Default Constructor
	 * 
	 * @param user
	 *            User name to connect with
	 * @param password
	 *            password to connect with
	 */
	private DataAccess(String user, String password)
	{
		m_userName = user;
		m_password = password;
	}

	/**
	 * Static factory method that returns a default instance of DataAccess
	 * 
	 * @return Default instance of DataAcccess
	 */
	public static synchronized DataAccess getInstance()
	{
		if(m_dataAccess == null)
		{
			m_dataAccess = new DataAccess();
		}

		return m_dataAccess;
	}

	/**
	 * Static factory method that returns a default instance of DataAccess
	 * 
	 * @return Default instance of DataAcccess
	 */
	public static synchronized DataAccess getInstance(String user,
			String password)
	{
		if(m_dataAccess == null)
		{
			// new instance
			m_dataAccess = new DataAccess(user, password);
		}
		else if(!(m_dataAccess.m_userName.equals(user)
				&& m_dataAccess.m_password.equals(password)))
		{
			// new connection instance - different user/password
			m_dataAccess = new DataAccess(user, password);
		}

		return m_dataAccess;
	}

	/**
	 * Saves an account Checks whether the account is new (as indicated by an id
	 * of -1) or existing (indicated by ID not equal to -1)
	 * 
	 * @param account
	 *            The account to save
	 * @throws AtmDataException
	 *             if there is a save error
	 */
	public void saveAccount(Account account) throws AtmDataException
	{
		Calendar now = Calendar.getInstance(); // Gets the current date and
		// time.
		Date updateDate = new java.sql.Date(now.getTime().getTime());

		try
		{
			if(account.getAccountId() == -1)
			{
				// new account - insert
				m_insertAccountStatement.setLong(1, account.getUserId());
				m_insertAccountStatement.setString(2, account.getName());
				m_insertAccountStatement.setDouble(3, account.getBalance());
				m_insertAccountStatement.setDate(4, updateDate);
				m_insertAccountStatement.executeUpdate();
				ResultSet keys = m_insertAccountStatement.getGeneratedKeys();
				if(keys.next())
				{
					long newId = keys.getLong(1);
					account.setAccountId(newId);
				}
				else
				{
					throw new AtmDataException(
							"Unable to retrieve new account id");
				}
			}
			else
			{
				// existing account - update
				m_updateAccountStatement.setLong(1, account.getUserId());
				m_updateAccountStatement.setString(2, account.getName());
				m_updateAccountStatement.setDouble(3, account.getBalance());
				m_updateAccountStatement.setDate(4, updateDate);
				m_updateAccountStatement.setLong(5, account.getUserId());
				m_updateAccountStatement.executeUpdate();
			}
		}
		catch(SQLException ex)
		{
			throw new AtmDataException(ex);
		}
	}

	/**
	 * Get the requested Account
	 * 
	 * @param accountId
	 *            Account ID to retrieve
	 * @return An Account reference
	 * @throws AtmDataException
	 *             on error
	 */
	public Account getAccount(int accountId) throws AtmDataException
	{
		Account account = null;
		try
		{
			m_selectAccountsByIdStatement.setLong(1, accountId);
			if(m_selectAccountsByIdStatement.execute())
			{
				ResultSet results = m_selectAccountsByIdStatement
						.getResultSet();
				while(results.next())
				{
					// get the data from the query
					long id = results.getLong("id");
					long userId = results.getLong("user_id");
					String accountName = results.getString("name");
					double balance = results.getDouble("balance");

					// retrieve the user
					User curUser = getUserById(userId);
					account = new Account(id, curUser, accountName, balance);
				}
			}
			else
			{

				throw new AtmDataException("Failed to get all acccounts");
			}
		}
		catch(SQLException ex)
		{
			throw new AtmDataException(ex);
		}

		return account;
	}

	/**
	 * Gets all the accounts in a list
	 * 
	 * @return List of Accounts
	 * @throws AtmDataException
	 *             on error
	 */
	public List<Account> getAllAccounts() throws AtmDataException
	{
		List<Account> accounts = new ArrayList<Account>();
		try
		{
			if(m_selectAllAccountsStatement.execute())
			{
				ResultSet results = m_selectAllAccountsStatement.getResultSet();
				while(results.next())
				{
					// get the data from the query
					long accountId = results.getLong("id");
					long userId = results.getLong("user_id");
					String accountName = results.getString("name");
					double balance = results.getDouble("balance");
					
					// retrieve the user
					User curUser = getUserById(userId);
					Account newAccount = new Account(accountId, curUser, accountName, balance);
					accounts.add(newAccount);
				}				
			}
			else
			{
				
				throw new AtmDataException("Failed to get all acccounts");
			}
		}
		catch(SQLException ex)
		{
			throw new AtmDataException(ex);
		}
		
		return accounts;
	}

	/**
	 * Saves a user to the database
	 * 
	 * @param user
	 *            User reference
	 * @throws AtmDataException
	 *             on error
	 */
	public void saveUser(User user) throws AtmDataException
	{
		Calendar now = Calendar.getInstance(); // Gets the current date and
												// time.
		Date updateDate = new java.sql.Date(now.getTime().getTime());

		try
		{
			if(user.getUserId() == -1)
			{
				// new user - insert, uses auto-increment
				m_insertUserStatement.setInt(1, user.getPin());
				m_insertUserStatement.setString(2, user.getFirstName());
				m_insertUserStatement.setString(3, user.getLastName());
				m_insertUserStatement.setDate(4, updateDate);
				m_insertUserStatement.executeUpdate();

				// get the newly created key and update the user object
				ResultSet keys = m_insertUserStatement.getGeneratedKeys();
				if(keys.next())
				{
					long newId = keys.getLong(1);
					user.setUserId(newId);
				}
				else
				{
					throw new AtmDataException(
							"Unable to retrieve new account id");
				}
			}
			else
			{
				// existing user - update
				m_UpdateUserStatement.setInt(1, user.getPin());
				m_UpdateUserStatement.setString(2, user.getFirstName());
				m_UpdateUserStatement.setString(3, user.getLastName());
				m_UpdateUserStatement.setDate(4, updateDate);
				m_UpdateUserStatement.setLong(5, user.getUserId()); // where
																	// clause
				m_UpdateUserStatement.executeUpdate();
			}
		}
		catch(SQLException ex)
		{
			throw new AtmDataException(ex);
		}
	}

	/**
	 * Returns the list of all users
	 * 
	 * @return List of User references
	 * @throws AtmDataException
	 *             on data error
	 */
	public List<User> getUsers() throws AtmDataException
	{
		List<User> userList = new ArrayList<User>();
		ResultSet resultSet = null;

		try
		{
			resultSet = m_selectUserStatement.executeQuery();

			while(resultSet.next())
			{
				long userId = resultSet.getLong("id");
				String first = resultSet.getString("first_name");
				String last = resultSet.getString("last_name");

				userList.add(new User(userId, first, last));
			}
		}
		catch(SQLException ex)
		{
			// log error
			throw new AtmDataException(ex);
		}

		return userList;
	}

	/**
	 * Retrieve the user by the user id
	 * 
	 * @param userId
	 *            Database id of the suer
	 * @return User object or null if not found
	 * @throws AtmDataException
	 *             on error.
	 */
	public User getUserById(long userId) throws AtmDataException
	{
		User user = null;
		try
		{
			m_selectUserByIdStatement.setLong(1, userId);
			if(m_selectUserByIdStatement.execute())
			{
				ResultSet results = m_selectUserByIdStatement.getResultSet();
				if(results.next())
				{
					long id = results.getLong("id");
					int pin = results.getInt("pin");
					String firstName = results.getString("first_name");
					String lastName = results.getString("last_name");
					user = new User(id, pin, firstName, lastName);
				}
			}
			else
			{
				throw new AtmDataException(
						"failed to find user with id: " + userId);
			}
		}
		catch(SQLException ex)
		{
			// log exception
			System.out.println(ex.getMessage());
			throw new AtmDataException(ex);
		}
		catch(Exception ex)
		{
			// log exception
			System.out.println(ex.getMessage());
			throw new AtmDataException(ex);
		}

		return user;
	}

	/**
	 * Delete the selected user
	 * 
	 * @param userId
	 *            User ID to delete
	 * @throws AtmDataException
	 *             on error
	 */
	public void deleteUserById(long userId) throws AtmDataException
	{
		try
		{
			m_deleteUserStatement.setLong(1, userId);
			m_deleteUserStatement.execute();
		}
		catch(SQLException ex)
		{
			// log error
			throw new AtmDataException(ex);
		}
	}

	/**
	 * Connects to the database using the provided credentials passed into the
	 * constructor.
	 * 
	 * @throws AtmDataException
	 *             on error
	 */
	public void connect() throws AtmDataException
	{
		try
		{
			// this will load the MySQL driver, each DB has its own driver
			Class.forName("com.mysql.cj.jdbc.Driver");

			// build connection string
			String conn = String.format(CONNECTION_STRING, m_userName,
					m_password);

			// setup the connection with the DB.
			m_connect = DriverManager.getConnection(conn);

			//
			// pre-compile prepared statements
			//
			// Note the RETURN_GENERATED_KEYS. when using auto-increment to
			// insert
			// you need to get the newly inserted row key (ID) to update the
			// user instance with
			// otherwise it will stay -1 and you'll always treat it like new.
			// MySQL lets you retrieve the new key, but you have to tell the
			// PrepareStatement you need it.
			m_insertUserStatement = m_connect.prepareStatement(INSERT_USER_SQL,
					PreparedStatement.RETURN_GENERATED_KEYS);
			m_selectUserStatement = m_connect.prepareStatement(SELECT_USER_SQL);
			m_selectUserByIdStatement = m_connect
					.prepareStatement(SELECT_USER_BY_ID_SQL);
			m_UpdateUserStatement = m_connect.prepareStatement(UPDATE_USER_SQL);
			m_deleteUserStatement = m_connect.prepareStatement(DELETE_USER_SQL);
			
			m_selectAllAccountsStatement = m_connect.prepareStatement(SELECT_ALL_ACCOUNTS_SQL);
			m_selectAccountsByIdStatement = m_connect.prepareStatement(SELECT_ACCOUNTS_BY_ID_SQL);
			m_insertAccountStatement = m_connect.prepareStatement(INSERT_ACCOUNT_SQL, PreparedStatement.RETURN_GENERATED_KEYS);
			m_updateAccountStatement = m_connect.prepareStatement(UPDATE_ACCOUNT_SQL);

			// TODO Initialize the Account prepared statements
		}
		catch(SQLException ex)
		{
			// log exception
			System.out.println(ex.getMessage());
			throw new AtmDataException(ex);
		}
		catch(Exception ex)
		{
			// log exception
			System.out.println(ex.getMessage());
			throw new AtmDataException(ex);
		}
	}

	/**
	 * Retrieves the connection object
	 * 
	 * @return Connection object reference
	 */
	public Connection getConnection()
	{
		return m_connect;
	}

	private static DataAccess m_dataAccess = new DataAccess();

	private Connection m_connect = null;
	private PreparedStatement m_insertUserStatement;
	private PreparedStatement m_selectUserStatement;
	private PreparedStatement m_selectUserByIdStatement;
	private PreparedStatement m_UpdateUserStatement;
	private PreparedStatement m_selectAccountsByIdStatement;
	private PreparedStatement m_deleteUserStatement;
	private PreparedStatement m_selectAllAccountsStatement;
	private PreparedStatement m_insertAccountStatement;
	private PreparedStatement m_updateAccountStatement;
	private String m_userName;
	private String m_password;

	// private String INSERT_USER_SQL = "insert into atm.user values (?, ?, ?,
	// ?)";
	// private String SELECT_USER_SQL = "SELECT id, first_name, last_name from
	// atm.user";

	private static String CONNECTION_STRING = "jdbc:mysql://localhost/atm?user=%s&password=%s&useSSL=false";

	private static String INSERT_USER_SQL = "INSERT INTO  atm.user (pin,first_name,last_name,last_update)  values (?, ?, ?, ?)";
	private static String SELECT_USER_SQL = "SELECT id, pin, first_name, last_name from atm.user";
	private static String SELECT_USER_BY_ID_SQL = "SELECT id, pin, first_name, last_name from atm.user WHERE id=?";
	private static String UPDATE_USER_SQL = "UPDATE atm.user SET pin=?, first_name=?, last_name=?,last_update=? WHERE  id=?";
	private static String DELETE_USER_SQL = "DELETE FROM atm.user WHERE id=?";

	private static String SELECT_ALL_ACCOUNTS_SQL = "SELECT id, user_id, name, balance FROM atm.account";
	private static String SELECT_ACCOUNTS_BY_ID_SQL = "SELECT id, user_id, name, balance FROM atm.account WHERE id=?";
	private static String INSERT_ACCOUNT_SQL = "INSERT INTO  atm.account (user_id,name,balance,last_update) values (?, ?, ?, ?)";
	private static String UPDATE_ACCOUNT_SQL = "UPDATE atm.account SET user_id=?, name=?, balance=?,last_update=? WHERE  id=?";
}
