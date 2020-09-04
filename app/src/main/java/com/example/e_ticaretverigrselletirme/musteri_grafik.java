package com.example.e_ticaretverigrselletirme;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.anychart.AnyChartView;

public class musteri_grafik extends AppCompatActivity {
   //seçilen raporun grafikselleştirilerek gösterildiği activity
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_musteri_grafik);
        Intent a = getIntent();
        musteri_sorgular ms=new musteri_sorgular(this); //raporların oluşturulduğu sınıftan nesne yaratılır

        AnyChartView anyChartView=(AnyChartView)findViewById(R.id.acw); //ekranda gözüken chart bilgisi alınır
        grafikler g=new grafikler();  //chart view'ına grafik verebilmek için grafikler sınıfından nesne orneklenir
        int secilen_Kategori = a.getIntExtra("position", 0);
        //secilen rapor ve kullanıcı id bilgisi diğer activityden alınır
        String user_id=a.getStringExtra("userid");
        if (secilen_Kategori == 0) //secilen rapora(listedeki ilk3 rapordan biri seçildiyse) göre view'a grafik verilir ,data olarakta seçilen rapor verilir(userıd bilgisi ile birlikte)
        {
            anyChartView.setChart(g.PieChart(ms.AylaraGoreAlim(user_id),"Aylara Göre Alım"));
        }

        if (secilen_Kategori == 1)
        {
            anyChartView.setChart(g.ColumnChart(ms.AdedeGoreKategori(user_id),"En Yüksek Alıma Göre Kategoriler(İlk 5)"));
        }

        if (secilen_Kategori == 2)
        {
            anyChartView.setChart(g.tBarChart(ms.EnCokAlimYapilanGunler_₺(user_id),"En Çok Alım Yapılan Tarihler(İlk 5)"));
        }
        //Grafiğe ihtiyaç duyulmayan tek bilgi istenirse sıralama bilgisi Toast mesajı olarak gösterilir ve önceki activitye userıd bilgisini kaybetmeden geri gönderir
        if (secilen_Kategori == 3)
        {
            Toast.makeText(this, "En çok Alım Yapan (₺) "+ms.MusteriyeGoreAlimSiralamasi_₺(user_id)+". kişisiniz. ", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(musteri_grafik.this, musteri_raporlar.class);

            i.putExtra("userid",user_id);
            startActivity(i);
            finish();
        }

        if (secilen_Kategori ==4 )
        {
            Toast.makeText(this, "Toplam alımınız: "+ms.ToplamAlim_₺(user_id)+"₺", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(musteri_grafik.this, musteri_raporlar.class);

            i.putExtra("userid",user_id);
            startActivity(i);
            finish();
        }
        if (secilen_Kategori ==5 )
        {
            Toast.makeText(this, "Sipariş Verilen Son adres "+ms.sonAdres(user_id), Toast.LENGTH_SHORT).show();
            Intent i = new Intent(musteri_grafik.this, musteri_raporlar.class);

            i.putExtra("userid",user_id);
            startActivity(i);
            finish();
        }


    }
}