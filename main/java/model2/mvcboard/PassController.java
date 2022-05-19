package model2.mvcboard;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class PassController extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// Get 요청시 처리
		/*
		System.out.println("PassController 정상작동 ");
		String mode = req.getParameter("mode"); 
		System.out.println( "mode 변수의 값 : " + mode);
		*/ 
		
		//mode : edit <== 글수정,   mode : delete <== 글삭제 
		req.setAttribute("mode", req.getParameter("mode"));
		req.getRequestDispatcher("/mvcboard/Pass.jsp").forward(req, resp);
		
	
		
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// Post 요청시 처리 
		
		
		
		
		
	}
	
	

}
