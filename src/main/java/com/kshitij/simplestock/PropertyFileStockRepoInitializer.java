package com.kshitij.simplestock;

import com.google.common.base.Splitter;
import com.kshitij.simplestock.modal.Stock;
import static com.kshitij.simplestock.modal.Stock.StockBuilder.createStockFromMap;
import com.kshitij.simplestock.repo.StockRepo;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;



@Component
@ConfigurationProperties(prefix="defaultstock")
public class PropertyFileStockRepoInitializer {
    private List<String> stocks = new ArrayList();
    private String delimiter;
    private static Logger LOG = Logger.getLogger(PropertyFileStockRepoInitializer.class);
    
    @Autowired
    private StockRepo repo;
    
    @PostConstruct
    public  void init(){
        LOG.info("Initializing stock repo with following data " + getInitialStocks());
        getInitialStocks().stream().forEach(s->repo.addStockToRepo(s));
    }
    

    public String getDelimiter() {
        return delimiter;
    }

    public void setDelimiter(String delimiter) {
        this.delimiter = delimiter;
    }
    
    
    public List<String> getStocks() {
        return stocks;
    }

    public void setStocks(List<String> stocks) {
        this.stocks = stocks;
    }
    
    public List<Stock> getInitialStocks(){
        List result = new ArrayList();
        for(String stock:stocks){
            result.add(createStockFromMap(Splitter.on(",").withKeyValueSeparator("=").split(stock)));
        }
        return result;
    }
    
    
}
