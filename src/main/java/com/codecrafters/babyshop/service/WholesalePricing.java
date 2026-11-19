package com.codecrafters.babyshop.service;
public class WholesalePricing implements PricingStrategy {@Override public double calculatePrice(double unitPrice,int quantity){double total=unitPrice*quantity;return quantity>=10?total*0.90:total;}}
