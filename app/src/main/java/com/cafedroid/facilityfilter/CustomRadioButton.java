package com.cafedroid.facilityfilter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.RadioButton;

import com.cafedroid.facilityfilter.model.Option;

public class CustomRadioButton extends RadioButton {

    private Context mContext;

    public CustomRadioButton(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        this.mContext = context;
        this.setPaddingRelative(dpToPx(12), dpToPx(12), dpToPx(12), dpToPx(12));
        this.setBackground(mContext.getDrawable(R.drawable.radio_button));
        this.setCompoundDrawablePadding(dpToPx(32));
        this.setButtonDrawable(android.R.color.transparent);
    }

    public void setOption(Option option) {
        this.setId(option.get_id());
        this.setChecked(option.isChecked());
        Drawable drawable;
        switch (option.getIcon()) {
            case "apartment":
                drawable = mContext.getDrawable(R.drawable.apartment);
                break;
            case "condo":
                drawable = mContext.getDrawable(R.drawable.condo);
                break;
            case "boat":
                drawable = mContext.getDrawable(R.drawable.boat);
                break;
            case "land":
                drawable = mContext.getDrawable(R.drawable.land);
                break;
            case "rooms":
                drawable = mContext.getDrawable(R.drawable.rooms);
                break;
            case "no-room":
                drawable = mContext.getDrawable(R.drawable.no_rooms);
                break;
            case "swimming":
                drawable = mContext.getDrawable(R.drawable.swimming);
                break;
            case "garden":
                drawable = mContext.getDrawable(R.drawable.garden);
                break;
            case "garage":
                drawable = mContext.getDrawable(R.drawable.garage);
                break;
            default:
                drawable = mContext.getDrawable(R.drawable.error_drawable);
                break;
        }
        this.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);
        this.setText(option.getName());
    }

    private int dpToPx(int dp) {
        float scale = mContext.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

}
