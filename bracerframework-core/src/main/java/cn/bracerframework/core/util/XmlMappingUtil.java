package cn.bracerframework.core.util;

import cn.bracerframework.core.annotation.xml.XMLGroup;
import cn.bracerframework.core.annotation.xml.XMLNode;
import cn.bracerframework.core.annotation.xml.XMLObject;
import cn.bracerframework.core.annotation.xml.XMLRootAttr;
import cn.bracerframework.core.exception.OperationException;
import cn.bracerframework.core.exception.SystemException;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.extra.servlet.ServletUtil;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * XML -> pojo 解析工具类<br>
 * 此工具依赖第三方 dom4j、javax.servlet-api 包
 *
 * @author Dracula
 */
public class XmlMappingUtil {

    /**
     * 从 request 中获取 Document 对象
     *
     * @param request 请求
     * @return Document 对象
     */
    public static Document getDocument(HttpServletRequest request) {
        return getDocument(ServletUtil.getBody(request));
    }

    /**
     * xml 解析成 Document 对象
     *
     * @param xml xml 文本
     * @return Document 对象
     */
    public static Document getDocument(String xml) {
        try {
            return DocumentHelper.parseText(xml);
        } catch (DocumentException e) {
            throw new OperationException("XML 解析失败：" + xml, e);
        }
    }

    /**
     * request 中获取 xml 映射成 pojo<br/>
     * <pre>
     *     提示：
     * 		1、pojo 需要配合注解 {@link XMLObject} {@link XMLGroup} {@link XMLNode}
     *     	2、字段如果使用到内容类，必须使用 static 进行修饰
     *     	3、为防止基础类型默认值干扰，不允许使用基础数据类型
     * </pre>
     *
     * @param request    请求
     * @param xmlMessage 目标 pojo 对象
     * @param <T>        pojo 对象类型
     * @return pojo 映射结果对象
     */
    public static <T> T xml2Pojo(HttpServletRequest request, T xmlMessage) {
        return xml2Pojo(getDocument(request), xmlMessage);
    }

    /**
     * xml 映射成 pojo<br/>
     * <pre>
     *     提示：
     * 		1、pojo 需要配合注解 {@link XMLObject} {@link XMLGroup} {@link XMLNode}
     *     	2、字段如果使用到内容类，必须使用 static 进行修饰
     *     	3、为防止基础类型默认值干扰，不允许使用基础数据类型
     * </pre>
     *
     * @param xml        xml 字符串
     * @param xmlMessage 目标 pojo 对象
     * @param <T>        pojo 对象类型
     * @return pojo 映射结果对象
     */
    public static <T> T xml2Pojo(String xml, T xmlMessage) {
        return xml2Pojo(getDocument(xml), xmlMessage);
    }

    /**
     * Document 映射成 pojo</br>
     * <pre>
     *     提示：
     * 		1、pojo 需要配合注解 {@link XMLObject} {@link XMLGroup} {@link XMLNode}
     *     	2、字段如果使用到内容类，必须使用 static 进行修饰
     *     	3、为防止基础类型默认值干扰，不允许使用基础数据类型
     * </pre>
     *
     * @param doc        Document 对象
     * @param xmlMessage 目标 pojo 对象
     * @param <T>        pojo 对象类型
     * @return pojo 映射结果对象
     */
    public static <T> T xml2Pojo(Document doc, T xmlMessage) {
        Class<?> clazz = xmlMessage.getClass();
        if (AnnotationUtil.getAnnotation(clazz, XMLObject.class) == null) {
            throw new SystemException("【" + clazz.getName() + "】未使用注解 XMLObject 标注为报文解析对象！");
        }

        Element root = doc.getRootElement();
        Object param;
        while (ClassUtil.isNotObject(clazz)) {
            Field[] fields = ClassUtil.getDeclaredFields(clazz);
            for (Field field : fields) {
                if (ClassUtil.isNotStatic(field)) {
                    param = null;

                    if (AnnotationUtil.getAnnotation(field, XMLGroup.class) != null) {
                        param = parserGroup(root, field);
                    } else if (AnnotationUtil.getAnnotation(field, XMLNode.class) != null) {
                        param = parserNode(root, field);
                    }
                    BeanUtil.setFieldValue(xmlMessage, field.getName(), param);
                }
            }
            clazz = clazz.getSuperclass();
        }
        return xmlMessage;
    }

    /**
     * 解析复合节点
     *
     * @param parentEle 父节点
     * @param field     当前字段
     * @return 复合节点映射后的对象
     */
    private static Object parserGroup(Element parentEle, Field field) {
        if (parentEle != null) {
            XMLGroup group = AnnotationUtil.getAnnotation(field, XMLGroup.class);
            if (ClassUtil.isAssignable(List.class, field.getType())) {
                List<Object> list = new ArrayList<>();
                List<Element> elements = getMappingEles(parentEle, group.mapping());

                for (Element element : elements) {
                    if (ClassUtil.isAssignable(Map.class, group.type())) {
                        Map<Object, Object> map = Convert.toMap(Object.class, Object.class, ReflectUtil.newInstance(group.type()));
                        for (Object node : element.elements()) {
                            map.put(((Element) node).getName(), ((Element) node).getText());
                        }
                        list.add(map);
                    } else {
                        list.add(ReflectUtil.newInstance(group.type()));
                        parserGroup(element, list.get(list.size() - 1));
                    }
                }
                return list;
            } else {
                Object param = ReflectUtil.newInstance(field.getType());
                List<Element> elements = getMappingEles(parentEle, group.mapping());
                if (elements.size() > 0) {
                    parserGroup(elements.get(0), param);
                }
                return param;
            }
        }
        return null;
    }

    /**
     * 解析复合节点，内容映射到对象中
     *
     * @param element 当前节点
     * @param obj     映射对象
     */
    private static void parserGroup(Element element, Object obj) {
        if (element != null) {
            Class<?> clazz = obj.getClass();
            Object param;
            while (ClassUtil.isNotObject(clazz)) {
                Field[] fields = ReflectUtil.getFields(clazz);
                for (Field field : fields) {
                    if (ClassUtil.isNotStatic(field)) {
                        param = null;
                        if (AnnotationUtil.getAnnotation(field, XMLGroup.class) != null) {
                            param = parserGroup(element, field);
                        } else if (AnnotationUtil.getAnnotation(field, XMLNode.class) != null) {
                            param = parserNode(element, field);
                        }
                        BeanUtil.setFieldValue(obj, field.getName(), param);
                    }
                }
                clazz = clazz.getSuperclass();
            }
        }
    }

    /**
     * 解析普通节点<br/>
     * 根据类变量所定义的类型，除了Date类型和Number的子类会进行格式转化，其他全都直接进行赋值
     *
     * @param parentEle 父节点
     * @param field     当前字段
     * @return 普通节点映射对象
     */
    private static Object parserNode(Element parentEle, Field field) {
        if (ClassUtil.isPrimitive(field.getType())) {
            throw new SystemException("报文转换对象字段类型不允许使用基础类型，防止基础类型默认值干扰！");
        }
        if (parentEle != null) {
            XMLNode node = AnnotationUtil.getAnnotation(field, XMLNode.class);
            String tip = node.desc() + "【" + node.mapping() + "】";
            List<Element> elements = getMappingEles(parentEle, node.mapping());
            String eleText = "";
            if (elements.size() > 0) {
                Element ele = elements.get(0);
                if (StrUtil.isEmpty(node.attrName())) {
                    eleText = ele.getText();
                } else {
                    eleText = ele.attribute(node.attrName()).getText();
                }
            }
            if (StrUtil.isEmpty(eleText) && node.required()) {
                throw new OperationException(tip + "不能为空");
            }
            if (node.length() > 0 && node.length() < eleText.length()) {
                throw new OperationException(tip + "长度大于指定长度：" + (node.length() - eleText.length()));
            }
            if (ClassUtil.isAssignable(Date.class, field.getType())) {
                return StrUtil.isEmpty(eleText) ? null : DateUtil.parse(eleText, node.format());
            } else if (ClassUtil.isAssignable(Number.class, field.getType())) {
                return StrUtil.isEmpty(eleText) ? null : ReflectUtil.newInstance(field.getType(), eleText);
            } else {
                return eleText;
            }
        }
        return null;
    }

    /**
     * 通过路径获取匹配的所有节点列表
     *
     * @param parentEle   父节点
     * @param mappingPath 匹配路径
     * @return 匹配的所有节点列表
     */
    private static List<Element> getMappingEles(Element parentEle, String mappingPath) {
        String[] mappings = StrUtil.split(mappingPath, ".");
        if (mappings.length > 1) {
            List<Element> elements = new ArrayList<>();
            List<Element> childElement = new ArrayList<>();
            List<Element> tempElement = new ArrayList<>();
            for (String mapping : mappings) {
                if (childElement.size() <= 0) {
                    childElement.addAll(parentEle.elements(mapping));
                } else {
                    for (int i = 0, size = childElement.size(); i < size; i++) {
                        tempElement.addAll(childElement.get(i).elements(mapping));
                    }
                    CollectionUtil.clear(childElement);
                    childElement.addAll(tempElement);
                    CollectionUtil.clear(tempElement);
                }
            }
            elements.addAll(childElement);
            CollectionUtil.clear(childElement);
            return elements;
        } else if (mappings.length == 1) {
            return parentEle.elements(mappingPath);
        }
        return new ArrayList<>();
    }

    /**
     * pojo 对象映射 xml</br>
     * <pre>
     *     提示：
     * 		1、pojo 需要配合注解 {@link XMLObject} {@link XMLGroup} {@link XMLNode} {@link XMLRootAttr}
     *     	2、空值会转换为空节点
     * </pre>
     *
     * @param xmlMessage 报文解析对象
     * @return 映射结果 xml，不带 xml 头
     */
    public static <T> String pojo2Xml(T xmlMessage) {
        return pojo2Xml(xmlMessage, false);
    }

    /**
     * pojo 对象映射 xml</br>
     * <pre>
     *     提示：
     * 		1、pojo 需要配合注解 {@link XMLObject} {@link XMLGroup} {@link XMLNode} {@link XMLRootAttr}
     *     	2、空值会转换为空节点
     * </pre>
     *
     * @param xmlMessage    报文解析对象
     * @param showXmlHeader 是否携带 xml 头
     * @return 映射结果 xml
     */
    public static <T> String pojo2Xml(T xmlMessage, boolean showXmlHeader) {
        Class<?> clazz = xmlMessage.getClass();
        if (AnnotationUtil.getAnnotation(clazz, XMLObject.class) == null) {
            throw new SystemException("【" + clazz.getName() + "】未使用注解 XMLObject 标注为报文解析对象！");
        }
        XMLObject xmlObject = AnnotationUtil.getAnnotation(clazz, XMLObject.class);
        Document doc = DocumentHelper.createDocument();
        Element root = doc.addElement(xmlObject.rootName());

        while (ClassUtil.isNotObject(clazz)) {
            Field[] fields = ClassUtil.getDeclaredFields(clazz);
            for (Field field : fields) {
                if (ClassUtil.isNotStatic(field)) {
                    if (AnnotationUtil.getAnnotation(field, XMLRootAttr.class) != null) {
                        addAttribute(root, xmlMessage, field);
                    } else if (AnnotationUtil.getAnnotation(field, XMLGroup.class) != null) {
                        appendGroup(root, xmlMessage, field);
                    } else if (AnnotationUtil.getAnnotation(field, XMLNode.class) != null) {
                        appendNode(root, xmlMessage, field);
                    }
                }
            }
            clazz = clazz.getSuperclass();
        }
        if (showXmlHeader) {
            return doc.asXML();
        } else {
            return doc.getRootElement().asXML();
        }
    }

    /**
     * 添加节点属性
     *
     * @param ele   待添加节点
     * @param obj   当前对象
     * @param field 当前字段
     */
    private static void addAttribute(Element ele, Object obj, Field field) {
        if (ClassUtil.isPrimitive(field.getType())) {
            throw new SystemException("报文转换对象字段类型不允许使用基础类型，防止基础类型默认值干扰！");
        }
        XMLRootAttr node = AnnotationUtil.getAnnotation(field, XMLRootAttr.class);
        if (node != null) {
            String tip = "根节点属性【" + node.desc() + "】";
            Object param = BeanUtil.getFieldValue(obj, field.getName());
            String nodeText = "";
            if (ObjectUtil.isNotEmpty(param)) {
                if (ClassUtil.isAssignable(Date.class, field.getType())) {
                    nodeText = DateUtil.format((Date) param, node.format());
                } else {
                    nodeText = param.toString();
                }
            }
            if (StrUtil.isEmpty(nodeText) && node.required()) {
                throw new OperationException(tip + "不能为空");
            }
            if (node.length() > 0 && node.length() < nodeText.length()) {
                throw new OperationException(tip + "长度大于指定长度：" + (node.length() - nodeText.length()));
            }
            ele.addAttribute(node.attrName(), nodeText);
        }
    }

    /**
     * 添加复合节点
     *
     * @param parentEle 父节点
     * @param obj       当前对象
     * @param field     当前字段
     */
    private static void appendGroup(Element parentEle, Object obj, Field field) {
        Object param = BeanUtil.getFieldValue(obj, field.getName());
        XMLGroup group = AnnotationUtil.getAnnotation(field, XMLGroup.class);
        if (StrUtil.isEmptyIfStr(param)) {
            String[] mappings = StrUtil.split(group.mapping(), ".");
            for (String mapping : mappings) {
                if (parentEle.element(mapping) == null) {
                    parentEle = parentEle.addElement(mapping);
                } else {
                    parentEle = parentEle.element(mapping);
                }
            }
            return;
        }
        List<Element> elements;
        if (ClassUtil.isAssignable(List.class, field.getType())) {
            elements = addMappingEles(parentEle, group.mapping(), Convert.toList(param).size());
        } else {
            elements = addMappingEles(parentEle, group.mapping(), 1);
        }

        if (ClassUtil.isAssignable(List.class, field.getType())) {
            List<?> temp = Convert.toList(param);
            for (int i = 0, size = temp.size(); i < size; i++) {
                if (ClassUtil.isAssignable(Map.class, group.type())) {
                    Map<Object, Object> mapTemp = Convert.toMap(Object.class, Object.class, temp.get(i));
                    for (Object key : mapTemp.keySet()) {
                        Object value = mapTemp.get(key);
                        String nodeText;
                        if (ClassUtil.isAssignable(Date.class, value.getClass())) {
                            nodeText = DateUtil.format((Date) value, "yyyy-MM-dd HH:mm:ss");
                        } else {
                            nodeText = value.toString();
                        }
                        elements.get(i).addElement(key.toString()).setText(nodeText);
                    }
                } else {
                    checkAnnotationAndAppend(elements.get(i), temp.get(i));
                }
            }
        } else {
            checkAnnotationAndAppend(elements.get(0), param);
        }
    }

    /**
     * 验证注解类型并进行节点添加
     *
     * @param parentEle 父节点
     * @param obj       当前对象
     */
    private static void checkAnnotationAndAppend(Element parentEle, Object obj) {
        Class<?> clazz = obj.getClass();
        while (ClassUtil.isNotObject(clazz)) {
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                if (ClassUtil.isNotStatic(field)) {
                    if (AnnotationUtil.getAnnotation(field, XMLGroup.class) != null) {
                        appendGroup(parentEle, obj, field);
                    } else if (AnnotationUtil.getAnnotation(field, XMLNode.class) != null) {
                        appendNode(parentEle, obj, field);
                    }
                }
            }
            clazz = clazz.getSuperclass();
        }
    }

    /**
     * 添加叶子节点
     *
     * @param parentEle 父节点
     * @param obj       当前对象
     * @param field     当前字段
     */
    private static void appendNode(Element parentEle, Object obj, Field field) {
        if (ClassUtil.isPrimitive(field.getType())) {
            throw new SystemException("报文转换对象字段类型不允许使用基础类型，防止基础类型默认值干扰！");
        }
        String nodeText = "";
        XMLNode node = AnnotationUtil.getAnnotation(field, XMLNode.class);
        String tip = node.desc() + "【" + node.mapping() + "】";
        Object param = BeanUtil.getFieldValue(obj, field.getName());
        Element ele = null;
        Element attr = null;
        if (StrUtil.isEmpty(node.attrName())) {
            ele = addMappingEles(parentEle, node.mapping(), 1).get(0);
        } else {
            List<Element> mappingEles = getMappingEles(parentEle, node.mapping());
            if (mappingEles.size() > 0) {
                attr = mappingEles.get(0);
            }
        }

        if (ObjectUtil.isNotEmpty(param)) {
            if (ClassUtil.isAssignable(Date.class, field.getType())) {
                nodeText = DateUtil.format((Date) param, node.format());
            } else {
                nodeText = param.toString();
            }
        }
        if (StrUtil.isEmpty(nodeText) && node.required()) {
            throw new OperationException(tip + "不能为空");
        }
        if (node.length() > 0 && node.length() < nodeText.length()) {
            throw new OperationException(tip + "长度大于指定长度：" + (node.length() - nodeText.length()));
        }
        if (StrUtil.isNotEmpty(nodeText)) {
            if (ele != null) {
                ele.setText(nodeText);
            } else if (attr != null) {
                attr.addAttribute(node.attrName(), nodeText);
            }
        }
    }

    /**
     * 通过路径添加节点
     *
     * @param parentEle   父节点
     * @param mappingPath 匹配路径
     * @param len         添加数量
     */
    private static List<Element> addMappingEles(Element parentEle, String mappingPath, int len) {
        List<Element> temp = new ArrayList<>();
        String[] mappings = StrUtil.split(mappingPath, ".");
        Element tempElement = null;
        if (mappings.length > 1) {
            for (int i = 0; i < mappings.length; i++) {
                if (tempElement == null) {
                    tempElement = parentEle;
                }
                if (tempElement.element(mappings[i]) != null) {
                    if (i + 1 == mappings.length) {
                        if (len == 1) {
                            tempElement = tempElement.element(mappings[i]);
                        } else {
                            tempElement = tempElement.addElement(mappings[i]);
                        }
                        break;
                    } else {
                        tempElement = tempElement.element(mappings[i]);
                    }
                    continue;
                }
                if (i + 1 == mappings.length) {
                    for (int j = 0; j < len; j++) {
                        temp.add(tempElement.addElement(mappings[i]));
                    }
                    break;
                } else {
                    tempElement = tempElement.addElement(mappings[i]);
                }
            }
        } else {
            for (int i = 0; i < len; i++) {
                temp.add(parentEle.addElement(mappingPath));
            }
        }
        return temp;
    }

}