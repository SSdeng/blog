package com.zyd.blog.controller;

import com.zyd.blog.plugin.kaptcha.Captcha;
import com.zyd.blog.plugin.kaptcha.GifCaptcha;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;

/**
 * 验证码控制类
 *
 * @author yadong.zhang (yadong.zhang0415(a)gmail.com)
 * @version 1.0
 * @website https://www.zhyd.me
 * @date 2018/4/24 14:37
 * @since 1.0
 */
@Slf4j
@Controller
public class KaptchaController {

    /**
     * 得到验证码
     */
    @GetMapping("/getKaptcha")
    @ResponseBody
    public void getKaptcha(HttpServletResponse response) {
        try {
            // Pragma 是一个在 HTTP/1.0 中规定的通用首部，这个首部的效果依赖于不同的实现，所以在“请求-响应”链中可能会有不同的效果。
            response.setHeader("Pragma", "No-cache");
            // 由于 Pragma 在 HTTP 响应中的行为没有确切规范，所以不能可靠替代 HTTP/1.1 中通用首部 Cache-Control，
            // 尽管在请求中，假如 Cache-Control 不存在的话，它的行为与 Cache-Control: no-cache 一致。
            // 建议只在需要兼容 HTTP/1.0 客户端的场合下应用 Pragma 首部。
            response.setHeader("Cache-Control", "no-cache");
            // Expires是RFC 2616（HTTP/1.0）协议中和网页缓存相关字段。用来控制缓存的失效日期，
            // 要注意的是，HTTP/1.0有一个功能比较弱的缓存控制机制：Pragma，使用HTTP/1.0的缓存将忽略Expires和Cache-Control头。
            response.setDateHeader("Expires", 0);
            response.setContentType("image/gif");
            /**
             * gif格式动画验证码
             * 宽，高，位数。
             */
            Captcha captcha = new GifCaptcha(146,33,4);
            //输出
            captcha.out(response.getOutputStream());
        } catch (Exception e) {
            e.printStackTrace();
            log.error("获取验证码异常：{}", e.getMessage());
        }
    }

}
