package ao.covidzero.covidzero.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import ao.covidzero.covidzero.R;

public class ItemRelatorio extends LinearLayout {

    public ItemRelatorio(Context context, AttributeSet attributeSet) {
        super(context);

        final Context tmpContext = context;

        // Inflate the custom widget layout xml file.
        LayoutInflater  layoutInflater = LayoutInflater.from(context);
        View v = layoutInflater.inflate(R.layout.item_relatorio, this);


        TypedArray a = context.getTheme().obtainStyledAttributes(
                attributeSet,
                R.styleable.CustomButton,
                0, 0);

        try {
            int qtd = a.getInt(R.styleable.CustomButton_qtd, 0);
            String nome = a.getString(R.styleable.CustomButton_nome);
            int ic = a.getInt(R.styleable.CustomButton_icone, 0);


            int icone = R.drawable.verified;
            int color = Color.RED;


            switch (ic){
                case 0:
                    nome = "Confirmados";
                    icone = R.drawable.verified;
                    color = Color.GREEN;
                    break;
                case 1:
                    nome = "Suspeitos";
                    icone = R.drawable.unverified;
                    break;
                case 2:
                    nome = "Recuperados";
                    icone = R.drawable.recovered;
                    color = Color.GREEN;
                    break;
                case 3:
                    nome = "Mortes";
                    icone = R.drawable.death;
                    color = Color.BLACK;
                    break;
                case 4:
                    nome = "Pessoas em Quarentena";
                    icone = R.drawable.quarentena;
                    color = Color.BLACK;
                    break;
            }

            ( (TextView) v.findViewById(R.id.nome) ).setText( nome );
            ( (TextView) v.findViewById(R.id.qtd) ).setTextColor(color);
            ( (TextView) v.findViewById(R.id.qtd) ).setText( String.valueOf(qtd) );
            ( (ImageView) v.findViewById(R.id.icone) ).setImageResource(icone);

        } finally {
            a.recycle();
        }


    }

    public void setQtd(int qtd){
        ( (TextView) findViewById(R.id.qtd) ).setText( String.valueOf(qtd) );
    }

    public void setNome(String nome){
        ( (TextView) findViewById(R.id.nome) ).setText( nome );
    }

    public void setIcone(int ic){

        int icone = R.drawable.verified;
        int color = Color.RED;


        switch (ic){
            case 0:
                icone = R.drawable.verified;
                color = Color.GREEN;
                break;
            case 1:
                icone = R.drawable.unverified;
                break;
            case 2:
                icone = R.drawable.recovered;
                color = Color.GREEN;
                break;
            case 3:
                icone = R.drawable.death;
                color = Color.BLACK;
                break;
            case 4:
                icone = R.drawable.quarentena;
                color = Color.BLACK;
                break;
        }


        ( (ImageView) findViewById(R.id.icone) ).setImageResource(icone);
        ( (TextView) findViewById(R.id.qtd) ).setTextColor(color);
    }
}
