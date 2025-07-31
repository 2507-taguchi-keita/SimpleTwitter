package chapter6.filter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import chapter6.beans.User;

@WebFilter(urlPatterns= {"/edit", "/setting"})
//LoginFilterという名前のクラスを定義して→Filterインターフェースを実装する
public class LoginFilter implements Filter {
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws ServletException, IOException {
		HttpServletRequest rq = (HttpServletRequest) request;
		HttpServletResponse rp = (HttpServletResponse) response;

		//現在のユーザーのセッション情報(ユーザーが行った一連の操作)を取得する
		HttpSession session = rq.getSession();
		//sessionから、ログイン情報を取得
		User user = (User) session.getAttribute("loginUser");

		if(user == null) {
			List<String> errorMessages = new ArrayList<String>();
            errorMessages.add("ログインしてください");
            session.setAttribute("errorMessages", errorMessages);
            rp.sendRedirect("./login");
            return;
		}
		chain.doFilter(rq, rp);
	}

	@Override
	public void init(FilterConfig filterConfig){

	}

	@Override
	public void destroy() {

	}

}
