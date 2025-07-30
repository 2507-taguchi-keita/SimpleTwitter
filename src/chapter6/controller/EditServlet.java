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

import chapter6.beans.Message;
import chapter6.logging.InitApplication;
import chapter6.service.MessageService;

@WebServlet(urlPatterns = { "/edit" })
public class EditServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	/**
	* ロガーインスタンスの生成
	*/
	Logger log = Logger.getLogger("twitter");

	/**
	* デフォルトコンストラクタ
	* アプリケーションの初期化を実施する。
	*/
	public EditServlet() {
		InitApplication application = InitApplication.getInstance();
		application.init();

	}

	/*画面表示*/
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		log.info(new Object() {
		}.getClass().getEnclosingClass().getName() +
				" : " + new Object() {
				}.getClass().getEnclosingMethod().getName());

		//requestからパラメータを取得、intに型変換
		//String strMessageId = request.getParameter("messageId");
		//int messageId = Integer.parseInt(strMessageId);

		String strMessageId = request.getParameter("messageId");
		HttpSession session = request.getSession();
		List<String> errorMessages = new ArrayList<>();

		//ネスト(構造の中に同じ構造がある状態)しすぎる方が良くない tryは予測出来ないエラーをキャッチするため。if文が多くあるのは悪くない
		//こういう事が起きたらエラーメッセージを出したい、という時はtryは使わない
		//出す条件が決まっている際はtry文ではなくif文
		// 空 or 空白 or null チェック　数値じゃない事もまとめられる
		if (StringUtils.isBlank(strMessageId) || !strMessageId.matches("^[0-9]+$")) {
			errorMessages.add("不正なパラメータが入力されました");
			session.setAttribute("errorMessages", errorMessages);
			response.sendRedirect("./"); // トップに戻る
			return;
		}

		// 数値に変換できるか
		int messageId = Integer.parseInt(strMessageId);

		// メッセージ取得
		Message message = new MessageService().select(messageId);
		if (message == null) {
			// 存在しないID
			errorMessages.add("不正なパラメータが入力されました");
			session.setAttribute("errorMessages", errorMessages);
			response.sendRedirect("./"); // トップに戻る
			return;
		}

		// 正常時 → 編集画面へ
		request.setAttribute("message", message);
		request.getRequestDispatcher("edit.jsp").forward(request, response);
	}

	/*編集画面　編集ボタン押下後*/
	@Override
	//ユーザーからのリクエストと、サーバーからのレスポンスを扱う
	//ユーザーからのリクエスト情報をrequestに、レスポンス情報をresponseに
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		log.info(new Object() {
		}.getClass().getEnclosingClass().getName() +
				" : " + new Object() {
				}.getClass().getEnclosingMethod().getName());

		//ただの文字として入力される→画面にはJava側の型とかは無いため
		String messageIdStr = request.getParameter("messageId");

		//空文字を型変換しようとしています、というエラーになっていた
		int messageid = Integer.parseInt(messageIdStr);

		//requestという情報の中に含まれるパラメータから、textというパラメータの値を取得する
		String messageText = request.getParameter("text");

		// バリデーションの実行
		List<String> errorMessages = new ArrayList<>();

		if (!isValid(messageText, errorMessages)) {
			// バリデーションエラーがあった場合、エラーメッセージをセット
			request.setAttribute("errorMessages", errorMessages);
			Message message = new Message();
			message.setId(messageid);
			message.setText(messageText);
			//setAtttribute→Javaのメソッド.HTMLに新しい要素を設定したり、更新する
			request.setAttribute("message", message);

			// エラーメッセージを再度表示し、編集画面に戻る
			request.getRequestDispatcher("edit.jsp").forward(request, response);
			return; // 更新処理は行わない
		}

		new MessageService().update(messageText, messageid); // メッセージの更新
		// 更新後はホーム画面にリダイレクト
		response.sendRedirect("./");

	}

	private boolean isValid(String messageText, List<String> errorMessages) {

		log.info(new Object() {
		}.getClass().getEnclosingClass().getName() +
				" : " + new Object() {
				}.getClass().getEnclosingMethod().getName());

		// 空のメッセージ
		if (StringUtils.isBlank(messageText)) {
			errorMessages.add("メッセージを入力してください");
		}
		// 140文字を超えている場合
		else if (messageText.length() > 140) {
			errorMessages.add("140文字以下で入力してください");
		}

		// バリデーションが通らない場合はエラーメッセージリストが返る
		return errorMessages.isEmpty();
	}
}