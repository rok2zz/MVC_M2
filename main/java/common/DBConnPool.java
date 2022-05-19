package common;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

public class DBConnPool {
	
	public Connection con;
	public Statement stmt;
	public PreparedStatement psmt;
	public ResultSet rs; 
	
	//기본 생성자 
	public DBConnPool() {
		
        try {
            // JDBC 드라이버 로드
            Class.forName("oracle.jdbc.OracleDriver");

            // DB에 연결
            String url = "jdbc:oracle:thin:@localhost:1521:xe";  
            String id = "hr2";
            String pwd = "1234"; 
            con = DriverManager.getConnection(url, id, pwd); 

            System.out.println("DB 연결 성공(기본 생성자)");
        }
        catch (Exception e) {            
            e.printStackTrace();
        }
		
		
/*		
 * System.out.println("DBCP 객체 출력 ");
		try {
			//톰켓에 설정한 커넥션 풀 (DBCP) 정보 얻어오기
			Context initCtx = new InitialContext(); 
			Context ctx = (Context)initCtx.lookup("java:comp/env"); 
			DataSource source = (DataSource)ctx.lookup("dbcp_myoracle"); 
			
			//커넥션 풀을 통해서 연결 얻기 
			con = source.getConnection();
			
			System.out.println("커넥션풀(DBCP) 연결 성공");
			
		}catch (Exception e) {
			System.out.println("커넥션풀(DBCP) 연결 실패");
			e.printStackTrace(); 	//오류 메세지 출력 : 자세한 정보 출력 
			System.out.println(e.getMessage()); //오류 메세지 출력 (간략 정보)
		}	
		
			*/
	}
	
	
	//자원 연결 해제  (자원 반납) : close() 메소드 호출시 자원을 반납하도록 설정 
    public void close() {
        try {            
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
            if (psmt != null) psmt.close();
            if (con != null) con.close();  // 자동으로 커넥션 풀로 반납됨

            System.out.println("DB 커넥션 풀 자원 반납");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
	
	

}
