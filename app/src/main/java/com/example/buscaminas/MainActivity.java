package com.example.buscaminas;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {


    TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv = findViewById(R.id.textViewA);

        AnimarTexto(tv);


    }


    public void Facil(View view)
    {

        Intent i = new Intent(this, Buscaminas.class);
        i.putExtra("dificultad","Facil");
        startActivity(i);


    }

    public void Medio(View view)
    {

        Intent i = new Intent(this, Buscaminas.class);
        i.putExtra("dificultad","Medio");
        startActivity(i);



    }

    public void Dificil(View view)
    {

        Intent i = new Intent(this, Buscaminas.class);
        i.putExtra("dificultad","Dificil");
        startActivity(i);


    }

    public void AnimarTexto(View v)
    {

        final Interpolator interpolador = AnimationUtils.loadInterpolator(getBaseContext(),
                android.R.interpolator.fast_out_slow_in);


        v.animate()
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
                        v.animate()
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

