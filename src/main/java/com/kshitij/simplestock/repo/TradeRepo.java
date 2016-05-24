package com.kshitij.simplestock.repo;

import com.kshitij.simplestock.modal.Trade;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author kshitij
 */
public class TradeRepo {
    private final Set trades = new HashSet();
           
    public void addTradeToRepo(Trade trade){
          trades.add(trade);
    }
    
}
