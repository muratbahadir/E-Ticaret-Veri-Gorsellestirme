package com.example.e_ticaretverigrselletirme;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class kurumsal_raporlar extends AppCompatActivity {
    //kurumsal raporların liste halinde gösterildiği activity
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kurumsal_sorgu_liste);
        //listView'a sorgu başlıkları yazılır
        ListView lw=(ListView)findViewById(R.id.liste);
        final String [] sorgular={"En Çok Alınan Ürünler","En Çok Satış Yapan Kategoriler(adet)","En Az Satış Yapılan Kategoriler(adet)",
                "En Çok Satış Yapan Kategori(₺)","En Az Satış Yapan Kategori(₺)","Birim Fiyatı En Pahalı Olan Ürünler","Birim Fiyatı En Ucuz Olan Ürünler"
                ,"En Çok Satış Yapan Markalar(adet)","En Çok Satış Yapan Markalar(₺)","En Çok Satış Yapılan İller(₺)"," En Çok Satış Yapılan İller(adet)"
                ,"En Çok Satış Yapılan Cinsiyet(₺)","En Çok Satış Yapılan Cinsiyet(adet)","Kadınlar İçin En Çok Satış Yapan Markalar (₺)",
                "Erkekler İçin En Çok Satış Yapan Markalar (₺)","Kadınlar İçin En Çok Satış Yapan Kategoriler (₺)","Erkekler İçin En Çok Satış Yapan Kategoriler (₺)",
                "En Fazla Ürün Alan Müşteriler","AylaraGöreSatışlar(₺)","Yıllara Göre Satışlar(₺)"
        };

        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, android.R.id.text1,sorgular);
        lw.setAdapter(adapter); //adaptör ile başlıklar viewa bağlanır
        lw.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) { //listede herhangi bir text'e tıklanıldığında tıklanılan pozisyon ve başlık bilgisi grafik
                //activitysine gönderilir
                Intent a=new Intent(kurumsal_raporlar.this,kurumsal_grafik.class);
                a.putExtra("position",position);
                a.putExtra("category",sorgular[position]);
                startActivity(a);
            }
        });

    }
}
