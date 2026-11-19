package com.codecrafters.babyshop.service;
public class RetailPricing implements PricingStrategy {@Override public double calculatePrice(double unitPrice,int quantity){return unitPrice*quantity;}}
