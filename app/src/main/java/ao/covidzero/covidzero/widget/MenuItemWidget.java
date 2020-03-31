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

import androidx.annotation.Nullable;

import ao.covidzero.covidzero.R;


public class MenuItemWidget extends LinearLayout {

    View view;

    public MenuItemWidget(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        LayoutInflater layoutInflater = LayoutInflater.from(context);
        view = layoutInflater.inflate(R.layout.item_metodos_prevencao, this);

        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.MenuItemWidget,
                0, 0);

        try {
            int ic = a.getInt(R.styleable.MenuItemWidget_icone_menu, 0);


            int icone = R.drawable.verified;

            switch (ic){
                case 0:
                    icone = R.drawable.lavamaos;
                    break;
                case 1:
                    icone = R.drawable.cubra;
                    break;
                case 2:
                    icone = R.drawable.aglumerados;
                    break;
                case 3:
                    icone = R.drawable.quarentena;
                    break;

            }

            ( (TextView) view.findViewById(R.id.titulo) ).setText( a.getString(R.styleable.MenuItemWidget_titulo_menu) );
            ( (TextView) view.findViewById(R.id.subtitulo) ).setText( a.getString(R.styleable.MenuItemWidget_subtitulo_menu) );
            ( (ImageView) view.findViewById(R.id.icone) ).setImageResource( icone );

        } finally {
            a.recycle();
        }

    }
}
