package com.kshitij.simplestock.cmd;

import com.kshitij.simplestock.modal.Stock;
import com.kshitij.simplestock.modal.Trade;
import com.kshitij.simplestock.repo.StockRepo;
import com.kshitij.simplestock.repo.TradeRepo;
import com.kshitij.simplestock.repo.VwapRepo;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.partitioningBy;
import static java.util.stream.Collectors.reducing;
import org.apache.log4j.Logger;


public class CommandProcessor {
    private static Logger LOG = Logger.getLogger(CommandProcessor.class);
    private StockRepo stockRepo;
    private TradeRepo tradeRepo;
    private VwapRepo vwapRepo;
    
    
    
    public double getDividendYied(String stockId,Double price){
         return stockBasedOperation(stockId, price, s -> s.getDividendYield());
    }
    
    public double getPE(String stockId, Double price){
        return stockBasedOperation(stockId, price, s -> s.getPERatio());
    }
    
    private double stockBasedOperation(String stockId, Double price,Function<Stock,Double> func){
        Optional<Stock> stockOp = stockRepo.getStockDetailsById(stockId);
        if(stockOp.isPresent()){
              Stock stock= stockOp.get();
              stock.setLastPrice(price);
             return func.apply(stock);
        }else{
            throw new RuntimeException("Incorrect stockID, unable to find " +
                    "details of stock with " + stockId );
        }
    }

    public String getVwapPriceFor(String stockId) {
        Optional<List<Trade>> vwapTrades = vwapRepo.getVwapTradesForStock(stockId);
        if(vwapTrades.isPresent() && vwapTrades.get().size()>0){
            List<Trade> vwapTradeLst = vwapTrades.get();
            LOG.info("Trade list for vwap " + vwapTradeLst);
            double numerator =vwapTradeLst.stream().collect(reducing(0.0,t ->
            { return new BigDecimal(t.getPrice()).multiply(new BigDecimal(t.getQty())).doubleValue();},(i,j)-> i + j));
            double dinominator =vwapTradeLst.stream().collect(reducing(0.0,t ->t.getQty()
            ,(i,j)-> i + j));
            LOG.info("Numerator -->" + numerator + " dinominator -->" + dinominator);
           return new BigDecimal(numerator).divide(new BigDecimal(dinominator),4,RoundingMode.HALF_EVEN).toPlainString();
        
        }else{
            return "No trades for last " + vwapRepo.getVwapComputationInMinutes() + " minutes found. No VWAP caculated ";
        }
    }

    public String getGBCEIndex() {
        Map<Boolean, List<Stock>> partitionedByPrice = stockRepo.getAllStocks().stream().collect(partitioningBy(s -> s.getLastPrice() != null));
        
        Optional<Double> gbce_prod = partitionedByPrice.get(true).stream().map(Stock::getLastPrice)
                .reduce((Double s, Double d) ->new BigDecimal(s).multiply(new BigDecimal(d)).doubleValue());
     
        if (gbce_prod.isPresent()) {
          
            LOG.info("Product of all valid stocks " + gbce_prod);
            
            Double gbce = Math.pow(gbce_prod.get().doubleValue(), (1.0 / (partitionedByPrice.get(true).size())));
            
            String stockNameForGBCE = partitionedByPrice.get(true).stream().map(Stock::getStockId).collect(joining(",", "[", "]"));
            String stockNameWithNoPrice = partitionedByPrice.get(false).stream().map(Stock::getStockId).collect(joining(",", "[", "]"));
            
            return "GBCE index for stock with valid prices is " + gbce + " \n Following stocks are included in index calculations "
                    + stockNameForGBCE + "\n Following stock are excluded because they don't have price yet:: " + stockNameWithNoPrice;
        } else {
            return "No stock has any last price updated. Please update stock price by entering their trades/other operations";
        }
    }

    public String enterTradeToRepo(String tradeString) {
        Trade trade = Trade.TradeBuilder.buildTradeFromString(tradeString);
        tradeRepo.addTradeToRepo(trade);
        stockRepo.updateStockLastPrice(trade.getStockId(), trade.getPrice());
        vwapRepo.addVwapTradeForStock(trade);
        
        return "Trade with following details [" + trade + "]is added to repo";
    }

    public StockRepo getStockRepo() {
        return stockRepo;
    }

    public void setStockRepo(StockRepo stockRepo) {
        this.stockRepo = stockRepo;
    }

    public TradeRepo getTradeRepo() {
        return tradeRepo;
    }

    public void setTradeRepo(TradeRepo tradeRepo) {
        this.tradeRepo = tradeRepo;
    }

    public VwapRepo getVwapRepo() {
        return vwapRepo;
    }

    public void setVwapRepo(VwapRepo vwapRepo) {
        this.vwapRepo = vwapRepo;
    }
    
    

}

