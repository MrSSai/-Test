package cn.artviewer.gallery.service;

import cn.artviewer.gallery.model.Permission;

public interface PermissionService {

	void save(Permission permission);

	void update(Permission permission);

	void delete(Long id);
}
