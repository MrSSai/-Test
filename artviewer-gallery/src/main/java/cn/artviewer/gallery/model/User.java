package cn.artviewer.gallery.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class User extends BaseEntity<Long> {

	private static final long serialVersionUID = -6525908145032868837L;

	private String username;
	private String password;
	@JsonIgnore
	private String salt;
	private String nickname;
	private String avatar;
	private int deptid;
	private String mobile;
	private String phone;
	private String email;
	@JsonFormat(pattern = "yyyy-MM-dd")
	private Date birthday;
	private String gender;
	private Integer status;
	private Integer recordstatus;
	private Integer ismobile;
	private Integer isdevice;

	public interface Status {
		int DISABLED = 0;
		int VALID = 1;
		int LOCKED = 2;
	}
}
