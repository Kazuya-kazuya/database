package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;

public class Main {

	public static void main(String[] args) {
		Main program = new Main();
		program.start();
	}

	public static String INSERT =
			"insert into " +
			"	omikuji(id, kikkyo) " +
			"values " +
			"	(?, ?)";

	public static String SELECT =
			"select " +
			"	* " +
			"from " +
			"	omikuji " +
			"where " +
			"	id = ?";

	public static String DELETE =
			"delete " +
			"from " +
			"	omikuji";

	private void start() {
		deleteOmikuji();

		registOmikuji(0, Kikkyo.daikichi);
		registOmikuji(1, Kikkyo.chukichi);
		registOmikuji(2, Kikkyo.shokichi);
		registOmikuji(3, Kikkyo.kichi);
		registOmikuji(4, Kikkyo.kyo);
		registOmikuji(5, Kikkyo.daikyo);

		Random random = new Random();
		int id = random.nextInt(6);
		openOmikuji(id);
	}

	private Connection getConnection() throws SQLException {
		try {
			Class.forName("com.mysql.jdbc.Driver");
		}
		catch(ClassNotFoundException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}

		String serverName = "localhost:3306";
		String DBName = "omikuji";
		String userName = "omikuji";
		String password = "omikuji";

		Connection con = DriverManager.getConnection(
        		"jdbc:mysql://"
        		+ serverName + "/"
        		+ DBName
        		//+ "?useUnicode=true&characterEncoding=utf8"
        		,
        		userName,
        		password);

		return con;
	}

	private void registOmikuji(int id, Kikkyo kikkyo) {
		try(
				Connection con = getConnection();
		)
		{
			PreparedStatement stmt = con.prepareStatement(INSERT);
			stmt.setInt(1, id);
			stmt.setString(2, kikkyo.toString());
			stmt.executeUpdate();
		}
		catch(SQLException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	private void openOmikuji(int id) {
		try(
				Connection con = getConnection();
		)
		{
			PreparedStatement stmt = con.prepareStatement(SELECT);
			stmt.setInt(1, id);
			ResultSet rset = stmt.executeQuery();

			Omikuji omikuji = new Omikuji();
			while(rset.next()) {
				omikuji.setId(rset.getInt(1));
				omikuji.setKikkyo(Kikkyo.valueOf(rset.getString(2)));
			}

			System.out.println(omikuji.toString());
		}
		catch(SQLException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	private void deleteOmikuji() {
		try(
				Connection con = getConnection();
		)
		{
			PreparedStatement stmt = con.prepareStatement(DELETE);
			stmt.executeUpdate();
		}
		catch(SQLException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
}

class Omikuji{
	private int id;
	private Kikkyo kikkyo;


	public Omikuji() {
		super();
	}

	public Omikuji(int id, Kikkyo kikkyo) {
		super();
		this.id = id;
		this.kikkyo = kikkyo;
	}


	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public Kikkyo getKikkyo() {
		return kikkyo;
	}
	public void setKikkyo(Kikkyo kikkyo) {
		this.kikkyo = kikkyo;
	}


	public String toString() {
		String str =
				this.kikkyo +
				"でした\n" +
				this.kikkyo.getMessage();
		return str;
	}
}

enum Kikkyo{
	daikichi("めっちゃええやん"),
	chukichi("まあまあええやん"),
	shokichi("ええやん"),
	kichi("ちょっとええやん"),
	kyo("だめやん"),
	daikyo("めっちゃだめやん");

	private String message;

	private Kikkyo(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}