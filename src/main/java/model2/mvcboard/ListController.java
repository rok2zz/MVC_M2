package model2.mvcboard;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import utils.BoardPage;   //페이징 처리하는 객체 

public class ListController extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// Get 방식으로 요청이 왔을때 서버에서 처리 
		
		//1. DAO 객체 생성 (Model : 비즈니스 로직 처리 ) 
		MVCBoardDAO dao = new MVCBoardDAO(); 
		
		//2.뷰에 전달할 매개변수 저장용 맵 생성 (Key,Value) 
	
        Map<String, Object> map = new HashMap<String, Object>();

        String searchField = req.getParameter("searchField");
        String searchWord = req.getParameter("searchWord");
        if (searchWord != null) {
            // 쿼리스트링으로 전달받은 매개변수 중 검색어가 있다면 map에 저장
            map.put("searchField", searchField);
            map.put("searchWord", searchWord);
        }
        int totalCount = dao.selectCount(map);  // 게시물 개수
		
	/* 페이징 처리 부분 start */
		
			//web.xml 에 셋팅된 변수값 불러오기 
		ServletContext application = getServletContext(); 
		int pageSize = Integer.parseInt(application.getInitParameter("POSTS_PER_PAGE")); 
		int blockPage = Integer.parseInt(application.getInitParameter("PAGES_PER_BLOCK"));
		
		//System.out.println(pageSize);
		//System.out.println(blockPage);
		
		//현재 페이지 확인 
		int pageNum = 1; 
		String pageTemp = req.getParameter("pageNum");  
		if (pageTemp != null && !pageTemp.equals("")) {
			pageNum = Integer.parseInt(pageTemp); 	//  값이 비어 있지 않을때 넘오논 페이지 변수를 정수로 변환해서 저장 
		}
		
		//목록에 출력할 게시물 범위 계산 
		int start = (pageNum -1) * pageSize + 1 ;  //첫 게시물 번호 
		int end = pageNum * pageSize;     //마지막 게시물 번호 
		
		//뷰 페이지에 값을 던져줌     
		map.put("start", start); 
		map.put("end", end);
		
		System.out.println(start);
		System.out.println(end);
		
		
		
	/* 페이징 처리 부분 end */ 
		
	// 게시물 목록을 받아오기 (DAO 객체에 작업을 전달 )
		//boardLists는 DB의 레코드를 담은 DTO객체(5개) 를 담고 있다. 
	
        List<MVCBoardDTO> boardLists = dao.selectListPage(map);  // 게시물 목록 받기
        dao.close(); // DB 연결 닫기
	
	//뷰페이지에 전달 할 매개변수들을 추가 
    String pagingImg = BoardPage.pagingStr(totalCount, pageSize,
            blockPage, pageNum, "../mvcboard/list.do");  // 바로가기 영역 HTML 문자열
    
    //view 페이지로 변수의 값 전달
    map.put("pagingImg", pagingImg);
    map.put("totalCount", totalCount);
    map.put("pageSize", pageSize);
    map.put("pageNum", pageNum); 
	
	//뷰페이지로 데이터 전달, request 영역에 전달할 데이터를 저장후 List.jsp (뷰페이지) 로 포워드 
    req.setAttribute("boardLists", boardLists);
    req.setAttribute("map", map);
    req.getRequestDispatcher("/mvcboard/List.jsp").forward(req, resp);
	
	
	
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// Post  방식으로 요청이 왔을때 서버에서 처리 
	}
	
}
