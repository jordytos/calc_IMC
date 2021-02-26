package com.example.calc_imc;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    Button envoyer = null;
    Button reset = null;
    EditText taille = null;
    EditText poids = null;
    CheckBox commentaire = null;
    RadioGroup group = null;
    TextView result = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        envoyer = (Button)findViewById(R.id.calcul);
        reset = (Button)findViewById(R.id.reset);
        taille = (EditText)findViewById(R.id.taille);
        poids = (EditText)findViewById(R.id.poids);
        commentaire = (CheckBox)findViewById(R.id.commentaire);
        group = (RadioGroup)findViewById(R.id.group);
        result = (TextView)findViewById(R.id.result);

        // On attribue un listener adapté aux vues qui en ont besoin
        envoyer.setOnClickListener(envoyerListener);
        reset.setOnClickListener(resetListener);
        commentaire.setOnClickListener(checkedListener);
        taille.addTextChangedListener(textWatcher);
        poids.addTextChangedListener(textWatcher);

    }

    private View.OnClickListener envoyerListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            //  on récupère la taille
            String t = taille.getText().toString();
            // On récupère le poids
            String p = poids.getText().toString();
            float tValue = Float.valueOf(t);



            // Puis on vérifie que la taille est cohérente
            if(tValue <= 0)
                Toast.makeText(MainActivity.this, "La taille doit être positive", Toast.LENGTH_SHORT).show();
            else {
                float pValue = Float.valueOf(p);

                if(pValue <= 0)
                    Toast.makeText(MainActivity.this, "Le poids doit etre positif", Toast.LENGTH_SHORT).show();

                else {
                    // Si l'utilisateur a indiqué que la taille était en centimètres
                    // On vérifie que la Checkbox sélectionnée est la deuxième à l'aide de son identifiant
                    if((t.contains(",")) || (t.contains(".")) ){
                        group.check(R.id.radio_metre);
                    }

                    if (group.getCheckedRadioButtonId() == R.id.radio_centimetre) tValue = tValue / 100;
                    if(group.getCheckedRadioButtonId() == R.id.radio_metre) tValue = tValue / 100*100;

                    float imc = pValue / (tValue * tValue);

                    String resultat="Votre IMC est " + imc+" : ";

                    if(commentaire.isChecked()) {
                        resultat += interpreteIMC(imc);
                    }

                    result.setText(resultat);
                }
            }
        }
    };


    private View.OnClickListener resetListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            poids.getText().clear();
            taille.getText().clear();
            result.setText(texteInit);
        }
    };

    private View.OnClickListener checkedListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            if(((CheckBox)v).isChecked()) {
                result.setText(texteInit);
            }
        }
    };

    private TextWatcher textWatcher = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            result.setText(texteInit);
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

        @Override
        public void afterTextChanged(Editable s) {}
    };


    public String interpreteIMC(Float imc){
        String resu;
        if(imc < 16.5)  resu = " famine";

        else if( 16.5 >= imc  || imc < 18.5 ) {
            resu = " maigreur";
        }
        else if( 18.5 >= imc  || imc < 25 ) {
            resu = " corpulence normale";
        }
        else if( 25 >= imc  || imc < 30 ) {
            resu = " surpoids";
        }
        else if( 30 >= imc  || imc < 35 ) {
            resu = " obésité modérée";
        }
        else if( 35 >= imc  || imc < 40 ) {
            resu = " obsésité sévère";
        }
        else {
            return resu =" obésité morbide ou massive";
        }

        return resu;
    }

    private final String texteInit = "Cliquez sur le bouton « Calculer l'IMC » pour obtenir un résultat.";
    public void popUp(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}