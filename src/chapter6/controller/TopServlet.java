package chapter6.controller;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import chapter6.beans.User;
import chapter6.beans.UserComment;
import chapter6.beans.UserMessage;
import chapter6.logging.InitApplication;
import chapter6.service.CommentService;
import chapter6.service.MessageService;

@WebServlet(urlPatterns = { "/index.jsp" })
public class TopServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	/**
	* ロガーインスタンスの生成
	*/
	Logger log = Logger.getLogger("twitter");

	/**
	* デフォルトコンストラクタ
	* アプリケーションの初期化を実施する。
	*/
	public TopServlet() {
		InitApplication application = InitApplication.getInstance();
		application.init();

	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		//現在のクラス名とメソッド名を文字列として取得し、ログに記録する
		log.info(new Object() {
		}.getClass().getEnclosingClass().getName() +
				" : " + new Object() {
				}.getClass().getEnclosingMethod().getName());

		// isShowMessageForm…つぶやきのフォームを展開するか否かを判断するための変数
		boolean isShowMessageForm = false;

		//sessionから、ログイン情報を取得
		User user = (User) request.getSession().getAttribute("loginUser");
		//ログインしていれば、isShowMessageFormをtrueにする
		if (user != null) {
			isShowMessageForm = true;
		}

		String userId = request.getParameter("user_id");
		String startDate = request.getParameter("startDate");
		String endDate = request.getParameter("endDate");

		//messages→つぶやきの事を指す
		List<UserMessage> messages = new MessageService().select(userId, startDate, endDate);

		String comment = request.getParameter("comment");
		List<UserComment> comments = new CommentService().select();

		request.setAttribute("messages", messages);
		request.setAttribute("comments", comments);
		request.setAttribute("isShowMessageForm", isShowMessageForm);
		request.getRequestDispatcher("/top.jsp").forward(request, response);
	}
}