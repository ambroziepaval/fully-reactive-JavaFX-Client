package com.ambroziepaval.stockui;

import com.ambroziepaval.stockclient.StockClient;
import com.ambroziepaval.stockclient.StockPrice;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

@Component
public class ChartController {

    @FXML
    public LineChart<String, Double> chart;
    private StockClient stockClient;

    public ChartController(StockClient stockClient) {
        this.stockClient = stockClient;
    }

    @FXML
    public void initialize() {
        String symbol1 = "ALFA";
        String symbol2 = "BETA";
        ObservableList<XYChart.Series<String, Double>> data = FXCollections.observableArrayList();

        StockPriceConsumer prices1 = new StockPriceConsumer(symbol1);
        StockPriceConsumer prices2 = new StockPriceConsumer(symbol2);
        data.add(prices1.getSeries());
        data.add(prices2.getSeries());
        chart.setData(data);

        stockClient.pricesFor(symbol1).subscribe(prices1);
        stockClient.pricesFor(symbol2).subscribe(prices2);
    }


    private static class StockPriceConsumer implements Consumer<StockPrice> {

        private ObservableList<XYChart.Data<String, Double>> seriesData = FXCollections.observableArrayList();
        private XYChart.Series<String, Double> series;

        public StockPriceConsumer(String symbol) {
            this.series = new XYChart.Series<>(symbol, seriesData);
        }

        @Override
        public void accept(StockPrice stockPrice) {
            Platform.runLater(() ->
                    seriesData.add(new XYChart.Data<>(
                            String.valueOf(stockPrice.getTime().getSecond()),
                            stockPrice.getPrice())));
        }

        public XYChart.Series<String, Double> getSeries() {
            return series;
        }
    }
}
