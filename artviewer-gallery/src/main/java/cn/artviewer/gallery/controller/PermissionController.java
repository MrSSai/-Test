package cn.artviewer.gallery.controller;


import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.google.common.collect.Maps;
import org.apache.commons.collections.CollectionUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import cn.artviewer.gallery.annotation.LogAnnotation;
import cn.artviewer.gallery.dao.PermissionDao;
import cn.artviewer.gallery.model.Permission;
import cn.artviewer.gallery.model.User;
import cn.artviewer.gallery.service.PermissionService;
import cn.artviewer.gallery.utils.UserUtil;

import io.swagger.annotations.ApiOperation;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * 权限相关接口
 * 
 * @author turkey
 *
 */
@RestController
@RequestMapping("/permissions")
public class PermissionController {

	@Autowired
	private PermissionDao permissionDao;
	@Autowired
	private PermissionService permissionService;

	@ApiOperation(value = "当前登录用户拥有的权限")
	@GetMapping("/current")
	public List<Permission> permissionsCurrent() {
		List<Permission> list = UserUtil.getCurrentPermissions();
		if (list == null) {
			User user = UserUtil.getCurrentUser();
			list = permissionDao.listByUserId(user.getId());
			UserUtil.setPermissionSession(list);
		}
		final List<Permission> permissions = list.stream().filter(l -> l.getType().equals(1))
				.collect(Collectors.toList());

		setChild(permissions);

		return permissions.stream().filter(p -> p.getParentId().equals(0L)).collect(Collectors.toList());
	}

	private void setChild(List<Permission> permissions) {
		permissions.parallelStream().forEach(per -> {
			List<Permission> child = permissions.stream().filter(p -> p.getParentId().equals(per.getId()))
					.collect(Collectors.toList());
			per.setChild(child);
		});
	}

	/**
	 * 菜单列表
	 * 
	 * @param pId
	 * @param permissionsAll
	 * @param list
	 */
	private void setPermissionsList(Long pId, List<Permission> permissionsAll, List<Permission> list) {
		for (Permission per : permissionsAll) {
			if (per.getParentId().equals(pId)) {
				list.add(per);
				if (permissionsAll.stream().filter(p -> p.getParentId().equals(per.getId())).findAny() != null) {
					setPermissionsList(per.getId(), permissionsAll, list);
				}
			}
		}
	}

	@GetMapping
	@ApiOperation(value = "菜单列表")
	@RequiresPermissions("sys:menu:query")
	public List<Permission> permissionsList() {
		List<Permission> permissionsAll = permissionDao.listAll();

		List<Permission> list = Lists.newArrayList();
		setPermissionsList(0L, permissionsAll, list);

		return list;
	}

	@GetMapping("/all")
	@ApiOperation(value = "所有菜单")
	@RequiresPermissions("sys:menu:query")
	public JSONArray permissionsAll() {
		List<Permission> permissionsAll = permissionDao.listAll();
		JSONArray array = new JSONArray();
		setPermissionsTree(0L, permissionsAll, array);

		return array;
	}

	@GetMapping("/parents")
	@ApiOperation(value = "一级菜单")
	@RequiresPermissions("sys:menu:query")
	public List<Permission> parentMenu() {
		List<Permission> parents = permissionDao.listParents();

		return parents;
	}

	@GetMapping("/list")
	public ModelAndView list(HttpServletRequest request) {
		ModelAndView mv = new ModelAndView("console/menu/menuList1");
		List<Permission> permissions = permissionDao.listAll();
		mv.addObject("permissions",permissions);
		return mv;
	}

	@GetMapping("/addMenu")
	public ModelAndView add(HttpServletRequest request) {
		ModelAndView mv = new ModelAndView("console/menu/addMenu");
		return mv;
	}

	@GetMapping("/updateMenu")
	public ModelAndView update(HttpServletRequest request) {
		ModelAndView mv = new ModelAndView("console/menu/updateMenu");
		return mv;
	}

	@GetMapping("/icon")
	public ModelAndView icon(HttpServletRequest request) {
		ModelAndView mv = new ModelAndView("console/menu/icon");
		return mv;
	}


	/**
	 * 菜单树
	 * 
	 * @param pId
	 * @param permissionsAll
	 * @param array
	 */
	private void setPermissionsTree(Long pId, List<Permission> permissionsAll, JSONArray array) {
		for (Permission per : permissionsAll) {
			if (per.getParentId().equals(pId)) {
				String string = JSONObject.toJSONString(per);
				JSONObject parent = (JSONObject) JSONObject.parse(string);
				array.add(parent);

				if (permissionsAll.stream().filter(p -> p.getParentId().equals(per.getId())).findAny() != null) {
					JSONArray child = new JSONArray();
					parent.put("child", child);
					setPermissionsTree(per.getId(), permissionsAll, child);
				}
			}
		}
	}

	@GetMapping(params = "roleId")
	@ApiOperation(value = "根据角色id删除权限")
	@RequiresPermissions(value = { "sys:menu:query", "sys:role:query" }, logical = Logical.OR)
	public List<Permission> listByRoleId(Long roleId) {
		return permissionDao.listByRoleId(roleId);
	}

	@LogAnnotation
	@PostMapping
	@ApiOperation(value = "保存菜单")
	@RequiresPermissions("sys:menu:add")
	public void save(@RequestBody Permission permission) {
		permissionDao.save(permission);
	}

	@GetMapping("/{id}")
	@ApiOperation(value = "根据菜单id获取菜单")
	@RequiresPermissions("sys:menu:query")
	public Permission get(@PathVariable Long id) {
		return permissionDao.getById(id);
	}

	@LogAnnotation
	@PutMapping
	@ApiOperation(value = "修改菜单")
	@RequiresPermissions("sys:menu:add")
	public void update(@RequestBody Permission permission) {
		permissionDao.update(permission);
	}

	/**
	 * 校验权限
	 * 
	 * @return
	 */
	@GetMapping("/owns")
	@ApiOperation(value = "校验当前用户的权限")
	public Set<String> ownsPermission() {
		List<Permission> permissions = UserUtil.getCurrentPermissions();
		if (CollectionUtils.isEmpty(permissions)) {
			return Collections.emptySet();
		}

		return permissions.parallelStream().filter(p -> !StringUtils.isEmpty(p.getPermission()))
				.map(Permission::getPermission).collect(Collectors.toSet());
	}

	@LogAnnotation
	@DeleteMapping("/{id}")
	@ApiOperation(value = "删除菜单")
	@RequiresPermissions(value = { "sys:menu:del" })
	public void delete(@PathVariable Long id) {
		permissionService.delete(id);
	}
}
