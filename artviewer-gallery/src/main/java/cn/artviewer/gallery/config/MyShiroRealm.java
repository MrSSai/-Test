package cn.artviewer.gallery.config;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.util.StringUtils;

import cn.artviewer.gallery.dao.PermissionDao;
import cn.artviewer.gallery.dao.RoleDao;
import cn.artviewer.gallery.model.Permission;
import cn.artviewer.gallery.model.Role;
import cn.artviewer.gallery.model.User;
import cn.artviewer.gallery.model.User.Status;
import cn.artviewer.gallery.service.UserService;
import cn.artviewer.gallery.utils.SpringUtil;
import cn.artviewer.gallery.utils.UserUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j(topic = "adminLogger")
public class MyShiroRealm extends AuthorizingRealm {

	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		UsernamePasswordToken usernamePasswordToken = (UsernamePasswordToken) token;

		String username = usernamePasswordToken.getUsername();
		UserService userService = SpringUtil.getBean(UserService.class);
		User user = userService.getUser(username);
		if (user == null) {
			throw new UnknownAccountException("用户名不存在");
		}

		if (!user.getPassword()
				.equals(userService.passwordEncoder(new String(usernamePasswordToken.getPassword()), user.getSalt()))) {
			throw new IncorrectCredentialsException("密码错误");
		}

		if (user.getStatus() != Status.VALID) {
			throw new IncorrectCredentialsException("无效状态，请联系管理员");
		}

		SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(user, user.getPassword(),
				ByteSource.Util.bytes(user.getSalt()), getName());

		UserUtil.setUserSession(user);

		return authenticationInfo;
	}

	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		log.debug("权限配置");

		SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
		User user = UserUtil.getCurrentUser();
		List<Role> roles = SpringUtil.getBean(RoleDao.class).listByUserId(user.getId());
		Set<String> roleNames = roles.stream().map(Role::getName).collect(Collectors.toSet());
		authorizationInfo.setRoles(roleNames);
		List<Permission> permissionList = SpringUtil.getBean(PermissionDao.class).listByUserId(user.getId());
		UserUtil.setPermissionSession(permissionList);
		Set<String> permissions = permissionList.stream().filter(p -> !StringUtils.isEmpty(p.getPermission()))
				.map(Permission::getPermission).collect(Collectors.toSet());
		authorizationInfo.setStringPermissions(permissions);
		return authorizationInfo;
	}

	/**
	 * 重写缓存key，否则集群下session共享时，会重复执行doGetAuthorizationInfo权限配置
	 */
	@Override
	protected Object getAuthorizationCacheKey(PrincipalCollection principals) {
		SimplePrincipalCollection principalCollection = (SimplePrincipalCollection) principals;
		Object object = principalCollection.getPrimaryPrincipal();
		if (object instanceof User) {
			User user = (User) object;
			return "authorization:cache:key:users:" + user.getId();
		}
		return super.getAuthorizationCacheKey(principals);
	}

}
