package com.testing;

import common.reports.EmailReporter;

public class exec {

	public static void main(String...args) {
		  try {
	    		EmailReporter.sendEmail(args[0], args[1], args[2]);


	        } catch (Exception e) {
	            e.printStackTrace();
	        }
		
	}
}
