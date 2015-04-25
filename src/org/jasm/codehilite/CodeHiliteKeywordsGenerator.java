package org.jasm.codehilite;

import java.io.File;
import java.io.FileReader;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CodeHiliteKeywordsGenerator {
	public static void main(String[] args) {
		File f = new File("grammar/JavaAssembler.g4");
		
		List<List<String>> list = new ArrayList<List<String>>();
		try {
			LineNumberReader reader = new LineNumberReader(new FileReader(f));
			String line = reader.readLine();
			boolean readKeywords = false;
			
			List<String> currentGroup = null;
			while (line != null) {
				line = line.trim();
				
				if (line.length()> 0) {
					if (readKeywords && !line.startsWith("//stopKeywords")) {
						String value;
						try {
							value = line.substring(line.indexOf('\'')+1, line.lastIndexOf('\''));
						} catch (Throwable e) {
							throw new RuntimeException(line,e);
						}
						currentGroup.add(value);
					} else if (line.startsWith("//startKeywords")) {
						readKeywords = true;
						currentGroup = new ArrayList<String>();
						list.add(currentGroup);
						
					} else if (line.startsWith("//stopKeywords")) {
						Collections.sort(currentGroup);
						readKeywords = false;
					} 
				} 
				
				line = reader.readLine();
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		for (List<String> group: list) {
			StringBuffer buf = new StringBuffer();
			buf.append("r'(");
			int index =0;
			for (String v: group) {
				if (index>0) {
					buf.append("|");
				}
				buf.append(v);
				index++;
			}
			buf.append(")\\b'");
			System.out.println(buf.toString());
		}
		
	}
}
