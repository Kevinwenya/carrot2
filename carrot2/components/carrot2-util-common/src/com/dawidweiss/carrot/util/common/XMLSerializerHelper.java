/*
 * Carrot2 Project
 * Copyright (C) 2002-2005, Dawid Weiss
 * All rights reserved.
 *
 * Refer to full text of the license "carrot2.LICENSE" in the root folder
 * of CVS checkout or at:
 * http://www.cs.put.poznan.pl/dweiss/carrot2.LICENSE
 */

package com.dawidweiss.carrot.util.common;


import java.io.IOException;
import java.io.Writer;


/**
 * A utility class for encoding Java String into XML text chunks. Please note that some characters
 * cannot be serialized into an XML text. Refer to <a
 * href="http://www.w3.org/TR/2000/REC-xml-20001006#charsets"> XML specification</a> for more
 * information.
 * 
 * Instances of this class are not guaranteed to be thread-safe. 
 */
public class XMLSerializerHelper
{
    /**
     * A public instance for faster one-time access, <b>this instance is synchronized
     * </b> because it uses a shared memory buffer to copy/ encode.
     */
    public static final XMLSerializerHelper instance = new XMLSerializerHelper() {
        public synchronized void writeValidXmlText(Writer writer, String str, 
            boolean exceptionOnUnescapable)
            throws IOException
        {
            super.writeValidXmlText(writer, str, exceptionOnUnescapable);
        }
        
        public synchronized String toValidXmlText(String str, boolean exceptionOnUnescapable)
        {
            return super.toValidXmlText(str, exceptionOnUnescapable);
        }

    };

    /**
     * Returns an instance of the serializer. The instance is not thread-safe,
     * but can be reused many times (and should be).
     */
    public static XMLSerializerHelper getInstance() {
        return new XMLSerializerHelper();
    }

    /**
     * Use static <code>instance</code> field.
     */
    private XMLSerializerHelper()
    {
    }

    /**
     * Escapes a string so that it can be safely put into an XML text node and writes it to the
     * <code>writer</code>.  Please note that some characters cannot be serialized into an XML
     * text. Refer to <a href="http://www.w3.org/TR/2000/REC-xml-20001006#charsets"> XML
     * specification</a> for more information.
     *
     * @param writer The writer to output the string to.
     * @param str The string to be escaped.
     * @param exceptionOnUnescapable If true, <code>IllegalArgumentException</code> is thrown when
     *        an unescapable sequence of characters is encountered. Otherwise, the offending
     *        characters will be omitted in the output.
     */
    public void writeValidXmlText(Writer writer, String str, boolean exceptionOnUnescapable)
        throws IOException
    {
        writer.write(toValidXmlText(str, exceptionOnUnescapable));
    }


    /**
     * Escapes a string so that it can be safely put into an XML text node. Please note that some
     * characters cannot be serialized into an XML text. Refer to <a
     * href="http://www.w3.org/TR/2000/REC-xml-20001006#charsets"> XML specification</a> for more
     * information.
     *
     * @param str The string to be escaped.
     * @param exceptionOnUnescapable If true, <code>IllegalArgumentException</code> is thrown when
     *        an unescapable sequence of characters is encountered. Otherwise, the offending
     *        characters will be omitted in the output.
     */
    public String toValidXmlText(String str, boolean exceptionOnUnescapable)
    {
        StringBuffer buffer = null;

        for (int i = 0; i < str.length(); i++)
        {
            char ch = str.charAt(i);
            String entity;

            switch (ch)
            {
                case '<': // '<'
                    entity = "&lt;";

                    break;

                case '>': // '>'
                    entity = "&gt;";

                    break;

                case '&': // '&'
                    entity = "&amp;";

                    break;

                case '\'':
                    entity = "&apos;";

                    break;

                case '"':
                    entity = "&quot;";

                    break;

                case 0x09: // valid xml characters
                case 0x0a:
                case 0x0d:
                    entity = null;

                    break;

                default:

                    // check if valid XML characters
                    if (
                        ((ch >= 0x20) && (ch <= 0xD7FF)) || ((ch >= 0xe000) && (ch <= 0xfffd))
                            || ((ch >= 0x10000) && (ch <= 0x10ffff))
                    )
                    {
                        entity = null;

                        break;
                    }
                    else
                    {
                        if (exceptionOnUnescapable)
                        {
                            throw new IllegalArgumentException(
                                "Character is not within valid XML characters (code: 0x"
                                + Integer.toHexString(ch) + ", position: " + i + ")."
                            );
                        }
                        else
                        {
                            // replace the character with an empty string.
                            entity = "";

                            break;
                        }
                    }
            }

            if (buffer == null)
            {
                if (entity != null)
                {
                    buffer = new StringBuffer(str.length() + 20);
                    buffer.append(str.substring(0, i));
                    buffer.append(entity);
                }
            }
            else
            {
                if (entity == null)
                {
                    buffer.append(ch);
                }
                else
                {
                    buffer.append(entity);
                }
            }
        }

        return (buffer != null) ? buffer.toString()
                                : str;
    }
}
