package cn.artviewer.gallery.filter;

import java.io.IOException;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.UserFilter;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.http.HttpStatus;

import com.alibaba.fastjson.JSONObject;
import cn.artviewer.gallery.constants.UserConstants;
import cn.artviewer.gallery.dto.ResponseInfo;
import cn.artviewer.gallery.service.TokenManager;
import cn.artviewer.gallery.utils.SpringUtil;

/**
 * Restful方式登陆<br>
 * 在参数中或者header里加参数login-token作为登陆凭证<br>
 * 参数值是登陆成功后的返回值中获取
 * 
 * @author turkey
 *
 *         2017年8月3日
 */
public class RestfulFilter extends UserFilter {

	@Override
	protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
		String loginToken = getToken(request);
		if (StringUtils.isBlank(loginToken)) {// 非Restful方式
			return super.isAccessAllowed(request, response, mappedValue);
		}

		TokenManager tokenManager = SpringUtil.getBean(TokenManager.class);
		UsernamePasswordToken token = tokenManager.getToken(loginToken);

		if (token != null) {
			try {
				Subject subject = getSubject(request, response);
				if (subject.getPrincipal() == null) {
					subject.login(token);
				}

				return true;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return false;
	}

	/**
	 * 根据参数或者header获取login-token
	 * 
	 * @param request
	 * @return
	 */
	public static String getToken(ServletRequest request) {
		HttpServletRequest httpServletRequest = WebUtils.toHttp(request);
		String loginToken = httpServletRequest.getParameter(UserConstants.LOGIN_TOKEN);
		if (StringUtils.isBlank(loginToken)) {
			loginToken = httpServletRequest.getHeader(UserConstants.LOGIN_TOKEN);
		}

		return loginToken;
	}

	@Override
	protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
		String loginToken = getToken(request);
		if (StringUtils.isBlank(loginToken)) {
			return super.onAccessDenied(request, response);
		}

		writeResponse(WebUtils.toHttp(response), HttpStatus.UNAUTHORIZED.value(), info);
		return false;
	}

	private static String info = JSONObject.toJSONString(
			ResponseInfo.builder().code(HttpStatus.UNAUTHORIZED.value() + "").message("token不存在或者过期").build());

	public static void writeResponse(HttpServletResponse response, int status, String json) {
		try {
			response.setHeader("Access-Control-Allow-Origin", "*");
			response.setHeader("Access-Control-Allow-Methods", "*");
			response.setContentType("application/json;charset=UTF-8");
			response.setStatus(status);
			response.getWriter().write(json);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
