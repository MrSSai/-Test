package cn.artviewer.gallery.controller.console;

import cn.artviewer.gallery.annotation.LogAnnotation;
import cn.artviewer.gallery.dao.UserDao;
import cn.artviewer.gallery.dto.UserDto;
import cn.artviewer.gallery.model.User;
import cn.artviewer.gallery.page.table.PageTableHandler;
import cn.artviewer.gallery.page.table.PageTableRequest;
import cn.artviewer.gallery.page.table.PageTableResponse;
import cn.artviewer.gallery.service.UserService;
import cn.artviewer.gallery.utils.PageInfo;
import cn.artviewer.gallery.utils.UserUtil;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author turkey
 * @description:
 * @date: Created in 14:32 2017/10/26
 */
@Slf4j(topic = "consoleLogger")
@RestController
@RequestMapping("/console/user")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private UserDao userDao;

    @LogAnnotation
    @PostMapping("/add")
    @ApiOperation(value = "保存用户")
    @RequiresPermissions("sys:user:add")
    public User saveUser(@RequestBody UserDto userDto) {
        User u = userService.getUser(userDto.getUsername());
        if (u != null) {
            throw new IllegalArgumentException(userDto.getUsername() + "已存在");
        }
        return userService.saveUser(userDto);
    }

    @LogAnnotation
    @PutMapping
    @ApiOperation(value = "修改用户")
    @RequiresPermissions("sys:user:add")
    public User updateUser(@RequestBody UserDto userDto) {
        return userService.updateUser(userDto);
    }

    @LogAnnotation
    @PutMapping(params = "avatar")
    @ApiOperation(value = "修改头像")
    public void updateAvatar(String avatar) {
        User user = UserUtil.getCurrentUser();
        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(user, userDto);
        userDto.setAvatar(avatar);
        userService.updateUser(userDto);
        log.debug("{}修改了头像", user.getUsername());
    }

    @LogAnnotation
    @PutMapping("/{username}")
    @ApiOperation(value = "修改密码")
    @RequiresPermissions("sys:user:password")
    public void changePassword(@PathVariable String username, String oldPassword, String newPassword) {
        userService.changePassword(username, oldPassword, newPassword);
    }

//    @LogAnnotation
    @GetMapping("/list")
    @ApiOperation(value = "用户列表")
//    @RequiresPermissions("sys:user:query")
    public ModelAndView listUsers(HttpServletRequest request) {
        ModelAndView mv = new ModelAndView("console/user/list");
        String pageNum = request.getParameter("pageNum");
        if(pageNum == null){
            pageNum = "1";
        }
        Integer pageNo = Integer.parseInt(pageNum);
        Page<User> users = userService.findByPage(pageNo, 2);
        PageInfo<User> pageInfo = new PageInfo<>(users);
        mv.addObject("page", pageInfo);
        return mv;
    }

    @ApiOperation(value = "当前登录用户")
    @GetMapping("/current")
    public User currentUser() {
        return UserUtil.getCurrentUser();
    }

    @ApiOperation(value = "根据用户id获取用户")
    @GetMapping("/{id}")
    @RequiresPermissions("sys:user:query")
    public User user(@PathVariable Long id) {
        return userDao.getById(id);
    }
}
