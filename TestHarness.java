package week06;

import test.AbstractTestHarness;

/**
 * File: TestHarness.java
 */
class TestHarness extends AbstractTestHarness
{
	public static void main(String[] args)
	{
		new TestHarness().runTests();
	}

    
    /**
	 * Implements the base class abstract method
	 */
	protected void runTests()
	{
		try
		{			
			boolean appTest = executeTest(week06.app.AppTest.class);
			boolean testData = executeTest(week06.data.DataAccessTest.class);
			boolean testLogging = executeTest(TestLogging.class);
			boolean testLogin = executeTest(TestLogIn.class);
			boolean testJavadoc = executeTest(JUnitJavadocValidation.class);
			boolean result = appTest && testData && testLogging && testLogin && testJavadoc;

			trace(result ? "Tests Passed" : "Tests Failed");
		}
		catch(Exception ex)
		{
			trace(ex.getMessage());
		}
	}
}