//package csci201.passwordmanager;

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
	
	private static void createConnection() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/password_protector?user="+dbUsername+"&password="+dbPassword+"&useSSL=false");
		} catch(ClassNotFoundException | SQLException e) {
			System.out.println(e.getMessage());
		}
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
		
		Returns the user's ID if credentials match or 0 if verification fails.
	*/
	public static int verifyUser(String name, String hash_pass) {
		int userID=0;
		if(conn==null) createConnection();
		if(conn != null) {
			PreparedStatement ps=null;
			ResultSet rs=null;
			try {
				ps=conn.prepareStatement("SELECT userID FROM user WHERE username=? AND master_pass=?");
				ps.setString(1, name);
				ps.setString(2, hash_pass);
				rs=ps.executeQuery();
				if(rs.next()) { //Check that the user exists
					userID=rs.getInt("user");
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
		return userID;
	}

	/*
		Creates a new user in the database with the user info passed as parameters
		
		Returns the userID of the created user or 0 if action failed.
	*/
	public static int createUser(String username, String hash_pass, String email, String phone) {
		int userID=0;
		if(conn==null) createConnection();
		if(conn != null) {
			PreparedStatement ps=null;
			ResultSet rs=null;
			try {
				ps=conn.prepareStatement("INSERT INTO user (username, master_pass, last_update, email, phone) VALUES (?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
				ps.setString(1, username);
				ps.setString(2, hash_pass);
				Calendar cal = new GregorianCalendar();
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
				dateFormat.setCalendar(cal); 
				String sqlNow = dateFormat.format(cal.getTime());
				ps.setString(3, sqlNow);
				ps.setString(4, email);
				ps.setString(5, phone);
				ps.executeUpdate();
				rs = ps.getGeneratedKeys();
				if(rs.next()) { //If successfully inserted return the userID
					userID=rs.getInt(1);
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

class DBPassword {
	private int passwordID;
	private String username;
	private String app_name;
	private String encrypted_pass;
	private Date last_update;
	private Date suggested_reset;
	private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	DBPassword(int passwordID, String app_name, String encrypted_pass, String last_update, String suggested_reset) {
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