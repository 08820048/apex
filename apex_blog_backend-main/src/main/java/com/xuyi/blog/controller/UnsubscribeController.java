package com.xuyi.blog.controller;

import com.xuyi.blog.service.EmailSubscriberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 取消订阅页面控制器
 * 
 * @author xuyi
 */
@Controller
@RequiredArgsConstructor
@Slf4j
public class UnsubscribeController {

    private final EmailSubscriberService emailSubscriberService;

    @Value("${blog.title:八尺妖剑}")
    private String blogTitle;

    @Value("${blog.url:https://ilikexff.cn}")
    private String blogUrl;

    @GetMapping("/unsubscribe")
    public String unsubscribe(@RequestParam String token, Model model) {
        try {
            emailSubscriberService.unsubscribe(token);
            
            model.addAttribute("success", true);
            model.addAttribute("message", "您已成功取消订阅！");
            model.addAttribute("blogTitle", blogTitle);
            model.addAttribute("blogUrl", blogUrl);
            
            log.info("用户通过页面取消订阅成功，token: {}", token);
        } catch (Exception e) {
            model.addAttribute("success", false);
            model.addAttribute("message", "取消订阅失败：" + e.getMessage());
            model.addAttribute("blogTitle", blogTitle);
            model.addAttribute("blogUrl", blogUrl);
            
            log.error("用户取消订阅失败，token: {}, 错误: {}", token, e.getMessage());
        }
        
        return "unsubscribe";
    }
}
