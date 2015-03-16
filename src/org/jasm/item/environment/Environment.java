package org.jasm.item.environment;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class Environment {
	
	private static final ThreadLocal<Properties> props = new ThreadLocal<Properties>();
	
	
	
	public static void setStringValue(String name, String value) {
		getProperties().put(name, value);
	}
	
	public static String getStringValue(String name) {
		if (getProperties().containsKey(name)) {
		   return (String)getProperties().get(name);
		} else {
		   throw new RuntimeException("unknown environment variable: "+name);
		}
	}
	
	public static void setBooleanValue(String name, boolean value) {
		getProperties().put(name, ""+value);
	}
	
	public static boolean getBooleanValue(String name) {
		if (getProperties().containsKey(name)) {
		   return Boolean.parseBoolean((String)getProperties().get(name));
		} else {
		   throw new RuntimeException("unknown environment variable: "+name);
		}
	}
	
	public static void setIntValue(String name, int value) {
		getProperties().put(name, ""+value);
	}
	
	public static int getIntValue(String name) {
		if (getProperties().containsKey(name)) {
		   return Integer.parseInt((String)getProperties().get(name));
		} else {
		   throw new RuntimeException("unknown environment variable: "+name);
		}
	}
	
	public static void initFrom(Properties props) {
		getProperties().putAll(props);
	}
	
	public List<String> getVariableNames() {
		List<String> result = new ArrayList<String>();
		for (Object k: createDefaultProperties().keySet()) {
			result.add((String)k);
		}
		
		return result;
	}
	
	public static Properties getContent() {
		Properties result = new Properties();
		result.putAll(getProperties());
		return result;
	}
	
	private static Properties getProperties() {
		Properties result = props.get();
		if (result == null) {
			props.set(createDefaultProperties());
		}
		
		return props.get();
	}
	
	private static Properties createDefaultProperties() {
		Properties result = new Properties();
		try {
			InputStream inp = Thread.currentThread().getContextClassLoader().getResourceAsStream("org/jasm/item/environment/default.conf");
			result.load(inp);
		} catch (Exception e) {
			throw new RuntimeException("Error loading default environment",e);
		}
		
		return result;
	}

}
