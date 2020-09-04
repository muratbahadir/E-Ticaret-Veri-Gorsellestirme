package com.example.e_ticaretverigrselletirme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class giris_sorgu extends AppCompatActivity {
    musteri_sorgular vt=new musteri_sorgular(this); //sorguların çekileceği sınıftan bir nesne üretilir.
    FirebaseDatabase db; //Google'ın firebaseDatabase yapısını kullanmak için nesne örneklenir
    EditText username;EditText password;
    Boolean k_or_m; //ana ekrandan gelen kullanıcı türü bilgisini tutar
    Boolean kayitKontrol =false; //girilen kullanıcının veritabanında olup olmadığını kontrol eder.
    String kullaniciTipi;  //kullanici tipini string olarak tutar.Veritabanı işlemlerinde hangi tablodan okuyacağını bilmesi için
    int id_index ; //kullanıcıların id indexini tutar
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_giris_sorgu);

        db = FirebaseDatabase.getInstance();

        Intent gelen = this.getIntent();
        ImageView resim = (ImageView) findViewById(R.id.imageView);
        k_or_m = gelen.getBooleanExtra("k_or_m", true); //mainActivitydem gelen kullanıcı türü alınır
        username = (EditText) findViewById(R.id.editText);
        password = (EditText) findViewById(R.id.editText2);
        Button girisButon = (Button) findViewById(R.id.button);

        if (k_or_m)  //kullanıcı türüne göre üst taraftaki ikon ayarlanır ve kullaniciTipi alanı doldurulur
        {
            kullaniciTipi = "Kurumsal";
            resim.setImageResource(R.drawable.responsibility);
        }

        else
       {
            kullaniciTipi = "Müşteri";
            resim.setImageResource(R.drawable.woman);
       }



        final Button yenikayit = (Button) findViewById(R.id.button2);
        //yeni kayit butonuna tıklanırsa  kullanıcı tipi bilgisiyle beraber yenikayit bölümüne gönderilir
        yenikayit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent geçiş=new Intent(giris_sorgu.this,yenikayit.class);
                geçiş.putExtra("k_or_m",k_or_m);
                startActivity(geçiş);
            }
        });

  //gerekli değerler girildikten sonra giriş butonuna tıklanıldığında...
        girisButon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                 final String girilenUsername=username.getText().toString();
                final String girilenPassword=password.getText().toString();
                //username ve password bilgileri alınır
                final DatabaseReference reference = db.getReference(kullaniciTipi); //kullanıcı tipinin olduğu ağaca göre veritabanı referansı alınır

                final ArrayList<kullanici> kullanicilar =new ArrayList<>();
                //veritabanındaki tüm kullanıcılar,kullanici tipindeki kullanicilar listesine atılır ve sorgu bu listeden yapılır
                reference.addListenerForSingleValueEvent(new ValueEventListener() { //verilen referanstan tek seferlik okuma yapılır( vt'de değişiklik olmayacağı için)
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot ds : snapshot.getChildren())
                        {
                            kullanicilar.add(ds.getValue(kullanici.class)); //çekilen her değer  nesne olarak listeye atılır
                        }

                        for(int i=0;i<kullanicilar.size();i++) //girilen username ve password bilgisi tüm kullanıcılarda taranır
                        {
                            if (girilenUsername.equals(kullanicilar.get(i).getUsername()) &&
                                    girilenPassword.equals(kullanicilar.get(i).getPassword())) //uyumlu kullanıcı bulunursa kullanıcının indexi alınır ve kayıt bulundugundan kayıtkontrol'e true atanır
                            {
                                id_index =i;
                                kayitKontrol = true;
                             }
                        }

                        if(kayitKontrol ==true) //kayıt girişi başarılıysa  kullanıcı tipine göre rapor ekranlarına gönderilir ve bu activity ekranı kapatılır
                        {
                            Toast.makeText(giris_sorgu.this,"Giriş Başarılı",Toast.LENGTH_SHORT).show(); //ekrana giriş başarılı uyarısı verilir
                            if(!k_or_m)
                            {  //kullanıcı tipi müşteriyse her müşterinin raporu farklı olacağından  rapor ekranına müşteri id'si de gönderilir
                                Intent gecis=new Intent(giris_sorgu.this, musteri_raporlar.class);
                                gecis.putExtra("userid",String.valueOf(kullanicilar.get(id_index).idd));
                                startActivity(gecis);
                                finish();
                            }
                            else
                            {
                                Intent gecis=new Intent(giris_sorgu.this, kurumsal_raporlar.class);

                                startActivity(gecis);
                                finish();
                            }

                        }
                        if(kayitKontrol ==false)  //kayıt bulunamadıysa giriş başarısız uyarısı verilerek edittextlerin içi boşaltılır
                        {
                            Toast.makeText(giris_sorgu.this,"Giriş Başarısız",Toast.LENGTH_SHORT).show();
                            username.setText("");
                            password.setText("");

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });

    }

}
