package chapter6.dao;

import static chapter6.utils.CloseableUtil.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import chapter6.beans.Message;
import chapter6.exception.SQLRuntimeException;
import chapter6.logging.InitApplication;

public class MessageDao {

	/**
	* ロガーインスタンスの生成
	*/
	Logger log = Logger.getLogger("twitter");

	/**
	* デフォルトコンストラクタ
	* アプリケーションの初期化を実施する。
	*/
	public MessageDao() {
		InitApplication application = InitApplication.getInstance();
		application.init();

	}

	public void insert(Connection connection, Message message) {

		log.info(new Object() {
		}.getClass().getEnclosingClass().getName() +
				" : " + new Object() {
				}.getClass().getEnclosingMethod().getName());

		PreparedStatement ps = null;
		try {
			StringBuilder sql = new StringBuilder();
			sql.append("INSERT INTO messages ( ");
			sql.append("    user_id, ");
			sql.append("    text, ");
			sql.append("    created_date, ");
			sql.append("    updated_date ");
			sql.append(") VALUES ( ");
			sql.append("    ?, "); // user_id
			sql.append("    ?, "); // text
			sql.append("    CURRENT_TIMESTAMP, "); // created_date
			sql.append("    CURRENT_TIMESTAMP "); // updated_date
			sql.append(")");

			ps = connection.prepareStatement(sql.toString());

			ps.setInt(1, message.getUserId());
			ps.setString(2, message.getText());

			ps.executeUpdate();
		} catch (SQLException e) {
			log.log(Level.SEVERE, new Object() {
			}.getClass().getEnclosingClass().getName() + " : " + e.toString(), e);
			throw new SQLRuntimeException(e);
		} finally {
			close(ps);
		}
	}

	//deleteメソッドで、ConnectionオブジェクトとString型のmessageIdを受け取り、Userオブジェクトを返すこと
	public void delete(Connection connection, String messageId) {
		log.info(new Object() {
		}.getClass().getEnclosingClass().getName() +
				" : " + new Object() {
				}.getClass().getEnclosingMethod().getName());

		PreparedStatement ps = null;
		try {
			StringBuilder sql = new StringBuilder();
			sql.append("DELETE FROM messages ");
			sql.append("WHERE messages.id = ? ");

			ps = connection.prepareStatement(sql.toString());

			ps.setString(1, messageId);

			ps.executeUpdate();

		} catch (SQLException e) {
			log.log(Level.SEVERE, new Object() {
			}.getClass().getEnclosingClass().getName() + " : " + e.toString(), e);
			throw new SQLRuntimeException(e);
		} finally {
			close(ps);
		}
		return;
	}

	//ResultSet型(実行結果)を、listのuser型に詰替える
	private List<Message> toMessages(ResultSet rs) throws SQLException {

		log.info(new Object() {
		}.getClass().getEnclosingClass().getName() +
				" : " + new Object() {
				}.getClass().getEnclosingMethod().getName());

		List<Message> messages = new ArrayList<Message>();
		try {
			while (rs.next()) {
				//1つbeanを展開
				Message message = new Message();

				//rs(実行結果)を、userにset
				message.setId(rs.getInt("id"));

				messages.add(message);
			}
			return messages;
		} finally {
			close(rs);
		}

	}


	public Message select(Connection connection, Integer messageId) {
		log.info(new Object() {
		}.getClass().getEnclosingClass().getName() +
				" : " + new Object() {
				}.getClass().getEnclosingMethod().getName());

		PreparedStatement ps = null;
		try {
			//SELECT * FROM messages…messagesテーブルから情報を取得
			//WHERE messages.id = ?…条件として、messageテーブルのidを指定
	        String sql = "SELECT * FROM messages WHERE messages.id = ?";

	        ps = connection.prepareStatement(sql);
	        ps.setInt(1, messageId);

	        //sql実行、実行結果を格納
			ResultSet rs = ps.executeQuery();

			//toMessage…ResultSet型(実行結果)を、listのmessage型に詰替え
			List<Message> message = toMessages(rs);

			//serviceへreturn
			return message.get(0);
		} catch (SQLException e) {
		log.log(Level.SEVERE, new Object() {
		}.getClass().getEnclosingClass().getName() + " : " + e.toString(), e);
		throw new SQLRuntimeException(e);
		} finally {
			close(ps);
		}

	}

	public void update(Connection connection, String messageId, String messageText, String updateddate) {

		log.info(new Object() {
		}.getClass().getEnclosingClass().getName() +
				" : " + new Object() {
				}.getClass().getEnclosingMethod().getName());

		PreparedStatement ps = null;
		try {
			//更新日時も更新する
			String sql = "UPDATE messages SET messages.text = ?, messages.updated_date = ?, WHERE messages.id = ?";

					//preparedstatementオブジェクトを取得
					ps = connection.prepareStatement(sql);
					//バインド変数に値を設定する
					ps.setString(1, messageText);
					ps.setString(2, updateddate);
					ps.setString(3, messageId);

					ps.executeUpdate();
		} catch (SQLException e) {
			log.log(Level.SEVERE, new Object() {
			}.getClass().getEnclosingClass().getName() + " : " + e.toString(), e);
			throw new SQLRuntimeException(e);
		} finally {
			close(ps);
		}
    }

}