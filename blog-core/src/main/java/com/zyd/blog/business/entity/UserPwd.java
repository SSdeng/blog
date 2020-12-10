package com.zyd.blog.business.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

/**
 * @author yadong.zhang (yadong.zhang0415(a)gmail.com)
 * @version 1.0
 * @website https://www.zhyd.me
 * @date 2018/6/6 16:34
 * @since 1.0
 *
 *
 * Date注解相当于@Getter @Setter @RequiredArgsConstructor @ToString @EqualsAndHashCode这5个注解的合集。
 * 让其生成的方法中调用父类的方法。
 * 当parent是Object（即无parent）时，不要使用@EqualsAndHashCode注解（也就是默认值false），当parent是自定义类时，应该使用true
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class UserPwd {
    @NotNull(message = "用户ID不可为空")
    private Long id;
    @NotNull(message = "原密码不可为空")
    private String password;
    @NotNull(message = "新密码不可为空")
    @Length(max = 20, min = 6, message = "新密码长度建议保持在6~20个字符以内")
    private String newPassword;
    @NotNull(message = "新密码不可为空")
    @Length(max = 20, min = 6, message = "新密码长度建议保持在6~20个字符以内")
    private String newPasswordRepeat;
}
