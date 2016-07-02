/*
 * Copyright (C) 2004-2014 L2J Server
 * 
 * This file is part of L2J Server.
 * 
 * L2J Server is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * L2J Server is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.l2jserver.xml;

import java.io.File;
import java.util.List;

import javax.xml.transform.Transformer;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * @author UnAfraid
 */
public class DOMXmlWriterImpl extends AbstractXMLWriter
{
	@Override
	public void processDocument(File dest, XMLDocument xDoc) throws Exception
	{
		final Document doc = XMLFactory.newDocument();
		
		// Build the document
		processElements(doc, doc, xDoc.getEntries());
		
		// Transformer
		final Transformer transformer = XMLFactory.newTransformer();
		transformer.transform(new DOMSource(doc), new StreamResult(dest));
	}
	
	public void processElements(Document doc, Node node, List<XMLElement> entries)
	{
		for (XMLElement entry : entries)
		{
			final Element element = doc.createElement(entry.getName());
			
			// Create attributes
			for (XMLAttribute attr : entry.getAttributes())
			{
				final Attr attribute = doc.createAttribute(attr.getName());
				attribute.setValue(attr.getValue());
				element.setAttributeNode(attribute);
			}
			
			// Create text if there is
			if (entry.getValue() != null)
			{
				element.appendChild(doc.createTextNode(entry.getValue()));
			}
			
			// Append to the node
			node.appendChild(element);
			
			// Process other elements
			processElements(doc, element, entry.getEntries());
		}
	}
}
