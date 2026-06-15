package com.iljin.apiServer.core.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
public class XmlParser {

    @SuppressWarnings("unchecked")
    public Object parse(MultipartFile message, Class<?> clazz, String header) throws Exception {
        InputStream inputStream = new ByteArrayInputStream(message.getBytes());
        DocumentBuilderFactory factory     = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder     = factory.newDocumentBuilder();
        Document document    = builder.parse(inputStream);

        document.getDocumentElement().normalize();

        NodeList rootNodeList = document.getElementsByTagName(header);
        Map<String, Object> map          = parseChildren(rootNodeList);

        if (log.isDebugEnabled()) {
            printMap((Map<String, Object>) map.get(header));
        }

        inputStream.close();

        Object object = clazz.newInstance();
        return reflectObject(object, clazz, (Map<String, Object>) map.get(header));
    }

    public Object parseRequest(MultipartFile message, Class<?> clazz) throws Exception {
        return parse(message, clazz, "iljin:req");
    }

    public Object parseResponse(MultipartFile message, Class<?> clazz) throws Exception {
        return parse(message, clazz, "iljin:rsp");
    }

    private Map<String, Object> parseChildren(NodeList nodeList) throws Exception {
        Map<String, Object> map = null;
        for (int index = 0; index < nodeList.getLength(); index++) {
            Node node = nodeList.item(index);
            if (Node.ELEMENT_NODE == node.getNodeType()) {
                Element el = (Element) node;
                if (el.getChildNodes().getLength() > 0) {
                    Map<String, Object> children = parseChildren(el.getChildNodes());
                    if (map == null) {
                        map = new LinkedHashMap<>();
                    }
                    if (children == null) {
                        map.put(el.getTagName(), el.getTextContent());
                    } else {
                        map.put(el.getTagName(), children);
                    }
                }
            }
        }
        return map;
    }

    private Object reflectObject(Object object, Class<?> clazz, Map<String, Object> map) throws Exception {
        if (map == null) {
            return null;
        }
        for (String key : map.keySet()) {
            StringBuilder logBuilder = new StringBuilder();
            logBuilder.append(String.format(">>>> %s", key));

            try {
                Field field = clazz.getDeclaredField(key);

                Object  value      = map.get(key);
                boolean accessible = field.isAccessible();
                if (!accessible) {
                    field.setAccessible(true);
                }

                logBuilder.append(String.format("(%s)", field.getType().getName()));

                value = map.get(field.getName());

                if (value != null && value instanceof String) {
                    String str = (String) value;
                    logBuilder.append(String.format(" %s", str));
                    if (field.getType() == String.class) {
                        field.set(object, str);
                    } else if (field.getType() == Integer.class) {
                        field.set(object, new Integer(str));
                    } else if (field.getType() == Date.class) {
                        logBuilder.append(" ");
                        logBuilder
                                .append(str
                                        .replaceAll("^(\\d{4})-(\\d{2})-(\\d{2})T(\\d{2}):(\\d{2}):(\\d{2})\\+(\\d{2}):(\\d{2})$",
                                                "$1-$2-$3 $4:$5:$6 +$7$8"));
                        try {
                            field
                                    .set(object, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z")
                                            .parse(str
                                                    .replaceAll("^(\\d{4})-(\\d{2})-(\\d{2})T(\\d{2}):(\\d{2}):(\\d{2})\\+(\\d{2}):(\\d{2})$",
                                                            "$1-$2-$3 $4:$5:$6 +$7$8")));
                        } catch (Exception e) {
                            logBuilder.append(" ");
                            logBuilder.append(e.getMessage());
                        }
                    }
                } else if (value != null && value instanceof Map) {
                    @SuppressWarnings("unchecked")
                    Map<String, Object> c = (Map<String, Object>) value;
                    field.set(object, reflectObject(field.getType().newInstance(), field.getType(), c));
                } else {
                    logBuilder.append(" is null");
                }

                field.setAccessible(accessible);
            } catch (Exception e) {
                logBuilder.append(String.format(" is not an member of %s", clazz.getName()));
            }

            if (log.isDebugEnabled()) {
                log.debug(logBuilder.toString());
            }
        }
        return object;
    }

    @SuppressWarnings("unchecked")
    private void printMap(Map<String, Object> map, String parent) {
        final String format = "%s%s = %s";
        if (map != null) {
            for (String key : map.keySet()) {
                Object value = map.get(key);
                if (value == null) {
                    System.out.println(String.format(format, parent, key, "null"));
                } else if (value instanceof String) {
                    System.out.println(String.format(format, parent, key, value));
                } else if (value instanceof Map) {
                    printMap((Map<String, Object>) value, parent + ("".equals(parent) ? "" : ".") + key);
                }
            }
        }
    }

    private void printMap(Map<String, Object> map) {
        log.info(">>>> printMap");
        printMap(map, "");
    }
}
