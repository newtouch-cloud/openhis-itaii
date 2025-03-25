package com.core.framework.web.service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.core.common.constant.CacheConstants;
import com.core.common.constant.Constants;
import com.core.common.constant.UserConstants;
import com.core.common.core.domain.entity.SysUser;
import com.core.common.core.domain.model.LoginUser;
import com.core.common.core.redis.RedisCache;
import com.core.common.exception.ServiceException;
import com.core.common.exception.user.*;
import com.core.common.utils.DateUtils;
import com.core.common.utils.MessageUtils;
import com.core.common.utils.StringUtils;
import com.core.common.utils.ip.IpUtils;
import com.core.framework.manager.AsyncManager;
import com.core.framework.manager.factory.AsyncFactory;
import com.core.framework.security.context.AuthenticationContextHolder;
import com.core.system.service.ISysConfigService;
import com.core.system.service.ISysUserService;

/**
 * 登录校验方法
 * 
 * @author system
 */
@Component
public class SysLoginService {
    @Autowired
    private TokenService tokenService;

    @Resource
    private AuthenticationManager authenticationManager;

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private ISysUserService userService;

    @Autowired
    private ISysConfigService configService;

    /**
     * 登录验证
     * 
     * @param username 用户名
     * @param password 密码
     * @param code 验证码
     * @param uuid 唯一标识
     * @return 结果
     */
    public String login(String username, String password, String code, String uuid) {
        // 验证码校验
        // validateCaptcha(username, code, uuid);
        // 登录前置校验
        loginPreCheck(username, password);
        // 用户验证
        Authentication authentication = null;
        try {
            UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(username, password);
            AuthenticationContextHolder.setContext(authenticationToken);
            // 该方法会去调用UserDetailsServiceImpl.loadUserByUsername
            authentication = authenticationManager.authenticate(authenticationToken);
        } catch (Exception e) {
            if (e instanceof BadCredentialsException) {
                AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL,
                    MessageUtils.message("user.password.not.match")));
                throw new UserPasswordNotMatchException();
            } else {
                AsyncManager.me()
                    .execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, e.getMessage()));
                throw new ServiceException(e.getMessage());
            }
        } finally {
            AuthenticationContextHolder.clearContext();
        }
        AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_SUCCESS,
            MessageUtils.message("user.login.success")));
        LoginUser loginUser = (LoginUser)authentication.getPrincipal();

        // -----start-----登录时set租户id,正常应该从请求头获取,这行代码只是测试使用
        Integer tenantId = 0;
        ServletRequestAttributes attributes = (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();
            // 从请求头获取租户ID，假设header名称为"X-Tenant-ID" ; 登录接口前端把租户id放到请求头里
            String tenantIdHeader = request.getHeader("X-Tenant-ID");
            if (tenantIdHeader != null && !tenantIdHeader.isEmpty()) {
                tenantId = Integer.parseInt(tenantIdHeader);
            }
        }
        loginUser.setTenantId(tenantId);
        // -----end-----登录时set租户id,正常应该从请求头获取,这行代码只是测试使用
        recordLoginInfo(loginUser.getUserId());
        // 生成token
        return tokenService.createToken(loginUser);
    }

    /**
     * 校验验证码
     * 
     * @param username 用户名
     * @param code 验证码
     * @param uuid 唯一标识
     * @return 结果
     */
    public void validateCaptcha(String username, String code, String uuid) {
        boolean captchaEnabled = configService.selectCaptchaEnabled();
        if (captchaEnabled) {
            String verifyKey = CacheConstants.CAPTCHA_CODE_KEY + StringUtils.nvl(uuid, "");
            String captcha = redisCache.getCacheObject(verifyKey);
            if (captcha == null) {
                AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL,
                    MessageUtils.message("user.jcaptcha.expire")));
                throw new CaptchaExpireException();
            }
            redisCache.deleteObject(verifyKey);
            if (!code.equalsIgnoreCase(captcha)) {
                AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL,
                    MessageUtils.message("user.jcaptcha.error")));
                throw new CaptchaException();
            }
        }
    }

    /**
     * 登录前置校验
     * 
     * @param username 用户名
     * @param password 用户密码
     */
    public void loginPreCheck(String username, String password) {
        // 用户名或密码为空 错误
        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
            AsyncManager.me().execute(
                AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("not.null")));
            throw new UserNotExistsException();
        }
        // 密码如果不在指定范围内 错误
        if (password.length() < UserConstants.PASSWORD_MIN_LENGTH
            || password.length() > UserConstants.PASSWORD_MAX_LENGTH) {
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL,
                MessageUtils.message("user.password.not.match")));
            throw new UserPasswordNotMatchException();
        }
        // 用户名不在指定范围内 错误
        if (username.length() < UserConstants.USERNAME_MIN_LENGTH
            || username.length() > UserConstants.USERNAME_MAX_LENGTH) {
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL,
                MessageUtils.message("user.password.not.match")));
            throw new UserPasswordNotMatchException();
        }
        // IP黑名单校验
        String blackStr = configService.selectConfigByKey("sys.login.blackIPList");
        if (IpUtils.isMatchedIp(blackStr, IpUtils.getIpAddr())) {
            AsyncManager.me().execute(
                AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("login.blocked")));
            throw new BlackListException();
        }
    }

    /**
     * 记录登录信息
     *
     * @param userId 用户ID
     */
    public void recordLoginInfo(Long userId) {
        SysUser sysUser = new SysUser();
        sysUser.setUserId(userId);
        sysUser.setLoginIp(IpUtils.getIpAddr());
        sysUser.setLoginDate(DateUtils.getNowDate());
        userService.updateUserProfile(sysUser);
    }
}
