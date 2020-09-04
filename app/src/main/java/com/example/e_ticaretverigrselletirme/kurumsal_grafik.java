package com.example.e_ticaretverigrselletirme;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.anychart.AnyChartView;

public class kurumsal_grafik extends AppCompatActivity {
    //seçilen raporun grafikselleştirilerek gösterildiği activity
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kurumsal_grafik);
        Intent a=getIntent();
        int position=a.getIntExtra("position",0); //   //secilen rapor bilgisi ve listedeki pozisyonu diğer activityden alınır
        String category=a.getStringExtra("category");
        AnyChartView anyChartView=(AnyChartView)findViewById(R.id.acw);//ekranda gözüken chart bilgisi alınır
        kurumsal_sorgular ks=new kurumsal_sorgular(this); //raporların oluşturulduğu sınıftan nesne yaratılır
        grafikler g=new grafikler();//chart view'ına grafik verebilmek için grafikler sınıfından nesne orneklenir
        if(position==0) //secilen rapora göre view'a grafik verilir ,data olarakta ks nesnesinden seçilen rapor verilir(grafik başlığıda category olarak verilerek)
        {
            anyChartView.setChart(g.tBarChart(ks.EnCokAlinanUrunler(),category));
        }
        if(position==1)
        {
            anyChartView.setChart(g.tBarChart(ks.EnCokSatisYapanKategoriler_adet(),category));
        }
        if(position==2)
        {
            anyChartView.setChart(g.ColumnChart(ks.EnAzSatisYapilanKategoriler_adet(),category));
        }
        if(position==3)
        {
            anyChartView.setChart(g.ColumnChart(ks.EnCokSatisKategori_₺(),category));
        }
        if(position==4)
        {
            anyChartView.setChart(g.ColumnChart(ks.EnAzSatisKategori_₺(),category));
        }
        if(position==5)
        {
            anyChartView.setChart(g.ColumnChart(ks.EnPahaliBirimFiyat(),category));
        }
        if(position==6)
        {
            anyChartView.setChart(g.ColumnChart(ks.EnUcuzBirimFiyat(),category));
        }
        if(position==7)
        {
            anyChartView.setChart(g.ColumnChart(ks.EnCokSatisYapanMarkalar_adet(),category));
        }
        if(position==8)
        {
            anyChartView.setChart(g.ColumnChart(ks.EnCokSatisYapanMarkalar_₺(),category));
        }

        if(position==9)
        {
            anyChartView.setChart(g.tBarChart(ks.EnCokSatisYapilanIller_₺(),category));
        }
        if(position==10)
        {
            anyChartView.setChart(g.ColumnChart(ks.EnCokSatisYapilanIller_adet(),category));
        }
        if(position==11)
        {
            anyChartView.setChart(g.PieChart(ks.EnCokSatisCinsiyet_₺(),category));
        }
        if(position==12)
        {
            anyChartView.setChart(g.PieChart(ks.EnCokSatisCinsiyet_adet(),category));
        }
        if(position==13)
        {
            anyChartView.setChart(g.tBarChart(ks.EnCokSatisYapanMarkalarKadinIcin_₺(),category));
        }
        if(position==14)
        {
            anyChartView.setChart(g.tBarChart(ks.EnCokSatisYapanMarkalarErkekIcin_₺(),category));
        }
        if(position==15)
        {
            anyChartView.setChart(g.tBarChart(ks.EnCokSatisYapanKategorilerKadinIcin_₺(),category));
        }
        if(position==16)
        {
            anyChartView.setChart(g.tBarChart(ks.EnCokSatisYapanKategorilerErkekIcin_₺(),category));
        }
        if(position==17)
        {
            anyChartView.setChart(g.PieChart(ks.EnFazlaUrunAlanMusteriler(),category));
        }
        if(position==18)
        {
            anyChartView.setChart(g.PieChart(ks.AylaraGoreSatislar(),category));
        }
        if(position==19)
        {
            anyChartView.setChart(g.PieChart(ks.YillaraGoreSatislar(),category));
        }

    }
}
