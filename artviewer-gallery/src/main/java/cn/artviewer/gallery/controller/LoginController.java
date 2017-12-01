package cn.artviewer.gallery.controller;

import cn.artviewer.gallery.utils.PageInfo;
import com.github.pagehelper.Page;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.artviewer.gallery.annotation.LogAnnotation;
import cn.artviewer.gallery.dto.Token;
import cn.artviewer.gallery.model.User;
import cn.artviewer.gallery.service.TokenManager;
import cn.artviewer.gallery.utils.UserUtil;

import io.swagger.annotations.ApiOperation;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * 登陆相关接口
 * 
 * @author turkey
 *
 */
@RestController
@RequestMapping("/")
public class LoginController {

	@Autowired
	private TokenManager tokenManager;

	@LogAnnotation
	@ApiOperation(value = "web端登陆")
	@PostMapping("/sys/login")
	public void login(String username, String password) {
		UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(username, password);
		SecurityUtils.getSubject().login(usernamePasswordToken);
	}

	@LogAnnotation
	@ApiOperation(value = "Restful方式登陆,前后端分离时登录接口")
	@PostMapping("/sys/login/restful")
	public Token restfulLogin(String username, String password) {
		UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(username, password);
		SecurityUtils.getSubject().login(usernamePasswordToken);
		return tokenManager.saveToken(usernamePasswordToken);
	}

	@ApiOperation(value = "当前登录用户")
	@GetMapping("/sys/login")
	private User getLoginInfo() {
		return UserUtil.getCurrentUser();
	}

	@PostMapping("/dologin")
	public void dologin(HttpServletRequest request) {
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(username, password);
		SecurityUtils.getSubject().login(usernamePasswordToken);
	}


	@GetMapping("/login")
	public ModelAndView signin(HttpServletRequest request) {
		ModelAndView mv = new ModelAndView("login");
		return mv;
	}

	// zss
	@GetMapping("/addUser")
	public  ModelAndView addU(HttpServletRequest request) {
		ModelAndView mv = new ModelAndView("/console/user/addUser");
		return mv;
	}
	// zss
	@GetMapping("/addNotice")
	public ModelAndView addN(HttpServletRequest request) {
		ModelAndView mv = new ModelAndView("/console/notice/addNotice");
		return mv;
	}

	@GetMapping("/updateUser")
	public ModelAndView update(HttpServletRequest request) {
		ModelAndView mv = new ModelAndView("/console/user/updateUser");
		return mv;
	}

	@GetMapping("/index")
	public ModelAndView index(HttpServletRequest request) {
		ModelAndView mv = new ModelAndView("index");
		mv.addObject("user",UserUtil.getCurrentUser());
		return mv;
	}
}
