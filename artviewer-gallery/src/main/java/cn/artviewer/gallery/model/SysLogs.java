package cn.artviewer.gallery.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SysLogs extends BaseEntity<Long> {

	private static final long serialVersionUID = -7809315432127036583L;
	private User user;
	private String module;
	private Boolean flag;
	private String remark;

}
