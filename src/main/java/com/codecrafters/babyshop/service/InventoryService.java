package com.codecrafters.babyshop.service;
import com.codecrafters.babyshop.exception.InsufficientStockException; import com.codecrafters.babyshop.model.Item;
public class InventoryService {
 public int calculateNewQuantity(Item item,int amount,boolean add)throws InsufficientStockException{if(amount<=0)throw new IllegalArgumentException("Enter a quantity greater than zero.");int next=item.getQty()+(add?amount:-amount);if(next<0)throw new InsufficientStockException("Not enough stock. Available quantity: "+item.getQty());return next;}
}
