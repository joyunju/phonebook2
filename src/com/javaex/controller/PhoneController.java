package com.javaex.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.javaex.dao.PhoneDao;
import com.javaex.util.WebUtil;
import com.javaex.vo.PersonVo;


//@WebServlet("/PhoneController")
//이름 변경
@WebServlet("/pbc")
//호출 : localhost:8088/phonebook2/pbc
public class PhoneController extends HttpServlet {
	
	// 필드
	private static final long serialVersionUID = 1L;

	// 생성자 (기본생성자 사용)
	
	// 메소드 -gs

	// 메소드 -일반
	// get방식으로 요청시 호출 메소드
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		//코드 작성
		System.out.println("PhoneController");

		// 포스트 방식일때 한글깨짐 방지
		request.setCharacterEncoding("UTF-8");

		// action파라미터 꺼내기
		// 모든 기능을 담당하는 controller에게 특정 임무를 구분지어 부여(원하는 임무 수행 의사표시)
		String action = request.getParameter("action");
		System.out.println(action);
		
		// if(action.equals("list")){} -> 이렇게 작성
		//getParameter("action")의 action 값이 null이 되면 꺼지니까
		// 아래와 같이 작성하는게 좋음 
		if ("list".equals(action)) { // 리스트일때
			// 데이터 가져오기
			PhoneDao phoneDao = new PhoneDao();
			List<PersonVo> phoneList = phoneDao.getPersonList();
			System.out.println(phoneList);

			// request에 데이터 추가 : 내부 db 추가
			request.setAttribute("pList", phoneList);

			// 데이터 + html --> jsp 시킨다
//			RequestDispatcher rd = request.getRequestDispatcher("/list.jsp");
//			rd.forward(request, response);		
			// forward & redirect 스태틱 인용
			WebUtil.forward(request, response, "/WEB-INF/list.jsp");



		} else if ("writeForm".equals(action)) { // 등록폼일때

			// 포워드
			WebUtil.forward(request, response, "/WEB-INF/writeForm.jsp");

//			RequestDispatcher rd = request.getRequestDispatcher("/writeForm.jsp");
//			rd.forward(request, response);

		} else if ("write".equals(action)) { // 등록일때

			// 파라미터에서 값 꺼내기(name, hp ,company)
			String name = request.getParameter("name");
			String hp = request.getParameter("hp");
			String company = request.getParameter("company");

			// vo만들어서 값 초기화
			PersonVo personVo = new PersonVo(name, hp, company);
			System.out.println(personVo);

			// phoneDao.personInsert()를 통해 저장하기
			PhoneDao phoneDao = new PhoneDao();

			int count = phoneDao.personInsert(personVo);
			System.out.println(count);

			// 리다이렉트 list
			WebUtil.redirect(request, response, "./pbc?action=list");

			// response.sendRedirect("./pbc?action=list");
			// response.sendRedirect("/phonebook2/pbc?action=list");

		} else if ("delete".equals(action)) { // 삭제일때

			// 파라미터에서 id값을 꺼낸다
			int id = Integer.parseInt(request.getParameter("id"));

			// phoneDao.personDelete()를 통해 삭제하기
			PhoneDao phoneDao = new PhoneDao();
			int count = phoneDao.personDelete(id);

			// 리다이렉트 list
			WebUtil.redirect(request, response, "./pbc?action=list");
			// response.sendRedirect("./pbc?action=list");

		} else {
			System.out.println("action 파라미터 없음");
		}

	}

	// post방식으로 요청시 호출 메소드
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		System.out.println("여기는 post");
		// doPost를 실행해도 doGet인데, 이 둘의 차이점은?
		doGet(request, response);
	}

}