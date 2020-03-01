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
public class ChartController implements Consumer<StockPrice> {

    @FXML
    public LineChart<String, Double> chart;
    private ObservableList<XYChart.Data<String, Double>> seriesData = FXCollections.observableArrayList();
    private StockClient stockClient;

    public ChartController(StockClient stockClient) {
        this.stockClient = stockClient;
    }

    @FXML
    public void initialize() {
        String symbol = "SYMBOL";

        ObservableList<XYChart.Series<String, Double>> data = FXCollections.observableArrayList();
        data.add(new XYChart.Series<>(symbol, seriesData));
        chart.setData(data);

        stockClient.pricesFor(symbol).subscribe(this);
    }

    @Override
    public void accept(StockPrice stockPrice) {

        Platform.runLater(() ->
                seriesData.add(new XYChart.Data<>(
                        String.valueOf(stockPrice.getTime().getSecond()),
                        stockPrice.getPrice())));
    }
}
