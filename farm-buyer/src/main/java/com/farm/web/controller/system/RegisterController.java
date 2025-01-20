package com.farm.web.controller.system;

import com.farm.common.annotation.Log;
import com.farm.common.constant.UserConstants;
import com.farm.common.core.controller.BaseController;
import com.farm.common.core.domain.AjaxResult;
import com.farm.common.core.domain.entity.SysDept;
import com.farm.common.core.domain.entity.SysUser;
import com.farm.common.enums.BusinessType;
import com.farm.common.utils.SecurityUtils;
import com.farm.common.utils.StringUtils;
import com.farm.system.service.ISysDeptService;
import com.farm.system.service.ISysPostService;
import com.farm.system.service.ISysRoleService;
import com.farm.system.service.ISysUserService;
import io.swagger.annotations.Api;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Data
@Api(tags = "buyer注册接口")
public class RegisterController extends BaseController {


    @Autowired
    private ISysUserService userService;

    @Autowired
    private ISysRoleService roleService;

    @Autowired
    private ISysDeptService deptService;

    @Autowired
    private ISysPostService postService;

    @Log(title = "用户管理", businessType = BusinessType.INSERT)
    @PostMapping("/register")
    public AjaxResult register(@Validated @RequestBody SysUser user)
    {

        user.setPhonenumber(user.getUserName());
        user.setNickName(user.getUserName());

        user.setDeptId(UserConstants.BUYER_DEPT);
        user.setRoleIds(new Long[]{UserConstants.BUYER_ROLE});

//        deptService.checkDeptDataScope(user.getDeptId());
//        roleService.checkRoleDataScope(user.getRoleIds());
        if (!userService.checkUserNameUnique(user))
        {
            return error("注册用户'" + user.getUserName() + "'失败，登录账号已存在");
        }
        else if (StringUtils.isNotEmpty(user.getPhonenumber()) && !userService.checkPhoneUnique(user))
        {
            return error("注册用户'" + user.getUserName() + "'失败，手机号码已存在");
        }
        else if (StringUtils.isNotEmpty(user.getEmail()) && !userService.checkEmailUnique(user))
        {
            return error("注册用户'" + user.getUserName() + "'失败，邮箱账号已存在");
        }
        user.setCreateBy(UserConstants.BY_MYSELF);
        user.setPassword(SecurityUtils.encryptPassword(user.getPassword()));
        return toAjax(userService.insertUser(user));
    }

}
