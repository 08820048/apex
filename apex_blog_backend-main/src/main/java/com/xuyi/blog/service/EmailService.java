package com.xuyi.blog.service;

import com.xuyi.blog.entity.Article;
import com.xuyi.blog.entity.EmailSubscriber;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.util.List;

/**
 * 邮件服务类
 * 
 * @author xuyi
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Value("${blog.title:Xuyi's Blog}")
    private String blogTitle;

    @Value("${blog.url:http://localhost:8888}")
    private String blogUrl;

    /**
     * 发送简单邮件
     */
    @Async
    public void sendSimpleEmail(String to, String subject, String content) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(to);
            message.setSubject(subject);
            message.setText(content);
            
            mailSender.send(message);
            log.info("发送邮件成功: {} -> {}", fromEmail, to);
        } catch (Exception e) {
            log.error("发送邮件失败: {} -> {}, 错误: {}", fromEmail, to, e.getMessage());
        }
    }

    /**
     * 发送HTML邮件
     */
    @Async
    public void sendHtmlEmail(String to, String subject, String htmlContent) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            
            helper.setFrom(fromEmail);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlContent, true);
            
            mailSender.send(message);
            log.info("发送HTML邮件成功: {} -> {}", fromEmail, to);
        } catch (MessagingException e) {
            log.error("发送HTML邮件失败: {} -> {}, 错误: {}", fromEmail, to, e.getMessage());
        }
    }

    /**
     * 发送订阅确认邮件
     */
    @Async
    public void sendSubscriptionConfirmationEmail(EmailSubscriber subscriber) {
        String subject = "欢迎订阅 " + blogTitle;
        String content = buildSubscriptionConfirmationContent(subscriber);
        sendHtmlEmail(subscriber.getEmail(), subject, content);
    }

    /**
     * 发送新文章通知邮件
     */
    @Async
    public void sendNewArticleNotification(List<EmailSubscriber> subscribers, Article article) {
        String subject = blogTitle + " - 新文章发布：" + article.getTitle();
        String content = buildNewArticleNotificationContent(article);
        
        for (EmailSubscriber subscriber : subscribers) {
            if (subscriber.getIsActive()) {
                String personalizedContent = content.replace("{{unsubscribe_url}}", 
                    buildUnsubscribeUrl(subscriber.getToken()));
                sendHtmlEmail(subscriber.getEmail(), subject, personalizedContent);
            }
        }
        
        log.info("发送新文章通知邮件: {} 篇文章，{} 个订阅者", article.getTitle(), subscribers.size());
    }

    /**
     * 发送取消订阅确认邮件
     */
    @Async
    public void sendUnsubscribeConfirmationEmail(String email) {
        String subject = "取消订阅确认 - " + blogTitle;
        String content = buildUnsubscribeConfirmationContent();
        sendHtmlEmail(email, subject, content);
    }

    /**
     * 构建订阅确认邮件内容
     */
    private String buildSubscriptionConfirmationContent(EmailSubscriber subscriber) {
        return String.format("""
            <!DOCTYPE html>
            <html lang="zh-CN">
            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <title>欢迎订阅 %s</title>
            </head>
            <body style="margin: 0; padding: 0; background-color: #f4f4f4; font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, 'Helvetica Neue', Arial, sans-serif;">
                <table cellpadding="0" cellspacing="0" border="0" width="100%%" style="background-color: #f4f4f4; padding: 20px 0;">
                    <tr>
                        <td align="center">
                            <table cellpadding="0" cellspacing="0" border="0" width="600" style="max-width: 600px; background-color: #ffffff; border-radius: 12px; box-shadow: 0 4px 12px rgba(0,0,0,0.1); overflow: hidden;">
                                <!-- Header -->
                                <tr>
                                    <td style="background: linear-gradient(135deg, #667eea 0%%, #764ba2 100%%); padding: 40px 30px; text-align: center;">
                                        <h1 style="color: #ffffff; font-size: 28px; font-weight: 600; margin: 0; text-shadow: 0 2px 4px rgba(0,0,0,0.1);">
                                            🎉 欢迎订阅 %s！
                                        </h1>
                                    </td>
                                </tr>

                                <!-- Content -->
                                <tr>
                                    <td style="padding: 40px 30px;">
                                        <div style="text-align: center; margin-bottom: 30px;">
                                            <div style="width: 80px; height: 80px; background: linear-gradient(135deg, #667eea 0%%, #764ba2 100%%); border-radius: 50%%; margin: 0 auto 20px; display: flex; align-items: center; justify-content: center; font-size: 36px;">
                                                ✉️
                                            </div>
                                        </div>

                                        <h2 style="color: #2c3e50; font-size: 24px; font-weight: 600; margin: 0 0 20px; text-align: center;">
                                            订阅成功！
                                        </h2>

                                        <p style="color: #555; font-size: 16px; line-height: 1.6; margin: 0 0 20px; text-align: center;">
                                            亲爱的读者，感谢您订阅我们的博客！
                                        </p>

                                        <div style="background: #f8f9fa; border-left: 4px solid #667eea; padding: 20px; margin: 20px 0; border-radius: 0 8px 8px 0;">
                                            <p style="color: #555; font-size: 16px; line-height: 1.6; margin: 0;">
                                                📚 您将第一时间收到最新文章的推送通知<br>
                                                🎯 精选优质内容，绝不浪费您的时间<br>
                                                🔒 我们承诺保护您的隐私，不会泄露您的邮箱
                                            </p>
                                        </div>

                                        <div style="text-align: center; margin: 30px 0;">
                                            <a href="%s" style="display: inline-block; background: linear-gradient(135deg, #667eea 0%%, #764ba2 100%%); color: #ffffff; text-decoration: none; padding: 14px 30px; border-radius: 25px; font-weight: 600; font-size: 16px; box-shadow: 0 4px 12px rgba(102, 126, 234, 0.3); transition: all 0.3s ease;">
                                                🏠 访问博客首页
                                            </a>
                                        </div>

                                        <div style="background: #fff3cd; border: 1px solid #ffeaa7; border-radius: 8px; padding: 15px; margin: 20px 0;">
                                            <p style="color: #856404; font-size: 14px; margin: 0; text-align: center;">
                                                💡 <strong>小贴士：</strong>为了确保您能收到我们的邮件，请将此邮箱地址添加到您的通讯录中。
                                            </p>
                                        </div>
                                    </td>
                                </tr>

                                <!-- Footer -->
                                <tr>
                                    <td style="background-color: #f8f9fa; padding: 30px; text-align: center; border-top: 1px solid #e9ecef;">
                                        <p style="color: #6c757d; font-size: 14px; margin: 0 0 10px;">
                                            如果您不想再收到此类邮件，可以
                                            <a href="%s" style="color: #dc3545; text-decoration: none; font-weight: 500;">取消订阅</a>
                                        </p>
                                        <p style="color: #6c757d; font-size: 12px; margin: 0;">
                                            此邮件由 <strong>%s</strong> 自动发送，请勿回复<br>
                                            博客地址：<a href="%s" style="color: #667eea; text-decoration: none;">%s</a>
                                        </p>
                                    </td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                </table>
            </body>
            </html>
            """, blogTitle, blogTitle, blogUrl, buildUnsubscribeUrl(subscriber.getToken()),
                 blogTitle, blogUrl, blogUrl);
    }

    /**
     * 构建新文章通知邮件内容
     */
    private String buildNewArticleNotificationContent(Article article) {
        String summary = StringUtils.hasText(article.getSummary()) ? 
                        article.getSummary() : 
                        (article.getContent().length() > 200 ? 
                         article.getContent().substring(0, 200) + "..." : 
                         article.getContent());

        return String.format("""
            <!DOCTYPE html>
            <html lang="zh-CN">
            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <title>新文章发布 - %s</title>
            </head>
            <body style="margin: 0; padding: 0; background-color: #f4f4f4; font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, 'Helvetica Neue', Arial, sans-serif;">
                <table cellpadding="0" cellspacing="0" border="0" width="100%%" style="background-color: #f4f4f4; padding: 20px 0;">
                    <tr>
                        <td align="center">
                            <table cellpadding="0" cellspacing="0" border="0" width="600" style="max-width: 600px; background-color: #ffffff; border-radius: 12px; box-shadow: 0 4px 12px rgba(0,0,0,0.1); overflow: hidden;">
                                <!-- Header -->
                                <tr>
                                    <td style="background: linear-gradient(135deg, #667eea 0%%, #764ba2 100%%); padding: 30px; text-align: center;">
                                        <h1 style="color: #ffffff; font-size: 24px; font-weight: 600; margin: 0; text-shadow: 0 2px 4px rgba(0,0,0,0.1);">
                                            📝 %s - 新文章发布
                                        </h1>
                                    </td>
                                </tr>

                                <!-- Content -->
                                <tr>
                                    <td style="padding: 40px 30px;">
                                        <div style="text-align: center; margin-bottom: 30px;">
                                            <div style="width: 60px; height: 60px; background: linear-gradient(135deg, #667eea 0%%, #764ba2 100%%); border-radius: 50%%; margin: 0 auto 15px; display: flex; align-items: center; justify-content: center; font-size: 24px;">
                                                ✨
                                            </div>
                                            <p style="color: #666; font-size: 14px; margin: 0;">有新内容等您阅读</p>
                                        </div>

                                        <h2 style="color: #2c3e50; font-size: 22px; font-weight: 600; margin: 0 0 20px; text-align: center; line-height: 1.3;">
                                            <a href="%s/articles/%d" style="color: #2c3e50; text-decoration: none;">%s</a>
                                        </h2>

                                        <div style="background: #f8f9fa; border-left: 4px solid #667eea; padding: 20px; margin: 25px 0; border-radius: 0 8px 8px 0;">
                                            <p style="color: #555; font-size: 15px; line-height: 1.6; margin: 0;">
                                                %s
                                            </p>
                                        </div>

                                        <div style="text-align: center; margin: 35px 0;">
                                            <a href="%s/articles/%d" style="display: inline-block; background: linear-gradient(135deg, #667eea 0%%, #764ba2 100%%); color: #ffffff; text-decoration: none; padding: 16px 32px; border-radius: 25px; font-weight: 600; font-size: 16px; box-shadow: 0 4px 12px rgba(102, 126, 234, 0.3); transition: all 0.3s ease;">
                                                📖 立即阅读全文
                                            </a>
                                        </div>

                                        <div style="background: #e8f4fd; border: 1px solid #bee5eb; border-radius: 8px; padding: 15px; margin: 25px 0; text-align: center;">
                                            <p style="color: #0c5460; font-size: 14px; margin: 0;">
                                                💡 喜欢这篇文章？别忘了分享给朋友们！
                                            </p>
                                        </div>
                                    </td>
                                </tr>

                                <!-- Footer -->
                                <tr>
                                    <td style="background-color: #f8f9fa; padding: 25px; text-align: center; border-top: 1px solid #e9ecef;">
                                        <p style="color: #6c757d; font-size: 14px; margin: 0 0 10px;">
                                            如果您不想再收到此类邮件，可以
                                            <a href="{{unsubscribe_url}}" style="color: #dc3545; text-decoration: none; font-weight: 500;">取消订阅</a>
                                        </p>
                                        <p style="color: #6c757d; font-size: 12px; margin: 0;">
                                            此邮件由 <strong>%s</strong> 自动发送，请勿回复<br>
                                            博客地址：<a href="%s" style="color: #667eea; text-decoration: none;">%s</a>
                                        </p>
                                    </td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                </table>
            </body>
            </html>
            """, blogTitle, blogTitle, blogUrl, article.getId(), article.getTitle(),
                 summary, blogUrl, article.getId(), blogTitle, blogUrl, blogUrl);
    }

    /**
     * 构建取消订阅确认邮件内容
     */
    private String buildUnsubscribeConfirmationContent() {
        return String.format("""
            <html>
            <body style="font-family: Arial, sans-serif; line-height: 1.6; color: #333;">
                <div style="max-width: 600px; margin: 0 auto; padding: 20px;">
                    <h2 style="color: #2c3e50;">取消订阅成功</h2>
                    <p>您已成功取消订阅 %s。</p>
                    <p>如果这不是您的操作，或者您想重新订阅，请访问我们的网站。</p>
                    <hr style="border: none; border-top: 1px solid #eee; margin: 20px 0;">
                    <p style="font-size: 12px; color: #666;">
                        博客地址：<a href="%s">%s</a>
                    </p>
                </div>
            </body>
            </html>
            """, blogTitle, blogUrl, blogUrl);
    }

    /**
     * 构建取消订阅URL
     */
    private String buildUnsubscribeUrl(String token) {
        return blogUrl + "/api/unsubscribe?token=" + token;
    }
}
