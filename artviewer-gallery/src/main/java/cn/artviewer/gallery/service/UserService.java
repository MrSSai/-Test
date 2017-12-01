package cn.artviewer.gallery.service;

import cn.artviewer.gallery.dto.UserDto;
import cn.artviewer.gallery.model.User;
import com.github.pagehelper.Page;

import java.util.List;

public interface UserService {

	User saveUser(UserDto userDto);
	
	User updateUser(UserDto userDto);

	String passwordEncoder(String credentials, String salt);

	User getUser(String username);

	void changePassword(String username, String oldPassword, String newPassword);

	/**
	 * 分页查询
	 * @param pageNo 页号
	 * @param pageSize 每页显示记录数
	 * @return
	 */
	Page<User> findByPage(int pageNo, int pageSize);

}
