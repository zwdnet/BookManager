import java.io.IOException;
import java.sql.ResultSet;
import java.util.Scanner;

/**
 *
 * @author 赵瑜敏
 * 电子书管理程序主程序
 *
 */
public class BookManager
{

	/**
	 * @param args
	 */
	//记录书籍信息的类
	private class BookInformation
	{
		public int BookID; //书籍ID
		public String BookName; //书籍名称
		public int BookPage; //书籍页数
		public int TypeID; //类型ID
		public String TypeName; //类型名称
		public String Author; //作者姓名
		public String Translater; //译者姓名
	}
	//私有方法
	//清除屏幕信息
	private void ClearScreen()
	{
		for (int i = 0; i < 50; i++)
		{
			System.out.println(" ");
		}
	}
	//获取用户输入
	private int GetInput()
	{
		int input = 0;
		int t = 0;
		Scanner scanner = new Scanner(System.in);
		do
		{
			ClearScreen();
			if (t > 0)
			{
				System.out.println("输入错误，请重新输入!");
			}
			t++;
			System.out.println("1.增加一本书.................");
			System.out.println("2.批量导入/导出图书............");
			System.out.println("3.删除一本书.................");
			System.out.println("4.删除所有图书................");
			System.out.println("5.用书名检索.................");
			System.out.println("6.用作者/译者检索.............");
			System.out.println("7.输出书籍总数................");
			System.out.println("8.退出.....................");
			System.out.println("欢迎使用！请按提示输入选择:");
			try
			{
				input = scanner.nextInt();
			}
			catch(Exception e)
			{
				return -1;
			}
			if (input >= 1 && input <= 9)
			{
				break;
			}
		}while(true);
		return input;
	}
	//输出并且换行
	private void Writeln(String info) throws IOException
	{
		System.out.println(info);
		System.in.read();
		System.in.read();
	}
	//输出但不换行
	private void Write(String info) throws IOException
	{
		System.out.print(info);
		System.in.read();
	}
	//主程序
	public static void main(String[] args)throws Exception
	{
		// TODO Auto-generated method stub
		int choose = -1; //用户输入的选项
		BookManager bookmanager = new BookManager();
		do
		{
			choose = bookmanager.GetInput();
			if (choose == -1)
			{
				System.out.println("输入错误！请重新输入！");
				System.in.read();
				continue;
			}
			if (choose == 8)
			{
				System.out.println("程序将结束，再见！");
				break;
			}
			switch (choose)
			{
			case 1:
				bookmanager.InsertOne();
				break;
			case 2:
				bookmanager.InsertAll();
				break;
			case 3:
				bookmanager.DeleteOne();
				break;
			case 4:
				bookmanager.DeleteAll();
				break;
			case 5:
				bookmanager.QueryByName();
				break;
			case 6:
				bookmanager.QueryByAuthor();
				break;
			case 7:
				bookmanager.Count();
				break;
			default:
				break;
			}
		}while(choose != 8);
	}
	//统计数据库中记录的总数
	private void Count()throws Exception
	{
		// TODO Auto-generated method stub
		String query = "select count(*) from book;";
		ResultSet rs = null;
		DataBase database = null;
		try
		{
			database = new DataBase();
			database.InitDatabase("book.db");
			rs = database.RunQuery(query);
			Writeln("数据库中的书籍记录总数为:" + rs.getInt(1));
		}
		catch (Exception e)
		{
			Writeln("查询书籍总数出错！详细信息：" + e.getMessage() +
					"请按回车键继续......");
			return;
		}
		finally
		{
			if (rs != null)
			{
				
			}
			database.CloseDatabase();
		}
	}
	//用作者、译者姓名检索数据库
	private void QueryByAuthor()throws Exception
	{
		// TODO Auto-generated method stub
		String writerName;
		System.out.print("请输入要查找的作者/译者姓名:");
		Scanner scanner = new Scanner(System.in);
		writerName = scanner.next();
		String querySql = "select book.BookID,book.BookName,book.BookPage,author.Author,author.Translater,type.TypeName"
				         + " from book,author,type where (author.Author like \"%"
				         + writerName + "%\" or author.Translater like \"%" + 
				           writerName + "%\") and author.BookID = book.BookID and "
				         + "book.TypeID = type.TypeID;";
		ResultSet rs = null;
		DataBase database = null;
		try
		{
			database = new DataBase();
			database.InitDatabase("book.db");
			rs = database.RunQuery(querySql);
			int n = 0;
			System.out.println("--------------------------------------");
			while (rs.next())
			{
				System.out.println("-----------" + "第" + (n+1) + "项结果--------------");
				System.out.println("书籍ID:" + rs.getInt("BookID"));
				System.out.println("书籍名称:" + rs.getString("BookName"));
				System.out.println("书籍页数:" + rs.getInt("BookPage"));
				System.out.println("作者:" + rs.getString("Author"));
				System.out.println("译者:" + rs.getString("Translater"));
				System.out.println("书籍分类:" + rs.getString("TypeName"));
				n++;
			}
			System.out.println("--------------------------------------");
			if (n == 0)
			{
				System.out.println("没有结果。");
			}
			else
			{
				System.out.println("共有" + n + "项结果。");
			}
			Writeln("查询结果输出完毕，按回车键继续......");
		}
		catch (Exception e)
		{
			Writeln("查询书籍信息出错！详细信息：" + e.getMessage() +
			           "请按回车键继续......");
			return;
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
	//用书名查询书籍信息
	private void QueryByName()throws Exception
	{
		// TODO Auto-generated method stub
		String bookName;
		System.out.print("请输入要查找的书名:");
		Scanner scanner = new Scanner(System.in);
		bookName = scanner.next();
		String querySql = "select book.BookID,book.BookName,book.BookPage,author.Author,author.Translater,type.TypeName"
				         + " from book,author,type where book.BookName like \"%"
				         + bookName + "%\" and author.BookID = book.BookID and "
				         + "book.TypeID = type.TypeID;";
		ResultSet rs = null;
		DataBase database = null;
		try
		{
			database = new DataBase();
			database.InitDatabase("book.db");
			rs = database.RunQuery(querySql);
			int n = 0;
			System.out.println("--------------------------------------");
			while (rs.next())
			{
				System.out.println("-----------" + "第" + (n+1) + "项结果--------------");
				System.out.println("书籍ID:" + rs.getInt("BookID"));
				System.out.println("书籍名称:" + rs.getString("BookName"));
				System.out.println("书籍页数:" + rs.getInt("BookPage"));
				System.out.println("作者:" + rs.getString("Author"));
				System.out.println("译者:" + rs.getString("Translater"));
				System.out.println("书籍分类:" + rs.getString("TypeName"));
				n++;
			}
			System.out.println("--------------------------------------");
			if (n == 0)
			{
				System.out.println("没有结果。");
			}
			else
			{
				System.out.println("共有" + n + "项结果。");
			}
			Writeln("查询结果输出完毕，按回车键继续......");
		}
		catch (Exception e)
		{
			Writeln("查询书籍信息出错！详细信息：" + e.getMessage() +
			           "请按回车键继续......");
			return;
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
	//删除数据库中所有图书信息
	private void DeleteAll()throws Exception
	{
		// TODO Auto-generated method stub
		String ask;
		System.out.print("要删除数据库中所有数据，确定吗?(Y/N)");
		Scanner scanner = new Scanner(System.in);
		ask = scanner.next();
		if (ask.equalsIgnoreCase("n"))
		{
			Writeln("用户放弃");
			return;
		}
		if (!ask.equalsIgnoreCase("y"))
		{
			Writeln("输入错误！");
			return;
		}
		//删除数据库中所有信息
		String tableName[] = {"book", "author", "type"};
		DataBase database = null;
		try
		{
			database = new DataBase();
			database.InitDatabase("book.db");
			for (int i = 0; i < 3; i++)
			{
				String sql = "delete from " + tableName[i];
				int n = database.RunUpdate(sql);
				if (n == 0)
				{
					String info = "删除" + tableName[i] + "表数据失败，按回车键继续......";
					System.out.println(info);
				}
				else
				{
					String info = "成功删除了" + tableName[i] + "表的" +  n + "行数据,按回车键继续......";
					System.out.println(info);
				}
				if (i == 2)
				{
					System.in.read();
				}
			}
		}
		catch (Exception e)
		{
			Writeln("删除书籍信息出错！详细信息：" + e.getMessage() +
			           "请按回车键继续......");
			return;
		}
		finally
		{
			database.CloseDatabase();
		}
	}
	
	//删除一本书的信息
	private void DeleteOne()throws Exception
	{
		// TODO Auto-generated method stub
		String bookName;
		System.out.println("输入要删除的书名:");
		Scanner scanner = new Scanner(System.in);
		bookName = scanner.next();
		String sql = "delete from book where book.BookName = \""
				     + bookName + "\";";
		DataBase database = null;
		try
		{
			database = new DataBase();
			database.InitDatabase("book.db");
			int n = database.RunUpdate(sql);
			if (n == 0)
			{
				Writeln("删除数据失败，按回车键继续......");
			}
			else
			{
				Writeln("成功删除了" + n + "行数据,按回车键继续......");
			}
		}
		catch (Exception e)
		{
			Writeln("删除书籍信息出错！详细信息：" + e.getMessage() +
			           "请按回车键继续......");
			return;
		}
		finally
		{
			database.CloseDatabase();
		}
	}
	//从文件插入所有图书信息
	private void InsertAll()
	{
		// TODO Auto-generated method stub

	}
	//插入一本书的信息
	private void InsertOne()throws Exception
	{
		// TODO Auto-generated method stub
		ClearScreen();
		System.out.println("插入一本书的信息..........");
		System.out.println("请按照以下顺序输入：书籍id、书名、书籍页数、书籍类型id、书籍类型、作者、" +
				"译者（无请输入\"NULL\")");
		BookInformation bookinfo = new BookInformation();
		Scanner scanner = new Scanner(System.in);
		bookinfo.BookID = scanner.nextInt();
		bookinfo.BookName = scanner.next();
		bookinfo.BookPage = scanner.nextInt();
		bookinfo.TypeID = scanner.nextInt();
		bookinfo.TypeName = scanner.next();
		bookinfo.Author = scanner.next();
		bookinfo.Translater = scanner.next();
		try
		{
			//插入book表
			String sql = "insert into book values(" + bookinfo.BookID + ",\"" +
					bookinfo.BookName + "\"," + bookinfo.BookPage + "," +
					bookinfo.TypeID + ")";
			InsertDatabase(sql);
			//插入type表
			sql = "insert into type values(" + bookinfo.TypeID + ",\"" + bookinfo.TypeName + "\")";
			InsertDatabase(sql);
			//插入author表
			sql = "insert into author values(" + bookinfo.BookID + ",\"" + bookinfo.Translater
					+ "\",\"" + bookinfo.Author + "\")";
			int n = InsertDatabase(sql);
			if (n == 0)
			{
				Write("插入数据库失败!按回车键继续......");
			}
			else
			{
				Write("插入了" + n + "行数据，按回车键继续......");
			}
		}
		catch (Exception e)
		{
			Writeln("插入书籍信息出错！详细信息：" + e.getMessage() +
					           "请按回车键继续......");
			return;
		}
		Write("插入书籍信息成功，按回车键继续......");
	}
	//执行数据库插入语句
	private int InsertDatabase(String sql)throws Exception
	{
		DataBase database = new DataBase();
		try
		{
			database.InitDatabase("book.db");
			return database.RunUpdate(sql);
		}
		finally
		{
			database.CloseDatabase();
		}
	}
}
