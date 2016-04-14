package endava.com.demoproject.others;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.support.annotation.NonNull;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;

import com.squareup.picasso.Transformation;

public class RoundedTransformation implements Transformation {

    private Resources res;
    private float cornerRadius;

    public RoundedTransformation(Resources res, float cornerRadius) {
        this.res = res;
        this.cornerRadius = cornerRadius;
    }

    @Override
    public Bitmap transform(Bitmap source) {
        RoundedBitmapDrawable drawable = RoundedBitmapDrawableFactory.create(res, source);
        drawable.setCornerRadius(getCornerRadius(source));
        Bitmap output = Bitmap.createBitmap(source.getWidth(), source.getHeight(), source.getConfig());
        Canvas canvas = new Canvas(output);
        drawable.setAntiAlias(true);
        drawable.setBounds(0, 0, source.getWidth(), source.getHeight());
        drawable.draw(canvas);
        if (source != output) {
            source.recycle();
        }
        return output;
    }

    @Override
    public String key() {
        return String.format("roundedRectBg[cornerRadius=%.1f]", cornerRadius);
    }

    protected float getCornerRadius(@NonNull Bitmap source) {
        return cornerRadius;
    }
}