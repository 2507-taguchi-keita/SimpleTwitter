package chapter6.beans;

import java.io.Serializable;
import java.util.Date;

public class UserMessage implements Serializable {

	private int id;
	private String account;
	private String name;
	private int userId;
	private String text;
	private Date createdDate;

	// getter/setterは省略されているので、自分で記述しましょう。
	//id
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	//account
	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	//name
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	//userId
	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	//text
	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	//createdDate
	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

}