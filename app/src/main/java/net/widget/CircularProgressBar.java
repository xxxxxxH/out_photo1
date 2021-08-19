package net.widget;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import net.basicmodel.R;

public class CircularProgressBar extends Dialog {

    Activity activity;

    public CircularProgressBar(Activity activity) {
        super(activity);
        this.activity = activity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_circular_progressbar);

        getWindow().setBackgroundDrawable(new ColorDrawable (android.graphics.Color.TRANSPARENT));

        ViewGroup.LayoutParams params = getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.MATCH_PARENT;

        getWindow().setAttributes((WindowManager.LayoutParams) params);

        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.dimAmount = 0.5f;
        getWindow().setAttributes(lp);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND);

        setCancelable(false);

        setOnCancelListener(new OnCancelListener () {
            @Override
            public void onCancel(DialogInterface dialog) {
                dismiss();
            }
        });

    }

}
