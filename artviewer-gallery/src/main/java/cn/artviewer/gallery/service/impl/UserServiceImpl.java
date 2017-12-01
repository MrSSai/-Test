package cn.artviewer.gallery.service.impl;

import java.util.List;
import java.util.UUID;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import cn.artviewer.gallery.constants.UserConstants;
import cn.artviewer.gallery.dao.UserDao;
import cn.artviewer.gallery.dto.UserDto;
import cn.artviewer.gallery.model.User;
import cn.artviewer.gallery.model.User.Status;
import cn.artviewer.gallery.service.UserService;
import cn.artviewer.gallery.utils.UserUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j(topic = "adminLogger")
@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserDao userDao;

	@Override
	@Transactional
	public User saveUser(UserDto userDto) {
		User user = userDto;
		user.setSalt(DigestUtils
				.md5Hex(UUID.randomUUID().toString() + System.currentTimeMillis() + UUID.randomUUID().toString()));
		user.setPassword(passwordEncoder(user.getPassword(), user.getSalt()));
		user.setStatus(Status.VALID);
		userDao.save(user);
		saveUserRoles(user.getId(), userDto.getRoleIds());

		log.debug("新增用户{}", user.getUsername());
		return user;
	}

	private void saveUserRoles(Long userId, List<Long> roleIds) {
		if (roleIds != null) {
			userDao.deleteUserRole(userId);
			if (!CollectionUtils.isEmpty(roleIds)) {
				userDao.saveUserRoles(userId, roleIds);
			}
		}
	}

	@Override
	public String passwordEncoder(String credentials, String salt) {
		Object object = new SimpleHash("MD5", credentials, salt, UserConstants.HASH_ITERATIONS);
		return object.toString();
	}

	@Override
	public User getUser(String username) {
		return userDao.getUser(username);
	}

	@Override
	public void changePassword(String username, String oldPassword, String newPassword) {
		User u = userDao.getUser(username);
		if (u == null) {
			throw new IllegalArgumentException("用户不存在");
		}

		if (!u.getPassword().equals(passwordEncoder(oldPassword, u.getSalt()))) {
			throw new IllegalArgumentException("密码错误");
		}

		userDao.changePassword(u.getId(), passwordEncoder(newPassword, u.getSalt()));

		log.debug("修改{}的密码", username);
	}

	@Override
	@Transactional
	public User updateUser(UserDto userDto) {
		userDao.update(userDto);
		saveUserRoles(userDto.getId(), userDto.getRoleIds());
		updateUserSession(userDto.getId());

		return userDto;
	}

	private void updateUserSession(Long id) {
		User current = UserUtil.getCurrentUser();
		if (current.getId().equals(id)) {
			User user = userDao.getById(id);
			UserUtil.setUserSession(user);
		}
	}

	@Override
	public Page<User> findByPage(int pageNo, int pageSize) {
		PageHelper.startPage(pageNo, pageSize);
		return userDao.findByPage();
	}
}
