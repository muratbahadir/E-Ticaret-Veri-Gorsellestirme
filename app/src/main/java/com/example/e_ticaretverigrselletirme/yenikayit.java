package com.example.e_ticaretverigrselletirme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;

//yeni kullanici kaydini FireBase veritabanına kaydeden activity
public class yenikayit extends AppCompatActivity {

private FirebaseDatabase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yenikayit);
        db=FirebaseDatabase.getInstance();

        Intent a=this.getIntent();
       final Boolean k_or_m=a.getBooleanExtra("k_or_m",true); //hangi tip kullanıcı kaydı yapılacağı bilgisi alınır
        //text alanlarındaki veriler alınır
        TextView mTextView=(TextView)findViewById(R.id.textView);
        mTextView.setTextColor(ContextCompat.getColor(this,R.color.colorPrimaryDark));
        final EditText username=(EditText)findViewById(R.id.username);
        final EditText password=(EditText)findViewById(R.id.password);
        final EditText p_again=(EditText)findViewById(R.id.passwordagain);
        Button kayit=(Button)findViewById(R.id.kayıt);
        final String kullanicitipi; //veritabanında hangi  ağaca kaydedileceği bilgisi

        if(k_or_m)
        {
            kullanicitipi="Kurumsal";
        }
        else
        {
            kullanicitipi="Müşteri";
        }

        final ArrayList<String> kullaniciAdlari=new ArrayList<>(); //veritabanındaki tüm kullanıcıların kullanıcıadını tutan liste
        DatabaseReference reference=db.getReference(kullanicitipi); //kullanıcının kayıt yapmak istediği kullanıcı tipindeki ağaca gidilir
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds:snapshot.getChildren())
                {
                    kullaniciAdlari.add(ds.getValue(kullanici.class).getUsername()); //tüm kullanıcı adları listeye atanır
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        kayit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {   //kayıt butonuna basıldığında

                if(!password.getText().toString().equals(p_again.getText().toString()))
                {  //şifreler uyuşmuyorsa uyarı verilir ve alanlar boşaltılır
                    Toast.makeText(yenikayit.this,"Şifreler aynı değil!",Toast.LENGTH_SHORT).show();
                    p_again.setText("");
                    password.setText("");

                }
                else
                    //şifreler aynıysa girilen kullanıcı adının veri tabanında kayıtlı olup olmadığına bakılır
                   //performans ve hız açısından her denemede veritabanına bağlanıp sorgulanmasındansa birkez sorgulanıp listeye atılarak listeden
                //sorgulanması daha mantıklı olur.
                {    Boolean kullaniciKontrol=false;
                    for(int i=0;i<kullaniciAdlari.size();i++)
                    {
                        if(kullaniciAdlari.get(i).equals(username.getText().toString()))
                        {
                            kullaniciKontrol=true;
                        }
                    }
                    if(kullaniciKontrol) //girilen kullanıcı adı zaten varsa
                    {
                        Toast.makeText(yenikayit.this,"Bu Kullanıcı Adı daha önce alınmış",Toast.LENGTH_SHORT).show();
                        username.setText("");
                        p_again.setText("");
                        password.setText("");
                    }
                    else{ //girilen değerler kayıt yapmaya uygunsa
                        DatabaseReference referans=db.getReference(kullanicitipi); //kullanıcıtipine göre referans alıır
                    String key=referans.push().getKey();  //üzerine yazma durumu olmaması için id benzeri keyler alınır
                    DatabaseReference guncelReferans=db.getReference(kullanicitipi+"/"+key); //her kayıt için ayrı key olacak şekilde yeni referans alınır
                    //güncel referansa bu bilgiler basılır
                    //kullanıcı sınıfından bir nesne oluşturularak bu nesneye alanları verilir ve bu nesne  veritabanına yazılır
                    guncelReferans.setValue(new kullanici(username.getText().toString(),password.getText().toString(),kullaniciAdlari.size()+1));
                    Toast.makeText(yenikayit.this,"Kayıt işlemi başarılı",Toast.LENGTH_SHORT).show();
                    Intent gecis=new Intent(yenikayit.this,giris_sorgu.class);
                    gecis.putExtra("k_or_m",k_or_m);//kullanici tipi bilgisi gönderilerek giriş ekranına yönlendirilir
                    startActivity(gecis);
                    finish();


                    }

                }

            }
        });


}



}
