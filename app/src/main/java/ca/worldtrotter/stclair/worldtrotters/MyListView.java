package ca.worldtrotter.stclair.worldtrotters;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * This is a custom class that extends list view
 * The only difference in the custom class is that it overrides onDraw
 * When on draw gets called, it sets the height of the listview to the height of its children
 * This makes the list view not scrollable, and always the height of its children
 * This is to make the list view look nicer in the card view that is hosting it
 */

public class MyListView extends ListView {

    private android.view.ViewGroup.LayoutParams params;
    private int oldCount = 0;

    public MyListView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        if (getCount() != oldCount)
        {
            int height = getChildAt(0).getHeight() + 1 ;
            oldCount = getCount();
            params = getLayoutParams();
            params.height = getCount() * height;
            setLayoutParams(params);
        }

        super.onDraw(canvas);
    }

}