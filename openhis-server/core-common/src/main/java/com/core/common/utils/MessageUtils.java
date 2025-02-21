package com.core.common.utils;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

import com.core.common.utils.spring.SpringUtils;
import org.springframework.lang.Nullable;

import java.util.Locale;

/**
 * 获取i18n资源文件
 * 
 * @author system
 */
public class MessageUtils {
    /**
     * 根据消息键和参数 获取消息 委托给spring messageSource
     *
     * @param code 消息键
     * @param args 参数
     * @return 获取国际化翻译值
     */
    public static String message(String code, Object... args) {
        MessageSource messageSource = SpringUtils.getBean(MessageSource.class);
        return messageSource.getMessage(code, args, LocaleContextHolder.getLocale());
    }

    public static String createMessage(String id, @Nullable Object[] params) {
        MessageSource messageSource = (MessageSource) SpringContextUtils.getApplicationContext().getBean(MessageSource.class);
        String messageConvert = messageSource.getMessage(id, params, Locale.getDefault());
        return messageConvert;
    }
}
