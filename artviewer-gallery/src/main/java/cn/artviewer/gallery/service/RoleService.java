package cn.artviewer.gallery.service;

import cn.artviewer.gallery.dto.RoleDto;
import cn.artviewer.gallery.model.Role;
import com.github.pagehelper.Page;

public interface RoleService {

	void saveRole(RoleDto roleDto);

	void deleteRole(Long id);

	/**
	 * 分页查询
	 * @param pageNo 页号
	 * @param pageSize 每页显示记录数
	 * @return
	 */
	Page<Role> findByPage(int pageNo, int pageSize);
}
