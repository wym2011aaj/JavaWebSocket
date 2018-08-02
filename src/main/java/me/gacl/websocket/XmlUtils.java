package me.gacl.websocket;

import org.w3c.dom.*;
import org.xml.sax.*;

import javax.xml.parsers.*;
import java.io.*;
import java.util.*;

/**
 * xml操作
 *
 * @author ccs
 * @version 1.0
 */

public final class XmlUtils
{

    public static final String XMLNS_PREFIX = "xmlns";

    public static final String XMLNS_PREFIX1 = "xmlns:";

    private final static EntityResolver ENTITYRESOLVER = new EntityResolver()
    {
        public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException
        {
            return new InputSource(new ByteArrayInputStream(new byte[]{}));
        }
    };

    private XmlUtils()
    {
    }


    public static Map<String, String> xmlToMap(Document document) throws Exception
    {
        Map<String, String> map = new HashMap<>();
        NodeList childNodes = document.getChildNodes().item(0).getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++)
        {
            map.put(childNodes.item(i).getNodeName(), childNodes.item(i).getTextContent());
        }
        return map;
    }

    /**
     * 从六读取数据创建xml文档
     *
     * @param in             输入流
     * @param namespaceAware 是否支持命名空间
     * @return xml文档
     * @throws Exception IO错误或者xml语法错误或者创建文档错误
     */
    public static Document createDocument(InputStream in, boolean namespaceAware) throws Exception
    {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(namespaceAware);
        DocumentBuilder builder = factory.newDocumentBuilder();
        builder.setEntityResolver(ENTITYRESOLVER);
        return builder.parse(in);
    }

    public static Document createDocument(InputStream in) throws Exception
    {
        return createDocument(in, true);
    }

    private static String getNamespacePrefix(String nodeName)
    {
        String prefix = null;

        if (XMLNS_PREFIX.equals(nodeName))
        {
            prefix = "";
        }
        else if (nodeName.startsWith(XMLNS_PREFIX1))
        {
            prefix = nodeName.substring(6);
        }

        return prefix;
    }

    public static String getNamespacePrefix(Element element, String namespace)
    {
        return getNamespacePrefix(element, namespace, new HashSet<String>());
    }

    private static String getNamespacePrefix(Element element, String namespace, Set<String> prefixs)
    {
        NamedNodeMap attributes = element.getAttributes();
        int n = attributes.getLength();
        for (int i = 0; i < n; i++)
        {
            Node item = attributes.item(i);

            if (item instanceof Attr)
            {
                String nodeName = item.getNodeName();
                String prefix = getNamespacePrefix(nodeName);

                if (prefix != null)
                {
                    if (!prefixs.contains(prefix))
                    {
                        if (namespace.equals(item.getNodeValue()))
                            return prefix;
                    }
                    else
                    {
                        prefixs.add(prefix);
                    }
                }
            }
        }

        Node parentNode = element.getParentNode();
        if (parentNode instanceof Document)
            return null;

        if (parentNode instanceof Element)
            return getNamespacePrefix((Element) parentNode, namespace);

        return null;
    }

}