package cn.bracerframework.core.util;

import cn.hutool.core.util.CharsetUtil;

/**
 * 随机工具类
 * 扩展自 {@link cn.hutool.core.util.RandomUtil}
 *
 * @author Dracula
 */
public class RandomUtil extends cn.hutool.core.util.RandomUtil {

    /**
     * 生成随机汉字
     *
     * @param charset 字符编码
     * @return 返回随机汉字
     */
    public static String randomCnChar(String charset) {
        byte[] b = new byte[2];
        // 计算高位数
        b[0] = new Integer(176 + Math.abs(randomInt(39))).byteValue();
        // 计算低位数
        b[1] = new Integer(161 + Math.abs(randomInt(93))).byteValue();
        return new String(b, CharsetUtil.charset(charset));
    }

}
