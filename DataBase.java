import java.sql.*;

import org.sqlite.JDBC;

/**
 *
 * @author 赵瑜敏
 * 电子书管理程序的数据库处理模块，用于进行sqlite数据库的操作
 *
 */
public class DataBase
{

	/**
	 * @param args
	 */
	private Connection connect;
	private Statement state;
	//初始化数据库
	public void InitDatabase(String DatabaseName)throws Exception
	{
		Class.forName("org.sqlite.JDBC");
		connect = DriverManager.getConnection("jdbc:sqlite:" + DatabaseName);
		state = connect.createStatement();
	}
	//执行数据库的更新语句
	public int RunUpdate(String sql)throws Exception
	{
		return state.executeUpdate(sql);
	}
	//执行创建数据表的语句
	public void RunCreate(String sql)throws Exception
	{
		state.execute(sql);
	}
	//执行查询
	public ResultSet RunQuery(String sql)throws Exception
	{
		ResultSet rs;
		rs = state.executeQuery(sql);
		return rs;
	}
	//关闭数据库
	public void CloseDatabase()throws Exception
	{
		if (connect != null)
		{
			connect.close();
		}
	}
	/*
	public static void main(String[] args)throws Exception
	{
		// TODO Auto-generated method stub
		DataBase database = null;
		ResultSet rs = null;
		try
		{
			database = new DataBase();
			//Class.forName("org.sqlite.JDBC");
			//Connection conn = DriverManager.getConnection("jdbc:sqlite:aa.db");
			database.InitDatabase("aa.db");
			//stat = RunCreate(conn, "create table tbl1(name varchar(20), salary int);");
			database.RunUpdate("insert into tbl1 values('zhangsan', 8000);");
			database.RunUpdate("insert into tbl1 values('Lisi', 7800);");
			rs = database.RunQuery("select * from tbl1");
			while (rs.next())
			{
				System.out.println("name = " + rs.getString("name") + " "
						           + "salary = " + rs.getShort("salary"));
			}
			rs.close();
			database.CloseDatabase();
		}
		finally
		{
			if (rs != null)
			{
				rs.close();
			}
			database.CloseDatabase();
		}
	}
	*/
}
