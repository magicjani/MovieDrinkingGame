package com.jadi.moviedrinkinggame;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.jadi.moviedrinkinggame.com.google.zxing.integration.android.IntentIntegrator;
import com.jadi.moviedrinkinggame.com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements OnClickListener
{
    public final static String prenesiFilm = "com.jadi.moviedrinkinggame.prenesenifilm";

    private Button scanBtn;
    private TextView tvFormat, tvContent;

    final BazaPomocnik bp = new BazaPomocnik(this);

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        scanBtn = (Button)findViewById(R.id.buttonScan);
        //tvFormat = (TextView)findViewById(R.id.textViewScanFormat);
        //tvContent = (TextView)findViewById(R.id.textViewScanContent);
        scanBtn.setOnClickListener(this);

        final ListView lv = (ListView) findViewById(R.id.listViewTest);

        //Dodajanje podatkov
        bp.dodajFilm(new Film(1, "film", "Avengers", "Science Fiction", 8, "Assemble!", 140));
        bp.dodajFilm(new Film(2, "film", "Iron Man", "Science Fiction", 7, "Tony Stark was able to build this in a cave!", 130));
        bp.dodajFilm(new Film(3, "film", "Shutter Island", "Mystery", 9, "Who is mad?", 120));
        bp.dodajFilm(new Film(4, "serija", "Game of Thrones S02E06", "ZF", 9, "Tu pa ne držimo vrat!", 55));
        bp.dodajFilm(new Film(5, "serija", "Gotham S02E10", "ZF", 7, "Batman.", 44));

        bp.dodajDogodek(new Dogodek(1, "Videl si Nick Furyja", "Spij malo vode za uvajanje", 5, 10, 1));
        bp.dodajDogodek(new Dogodek(2, "Videl si Lokija", "Na vrsti je 1 shot (vode, seveda)", 20, 15, 1));
        bp.dodajDogodek(new Dogodek(3, "Videl si Iron Mana", "Naredi 10 počepov", 10, 20, 1));
        bp.dodajDogodek(new Dogodek(4, "Hulk je podivjal?!?", "Spij 2 dcl vode za živce", 9, 25, 1));
        bp.dodajDogodek(new Dogodek(5, "Konec filma", "Eksaj vodo do konca!", 7, 30, 1));

        bp.dodajDogodek(new Dogodek(6, "Iron Man je ujet", "Spij malo vode, saj si v puščavi!", 9, 10, 2));
        bp.dodajDogodek(new Dogodek(7, "Prvi oklep", "Na vrsti je 1 shot (vode, seveda)", 7, 20, 2));
        bp.dodajDogodek(new Dogodek(8, "Zmrznil si", "Dodaj led v kozarec vode", 15, 25, 2));
        bp.dodajDogodek(new Dogodek(9, "Tonyju Starku je to uspelo narediti v jami!", "EKSAJ!", 6, 30, 2));
        bp.dodajDogodek(new Dogodek(10, "Eksplozija", "Bomba vode", 8, 80, 2));
        bp.dodajDogodek(new Dogodek(11, "Že konec??", "Odnesi vodo in jo zlij", 8, 80, 2));

        bp.dodajDogodek(new Dogodek(12, "Doktor prispel na otok.", "Morska bolezen - spij vodo", 5, 10, 3));
        bp.dodajDogodek(new Dogodek(13, "Doktor razstreli avto.", "Adijo.", 5, 15, 3));
        bp.dodajDogodek(new Dogodek(14, "Doktor je norec?!?", "Eksaj.", 5, 20, 3));

        //bp.izbrisiVseFilme();
        //bp.izbrisiVseDogodke();

        ArrayList<Film> vsiFilmi = bp.beriVseFilme();
        FilmAdapter filmAdapter = new FilmAdapter(this, vsiFilmi);
        lv.setAdapter(filmAdapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                Film izbraniFilm = (Film) (lv.getItemAtPosition(position));

                //Prenesi na naslednji Activity
                Intent intentNov = new Intent(MainActivity.this, DogodkiActivity.class);
                intentNov.putExtra(prenesiFilm, String.valueOf(izbraniFilm.idFilm)+ ": " + izbraniFilm.naslov);
                startActivity(intentNov);
            }
        });
    }

    public void onClick(View v)
    {
        if(v.getId()==R.id.buttonScan)
        {
            IntentIntegrator scanIntegrator = new IntentIntegrator(this);
            scanIntegrator.initiateScan();
        }
    }
    public void onActivityResult(int requestCode, int resultCode, Intent intent)
    {
        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);

        if (scanningResult != null)
        {
            String scanContent = scanningResult.getContents();
            String scanFormat = scanningResult.getFormatName();
            //tvFormat.setText("FORMAT: " + scanFormat);
            //tvContent.setText("CONTENT: " + scanContent);

            //Preberi id skeniranega filma
            Film izbraniFilm = bp.beriFilm(Integer.parseInt(scanContent));

            //Prenesi na naslednji Activity
            Intent intentNov = new Intent(MainActivity.this, DogodkiActivity.class);
            intentNov.putExtra(prenesiFilm, String.valueOf(izbraniFilm.idFilm)+ ": " + izbraniFilm.naslov);
            startActivity(intentNov);
        }
        else
        {
            Toast toast = Toast.makeText(getApplicationContext(), "No scan data received!", Toast.LENGTH_SHORT); toast.show();
        }
    }
}