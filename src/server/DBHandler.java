package server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import data.Password;
import data.SecurityQuestion;

public class DBHandler {
	private static Connection conn = null;
	private static String dbUsername = "root";
	private static String dbPassword= "root";
	
	public static boolean createConnection() {
		boolean created=false;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/password_protector?user="+dbUsername+"&password="+dbPassword+"&useSSL=false");
			created = true;
		} catch(ClassNotFoundException | SQLException e) {
			System.out.println(e.getMessage());
			created=false;
		}
		return created;
	}
	
	public static void closeConnection() {
		try {
			if(conn != null) {
				conn.close();
				conn=null;
			}
		} catch(SQLException e) {
			System.out.println(e.getMessage());
		}
	}
	
	/*
		Query the database to verify the the given username and hash password match exist.
		
		Returns the user's info if credentials match or null if verification fails.
	*/
	public static DBUserInfo verifyUser(String name, String hash_pass) {
		DBUserInfo info = null;
		if(conn==null) createConnection();
		if(conn != null) {
			PreparedStatement ps=null;
			ResultSet rs=null;
			try {
				ps=conn.prepareStatement("SELECT userID, username, last_update, email FROM user WHERE username=? AND master_pass=?");
				ps.setString(1, name);
				ps.setString(2, hash_pass);
				rs=ps.executeQuery();
				if(rs.next()) { //Check that the user exists
					info=new DBUserInfo(rs.getInt("userID"), rs.getString("username"), rs.getString("last_update"), rs.getString("email"));
				}
			} catch(SQLException e) {
				System.out.println(e.getMessage());
			} finally {
				try {
					if(ps != null) ps.close();
				} catch(SQLException e) {
					System.out.println(e.getMessage());
				}
			}
		}
		return info;
	}

	/*
		Creates a new user in the database with the user info passed as parameters
		
		Returns the userID of the created user, -1 if username already in use or 0 if action failed.
	*/
	public static int createUser(String username, String hash_pass, String email) {
		int userID=0;
		if(conn==null) createConnection();
		if(conn != null) {
			PreparedStatement ps=null;
			ResultSet rs=null;
			try {
				ps=conn.prepareStatement("SELECT COUNT(*) FROM user WHERE username=?");
				ps.setString(1, username);
				rs=ps.executeQuery();
				if(rs.next()) { //Check if the user exists, if so then we can't create another user with the same username
					userID = -1;
				} else {
					if(rs != null) rs.close();
					if(ps != null) ps.close();
					ps=conn.prepareStatement("INSERT INTO user (username, master_pass, last_update, email) VALUES (?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
					ps.setString(1, username);
					ps.setString(2, hash_pass);
					Calendar cal = new GregorianCalendar();
					SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
					dateFormat.setCalendar(cal); 
					String sqlNow = dateFormat.format(cal.getTime());
					ps.setString(3, sqlNow);
					ps.setString(4, email);
					ps.executeUpdate();
					rs = ps.getGeneratedKeys();
					if(rs.next()) { //If successfully inserted then return the userID
						userID=rs.getInt(1);
					}
				}
			} catch(SQLException e) {
				System.out.println(e.getMessage());
			} finally {
				try {
					if(rs != null) rs.close();
					if(ps != null) ps.close();
				} catch(SQLException e) {
					System.out.println(e.getMessage());
				}
			}
		}
		return userID;
	}
	
	/*
		Returns a list of the stored passwords for the user with the given ID
	*/
	public static ArrayList<Password> getPasswords(int userID) {
		ArrayList<Password> results = new ArrayList<Password>();
		if(conn==null) createConnection();
		if(conn != null) {
			PreparedStatement ps=null;
			ResultSet rs=null;
			try {
				ps=conn.prepareStatement("SELECT passwordID, username, app_name, encrypted_pass, last_update, suggested_reset FROM password WHERE userID=?");
				ps.setInt(1, userID);
				rs=ps.executeQuery();
				while(rs.next()) {
					results.add(new Password(rs.getInt("passwordID"), rs.getString("username"), rs.getString("app_name"), rs.getString("encrypted_pass"), rs.getString("last_update"), rs.getString("suggested_reset")));
				}
			} catch(SQLException e) {
				System.out.println(e.getMessage());
			} finally {
				try {
					if(rs != null) rs.close();
					if(ps != null) ps.close();
				} catch(SQLException e) {
					System.out.println(e.getMessage());
				}
			}
		}
		return results;
	}
	
	/*
		Returns a list of the security questions for the password with the given ID
	*/
	public static ArrayList<SecurityQuestion> getSecurityQuestions(int passwordID) {
		ArrayList<SecurityQuestion> results = new ArrayList<SecurityQuestion>();
		if(conn==null) createConnection();
		if(conn != null) {
			PreparedStatement ps=null;
			ResultSet rs=null;
			try {
				ps=conn.prepareStatement("SELECT question, answer FROM security_question WHERE passwordID=?");
				ps.setInt(1, passwordID);
				rs=ps.executeQuery();
				while(rs.next()) {
					results.add(new SecurityQuestion(rs.getString("question"),rs.getString("answer")));
				}
			} catch(SQLException e) {
				System.out.println(e.getMessage());
			} finally {
				try {
					if(rs != null) rs.close();
					if(ps != null) ps.close();
				} catch(SQLException e) {
					System.out.println(e.getMessage());
				}
			}
		}
		return results;
	}
	
	/* 
	 	Add a password for the user with the given userID and the given password info
	 	
		Returns: true if successful, false otherwise
	 */
	public static boolean addPassword(int userID, String username, String app_name, String encrypted_pass, String last_update, String suggested_reset) {
		boolean success = false;
		if(conn==null) createConnection();
		if(conn != null) {
			PreparedStatement ps=null;
			ResultSet rs=null;
			try {
				ps=conn.prepareStatement("INSERT INTO password (userID, username, app_name, encrypted_pass, last_update, suggested_reset) VALUES (?,?,?,?,?,?)");
				ps.setInt(1, userID);
				ps.setString(2, username);
				ps.setString(3, app_name);
				ps.setString(4, encrypted_pass);
				ps.setString(5, last_update);
				ps.setString(6, suggested_reset);
				ps.executeUpdate();
				success=true;
			} catch(SQLException e) {
				System.out.println(e.getMessage());
			} finally {
				try {
					if(rs != null) rs.close();
					if(ps != null) ps.close();
				} catch(SQLException e) {
					System.out.println(e.getMessage());
				}
			}
		}
		return success;
	}
	
	/*
		Remove a password and the security questions for the given passwordID if the given userID is the creator
		
		Returns: true if successful, false otherwise
	 */
	public static boolean removePassword(int userID, int passwordID) {
		boolean success = false;
		if(conn==null) createConnection();
		if(conn != null) {
			PreparedStatement ps=null;
			ResultSet rs=null;
			try {
				ps=conn.prepareStatement("SELECT COUNT(*) FROM password WHERE userID=? AND passwordID=?");
				ps.setInt(1, userID);
				ps.setInt(2, passwordID);
				rs=ps.executeQuery();
				if(rs.next()) { //Password belongs to the given user, delete security questions tied to the password
					ps.close();
					ps=conn.prepareStatement("DELETE FROM security_question WHERE passwordID=?");
					ps.setInt(1, passwordID);
					ps.executeUpdate();
					ps.close();
					ps=conn.prepareStatement("DELETE FROM password WHERE userID=? AND passwordID=?");
					ps.setInt(1, userID);
					ps.setInt(2, passwordID);
					ps.executeUpdate();
					success=true;
				}
			} catch(SQLException e) {
				System.out.println(e.getMessage());
			} finally {
				try {
					if(rs != null) rs.close();
					if(ps != null) ps.close();
				} catch(SQLException e) {
					System.out.println(e.getMessage());
				}
			}
		}
		return success;
	}
	
	/* 
		Add a security question for the password with the given passwordID and the given question info
 	
		Returns: true if successful, false otherwise
	*/
	public static boolean addQuestion(int passwordID, String question, String answer) {
		boolean success = false;
		if(conn==null) createConnection();
		if(conn != null) {
			PreparedStatement ps=null;
			ResultSet rs=null;
			try {
				ps=conn.prepareStatement("INSERT INTO security_question (passwordID, question, answer) VALUES (?,?,?)");
				ps.setInt(1, passwordID);
				ps.setString(2, question);
				ps.setString(3, answer);
				ps.executeUpdate();
				success=true;
			} catch(SQLException e) {
				System.out.println(e.getMessage());
			} finally {
				try {
					if(rs != null) rs.close();
					if(ps != null) ps.close();
				} catch(SQLException e) {
					System.out.println(e.getMessage());
				}
			}
		}
		return success;
	}
	
	/*
		Remove a question for the given passwordID if the given userID is the creator of the password
		
		Returns: true if successful, false otherwise
	 */
	public static boolean removeQuestion(int userID, int passwordID, int questionID) {
		boolean success = false;
		if(conn==null) createConnection();
		if(conn != null) {
			PreparedStatement ps=null;
			ResultSet rs=null;
			try {
				ps=conn.prepareStatement("SELECT COUNT(*) FROM password WHERE userID=? AND passwordID=?");
				ps.setInt(1, userID);
				ps.setInt(2, passwordID);
				rs=ps.executeQuery();
				if(rs.next()) { //Password belongs to the given user, this security question
					ps.close();
					ps=conn.prepareStatement("DELETE FROM security_question WHERE questionID=?");
					ps.setInt(1, questionID);
					ps.executeUpdate();
					success=true;
				}
			} catch(SQLException e) {
				System.out.println(e.getMessage());
			} finally {
				try {
					if(rs != null) rs.close();
					if(ps != null) ps.close();
				} catch(SQLException e) {
					System.out.println(e.getMessage());
				}
			}
		}
		return success;
	}
}

class DBUserInfo {
	private int userID;
	private String username;
	private String last_update;
	private String email;
	
	DBUserInfo(int userID, String username, String last_update, String email) {
		this.userID=userID;
		this.username=username;
		this.last_update=last_update;
		this.email=email;
	}
	
	int getUserID() {
		return userID;
	}
	
	String getUsername() {
		return username;
	}
	
	String getLastUpdate() {
		return last_update;
	}
	
	String getEmail() {
		return email;
	}
}