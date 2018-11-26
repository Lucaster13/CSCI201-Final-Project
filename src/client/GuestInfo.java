package client;

import java.util.ArrayList;

import application2.UserHomeController.DisplayPassword;

public class GuestInfo {
	private static ArrayList<DisplayPassword> passwordList = new ArrayList<DisplayPassword>();
	private static int count = 0;
	
	public static DisplayPassword addPassword(String appname, String username, String password) {
		DisplayPassword dp = new DisplayPassword(count++, appname, username, password);
		passwordList.add(dp);
		return dp;
	}
	
	public static void editPassword(int passID, String property, String value) {
		for(int i=0; i<passwordList.size(); i++) {
			if(passwordList.get(i).getPassID()==passID) {
				System.out.println("Edit");
				DisplayPassword dp = passwordList.get(i);
				if(property.equals("Application")) {
					dp.setAppname(value);
				} else if(property.equals("Username")) {
					dp.setUsername(value);
				} else if(property.equals("Password")) {
					dp.setPassword(value);
				}
				return;
			}
		}
		System.out.println("No Edit");
	}
	
	public static void reset() {
		passwordList = new ArrayList<DisplayPassword>();
		count=0;
	}
	
	public static void removePassword(int passID) {
		for(int i=0; i<passwordList.size(); i++) {
			if(passwordList.get(i).getPassID()==passID) {
				passwordList.remove(i);
				return;
			}
		}
	}
	
	public static ArrayList<DisplayPassword> getPasswords() {
		return passwordList;
	}
}
