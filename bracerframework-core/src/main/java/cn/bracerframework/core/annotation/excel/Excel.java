package cn.bracerframework.core.annotation.excel;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Excel 列映射pojo注解
 *
 * @author Dracula
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface Excel {

    /**
     * @return 列下标
     */
    int index();

    /**
     * @return 表头值
     */
    String headerName();

    /**
     * @return 是否必须
     */
    boolean required() default false;

    /**
     * @return 是否除去头尾部的空白
     */
    boolean trim() default true;

}