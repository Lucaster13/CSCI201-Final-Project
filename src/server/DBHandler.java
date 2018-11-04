package server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

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
				ps=conn.prepareStatement("SELECT userID, username, last_update, phone FROM user WHERE username=? AND master_pass=?");
				ps.setString(1, name);
				ps.setString(2, hash_pass);
				rs=ps.executeQuery();
				if(rs.next()) { //Check that the user exists
					info=new DBUserInfo(rs.getInt("userID"), rs.getString("username"), rs.getString("last_update"), rs.getString("phone"));
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
	public static int createUser(String username, String hash_pass, String phone) {
		int userID=0;
		if(conn==null) createConnection();
		if(conn != null) {
			PreparedStatement ps=null;
			ResultSet rs=null;
			try {
				ps=conn.prepareStatement("SELECT userID, master_pass FROM user WHERE username=?");
				ps.setString(1, username);
				rs=ps.executeQuery();
				if(rs.next()) { //Check if the user exists
					if(rs.getString("master_pass").equals(hash_pass)) {
						userID = -1;
					}
				} else {
					if(rs != null) rs.close();
					if(ps != null) ps.close();
					ps=conn.prepareStatement("INSERT INTO user (username, master_pass, last_update, phone) VALUES (?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
					ps.setString(1, username);
					ps.setString(2, hash_pass);
					Calendar cal = new GregorianCalendar();
					SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
					dateFormat.setCalendar(cal); 
					String sqlNow = dateFormat.format(cal.getTime());
					ps.setString(3, sqlNow);
					ps.setString(4, phone);
					ps.executeUpdate();
					rs = ps.getGeneratedKeys();
					if(rs.next()) { //If successfully inserted return the userID
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
	public static ArrayList<DBPassword> getPasswords(int userID) {
		ArrayList<DBPassword> results = new ArrayList<DBPassword>();
		if(conn==null) createConnection();
		if(conn != null) {
			PreparedStatement ps=null;
			ResultSet rs=null;
			try {
				ps=conn.prepareStatement("SELECT passwordID, username, app_name, encrypted_pass, last_update, suggested_reset FROM password WHERE userID=?");
				ps.setInt(1, userID);
				rs=ps.executeQuery();
				while(rs.next()) {
					results.add(new DBPassword(rs.getInt("passwordID"), rs.getString("username"), rs.getString("app_name"), rs.getString("encrypted_pass"), rs.getString("last_update"), rs.getString("suggested_reset")));
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
	public static ArrayList<DBSecurityQuestion> getSecurityQuestions(int passwordID) {
		ArrayList<DBSecurityQuestion> results = new ArrayList<DBSecurityQuestion>();
		if(conn==null) createConnection();
		if(conn != null) {
			PreparedStatement ps=null;
			ResultSet rs=null;
			try {
				ps=conn.prepareStatement("SELECT question, answer FROM security_question WHERE passwordID=?");
				ps.setInt(1, passwordID);
				rs=ps.executeQuery();
				while(rs.next()) {
					results.add(new DBSecurityQuestion(rs.getString("question"),rs.getString("answer")));
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
}

class DBUserInfo {
	private int userID;
	private String username;
	private String last_update;
	private String phone;
	
	DBUserInfo(int userID, String username, String last_update, String phone) {
		this.userID=userID;
		this.username=username;
		this.last_update=last_update;
		this.phone=phone;
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
	
	String getPhone() {
		return phone;
	}
}

class DBPassword {
	private int passwordID;
	private String username;
	private String app_name;
	private String encrypted_pass;
	private Date last_update;
	private Date suggested_reset;
	private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	DBPassword(int passwordID, String username, String app_name, String encrypted_pass, String last_update, String suggested_reset) {
		this.username=username;
		this.passwordID=passwordID;
		this.app_name=app_name;
		this.encrypted_pass=encrypted_pass;
		try {
			this.last_update=dateFormat.parse(last_update);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		try {
			this.suggested_reset=dateFormat.parse(suggested_reset);
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	int getID() {
		return passwordID;
	}
	String getUsername() {
		return username;
	}
	String getName() {
		return app_name;
	}
	String getPass() {
		return encrypted_pass;
	}
	Date getLastUpdate() {
		return last_update;
	}
	Date getSuggestedReset() {
		return suggested_reset;
	}
	long numDaysUntilReset() {
		return Math.round((suggested_reset.getTime() - last_update.getTime()) / (double) 86400000);
	}
}

class DBSecurityQuestion {
	private String question;
	private String answer;
	DBSecurityQuestion(String question, String answer) {
		this.question=question;
		this.answer=answer;
	}
	String getQuestion() {
		return question;
	}
	String getAnswer() {
		return answer;
	}
}