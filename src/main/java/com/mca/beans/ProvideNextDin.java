package com.mca.beans;

public class ProvideNextDin {

    private static ProvideNextDin myInstance;
	private static String dinValue; // = din+String.valueOf(initialValue);
	private static int initialValue = 870801;
	static String din = "00";	
	
	public ProvideNextDin() {
		dinValue = din+String.valueOf(initialValue);
	}
	
    public static String getInstance() {
        if (myInstance == null) {
            synchronized(ProvideNextDin.class) {
                if (myInstance == null) {
                    myInstance = new ProvideNextDin();
                }
            }
        } else {
        	synchronized(ProvideNextDin.class) {
                initialValue = initialValue + 1;
                dinValue = din+String.valueOf(initialValue);
            }
        }
        return ProvideNextDin.dinValue;
    }
	 

}
