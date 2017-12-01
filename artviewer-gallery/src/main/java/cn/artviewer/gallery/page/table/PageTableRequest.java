package cn.artviewer.gallery.page.table;

import java.io.Serializable;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

/**
 * 分页查询参数
 * 
 * @author turkey
 *
 */
@Getter
@Setter
public class PageTableRequest implements Serializable {

	private static final long serialVersionUID = 7328071045193618467L;

	private Integer offset;
	private Integer limit;
	private Map<String, Object> params;
}
