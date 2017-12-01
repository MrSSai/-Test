package cn.artviewer.gallery.page.table;

import java.util.Arrays;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.google.common.collect.Maps;

/**
 * 分页、查询参数解析
 * 
 * @author turkey
 *
 */
public class PageTableArgumentResolver implements HandlerMethodArgumentResolver {

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		Class<?> cla = parameter.getParameterType();

		return cla.isAssignableFrom(PageTableRequest.class);
	}

	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
			NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
		HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);

		PageTableRequest tableRequest = new PageTableRequest();
		Map<String, String[]> param = request.getParameterMap();
		if (param.containsKey("start")) {
			tableRequest.setOffset(Integer.parseInt(request.getParameter("start")));
		}

		if (param.containsKey("length")) {
			tableRequest.setLimit(Integer.parseInt(request.getParameter("length")));
		}

		Map<String, Object> map = Maps.newHashMap();
		tableRequest.setParams(map);

		param.forEach((k, v) -> {
			if (v.length == 1) {
				map.put(k, v[0]);
			} else {
				map.put(k, Arrays.asList(v));
			}
		});

		setOrderBy(tableRequest, map);

		return tableRequest;
	}

	private void setOrderBy(PageTableRequest tableRequest, Map<String, Object> map) {
		StringBuilder orderBy = new StringBuilder();
		int size = map.size();
		for (int i = 0; i < size; i++) {
			String index = (String) map.get("order[" + i + "][column]");
			if (StringUtils.isEmpty(index)) {
				break;
			}
			String column = (String) map.get("columns[" + index + "][data]");
			if (StringUtils.isBlank(column)) {
				continue;
			}
			String sort = (String) map.get("order[" + i + "][dir]");

			orderBy.append(column).append(" ").append(sort).append(", ");
		}

		if (orderBy.length() > 0) {
			tableRequest.getParams().put("orderBy",
					" order by " + StringUtils.substringBeforeLast(orderBy.toString(), ","));
		}
	}

}
