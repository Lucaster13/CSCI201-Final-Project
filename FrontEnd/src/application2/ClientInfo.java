package application2;

public class ClientInfo 
{
	private static String username;
	private static boolean guest;
	private static boolean verifyBackToLogin;
	private static String email;
	
	ClientInfo(String username, boolean guest)
	{
		ClientInfo.username = username;
		ClientInfo.guest = guest;
		ClientInfo.verifyBackToLogin = true;
	}
	
	ClientInfo(boolean guest)
	{
		ClientInfo.guest = guest;
		ClientInfo.verifyBackToLogin = true;
	}
	
	public static String getUsername()
	{
		return username;
	}
	
	public static boolean getGuestStatus()
	{
		return guest;
	}
	
	public static boolean getVerify()
	{
		return verifyBackToLogin;
	}
	
	public static void verifyInManage()
	{
		verifyBackToLogin = false;
	}
}
