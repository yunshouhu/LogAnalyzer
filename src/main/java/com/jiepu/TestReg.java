package com.jiepu;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TestReg {

	public static void main(String[] args) {
		
		Pattern pattern = Pattern.compile("d*.txt");
		Matcher matcher=pattern.matcher("single.txt");
		if(matcher.find())
		{
			System.out.println(matcher.group());
		}
		System.out.println(matcher.group());
		
		boolean match=Pattern.matches("*txt", "single.txt");
		System.out.println(match);
		
		
	}
}
