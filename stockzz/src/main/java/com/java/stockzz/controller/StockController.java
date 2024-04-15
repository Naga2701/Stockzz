package com.java.stockzz.controller;

import com.java.stockzz.model.Stock;
import com.java.stockzz.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/api")
public class StockController {

    @Autowired
    private StockService stockService;

    @PostMapping("/stock")
    public ResponseEntity<Stock> addStock(@RequestBody Stock stock) {
        Stock newStock = stockService.createStock(stock);
        if (newStock != null) {
            return new ResponseEntity<>(newStock, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/stocks")
    public ResponseEntity<Page<Stock>> getAllStocks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        Page<Stock> stocks = stockService.getAllStocks(pageable);

        if (stocks.hasContent()) {
            return new ResponseEntity<>(stocks, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/stocks/sorted")
    public ResponseEntity<Page<Stock>> getAllStocksSorted(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy) {

        Pageable pageable = PageRequest.of(page, size);

        Page<Stock> stocks = stockService.getAllStocksSorted(sortBy, pageable);
        if (stocks.hasContent()) {
            return new ResponseEntity<>(stocks, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/stock/{stockId}")
    public ResponseEntity<Stock> updateStock(@PathVariable int stockId, @RequestBody Stock stock) {
        boolean updated = stockService.updateStock(stockId, stock);
        if (updated) {
            return new ResponseEntity<>(stock, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/stock/{stockId}")
    public ResponseEntity<Boolean> deleteStock(@PathVariable int stockId) {
        boolean deleted = stockService.deleteStock(stockId);
        if (deleted) {
            return new ResponseEntity<>(true, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(false, HttpStatus.NOT_FOUND);
        }
    }
}