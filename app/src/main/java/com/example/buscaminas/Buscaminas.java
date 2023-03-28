package com.example.buscaminas;

import android.animation.Animator;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.material.snackbar.Snackbar;


public class Buscaminas extends AppCompatActivity implements Runnable {

    private InterstitialAd mInterstitialAd;

    private static final String AD_UNIT_ID = "ca-app-pub-3500331241795624/9048428191";
    private static final String TAG = "MyActivity";

    private InterstitialAd interstitialAd;

    String patroncronometro = "%02d:%02d:%02d";

    long milisegundos;

    int sec;

    int mins;

    int hours;

    boolean iniciar = true;

    TextView visor;

    Thread CronRun;

    LinearLayout lit;

    int[][] Minas;

    int[][] Numeros;

    GridLayout grl;

    Boolean[][] Pulsado;

    Boolean[][] Abanderado;

    Boolean ganarperder = true;

    TextView textorul;

    boolean Ganar;

    int filasC;

    int minas;

    String dificultad;

    boolean contador = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buscaminas);

         dificultad = getIntent().getStringExtra("dificultad");

        System.out.println(dificultad);

        if(dificultad.equals("Facil"))
        {
            filasC = 4;
            System.out.println("A");
            minas = 5;

        }

        if(dificultad.equals("Medio"))
        {

            filasC = 6;

            minas = 7;

        }

        if(dificultad.equals("Dificil"))
        {
            filasC = 8;

            minas = 9;

        }



        grl = (GridLayout) findViewById(R.id.LayoutGrid1);

        lit = (LinearLayout) findViewById(R.id.lin);

        visor = findViewById(R.id.textView9);

        CrearBotones();

        ponerminas();

        generarmatriz();




    }





    public void CrearBotones()
    {

        System.out.println(filasC);

        System.out.println(Minas);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        grl.setColumnCount(filasC);

        grl.setRowCount(filasC);

        int numColumnas = grl.getColumnCount();

        int numFilas = grl.getRowCount();

        Pulsado = new Boolean[grl.getRowCount()][grl.getColumnCount()];

        Abanderado =new Boolean[grl.getRowCount()][grl.getColumnCount()];


        int r = 0;

        for (int i = 0; i < grl.getRowCount() * grl.getColumnCount(); i++) {

            Button bt = new Button(this);


            bt.setLayoutParams(new ViewGroup.LayoutParams((displayMetrics.widthPixels) / numColumnas, (displayMetrics.heightPixels/2) / numFilas));

            bt.setId(r);


            bt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(ganarperder && Ganar == false)
                    {
                        comprobarbotones(bt);
                        ComprobarGanar();

                        if (contador == false)
                        {

                            contador = true;

                            EmpezaraContar();

                        }

                    }

                }
            });

            bt.setOnLongClickListener(new View.OnLongClickListener()
            {
                @Override
                public boolean onLongClick(View v)
                {

                    if(ganarperder)
                    {
                        banderitas(bt);
                    }

                    return false;
                }
            });


            System.out.println(bt);

            grl.addView(bt);

            r++;

        }


    }


    public int[] NumAleatorios() {


        int aleatorio2 = 0;


        int[] fila = new int[minas];

        for (int i = 0; i < fila.length; i++) {


            do {

                aleatorio2 = (int) (Math.random() * filasC*filasC) ;

            } while (repetido(aleatorio2, fila));


            fila[i] = aleatorio2;

        }

        return fila;


    }

    public void banderitas(Button bt)
    {
        int F = bt.getId() / grl.getRowCount();
        int C = bt.getId() % grl.getColumnCount();

        if(Pulsado[F][C] == false && Abanderado[F][C] == false)
        {

            Abanderado[F][C] = true;

            bt.setText("🚩");

            bt.setBackgroundColor(Color.parseColor("#FFDF64"));

            animar2(F,C);
        }
        else if (Abanderado[F][C] == true)
        {

            Abanderado[F][C] = false;
            bt.setBackgroundColor(Color.GRAY);

            animar2(F,C);

        }





    }


    public void ponerminas() {
        int[] array = NumAleatorios();

        Minas = new int[grl.getRowCount()][grl.getColumnCount()];

        for (int i = 0; i < Minas.length; i++) {
            for (int r = 0; r < Minas[i].length; r++) {

                Minas[i][r] = 0;

            }

        }

        for (int i = 0; i < array.length; i++) {
            int x = array[i] / grl.getRowCount();

            System.out.println(array[i] / grl.getRowCount());

            int y = array[i] % grl.getColumnCount();

            Minas[x][y] = 1;


        }

    }

    public void generarmatriz() {
        Numeros = new int[grl.getRowCount()][grl.getColumnCount()];

        for (int i = 0; i < Numeros.length; i++) {
            for (int k = 0; k < Numeros[i].length; k++) {


                Pulsado[i][k] = false;

                Abanderado[i][k] = false;

                if (Minas[i][k] == 0) {

                    Numeros[i][k] = Buscarminas(i + 1, k) + Buscarminas(i - 1, k) + Buscarminas(i, k + 1) + Buscarminas(i, k - 1) + Buscarminas(i + 1, k + 1) + Buscarminas(i + 1, k - 1) + Buscarminas(i - 1, k + 1) + Buscarminas(i - 1, k - 1);


                }


            }


        }


    }

    public int Buscarminas(int x, int y) {

        int valor = 0;

        if (x >= 0 && y >= 0 && x < grl.getRowCount() && y < grl.getColumnCount()) {

            valor = Minas[x][y];

        }


        return valor;
    }


    public boolean repetido(int aleat, int[] Array) {

        boolean repetido = false;

        for (int i = 0; i < Array.length && !repetido; i++) {


            if (aleat == Array[i]) {

                repetido = true;

            }

        }


        return repetido;
    }

    public void comprobarbotones(Button bt) {

        int F = bt.getId() / grl.getRowCount();
        int C = bt.getId() % grl.getColumnCount(); //bgrl.getcolum*F+c

        if (Minas[F][C] == 1 && Abanderado[F][C] == false)
        {

            ganarperder = false;

            explotarminas();


        } else {


            ComprobarGanar();

            expandir(F,C);

        }




    }

    public void explotarminas()
    {

        for (int i = 0; i < Minas.length; i++)
        {
            for (int j = 0; j < Minas[i].length; j++)
            {

                if(Minas[i][j] == 1 )
                {

                    iniciar = false;
                    Button bt = ((Button) findViewById(ButtonID(i,j)));


                    System.out.println("Has perdido");

                    bt.setText("\uD83D\uDCA3");


                    ViewGroup v = (ViewGroup) ((ViewGroup) this
                            .findViewById(android.R.id.content)).getChildAt(0);

                    Snackbar.make(v, "Has perdido", Snackbar.LENGTH_LONG).show();


                    bt.setBackgroundColor(Color.parseColor("#FF495C"));

                    animar2(i,j);


                }

            }

        }



    }


    public void expandir( int x , int y) {


        if(x >= 0 && y >= 0 && x < grl.getColumnCount() && y <  grl.getRowCount())
        {


            System.out.println(x+""+y);

            System.out.println(Numeros[x][y]);

            if(Pulsado[x][y] != true && Abanderado[x][y] == false )
            {

                Pulsado[x][y] = true;
                if (Numeros[x][y] != 0  )
                {

                    ((Button) findViewById(ButtonID(x,y))).setText(Numeros[x][y] + "");

                    animar(x,y);



                }
                else {


                    ((Button) findViewById(ButtonID(x,y))).setText("-");
                    ((Button) findViewById(ButtonID(x,y))).setBackgroundColor(Color.GRAY);

                    final Interpolator interpolador = AnimationUtils.loadInterpolator(getBaseContext(),
                            android.R.interpolator.fast_out_slow_in);
                    Button fab = ((Button) findViewById(ButtonID(x,y)));

                    fab.animate()
                            .scaleX(0)
                            .scaleY(0)
                            .setInterpolator(interpolador)
                            .setDuration(600)
                            .setListener(new Animator.AnimatorListener() {
                                @Override
                                public void onAnimationStart(Animator animation) {
                                    //Se repite una vez por cada uno de los 600 arranques de animación
                                }

                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    //Le devuelve a su tamaño original
                                    fab.setBackgroundColor(Color.parseColor("#877B66"));
                                          ;
                                }

                                @Override
                                public void onAnimationCancel(Animator animation) {
                                    //Se repite una vez por cada uno de los 600 paros de animación
                                }

                                @Override
                                public void onAnimationRepeat(Animator animation) {

                                }
                            });




                    expandir(x+1,y);
                    expandir(x-1,y);
                    expandir(x,y+1);
                    expandir(x,y-1);
                    expandir(x+1,y+1);
                    expandir(x+1,y-1);
                    expandir(x-1,y-1);
                    expandir(x-1,y+1);




                }

            }






        }



    }

    public void animar2(int x,int y)
    {
        if(!Pulsado[x][y])
        {

            final Interpolator interpolador = AnimationUtils.loadInterpolator(getBaseContext(),
                    android.R.interpolator.fast_out_slow_in);
            Button fab = ((Button) findViewById(ButtonID(x,y)));

            fab.animate()
                    .scaleX(2)
                    .scaleY(2)
                    .setInterpolator(interpolador)
                    .setDuration(300)
                    .setListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animation) {
                            //Se repite una vez por cada uno de los 600 arranques de animación
                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            //Le devuelve a su tamaño original
                            fab.animate()
                                    .scaleX(1)
                                    .scaleY(1)
                                    .setInterpolator(interpolador)
                                    .setDuration(300)                        ;
                        }

                        @Override
                        public void onAnimationCancel(Animator animation) {
                            //Se repite una vez por cada uno de los 600 paros de animación
                        }

                        @Override
                        public void onAnimationRepeat(Animator animation) {

                        }
                    });
        }


    }

    public void animar(int x, int y)
    {
        if(!Pulsado[x][y])
        {

        final Interpolator interpolador = AnimationUtils.loadInterpolator(getBaseContext(),
                android.R.interpolator.fast_out_slow_in);
        Button fab = ((Button) findViewById(ButtonID(x,y)));

        fab.animate()
                .scaleX(0)
                .scaleY(0)
                .setInterpolator(interpolador)
                .setDuration(300)
                .setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        //Se repite una vez por cada uno de los 600 arranques de animación
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        //Le devuelve a su tamaño original
                        fab.animate()
                                .scaleX(1)
                                .scaleY(1)
                                .setInterpolator(interpolador)
                                .setDuration(300)                        ;
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                        //Se repite una vez por cada uno de los 600 paros de animación
                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });
    }

    }


    public int ButtonID(int x, int y) {

        int idr = 0;


        idr = x*grl.getColumnCount()+y;



        return idr;


    }


    public void ComprobarGanar()
    {

        Ganar = true;



        for (int i = 0; i < filasC; i++)
        {

            for (int j = 0; j < filasC ; j++)
            {

                if(Minas[i][j] != 1 )
                {


                    if(Pulsado[i][j] == false )
                    {

                        Ganar = false;

                    }



                }

            }


        }


        if(Ganar)
        {

            ViewGroup v = (ViewGroup) ((ViewGroup) this
                    .findViewById(android.R.id.content)).getChildAt(0);
            Snackbar.make(v, "Has Ganado", Snackbar.LENGTH_LONG).show();
            iniciar = false;


        }


    }

    public void volver(View view)
    {
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        loadAd();
        finish();
        Intent i = new Intent(this, Buscaminas.class);
        i.putExtra("dificultad",dificultad);

        startActivity(i);





    }

    private void loadAd() {
        Activity a = this;
        System.out.println("AAAAAAAAAAA");
        AdRequest adRequest = new AdRequest.Builder().build();
        InterstitialAd.load(
                this,
                AD_UNIT_ID,
                adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        // The mInterstitialAd reference will be null until
                        // an ad is loaded.
                        Buscaminas.this.interstitialAd = interstitialAd;
                        interstitialAd.show(a);
                        Log.i(TAG, "onAdLoaded");
                        interstitialAd.setFullScreenContentCallback(
                                new FullScreenContentCallback() {
                                    @Override
                                    public void onAdDismissedFullScreenContent() {
                                        // Called when fullscreen content is dismissed.
                                        // Make sure to set your reference to null so you don't
                                        // show it a second time.
                                        Buscaminas.this.interstitialAd = null;
                                        Log.d("TAG", "The ad was dismissed.");
                                    }

                                    @Override
                                    public void onAdFailedToShowFullScreenContent(AdError adError) {
                                        // Called when fullscreen content failed to show.
                                        // Make sure to set your reference to null so you don't
                                        // show it a second time.
                                        Buscaminas.this.interstitialAd = null;
                                        Log.d("TAG", "The ad failed to show.");
                                    }

                                    @Override
                                    public void onAdShowedFullScreenContent() {
                                        // Called when fullscreen content is shown.
                                        Log.d("TAG", "The ad was shown.");

                                    }
                                });
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error
                        Log.i(TAG, loadAdError.getMessage());
                        interstitialAd = null;

                    }
                });


    }

    public void reiniciar(View view)
    {
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        finish();
        Intent i = new Intent(this,MainActivity.class);

        startActivity(i);



    }

    private void EmpezaraContar()
    {

        iniciar = true;

        CronRun = new Thread(this);

        CronRun.start();


    }

    public void run() {


        milisegundos = System.currentTimeMillis();

        System.out.println(milisegundos);

        mins = 0;

        hours = 0;

        while (iniciar) {


            long milisegundos2 = System.currentTimeMillis();

            milisegundos2 = milisegundos2 - milisegundos;


            hours = (int) (milisegundos2%100);

            sec = (int) (milisegundos2 / 1000);


            if (sec == 60) {
                //System.out.print("AA");
                mins++;
                sec = 0;
                milisegundos = milisegundos2 + milisegundos;


            }



            visor.setText(String.format(patroncronometro, mins, sec,hours ));



        }
    }
}




