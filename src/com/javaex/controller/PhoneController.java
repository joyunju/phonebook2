package com.javaex.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.javaex.dao.PhoneDao;
import com.javaex.util.WebUtil;
import com.javaex.vo.PersonVo;

@WebServlet("/pbc")
// 호출 : localhost:8088/phonebook2/pbc
public class PhoneController extends HttpServlet {
	// 필드
	private static final long serialVersionUID = 1L;

	// 생성자 (default값 사용)
	// gs
	// 일반

	// get방식 요청
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		// 포스트 방식 한글깨짐 패치
		request.setCharacterEncoding("UTF-8");

		// 모든 기능을 담당하는 controller에게 특정 임무를 구분지어 부여(원하는 임무 수행 의사표시)
		String action = request.getParameter("action");

		if ("list".equals(action)) {
			PhoneDao phoneDao = new PhoneDao();
			List<PersonVo> pList = phoneDao.dbSelect();

			// request 내부 db 추가
			request.setAttribute("personList", pList);

			// forward & redirect 스태틱 인용
			WebUtil.forward(request, response, "./WEB-INF/list.jsp");

			/*
			 * RequestDispatcher rd = request.getRequestDispatcher("./list.jsp");
			 * rd.forward(request, response);
			 */

		} else if ("writeForm".equals(action)) {
			// 포워드 작업

			WebUtil.forward(request, response, "./WEB-INF/writeForm.jsp");

			/*
			 * RequestDispatcher rd = request.getRequestDispatcher("./writeForm.jsp");
			 * rd.forward(request, response);
			 */

		} else if ("write".equals(action)) {
			// 파라미터에서 값 꺼내가(name, hp, company)
			String name = request.getParameter("name");
			String hp = request.getParameter("hp");
			String company = request.getParameter("company");

			PersonVo personVo = new PersonVo(name, hp, company);
			PhoneDao phoneDao = new PhoneDao();

			phoneDao.dbInsert(personVo);

			// list redirect
			WebUtil.redirect(request, response, "/phonebook2/pbc?action=list");
			// response.sendRedirect("/phonebook2/pbc?action=list");

		} else if ("delete".equals(action)) {
			int id = Integer.parseInt(request.getParameter("id"));

			PersonVo personVo = new PersonVo(id);
			PhoneDao phoneDao = new PhoneDao();

			phoneDao.dbDelete(personVo);

			WebUtil.redirect(request, response, "/phonebook2/pbc?action=list");
			// response.sendRedirect("/phonebook2/pbc?action=list");

		} else if ("updateForm".equals(action)) {

			WebUtil.forward(request, response, "./WEB-INF/updateForm.jsp");
			/*
			 * RequestDispatcher rd = request.getRequestDispatcher("./updateForm.jsp");
			 * rd.forward(request, response);
			 */

		} else if ("update".equals(action)) {
			int id = Integer.parseInt(request.getParameter("id"));
			String name = request.getParameter("name");
			String hp = request.getParameter("hp");
			String company = request.getParameter("company");

			PersonVo personVo = new PersonVo(id, name, hp, company);
			PhoneDao phoneDao = new PhoneDao();

			phoneDao.dbUpdate(personVo);

			WebUtil.redirect(request, response, "/phonebook2/pbc?action=list");
			// response.sendRedirect("/phonebook2/pbc?action=list");

		} else {
			System.out.println("파라미터 없음");
		}

	}

	// post방식 요청 (cf. post시 한글이 깨질 수 있음, get에서 사전조치)
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}