package com.admin.UserManagement.dao;
import com.admin.UserManagement.bean.*;
import java.sql.*;
import java.util.*;
public class UserDao {
	private String jdbcURL= "jdbc:mysql://localhost:3306/sayak?useSSL=false";
	// sayak=database name
	private String jdbcUsername="root";
	private String jdbcPassword="";
	
	private static final String INSERT_LIST_SQL="insert into list" + " (name,email,country) values" + " (?,?,?);";
	private static final String SELECT_USER_BY_ID="select id,name,email,country from list where id=?";
	private static final String SELECT_ALL_LIST= "select * from list";
	private static final String DELETE_LIST_SQL="delete from list where id=?;";
	private static final String UPDATE_USER_SQL="update list set name=?,email=?,country=? where id=?;";
	
	public UserDao(){}
	
	protected Connection getConnection() {
		Connection connection=null;
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			connection= DriverManager.getConnection(jdbcURL,jdbcUsername,jdbcPassword);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return connection;
	}
	public void insertUser(User user) throws SQLException {
        //System.out.println(INSERT_USERS_SQL);
        // try-with-resource statement will auto close the connection.
        try (Connection connection = getConnection();
        PreparedStatement preparedStatement =
connection.prepareStatement(INSERT_LIST_SQL)) {
            preparedStatement.setString(1, user.getName());
            preparedStatement.setString(2, user.getEmail());
            preparedStatement.setString(3, user.getCountry());
            //System.out.println(preparedStatement);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            printSQLException(e);
        }
    }
		
		public User selectUser(int id) {
			User user=null;
			//Step-1: Establishing a connection
			try (Connection connection=getConnection();
					//Step-2: Create a statement using connection object
					PreparedStatement preparestatement= connection.prepareStatement(SELECT_USER_BY_ID);){
				preparestatement.setInt(1, id);
				System.out.println(preparestatement);
				//Step-3: Execute the query or update query
				ResultSet rs= preparestatement.executeQuery();
				//step-4: Process the resultSet object
				while(rs.next()) {
					String name=rs.getString("name");
					String email=rs.getString("email");
					String country=rs.getString("country");
					user= new User(id,name,email,country);
				}
			
			}
			catch(SQLException e) {
				printSQLException(e);
			}
			return user;
		}
		public List<User>selectAllUsers(){
			//using try-with-resources to avoid closing resources(Boiler Plate Code)
			List <User>list=new ArrayList< >();
			//Step-1: Establising a connection
			try(Connection connection=getConnection();
					//Step-2: Create a statement using connection object
					PreparedStatement prepareStatement=connection.prepareStatement(SELECT_ALL_LIST);){
				System.out.println(prepareStatement);
				//Step-3: Execute the query or update query
				ResultSet rs= prepareStatement.executeQuery();
				//Step-4: Process the ResultSet object.
				while(rs.next()) {
					int id=rs.getInt("Id");
					String name=rs.getString("name");
					String email=rs.getString("email");
					String country=rs.getString("country");
					list.add(new User(id,name,email,country));
				}
			}
			catch(SQLException e) {
				printSQLException(e);
			}
			return list;
		}
		
		public boolean deleteUser(int id)throws SQLException{
			boolean rowDeleted;
			try(Connection connection=getConnection(); PreparedStatement statement=connection.prepareStatement(DELETE_LIST_SQL);){
				statement.setInt(1, id);
				rowDeleted=statement.executeUpdate()>0;
			}
			return rowDeleted;
		}
		public boolean updateUser(User user) throws SQLException {
			boolean rowUpdated;
			try(Connection connection=getConnection();
					PreparedStatement statement=connection.prepareStatement(UPDATE_USER_SQL);){
				statement.setString(1, user.getName());
				statement.setString(2, user.getEmail());
				statement.setString(3, user.getCountry());
				statement.setInt(4,user.getId());
				
				rowUpdated=statement.executeUpdate()>0;
			}
			return rowUpdated;
					
		}
private void printSQLException(SQLException ex) {
	for(Throwable e:ex) {
		if(e instanceof SQLException) {
			e.printStackTrace(System.err);
			System.err.println("SQLState: "+((SQLException) e).getSQLState());
			System.err.println("Error code: "+((SQLException)e).getSQLState());
			System.err.println("Message: "+ e.getMessage());
			Throwable t=ex.getCause();
			while(t!=null) {
				System.out.println("Cause: "+t);
				t=t.getCause();
				}
			}
		}
	}
}
	 


