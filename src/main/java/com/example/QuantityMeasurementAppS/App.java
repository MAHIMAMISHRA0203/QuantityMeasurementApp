package com.example.QuantityMeasurementAppS;

import java.util.Scanner;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) {
    	Scanner sc=new Scanner(System.in);
    	System.out.println("Please enter value 1:");
    	double value1=sc.nextDouble();
    	System.out.println("Please enter value 2:");
    	double value2=sc.nextDouble();
    	FeetPOJO f1=new FeetPOJO(value1);
    	FeetPOJO f2=new FeetPOJO(value2);


        FeetServices service =
                new FeetServices();

        boolean result = service.areEqual(f1, f2);

        System.out.println("Are equal? " + result);
    	
    	
    }
}
