package ao.covidzero.covidzero.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import ao.covidzero.covidzero.R;

public class SintomaWidget extends LinearLayout {

    View view;

    public SintomaWidget(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        LayoutInflater layoutInflater = LayoutInflater.from(context);
        view = layoutInflater.inflate(R.layout.sintomas, this);

        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.MenuItemWidget,
                0, 0);

        try {
            int ic = a.getInt(R.styleable.MenuItemWidget_icone_menu, 0);


            int icone = R.drawable.verified;

            switch (ic){
                case 0:
                    icone = R.drawable.quarentena;
                    break;
                case 1:
                    icone = R.drawable.febre;
                    break;
                case 2:
                    icone = R.drawable.respirar;
                    break;
            }

            ( (TextView) view.findViewById(R.id.nome) ).setText( a.getString(R.styleable.MenuItemWidget_titulo_menu) );
            ( (ImageView) view.findViewById(R.id.icone) ).setImageResource( icone );

        } finally {
            a.recycle();
        }

    }
}
