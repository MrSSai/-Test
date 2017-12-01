package cn.artviewer.gallery.controller;

import java.util.List;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.Maps;
import cn.artviewer.gallery.annotation.LogAnnotation;
import cn.artviewer.gallery.dao.RoleDao;
import cn.artviewer.gallery.dto.RoleDto;
import cn.artviewer.gallery.model.Role;
import cn.artviewer.gallery.page.table.PageTableRequest;
import cn.artviewer.gallery.page.table.PageTableHandler;
import cn.artviewer.gallery.page.table.PageTableResponse;
import cn.artviewer.gallery.page.table.PageTableHandler.CountHandler;
import cn.artviewer.gallery.page.table.PageTableHandler.ListHandler;
import cn.artviewer.gallery.service.RoleService;

import io.swagger.annotations.ApiOperation;
import org.springframework.web.servlet.ModelAndView;

import javax.management.relation.RoleList;
import javax.servlet.http.HttpServletRequest;

/**
 * 角色相关接口
 * 
 * @author turkey
 *
 */
@RestController
@RequestMapping("/roles")
public class RoleController {

	@Autowired
	private RoleService roleService;
	@Autowired
	private RoleDao roleDao;

	@LogAnnotation
	@PostMapping
	@ApiOperation(value = "保存角色")
	@RequiresPermissions("sys:role:add")
	public void saveRole(@RequestBody RoleDto roleDto) {
		roleService.saveRole(roleDto);
	}

	@GetMapping
	@ApiOperation(value = "角色列表")
	@RequiresPermissions("sys:role:query")
	public PageTableResponse<Role> listRoles(PageTableRequest request) {
		return PageTableHandler.<Role> builder().countHandler(new CountHandler() {

			@Override
			public int count(PageTableRequest request) {
				return roleDao.count(request.getParams());
			}
		}).listHandler(new ListHandler<Role>() {

			@Override
			public List<Role> list(PageTableRequest request) {
				List<Role> list = roleDao.list(request.getParams(), request.getOffset(), request.getLimit());
				return list;
			}
		}).build().handle(request);
	}

	@GetMapping("/{id}")
	@ApiOperation(value = "根据id获取角色")
	@RequiresPermissions("sys:role:query")
	public Role get(@PathVariable Long id) {
		return roleDao.getById(id);
	}


	// zss ---未使用分页
//	@GetMapping("/list")
//	@ApiOperation("获取role角色")
//	@RequiresPermissions("sys:role:query")
//	public ModelAndView roleList(HttpServletRequest request) {
//		ModelAndView mv = new ModelAndView("console/role/roleList1");
//		List<Role> roles = roleDao.list(Maps.newHashMap(), null, null);
//		mv.addObject("roles",roles);
//		return mv;
//	}

	//  zss ---使用分页
	@GetMapping("/list")
	@ApiOperation("获取role角色")
	@RequiresPermissions("sys:role:query")
	public ModelAndView roleList(HttpServletRequest request) {
		ModelAndView mv = new ModelAndView("console/role/roleList");
		String pageNum = request.getParameter("pageNum");
		if(pageNum == null){
			pageNum = "1";
		}
		Integer pageNo = Integer.parseInt(pageNum);
		Page<Role> roles = roleService.findByPage(pageNo, 2);
		PageInfo<Role>  pageInfo = new PageInfo<>(roles);
		mv.addObject("page",pageInfo);
		return mv;
	}

	@GetMapping(params = "userId")
	@ApiOperation(value = "根据用户id获取拥有的角色")
	@RequiresPermissions(value = { "sys:user:query", "sys:role:query" }, logical = Logical.OR)
	public List<Role> roles(Long userId) {
		return roleDao.listByUserId(userId);
	}

	@LogAnnotation
	@DeleteMapping("/{id}")
	@ApiOperation(value = "删除角色")
	@RequiresPermissions(value = { "sys:role:del" })
	public void delete(@PathVariable Long id) {
		roleService.deleteRole(id);
	}

	// zss
	@GetMapping("addRole")
	public ModelAndView add(HttpServletRequest request) {
		ModelAndView mv = new ModelAndView("/console/role/addRole");
		return mv;
	}
}
