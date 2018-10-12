setlocal
TITLE Week06 TcpServer

SET JAVA_EXE=C:\Users\scottl\tools\java\jdk1.7.0_40\bin\java.exe
SET CP=.;../lib/jdom-2.0.5.jar;../lib/mysql-connector-java-5.1.32-bin.jar;

%JAVA_EXE% -cp %CP% week06.io.TcpServer

endlocal