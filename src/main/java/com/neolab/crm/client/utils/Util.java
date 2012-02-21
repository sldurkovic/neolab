package com.neolab.crm.client.utils;

import java.util.logging.Logger;

public class Util {
	
	public static void logFine(Logger log, String msg){
		log.fine(":: "+msg+" ::");
	}
	
	public static void logWarn(Logger log, String msg){
		log.warning("## "+msg+" ##");
	}

}
