package cn.artviewer.gallery.dto;

import java.util.List;

import cn.artviewer.gallery.model.Role;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoleDto extends Role {

	private static final long serialVersionUID = 4218495592167610193L;

	private List<Long> permissionIds;
}
