package com.example.e_ticaretverigrselletirme;

import com.anychart.AnyChart;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.charts.Cartesian;
import com.anychart.charts.Cartesian3d;
import com.anychart.charts.Pie;
import java.util.List;

public class grafikler {
    //anyChart view'ına set edilecek grafiklerin tutulduğu class
    //hangi raporda hangi grafik kullanılacağı yazılımcı insiyatifine göre seçilmiştir.
    //fonksiyonlar parametre olarak grafik başlığı ve raporlanmış veri alır
    public Pie PieChart(List<DataEntry> veri,String title)
    {
        Pie pie= AnyChart.pie();
        pie.data(veri);
        pie.title(title);
        return pie;
    }

    public Cartesian3d tBarChart(List<DataEntry> veri,String title)
    {
        Cartesian3d tBar = AnyChart.bar3d();
        tBar.data(veri);
        tBar.title(title);
        return tBar;
    }


    public Cartesian ColumnChart(List<DataEntry> veri,String title)
    {

        Cartesian column=AnyChart.column();
        column.data(veri);
        column.title(title);
        return column;
    }


}
