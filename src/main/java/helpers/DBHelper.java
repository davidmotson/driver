package helpers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.annotation.Resource;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class DBHelper {

	private static final String CREATE_USER = "INSERT INTO users (email,password,phonenumber,ubertoken,lyfttoken,flywheelpass,sidecarpass) VALUES (?,?,?,?,?,?,?)";
	private static final String CREATE_FAVORITE = "INSERT INTO favorites (user_id,lat,long,name) VALUES (?,?,?,?)";
	private static final String DELETE_FAVORITE = "DELETE FROM favorites where id = ? AND user_id = ?";
	private static final String GET_USER = "SELECT id,phonenumber,ubertoken,lyfttoken,flywheelpass,sidecarpass FROM users WHERE email = ? AND password = ?";
	private static final String GET_FAVORITES = "SELECT id,lat,long,name FROM favorites WHERE user_id = ?";


	//@Resource(name = "jdbc/driver")
	private static DataSource database;

	private static synchronized DataSource getDatabase(){
		if(database == null){
			Context initContext;
			try {
				initContext = new InitialContext();
				Context envContext = (Context) initContext.lookup("java:comp/env");
				database = (DataSource) envContext.lookup("jdbc/driver");
			} catch (NamingException e) {
				e.printStackTrace();
			}
		}
		return database;
	}

	public static boolean createUser(String email,String password, String phoneNumber,
			String uberToken, String lyftToken, String sidecarPass, String flywheelPass){
		try(Connection con = getDatabase().getConnection()){
			PreparedStatement stmt = con.prepareStatement(CREATE_USER);
			stmt.setString(1, email);
			stmt.setString(2, password);
			stmt.setString(3, phoneNumber);
			stmt.setString(4, uberToken);
			stmt.setString(5, lyftToken);
			stmt.setString(6, flywheelPass);
			stmt.setString(7, sidecarPass);
			stmt.execute();
			return true;
		}catch(SQLException e){
			e.printStackTrace();
			return false;
		}
	}

	public static boolean createFavorite(int userId, double latitude, double longitude, String name){
		try(Connection con = getDatabase().getConnection()){
			PreparedStatement stmt = con.prepareStatement(CREATE_FAVORITE);
			stmt.setInt(1, userId);
			stmt.setDouble(2, latitude);
			stmt.setDouble(3, longitude);
			stmt.setString(4, name);
			stmt.execute();
			return true;
		}catch(SQLException e){
			e.printStackTrace();
			return false;
		}
	}

	public static boolean deleteFavorite(int faveId, int userId){
		try(Connection con = getDatabase().getConnection()){
			PreparedStatement stmt = con.prepareStatement(DELETE_FAVORITE);
			stmt.setInt(1, faveId);
			stmt.execute();
			return true;
		}catch(SQLException e){
			e.printStackTrace();
			return false;
		}
	}

	public static User getUser(String email, String password){
		try(Connection con = getDatabase().getConnection()){
			PreparedStatement stmt = con.prepareStatement(GET_USER);
			stmt.setString(1, email);
			stmt.setString(2, password);
			ResultSet rs = stmt.executeQuery();
			if(!rs.next()){
				return null;
			}
			return new User(email, rs.getString(2), rs.getString(3),rs.getString(4),rs.getString(5), rs.getString(6), getFavorites(rs.getInt(1)),rs.getInt(1));
		}catch(SQLException e){
			e.printStackTrace();
			return null;
		}
	}

	public static Favorite[] getFavorites(int userId){
		try(Connection con = getDatabase().getConnection()){
			PreparedStatement stmt = con.prepareStatement(GET_FAVORITES);
			stmt.setInt(1, userId);
			ResultSet rs = stmt.executeQuery();
			ArrayList<Favorite> tempOut = new ArrayList<Favorite>();
			while(rs.next()){
				tempOut.add(new Favorite(rs.getDouble(2),rs.getDouble(3),rs.getString(4),rs.getInt(1)));
			}
			return tempOut.toArray(new Favorite[tempOut.size()]);
		}catch(SQLException e){
			e.printStackTrace();
			return null;
		}

	}


}
