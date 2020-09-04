package com.example.e_ticaretverigrselletirme;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class musteri_raporlar extends AppCompatActivity {
        String id_index;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_musteri_detay_analiz);
       //müşteriye özel raporların listelendiği activity
        id_index=getIntent().getStringExtra("userid"); //müşteriye göre raporlar olacağı için hangi müşterinin giriş yaptığının bilgisi alınır


        ListView lw = (ListView) findViewById(R.id.liste);//activitydeki listeye raporlar sırasıyla yazılır
        String[] kategoriler = {"Aylara Göre Alım(₺)", "Adede Göre Kategori", "En Çok Alım Yapılan Günler","Kurum İçindeki Alım Sıralaması(₺)"
                ,"Toplam Alım(₺)","Sipariş Verilen Son Adres"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, kategoriler);
        lw.setAdapter(adapter);
        lw.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //listede seçim yapıldığında seçilen rapor bilgisi ve kullanıcı ıd'si grafiğin gösterileceği activitye gönderilir ve activity başlatılır
                Intent a = new Intent(musteri_raporlar.this, musteri_grafik.class);
                a.putExtra("position", position);
                a.putExtra("userid",id_index);
                startActivity(a);

            }
        });


    }
}