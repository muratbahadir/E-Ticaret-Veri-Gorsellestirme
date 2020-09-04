package com.example.e_ticaretverigrselletirme;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import androidx.annotation.Nullable;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import java.util.ArrayList;
import java.util.List;

//müşteriye özel raporların oluşturulduğu sorgular bu classda yer alıyor.
public class musteri_sorgular extends SQLiteOpenHelper {
    SQLiteDatabase db;
    public musteri_sorgular(@Nullable Context context) {
        super(context, "ETRADE", null, 1);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public List<DataEntry> veriTabaniOkumaCift(String komut) //veritabanından çift tablo alınması istenen durumlarda kullanılır(tanım ve değer)

    {
        Cursor c=db.rawQuery(komut,null); //veritabanından okuma işlemini yapacak cursor
        ArrayList<String> tanim=new ArrayList<>(); //tanim değerlerini tutan liste
        ArrayList<Float> deger=new ArrayList<>(); //deger değerkerini tutan liste


        if(c.moveToFirst()) //currsorun gösterdiği veritabanının ilk elemanı varsa
        {
            do {
                tanim.add(c.getString(0)); //ilk tablodaki değeri stringe çevirerek tanim listesine at
                deger.add(Float.parseFloat(c.getString(1))); //ikinci tablodaki değeri float olarak deger listesine ata
                }
            while (c.moveToNext()); //tüm satırların elemanları okunana kadar bu işlem yapılır ve tablodaki tanım ve değerler alınır
        }

        List<DataEntry> data = new ArrayList<>(); //bu iki liste tek bir veri tipine atanır

        for(int i=0;i<tanim.size();i++) //elemanlar doldurulur
        {
            data.add(new ValueDataEntry(tanim.get(i),deger.get(i)));
        }
        db.close();  //veritabanı kapatılır

        return data;  //istenen tanım ve değer datası döndürülür
    }
    public ArrayList<String> veriTabaniOkumaSiralama(String komut)
    {//herhangi bir parametreye göre müşterinin sıralamasının isteneceği durumda müşterinin konumunun aranacağı tablo bir listeye atanır
        Cursor c=db.rawQuery(komut,null);
        ArrayList<String> siralama=new ArrayList<>();
        if(c.moveToFirst())
        {
            do {
                siralama.add(c.getString(0));

            }while (c.moveToNext());
        }
        return siralama;

    }

    public String veriTabaniOkumaTek(String komut)  //veritabanından okunacak değerin bir tane olması durumunda kullanılır(en cok alınan ürün gibi)

    {
        Cursor c=db.rawQuery(komut,null);

        if(c.moveToFirst())
        {
            do {
                return (c.getString(0));
                }
            while (c.moveToNext());
        }

        return "0";
    }

    public String EnCokAlinanUrun(String userId)  //Adet olarak en fazla alınan ürün döndürülür
    {
        db=getReadableDatabase();
        String komut="SELECT ITEMS.ITEMNAME FROM ORDERDETAILS\n" + //adet satış miktarı orderdetaıls tablosunda olduğunda  ve ıtemle orderdetaılsın bağı olmadığından
                "JOIN ORDERS ON ORDERS.ID=ORDERDETAILS.ORDERID\n" +   //orders,orderdetaıls tablosunu joınler.ıtem adı ve userıd gerektiği içinde bu tablolar joınlenır
                "JOIN USERS ON USERS.ID=ORDERS.USERID\n" +
                "JOIN ITEMS ON ITEMS.ID=ORDERDETAILS.ITEMID\n" +
                "WHERE USERS.ID="+userId+"\n" +
                "ORDER BY ORDERDETAILS.AMOUNT DESC LIMIT 1";  //alınan urunler coktan aza sıralanır ve tablodaki ilk elaman sorgulanır
        return this.veriTabaniOkumaTek(komut);
    }

    public int MusteriyeGoreAlimSiralamasi_₺(String userId) { //encok alım yapan müşteriler  id'lerine göre çoktan aza sıralanır
        db = getReadableDatabase();
        String komut = "SELECT USERS.ID,SUM(TOTALPRICE) FROM ORDERS\n" +
                "JOIN USERS ON USERS.ID=ORDERS.USERID\n" +
                "JOIN ADDRESS ON ADDRESS.ID=ORDERS.ADDRESSID\n" +
                "JOIN CITIES ON CITIES.ID=ADDRESS.CITYID\n" +
                "GROUP BY USERS.NAMESURNAME\n" +
                "ORDER BY SUM(TOTALPRICE) DESC";
        ArrayList<String> siralama = this.veriTabaniOkumaSiralama(komut); //sıralanmıs tablo listeye eklenir ve müşterinin bu tablodaki sırası aranır
        int i=0;
        for(i=0;i<siralama.size();i++)
        {
            if(siralama.get(i).equals(userId))
                break;//konum bulununca fonksiyondan çıkılır i'nin son değeri tutularak

             //konum bulunana kadar 1 artırılarak bakılır
        }
        int konum=i+1; //i 0dan başladığı için 1 fazlası konumu verir
        return konum;
    }



    public String ToplamAlim_₺(String userId)
    {   //musterinin toplam kaç tl'lik alışveriş yaptığını döndürür
        db=getReadableDatabase();
        String komut="SELECT SUM(TOTALPRICE)/10000 FROM ORDERS\n" +
                "JOIN USERS ON ORDERS.USERID=USERS.ID\n" +
                "WHERE USERS.ID="+userId+"\n" +
                "GROUP BY USERS.USERNAME_";
        return this.veriTabaniOkumaTek(komut);
    }

    public String sonAdres(String userId) //sipariş verilen son adres döndürülür
    {
        db=getReadableDatabase();
        String komut="SELECT ADDRESS.ADDRESSTEXT FROM INVOICES \n" +
                "JOIN ADDRESS ON ADDRESS.ID=INVOICES.ADDRESSID\n" +
                "JOIN USERS ON USERS.ID=ADDRESS.USERID\n" +
                "\n" +
                "WHERE USERS.ID="+userId+"\n" +
                "ORDER BY INVOICES.DATE_ DESC LIMIT 1";
        return this.veriTabaniOkumaTek(komut);
    }


    public List<DataEntry> EnCokAlimYapilanGunler_₺(String userId)
    {
        db=getReadableDatabase();
        String komut="\n" +
                "SELECT STRFTIME('%Y %m %d',INVOICES.DATE_),SUM(INVOICES.TOTALPRICE)/10000 FROM INVOICES \n" +
                "JOIN ORDERS ON ORDERS.ID=INVOICES.ORDERID\n" +
                "JOIN USERS ON USERS.ID=ORDERS.USERID\n" +

                "WHERE USERS.ID="+userId+" AND INVOICES.TOTALPRICE<>0   " +
                "GROUP BY STRFTIME('%Y %m %d',INVOICES.DATE_)"+
                " ORDER BY STRFTIME('%Y %m %d',INVOICES.DATE_) DESC LIMIT 5\n";

        return this.veriTabaniOkumaCift(komut);
    }


    public List<DataEntry> AdedeGoreKategori(String userId)
    {
        db=getReadableDatabase();
        String komut="\n" +
                "SELECT   ITEMS.CATEGORY1,SUM(ORDERDETAILS.AMOUNT) FROM ORDERDETAILS \n" +
                "                JOIN ORDERS  ON ORDERS.ID=ORDERDETAILS.ORDERID \n" +
                "                JOIN ITEMS ON ITEMS.ID=ORDERDETAILS.ITEMID \n" +
                "                WHERE ORDERS.USERID=" +userId+
                "                GROUP BY ITEMS.CATEGORY1 "+
                "ORDER BY SUM(ORDERDETAILS.AMOUNT) DESC LIMIT 5";

        return this.veriTabaniOkumaCift(komut);
    }

    public  List<DataEntry> AylaraGoreAlim(String userId)
    {
        db=getReadableDatabase();
        String komut="SELECT strftime('%Y %m',ORDERS.DATE_),SUM(TOTALPRICE)/10000 FROM ORDERS " +
                "WHERE ORDERS.USERID=" +userId+
                " GROUP BY strftime('%Y-%m',ORDERS.DATE_)"+
                " ORDER BY strftime('%Y-%m',ORDERS.DATE_) ";

        return this.veriTabaniOkumaCift(komut);
    }


    }









