package cn.artviewer.gallery.service.impl;

import java.util.List;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import cn.artviewer.gallery.dao.RoleDao;
import cn.artviewer.gallery.dto.RoleDto;
import cn.artviewer.gallery.model.Role;
import cn.artviewer.gallery.service.RoleService;

import lombok.extern.slf4j.Slf4j;

@Slf4j(topic = "adminLogger")
@Service
public class RoleServiceImpl implements RoleService {

	@Autowired
	private RoleDao roleDao;

	@Override
	@Transactional
	public void saveRole(RoleDto roleDto) {
		Role role = roleDto;

		if (role.getId() != null) {// 修改
			Role r = roleDao.getRole(role.getName());
			if (r != null && r.getId() != role.getId()) {
				throw new IllegalArgumentException(role.getName() + "已存在");
			}

			roleDao.update(role);
		} else {// 新增
			Role r = roleDao.getRole(role.getName());
			if (r != null) {
				throw new IllegalArgumentException(role.getName() + "已存在");
			}

			roleDao.save(role);

			log.debug("新增角色{}", role.getName());
		}

		saveRolePermission(role.getId(), roleDto.getPermissionIds());
	}

	private void saveRolePermission(Long roleId, List<Long> permissionIds) {
		roleDao.deleteRolePermission(roleId);
		permissionIds.remove(0L);
		if (!CollectionUtils.isEmpty(permissionIds)) {
			roleDao.saveRolePermission(roleId, permissionIds);
		}
	}

	@Override
	@Transactional
	public void deleteRole(Long id) {
		roleDao.deleteRolePermission(id);
		roleDao.delete(id);

		log.debug("删除角色id:{}", id);
	}

	@Override
	public Page<Role> findByPage(int pageNo, int pageSize) {
		PageHelper.startPage(pageNo, pageSize);
		return roleDao.findByPage();
	}

}
