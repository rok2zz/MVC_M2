package model2.mvcboard;

import java.util.List;
import java.util.Map;
import java.util.Vector;

import common.DBConnPool;

public class MVCBoardDAO extends DBConnPool {
	
	//기본 생성자 호출시 부모 클래스를 호출 
	public MVCBoardDAO () {
		super(); 			//DBConnPool객체의 기본 생성자 호출 , DBCP에서 con 객체 활성화
	}
	
	//검색 조건에 맞는 게시물 개수를 반환합니다. 
	public int selectCount( Map<String, Object> map ) {
		int totalCount = 0; 
		String query = "SELECT COUNT(*) FROM mvcboard";    //레코드의 총갯수 반환 ,  
			if (map.get("searchWord") != null) {			// 검색기능을 사용했을시 where 
				query += " Where " + map.get("searchField") + " " + "like '%" + map.get("searchWord") + "%'"; 
			}
		try {
			stmt = con.createStatement(); 
			rs = stmt.executeQuery(query);
			rs.next(); 
			totalCount = rs.getInt(1); 
			
		} catch (Exception e) {
			System.out.println("게시물 카운트중 예외 발생");
			e.printStackTrace();
		}
					
		return totalCount; 
	}
	
	
	
	
	//검색 조건에 맞는 게시물 목록을 반환합니다.
		//DataBase에서 Select 한 결과 값을  DTO에 담아서 리턴 시켜줌.  
    public List<MVCBoardDTO> selectListPage(Map<String,Object> map) {
        List<MVCBoardDTO> board = new Vector<MVCBoardDTO>();
        String query = " "
                     + "SELECT * FROM ( "
                     + "    SELECT Tb.*, ROWNUM rNum FROM ( "
                     + "        SELECT * FROM mvcboard ";

        if (map.get("searchWord") != null)
        {
            query += " WHERE " + map.get("searchField")
                   + " LIKE '%" + map.get("searchWord") + "%' ";
        }

        query += "        ORDER BY idx DESC "
               + "    ) Tb "
               + " ) "
               + " WHERE rNum BETWEEN ? AND ?";
        
        System.out.println(query);  //콘솔에 전체 쿼리 출력 

        try {
            psmt = con.prepareStatement(query);
            psmt.setString(1, map.get("start").toString());
            psmt.setString(2, map.get("end").toString());
            rs = psmt.executeQuery();

            while (rs.next()) {
                MVCBoardDTO dto = new MVCBoardDTO();

                dto.setIdx(rs.getString(1));
                dto.setName(rs.getString(2));
                dto.setTitle(rs.getString(3));
                dto.setContent(rs.getString(4));
                dto.setPostdate(rs.getDate(5));
                dto.setOfile(rs.getString(6));
                dto.setSfile(rs.getString(7));
                dto.setDowncount(rs.getInt(8));
                dto.setPass(rs.getString(9));
                dto.setVisitcount(rs.getInt(10));

                board.add(dto);
            }
        }
        catch (Exception e) {
            System.out.println("게시물 조회 중 예외 발생");
            e.printStackTrace();
        }
        return board;
    }
	
	
	
	
	//목록 상세 검색 (Select ) : 주어진 일련 번호에 해당 하는 값을 DTO 에 담 아 반환합니다. (한페이지 read)
    //ViewController 요청을 처리 
    
    public MVCBoardDTO selectView(String idx) {
    	MVCBoardDTO dto = new MVCBoardDTO();
    	String query = "SELECT * FROM mvcboard WHERE idx = ?"; 
    	
    	try {
    		psmt = con.prepareStatement(query); 
    		psmt.setString(1,idx); 
    		rs = psmt.executeQuery(); 
    		
    		if (rs.next()) {
    			dto.setIdx(rs.getString(1));
    			dto.setName(rs.getString(2));
    			dto.setTitle(rs.getString(3));
    			dto.setContent(rs.getString(4));
    			dto.setPostdate(rs.getDate(5));
    			dto.setOfile(rs.getString(6));
    			dto.setSfile(rs.getString(7));
    			dto.setDowncount(rs.getInt(8));
    			dto.setPass(rs.getString(9));
    			dto.setVisitcount(rs.getInt(10));
    			
    		}
    		
    	}catch (Exception e) {
    		System.out.println("게시물 상세정보 출력중 예외 발생");
    		e.printStackTrace();
    	}
    	
    	return dto; 
    }
    
    //주어진 일련 번호에 해당하는 게시물의 조회수를 1 증가 시킴. 
    public void updateVisitCount(String idx) {
    	String query = "UPDATE mvcboard SET "
    			+ " visitcount = visitcount + 1 "
    			+ " WHERE idx = ?"; 
    	
    	try {
    		psmt = con.prepareStatement(query); 
    		psmt.setString (1, idx); 
    		psmt.executeUpdate(); 
    		
    	}catch (Exception e) {
    		e.printStackTrace();
    		System.out.println("게시물 조회수 증가시 예외 발생");
    	}    	
    }
    
    
    
    
    
	
	//데이터 삽입 (Insert)
	public int insertWrite (MVCBoardDTO dto) {
		int result = 0 ; 
		try {
			String query = "INSERT INTO mvcboard ( "
					+ " idx,name, title, content, ofile,sfile,pass) "
					+ " VALUES ("
					+ " seq_board_num.nextval, ?, ?, ?, ?, ?, ?)"; 
			
		psmt = con.prepareStatement(query);  //PareparedStatement 객체 생성 
		
		psmt.setString(1, dto.getName());
		psmt.setString(2, dto.getTitle());
		psmt.setString(3, dto.getContent());
		psmt.setString(4, dto.getOfile());
		psmt.setString(5, dto.getSfile());
		psmt.setString(6, dto.getPass());
		
		result = psmt.executeUpdate();  //insert가 성공일때 result > 0   //DB 에 값을 저장 
		
		
		}catch (Exception e) {
			e.printStackTrace();
		}
			
		return result; 		// result : Insert 성공시 > 0 , 실패시 : 0 
	}
	
	
	//데이터 수정 (Update)
	public int updatePost (MVCBoardDTO dto) {
		int result = 0 ; 
		
		try {
			String query =    "UPDATE mvcboard "
							+ " SET title = ? , name = ?, content = ?, ofile = ?, sfile = ? "
							+ " WHERE idx = ? and pass = ?"; 
			//쿼리문 준비 
			psmt = con.prepareStatement(query); 
			
			psmt.setString(1, dto.getTitle());
			psmt.setString(2, dto.getName());
			psmt.setString(3, dto.getContent());
			psmt.setString(4, dto.getOfile());
			psmt.setString(5, dto.getSfile());
			psmt.setString(6, dto.getIdx());
			psmt.setString(7, dto.getPass()); 
			
			result = psmt.executeUpdate();   //update성공시 result 변수의 값이 > 0 		
			
		}catch (Exception e) {
			e.printStackTrace();
			System.out.println("Update시 예외발생");
		}
				
		return result ;   //result > 0 : 수정 성공, result = 0 : 수정 실패 		
	}

	
	//데이터 삭제 (delete)
	public int deletePost(String idx) {
		int result = 0 ; 
		
		try {
			String query = "DELETE mvcboard WHERE idx = ?"; 
			psmt = con.prepareStatement(query); 
			psmt.setString(1, idx);
			
			result = psmt.executeUpdate();  //result > 0 삭제 성공, result = 0 : 삭제 실패 
			
		}catch (Exception e) {
			e.printStackTrace();
			System.out.println("delete 시 예외 발생 ");
		}
				
		return result; 				
	}
	
	
	
	//다운 로드 횟수를 증가시키는 메소드 
	public void downCountPlus (String idx) {
		String query = "UPDATE mvcboard SET downcount = downcount + 1 "
					   + " WHERE idx = ?"; 
		
		try {
			psmt = con.prepareStatement(query); 
			psmt.setString(1, idx);
			psmt.executeUpdate();
			
		}catch (Exception e) {
			e.printStackTrace();
			System.out.println("다운로드 횟수 증가시 오류 발생");
		}
	}
	
	// 비밀번호를 확인 하는 메소드 (입력한 비밀 번호가 DataBase 의 값과 일치하는지 확인 
	public boolean confirmPassword(String pass, String idx) {
		boolean isCorr = true; 
		try {
				//COUNT(*) = 1 : 레코드가 존재하면 : client에서 넘긴 pass , idx DB에 존재 
				//COUNT(*) = 0 : 레코드가 존재하지 않으면 client에서 넘긴 값이 DB에 존재하지 않는다. 
			String query = "SELECT COUNT(*) FROM mvcboard WHERE pass = ? and idx = ?";
			psmt = con.prepareStatement(query); 
			psmt.setString(1, pass);
			psmt.setString(2, idx);
			rs = psmt.executeQuery(); 
			
			rs.next(); 	
			if (rs.getInt(1) == 0) {
				isCorr = false; 
			}
					
		}catch (Exception e) {
			e.printStackTrace();
			System.out.println("비밀번호 확인시 예외 발생");
		}		
		return isCorr; 
	}
	
	
	
	
	
	
	

}
