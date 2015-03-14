package org.jasm.item.clazz;

import java.util.List;

import org.jasm.item.attribute.Attribute;
import org.jasm.item.attribute.Attributes;
import org.jasm.item.attribute.IAttributeContent;

public interface IAttributesContainer {
	
	public Attributes getAttributes();
	
	public static <T extends IAttributeContent> T getUniqueAttributeContentCreatingIfNecessary(Class<T> clazz, IAttributesContainer container) {
		List<Attribute> attributes = container.getAttributes().getAttributesByContentType(clazz);
		if (attributes.size() == 0) {
			Attribute attr = new Attribute();
			container.getAttributes().add(attr);
			IAttributeContent content;
			try {
				content = clazz.newInstance();
				attr.setContent(content);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
			return (T)content;
		} else if (attributes.size() == 1) {
			return (T)attributes.get(0).getContent();
		} else {
			throw new IllegalStateException("multiple attributes with content type: "+clazz.getName());
		}
	}

}
