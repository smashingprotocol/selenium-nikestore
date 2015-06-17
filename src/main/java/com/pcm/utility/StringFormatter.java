package com.pcm.utility;

import java.text.DecimalFormat;

public class StringFormatter {

	public static String formatFloattoCommaDecimalNumString(
			Float floatValue) {
		
		String formattedString = String.format("%.02f", floatValue);
		Double dValue = Double.parseDouble(formattedString);
		DecimalFormat formatter = new DecimalFormat("#,###.00");
		formattedString = formatter.format(dValue);
		
		
		return formattedString;
	}

}
