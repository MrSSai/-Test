package cn.artviewer.gallery.dto;

import java.util.List;

import cn.artviewer.gallery.model.User;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDto extends User {

	private static final long serialVersionUID = -184009306207076712L;

	private List<Long> roleIds;

}
