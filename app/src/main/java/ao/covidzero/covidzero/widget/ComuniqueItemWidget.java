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


public class ComuniqueItemWidget extends LinearLayout {

    View view;

    public ComuniqueItemWidget(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        LayoutInflater layoutInflater = LayoutInflater.from(context);
        view = layoutInflater.inflate(R.layout.item_metodos_comunique, this);

        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.ComuniqueItemWidget,
                0, 0);

        try {
            int ic = a.getResourceId(R.styleable.ComuniqueItemWidget_icone_3, 0);
            int ic2 = a.getResourceId(R.styleable.ComuniqueItemWidget_icone_4, 0);

            ( (TextView) view.findViewById(R.id.titulo) ).setText( a.getString(R.styleable.ComuniqueItemWidget_titulo) );
            ( (TextView) view.findViewById(R.id.texto) ).setText( a.getString(R.styleable.ComuniqueItemWidget_subtitulo) );
            ( (ImageView) view.findViewById(R.id.icone) ).setImageResource( ic );
            ( (ImageView) view.findViewById(R.id.icone2) ).setImageResource( ic2 );

        } finally {
            a.recycle();
        }

    }
}
