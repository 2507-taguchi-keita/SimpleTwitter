package chapter6.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;

import chapter6.beans.Comment;
import chapter6.beans.User;
import chapter6.logging.InitApplication;
import chapter6.service.CommentService;

@WebServlet(urlPatterns = { "/comment" })
public class CommentServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	/**
	* ロガーインスタンスの生成
	*/
	Logger log = Logger.getLogger("twitter");

	/**
	* デフォルトコンストラクタ
	* アプリケーションの初期化を実施する。
	*/
	public CommentServlet() {
		InitApplication application = InitApplication.getInstance();
		application.init();

	}

	//メソッドが親クラスのメソッドをオーバーライド(上書き)していることを示す
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		log.info(new Object() {
		}.getClass().getEnclosingClass().getName() +
				" : " + new Object() {
				}.getClass().getEnclosingMethod().getName());

		//現在のユーザーのセッション情報(ユーザーが行った一連の操作)を取得する
		HttpSession session = request.getSession();
		List<String> errorMessages = new ArrayList<>();
		String comment = request.getParameter("comment");
		String strMessageId = request.getParameter("messageId");
		int messageId = Integer.parseInt(strMessageId);

		//もしコメントが有効でない場合、if文の中の処理が実行される ！があれば、コメントが正しかった場合の処理になる
		if (!isValid(comment, errorMessages)) {
			// バリデーションエラーがあった場合、エラーメッセージをセット
			//リクエストスコープにerrorMessagesという名前で、エラーメッセージ一覧を保存している
			session.setAttribute("errorMessages", errorMessages);
			// エラーメッセージを再度表示し、画面に戻る→サーバー内部で動きたいので、リダイレクト
			response.sendRedirect("./");
			return;
		}

		//sessionから、ログイン中のユーザー情報を取得
		User user = (User) session.getAttribute("loginUser");
		//Messageクラスのインスタンスを新しく作っている
		Comment comments = new Comment();
		//作成したメッセージにメッセージIDを設定している
		comments.setMessageId(messageId);
		//ユーザーが入力したコメントをmessageにセットしている
		comments.setText(comment);
		// メッセージに、ログインしているユーザーのIDを紐づける
		comments.setUserId(user.getId());

		//コメントの更新
		new CommentService().insert(comments);
		//TopServletのdoGetを通る
		response.sendRedirect("./");
	}

	private boolean isValid(String comment, List<String> errorMessages) {
		log.info(new Object() {
		}.getClass().getEnclosingClass().getName() +
				" : " + new Object() {
				}.getClass().getEnclosingMethod().getName());

		// 空のメッセージ
		if (StringUtils.isBlank(comment)) {
			errorMessages.add("メッセージを入力してください");
		}
		// 140文字を超えている場合
		else if (comment.length() > 140) {
			errorMessages.add("140文字以下で入力してください");

		}
		if (errorMessages.size() != 0) {
			return false;
		}
		// バリデーションが通らない場合はエラーメッセージリストが返る
		return true;
	}
}