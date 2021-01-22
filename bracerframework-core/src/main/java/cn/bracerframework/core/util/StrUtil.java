package cn.bracerframework.core.util;

import cn.hutool.core.util.ReUtil;
import cn.hutool.extra.template.TemplateConfig;
import cn.hutool.extra.template.TemplateUtil;

/**
 * 字符串工具类
 * 扩展自 {@link cn.hutool.core.util.StrUtil}
 *
 * @author Lifeng.Lin
 */
public class StrUtil extends cn.hutool.core.util.StrUtil {

    /**
     * 是否存在空字符串
     *
     * @param params 字符串对象
     * @return 是否存在空字符串
     */
    public static boolean haveOneEmpty(String... params) {
        for (String str : params) {
            if (StrUtil.isEmpty(str)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 去除所有不可见字符
     *
     * @param str 待处理文本
     * @return 处理后的字符串
     */
    public static String removeAllNonPrintable(CharSequence str) {
        return ReUtil.replaceAll(str, "\\s", "");
    }

    /**
     * 替换连续多个不可见字符为一个空格
     *
     * @param str 待处理文本
     * @return 处理后的字符串
     */
    public static String formatOneLineOneSpace(CharSequence str) {
        return trim(ReUtil.replaceAll(ReUtil.replaceAll(str, "\\s", " "), "\\s{2,}", " "));
    }

    /**
     * 将换行符转换为指定文本
     *
     * @param str                 待处理文本
     * @param replacementTemplate 目标文本
     * @return 处理后的字符串
     */
    public static String replaceAllLine(CharSequence str, String replacementTemplate) {
        return ReUtil.replaceAll(str, "(\r\n|\n\r|\r|\n)", replacementTemplate);
    }

    /**
     * 格式化文本，使用 {varName} 占位<br>
     * bean = {a: "aValue", b: "bValue"} format("{a} and {b}", map) ---=》 aValue and bValue
     *
     * @param template 文本模板，被替换的部分用 {key} 表示
     * @param bean     参数对象
     * @return 格式化后的文本
     */
    public static String format(CharSequence template, Object bean) {
        return format(template, BeanUtil.beanToMap(bean));
    }

    /**
     * 通过模板引擎格式化文本
     *
     * @param content 文本内容
     * @param bean    参数对象
     * @return 格式化后的文本
     */
    public static String renderTemplate(String content, Object bean) {
        return TemplateUtil.createEngine().getTemplate(content).render(BeanUtil.beanToMap(bean));
    }

    /**
     * 通过模板引擎格式化文本
     *
     * @param path 模板路径
     * @param bean 参数对象
     * @return 格式化后的文本
     */
    public static String renderClassPathTemplate(String path, Object bean) {
        return TemplateUtil.createEngine(new TemplateConfig("/", TemplateConfig.ResourceMode.CLASSPATH)).getTemplate(path).render(BeanUtil.beanToMap(bean));
    }

    /**
     * 大写首字母<br>
     * 例如：str = name, return Name
     *
     * @param str        字符串
     * @param lowerOther 其他是否小写
     * @return 字符串
     */
    public static String upperFirst(CharSequence str, boolean lowerOther) {
        if (lowerOther) {
            if (null == str) {
                return null;
            }
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < str.length(); i++) {
                char cr = str.charAt(i);
                if (i == 0) {
                    if (Character.isLowerCase(cr)) {
                        sb.append(Character.toUpperCase(cr));
                    } else {
                        sb.append(cr);
                    }
                } else {
                    sb.append(Character.toLowerCase(cr));
                }
            }
            return sb.toString();
        } else {
            return upperFirst(str);
        }
    }

}