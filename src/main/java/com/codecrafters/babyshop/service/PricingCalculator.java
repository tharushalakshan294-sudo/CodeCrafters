package com.codecrafters.babyshop.service;

import com.codecrafters.babyshop.model.Customer;
import com.codecrafters.babyshop.model.Item;

/** Demonstrates overloading and runtime polymorphism through PricingStrategy. */
public class PricingCalculator {
    private final PricingStrategy retail = new RetailPricing();
    private final PricingStrategy wholesale = new WholesalePricing();

    public double calculate(Item item, int quantity) {
        return calculate(item, quantity, null);
    }

    public double calculate(Item item, int quantity, Customer customer) {
        if (quantity <= 0) throw new IllegalArgumentException("Quantity must be greater than zero.");
        PricingStrategy strategy = customer != null
                && "Wholesale".equalsIgnoreCase(customer.getCustomerType())
                ? wholesale : retail;
        return strategy.calculatePrice(item.getPrice(), quantity);
    }
}
