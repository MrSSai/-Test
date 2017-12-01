package cn.artviewer.gallery.dto;

import java.io.Serializable;
import java.util.List;

import cn.artviewer.gallery.model.Notice;
import cn.artviewer.gallery.model.User;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NoticeVO implements Serializable {

	private static final long serialVersionUID = 7363353918096951799L;

	private Notice notice;

	private List<User> users;
}
