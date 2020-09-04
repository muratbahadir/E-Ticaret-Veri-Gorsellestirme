package com.example.e_ticaretverigrselletirme;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
//Programın ilk giriş ekranıdır. Kullanıcının kurumsal kimlik veya müşteri kimliğinden hangisiyle giriş yapacağı sorgulanır.

public class MainActivity extends AppCompatActivity {
    Boolean k_or_m; //hangi türün seçildiğini tutan değer k=kurumsal_true,m=musteri=false
    Context c=this;
    DataBaseHelper db=new DataBaseHelper(c); //Veritabanı oluşturulup verileri ekleyen sınıftan nesne üretilir.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db.tabloBosalt(); //Uygulama daha önce çalıştırıldıysa yeni yazılan verilerin üzerine yazmaması için tüm tablolar boşaltılır.
        db.ıtemsC_and_I();  //Tüm tablolar sırasıyla doldurulur.
        db.veriEkleUsers();
        db.VeriEkleCities();
        db.countryOlusturVeEkle();
        db.adresTablosuOlusturVeEkle();
        db.InvoicedetailsC_and_I();
        db.OrderdetailsC_And_I();
        db.OrdersCreateandInsert();
        db.InvoicesCreateAndInsert();
        db.TownsC_and_I();
        db.Dıstrıct_C_and_I();
        db.PaymentsC_and_I();


        Button musteri=(Button) findViewById(R.id.musteri_buton);
        Button kurumsal=(Button) findViewById(R.id.kurumsal_buton);

        final Intent gecis=new Intent(MainActivity.this,giris_sorgu.class);
        //kurumsal butonuna tıklanırsa giriş eklanına gönderilirken kurumsal true bilgisi gönderilir.
        kurumsal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                k_or_m=true;
                gecis.putExtra("k_or_m",k_or_m);

                startActivity(gecis);
                finish();
            }
        });

        //kurumsal butonuna tıklanırsa giriş eklanına gönderilirken kurumsal true bilgisi gönderilir.
        musteri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                k_or_m=false;
                gecis.putExtra("k_or_m",k_or_m);

                startActivity(gecis);
                finish();

            }
        });
    }
}
