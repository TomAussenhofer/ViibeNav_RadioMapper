package de.beacon.tom.viibenav_radiomapper.model.widget;

/**
 * Created by TomTheBomb on 30.08.2015.
 */
import android.content.Context;
import android.util.AttributeSet;
import android.widget.AutoCompleteTextView;

public class InstantAutoComplete extends AutoCompleteTextView {

    private int myThreshold;

    public InstantAutoComplete(Context context) {
        super(context);
    }

    public InstantAutoComplete(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public InstantAutoComplete(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setThreshold(int threshold) {
        if (threshold < 0) {
            threshold = 0;
        }
        myThreshold = threshold;
    }

    @Override
    public boolean enoughToFilter() {
        return getText().length() >= myThreshold;
    }

    @Override
    public int getThreshold() {
        return myThreshold;
    }

}