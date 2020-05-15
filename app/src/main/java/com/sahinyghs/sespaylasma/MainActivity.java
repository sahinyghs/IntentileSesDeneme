package com.sahinyghs.sespaylasma;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class MainActivity extends AppCompatActivity {

    Button sespaylasma;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sespaylasma = (Button) findViewById(R.id.btnsespaylas);

        //butona tıklayınca ses paylaşıyoruz
        sespaylasma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //eğer kaydedilecek klasör oluşturulmadıysa oluşturuyoruz
                File kaydedileceklasor = new File(getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)+"/Sesler/");
                if (!kaydedileceklasor.exists()){
                    kaydedileceklasor.mkdirs();
                }


                //fonskiyonumuzu hangi ses dosyasını göndermek istiyorsak onu belirterek çağıracağız
                dosyayikopyala(R.raw.ses1);

                //şu anda ses1 dosyası telefonda da bulunuyor , bulunduğu yere giderek paylaşacağız.


                //burada ses dosyasının bulunduğu yere gittik , ses dosyamızı ismi ile bulduk ve artık hazır
                String seskonumu ="file://"+ getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS) + "/Sesler/"+getResources().getResourceEntryName(R.raw.ses1)+".mp3";

                Intent intent = new Intent(Intent.ACTION_SEND);
                //audio şeklinin her türlüsünü desteklemesi için
                //bazı yerlerde bu şekilde whatsappın kabul etmediği yazar ama doğru değil, yani hata düzeltilmiş
                intent.setType("audio/*");
                //aşağıdaki kod isteğe bağlıdır yani sadece whatsapp uygulaması üzerinden paylaşıma açık
                //başka bir uygulama için yapmak istiyorsanız onun paket adını yazarsınız.
                intent.setPackage("com.whatsapp");
                intent.putExtra(Intent.EXTRA_STREAM,  Uri.parse(seskonumu));
                startActivity(Intent.createChooser(intent, "Sesi Paylaş"));
            }
        });





    }

    public void dosyayikopyala(int resId){

        //kopyalamak istediğimiz ses dosyasını belirttik
        InputStream inputStream = getResources().openRawResource(resId);
        //sesin kopyalandığı yerde de ismi aynı olsun diye ayni ismi getirttik. bunu isterseni değiştirebilirsiniz ama uzantıyı sakın unutmayın.
        String dosyaAdi = getResources().getResourceEntryName(resId)+".mp3";

        //burada dosyanın kopyalanacağı yolu gösterdik, yani dosya şuraya kopyalanacak /storage/emulated/0/Android/data/<paketadınız>/Sesler/
        String kopyalanacakyer = getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS) + "/Sesler/";

        File f = new File(dosyaAdi);

        //dosya yoksa işleme devam ediyoruz , varsa problem çıkabilir
        if(!f.exists()){
            try {
                //burada dosya yolunu  bulunduğu klasörün içine belirlediğimiz isimle kopyalanması komutunu verdik
                OutputStream outputStream = new FileOutputStream(new File(kopyalanacakyer, dosyaAdi));
                byte[] buffer = new byte[1024];
                int len;
                while((len = inputStream.read(buffer, 0, buffer.length)) != -1){
                    outputStream.write(buffer, 0, len);
                }
                inputStream.close();
                outputStream.close();
            } catch (FileNotFoundException e) {
                Log.i("Test", "Dosya Bulunamadı "+e.getMessage());
            } catch (IOException e) {
                Log.i("Test", "Hata oluştu "+e.getMessage());
            }
        }
    }

}
