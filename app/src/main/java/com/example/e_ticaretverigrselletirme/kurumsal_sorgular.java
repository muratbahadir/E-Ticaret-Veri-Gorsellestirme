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
//kurumsal kategorisi için oluşturulan raporların bulunduğu class
//burdaki sorgularda userıd'ye gerek olmadan kurumun tüm verilerini raporladığı için her kullanıcıya aynı raporlar gözüküyor.
public class kurumsal_sorgular extends SQLiteOpenHelper {
    SQLiteDatabase db;
    public kurumsal_sorgular(@Nullable Context context) {
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
    


    public String veriTabaniOkumaTek(String komut)  //veritabanından okunacak değerin bir tane olması durumunda kullanılır(en cok alınan ürün gibi)

    {
        Cursor c=db.rawQuery(komut,null);

        if(c.moveToFirst())
        {
            do {
                return (c.getString(0));

            }while (c.moveToNext());
        }

        return "0";
    }
    //sorgular,musterı_sorgular classındaki sorgular ile  mantıkla aynı çalışıyor
    //fiyat yazdırılan durumlarda 10000'e bölünmesinin sebebi ise random oluşturulan veritabanının MsSql'den Excel'e excelden sqlite veritabanına aktarılırken
    //virgüllü sayıları  ınteger alması oldu.
    //Örnek proje olduğu veritabanını yeniden oluşturmaktansa bu yolla hatayı düzelttim.
    public String EnCokAlinanUrun()
    {
        db=getReadableDatabase();
        String komut="SELECT ITEMS.ITEMNAME FROM ORDERDETAILS\n" +
                "JOIN ITEMS ON ITEMS.ID=ORDERDETAILS.ITEMID\n" +
                "ORDER BY ORDERDETAILS.AMOUNT DESC LIMIT 1";
        return this.veriTabaniOkumaTek(komut);
    }
    public List<DataEntry> EnCokAlinanUrunler()
    {
        db=getReadableDatabase();
        String komut="SELECT ITEMS.ITEMNAME,ORDERDETAILS.AMOUNT FROM ORDERDETAILS\n" +
                "JOIN ITEMS ON ITEMS.ID=ORDERDETAILS.ITEMID\n" +
                "ORDER BY ORDERDETAILS.AMOUNT DESC\n LIMIT 5";
        return this.veriTabaniOkumaCift(komut);
    }
    public List<DataEntry> EnCokSatisYapanKategoriler_adet()
    {
        db=getReadableDatabase();
        String komut="SELECT ITEMS.CATEGORY1,SUM(ORDERDETAILS.AMOUNT) FROM ORDERDETAILS\n" +
                "JOIN ITEMS ON ITEMS.ID=ORDERDETAILS.ITEMID\n" +
                "GROUP BY ITEMS.CATEGORY1\n" +
                "ORDER BY SUM(ORDERDETAILS.AMOUNT) DESC\n" +
                "LIMIT 5\n";
        return  this.veriTabaniOkumaCift(komut);
    }
    public List<DataEntry> EnAzSatisYapilanKategoriler_adet()
    {
        db=getReadableDatabase();
        String komut="SELECT ITEMS.CATEGORY1,SUM(ORDERDETAILS.AMOUNT) FROM ORDERDETAILS\n" +
                "JOIN ITEMS ON ITEMS.ID=ORDERDETAILS.ITEMID\n" +
                "GROUP BY ITEMS.CATEGORY1\n" +
                "ORDER BY SUM(ORDERDETAILS.AMOUNT) ASC\n" +
                "LIMIT 5\n";
        return  this.veriTabaniOkumaCift(komut);
    }
    public List<DataEntry> EnCokSatisKategori_₺()
    {
        db=getReadableDatabase();
        String komut="SELECT ITEMS.CATEGORY1,SUM(ORDERDETAILS.LINETOTAL)/10000 FROM ORDERDETAILS\n" +
                "JOIN ITEMS ON ITEMS.ID=ORDERDETAILS.ITEMID\n" +
                "GROUP BY ITEMS.CATEGORY1\n" +
                "ORDER BY SUM(ORDERDETAILS.LINETOTAL) DESC\n" +
                "LIMIT 5\n";
        return this.veriTabaniOkumaCift(komut);
    }
    public List<DataEntry> EnAzSatisKategori_₺()
    {
        db=getReadableDatabase();
        String komut="SELECT ITEMS.CATEGORY1,SUM(ORDERDETAILS.LINETOTAL)/10000 FROM ORDERDETAILS\n" +
                "JOIN ITEMS ON ITEMS.ID=ORDERDETAILS.ITEMID\n" +
                "GROUP BY ITEMS.CATEGORY1\n" +
                "ORDER BY SUM(ORDERDETAILS.LINETOTAL) asc\n" +
                "LIMIT 5\n";
        return this.veriTabaniOkumaCift(komut);

    }
    public List<DataEntry> EnPahaliBirimFiyat()
    {
        db=getReadableDatabase();
        String komut="SELECT ITEMS.ITEMNAME,ORDERDETAILS.UNITPRICE/10000 FROM ORDERDETAILS\n" +
                "JOIN ITEMS ON ITEMS.ID=ORDERDETAILS.ITEMID\n" +
                "\n" +
                "ORDER BY ORDERDETAILS.UNITPRICE DESC\n" +
                "LIMIT 5";
        return this.veriTabaniOkumaCift(komut);

    }
    public List<DataEntry> EnUcuzBirimFiyat()
    {
        db=getReadableDatabase();
        String komut="SELECT ITEMS.ITEMNAME,ORDERDETAILS.UNITPRICE FROM ORDERDETAILS\n" +
                "JOIN ITEMS ON ITEMS.ID=ORDERDETAILS.ITEMID\n" +
                "\n" +
                "ORDER BY ORDERDETAILS.UNITPRICE ASC\n" +
                "LIMIT 4";
        return this.veriTabaniOkumaCift(komut);
    }
    public List<DataEntry> EnCokSatisYapanMarkalar_adet()
    {
        db=getReadableDatabase();
        String komut="SELECT ITEMS.BRAND,ORDERDETAILS.AMOUNT FROM ORDERDETAILS\n" +
                "JOIN ITEMS ON ITEMS.ID=ORDERDETAILS.ITEMID\n" +
                "\n" +
                "ORDER BY ORDERDETAILS.AMOUNT DESC\n" +
                "LIMIT 5\n";
        return this.veriTabaniOkumaCift(komut);
    }
    public List<DataEntry> EnCokSatisYapanMarkalar_₺()
    {
        db=getReadableDatabase();
        String komut="SELECT ITEMS.BRAND,ORDERDETAILS.LINETOTAL/10000 FROM ORDERDETAILS\n" +
                "JOIN ITEMS ON ITEMS.ID=ORDERDETAILS.ITEMID\n" +
                "\n" +
                "ORDER BY ORDERDETAILS.LINETOTAL DESC\n" +
                "LIMIT 5\n";
        return this.veriTabaniOkumaCift(komut);
    }
    public List<DataEntry> EnCokSatisYapilanIller_₺()
    {
        db=getReadableDatabase();
        String komut="SELECT CITIES.CITY,SUM(ORDERDETAILS.LINETOTAL)/10000 FROM ORDERDETAILS\n" +
                "JOIN ORDERS ON ORDERDETAILS.ORDERID=ORDERS.ID\n" +
                "JOIN ADDRESS ON ADDRESS.ID=ORDERS.ADDRESSID\n" +
                "JOIN CITIES ON ADDRESS.CITYID=CITIES.ID\n" +
                "GROUP BY CITIES.CITY\n" +
                "ORDER BY SUM(ORDERDETAILS.LINETOTAL) DESC\n";
        return this.veriTabaniOkumaCift(komut);
    }
    public List<DataEntry> EnCokSatisYapilanIller_adet()
    {
        db=getReadableDatabase();
        String komut="SELECT CITIES.CITY,SUM(ORDERDETAILS.AMOUNT) FROM ORDERDETAILS\n" +
                "JOIN ORDERS ON ORDERDETAILS.ORDERID=ORDERS.ID\n" +
                "JOIN ADDRESS ON ADDRESS.ID=ORDERS.ADDRESSID\n" +
                "JOIN CITIES ON ADDRESS.CITYID=CITIES.ID\n" +
                "GROUP BY CITIES.CITY\n" +
                "ORDER BY SUM(ORDERDETAILS.AMOUNT) DESC\n";
        return this.veriTabaniOkumaCift(komut);
    }
    public List<DataEntry> EnCokSatisCinsiyet_₺()
    {
        db=getReadableDatabase();
        String komut="SELECT USERS.GENDER,SUM(ORDERDETAILS.LINETOTAL)/10000 FROM USERS\n" +
                "JOIN ORDERS ON ORDERS.USERID=USERS.ID\n" +
                "JOIN ORDERDETAILS ON ORDERDETAILS.ORDERID=ORDERS.ID\n" +
                "GROUP BY USERS.GENDER\n";
        return this.veriTabaniOkumaCift(komut);
    }
    public List<DataEntry> EnCokSatisCinsiyet_adet()
    {
        db=getReadableDatabase();
        String komut="SELECT USERS.GENDER,SUM(ORDERDETAILS.AMOUNT) FROM USERS\n" +
                "JOIN ORDERS ON ORDERS.USERID=USERS.ID\n" +
                "JOIN ORDERDETAILS ON ORDERDETAILS.ORDERID=ORDERS.ID\n" +
                "GROUP BY USERS.GENDER\n";
        return this.veriTabaniOkumaCift(komut);
    }
    public List<DataEntry> EnCokSatisYapanMarkalarKadinIcin_₺()
    {
        db=getReadableDatabase();
        String komut="SELECT ITEMS.BRAND,ORDERDETAILS.LINETOTAL/10000 FROM ORDERDETAILS\n" +
                "JOIN ITEMS ON ITEMS.ID=ORDERDETAILS.ITEMID\n" +
                "JOIN ORDERS ON ORDERS.ID=ORDERDETAILS.ORDERID "+
                "JOIN USERS ON USERS.ID=ORDERS.USERID\n" +
                "WHERE USERS.GENDER='K' "+
                "ORDER BY ORDERDETAILS.LINETOTAL DESC\n" +
                "LIMIT 5\n";
        return this.veriTabaniOkumaCift(komut);
    }
    public List<DataEntry> EnCokSatisYapanMarkalarErkekIcin_₺()
    {
        db=getReadableDatabase();
        String komut="SELECT ITEMS.BRAND,ORDERDETAILS.LINETOTAL/10000 FROM ORDERDETAILS\n" +
                "JOIN ITEMS ON ITEMS.ID=ORDERDETAILS.ITEMID\n" +
                "JOIN ORDERS ON ORDERS.ID=ORDERDETAILS.ORDERID "+
                "JOIN USERS ON USERS.ID=ORDERS.USERID\n" +
                "WHERE USERS.GENDER='E' "+
                "ORDER BY ORDERDETAILS.LINETOTAL DESC \n" +
                "LIMIT 5\n";
        return this.veriTabaniOkumaCift(komut);
    }

    public List<DataEntry> EnCokSatisYapanKategorilerKadinIcin_₺()
    {
        db=getReadableDatabase();
        String komut="SELECT ITEMS.CATEGORY1,ORDERDETAILS.LINETOTAL/10000 FROM ORDERDETAILS\n" +
                "JOIN ITEMS ON ITEMS.ID=ORDERDETAILS.ITEMID\n" +
                "JOIN ORDERS ON ORDERS.ID=ORDERDETAILS.ORDERID "+
                "JOIN USERS ON USERS.ID=ORDERS.USERID\n" +
                "WHERE USERS.GENDER='K'"+
                "ORDER BY ORDERDETAILS.LINETOTAL DESC\n" +
                "LIMIT 5\n";
        return this.veriTabaniOkumaCift(komut);
    }
    public List<DataEntry> EnCokSatisYapanKategorilerErkekIcin_₺()
    {
        db=getReadableDatabase();
        String komut="SELECT ITEMS.CATEGORY1,ORDERDETAILS.LINETOTAL/10000 FROM ORDERDETAILS\n" +
                "JOIN ITEMS ON ITEMS.ID=ORDERDETAILS.ITEMID\n" +
                "JOIN ORDERS ON ORDERS.ID=ORDERDETAILS.ORDERID "+
                "JOIN USERS ON USERS.ID=ORDERS.USERID\n" +
                "WHERE USERS.GENDER='E'"+
                "ORDER BY ORDERDETAILS.LINETOTAL DESC\n" +
                "LIMIT 5\n";
        return this.veriTabaniOkumaCift(komut);
    }
    public List<DataEntry> EnFazlaUrunAlanMusteriler()
    {
        db=getReadableDatabase();
        String komut="SELECT USERS.NAMESURNAME,SUM(ORDERDETAILS.AMOUNT) FROM USERS\n" +
                "JOIN ORDERS ON ORDERS.USERID=USERS.ID\n" +
                "JOIN ORDERDETAILS ON ORDERDETAILS.ORDERID=ORDERS.ID\n" +
                "GROUP BY USERS.NAMESURNAME\n" +
                "ORDER BY SUM(ORDERDETAILS.AMOUNT) DESC\n" +
                "LIMIT 5";
        return this.veriTabaniOkumaCift(komut);
    }
    public  List<DataEntry> AylaraGoreSatislar()
    {
        db=getReadableDatabase();
        String komut="SELECT strftime('%Y %m',ORDERS.DATE_),SUM(TOTALPRICE)/1000 FROM ORDERS " +
                " GROUP BY strftime('%Y-%m',ORDERS.DATE_)"+
                " ORDER BY strftime('%Y-%m',ORDERS.DATE_) ";

        return this.veriTabaniOkumaCift(komut);
    }
    public List<DataEntry> YillaraGoreSatislar()
    {
        db=getReadableDatabase();
        String komut="SELECT strftime('%Y',ORDERS.DATE_),SUM(TOTALPRICE)/10000 FROM ORDERS \n" +
                "                GROUP BY strftime('%Y',ORDERS.DATE_)\n" +
                "                ORDER BY strftime('%Y',ORDERS.DATE_) ";
        return this.veriTabaniOkumaCift(komut);
    }


}
