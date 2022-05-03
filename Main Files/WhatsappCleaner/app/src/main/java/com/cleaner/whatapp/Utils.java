package com.cleaner.whatapp;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.regex.Pattern;

public class Utils {

    static boolean clicked=false;
    public static int LOGIN=-1;
    public static int GIRL_LOGIN=-1;
    public static AlertDialog mAlertDialog;
    public static void SetFont(TextView textView,Activity activity)
    {
        Typeface font = Typeface.createFromAsset(activity.getAssets(), "lucida.ttf");
        textView.setTypeface(font);
    }
    public static void SetFont(Button button,Activity activity)
    {
        Typeface font = Typeface.createFromAsset(activity.getAssets(), "lucida.ttf");
        button.setTypeface(font);
    }
    public static void SetFont(EditText editText,Activity activity)
    {
        Typeface font = Typeface.createFromAsset(activity.getAssets(), "lucida.ttf");
        editText.setTypeface(font);
    }
    public static ProgressDialog SetProgressBar(ProgressDialog mDialog, Activity activity)
    {
        mDialog=ProgressDialog.show(activity, "","");
        mDialog.setContentView(R.layout.progress_bar);
        mDialog.setCancelable(false);
        mDialog.setIndeterminate(false);
        mDialog.show();
        return mDialog;
    }
    public static ProgressDialog SetProgressBar1(ProgressDialog mDialog, Activity activity)
    {
        //		mDialog=ProgressDialog.show(activity, "","");
        mDialog.setContentView(R.layout.progress_bar);
        mDialog.setCancelable(true);
        mDialog.setIndeterminate(false);
        mDialog.show();
        return mDialog;
    }
    public static void CopyStream(InputStream is, OutputStream os) {
        final int buffer_size = 1024;
        try {
            byte[] bytes = new byte[buffer_size];
            for (;;) {
                int count = is.read(bytes, 0, buffer_size);
                if (count == -1)
                    break;
                os.write(bytes, 0, count);
            }
        } catch (Exception ex) {
        }
    }
    public static void SetDiolog(Activity activity,String message)
    {
        mAlertDialog=new AlertDialog.Builder(activity).create();
        mAlertDialog.setMessage(message);

        mAlertDialog.setButton("OK",new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        mAlertDialog.show();
    }

    public final static Pattern EMAIL_ADDRESS_PATTERN = Pattern.compile(
            "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                    "\\@" +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                    "(" +
                    "\\." +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                    ")+"
    );
    public static boolean checkEmail(String email) {
        return EMAIL_ADDRESS_PATTERN.matcher(email).matches();
    }
    public static boolean isConnectingToInternet(Context context){
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null)
        {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED)
                    {
                        return true;
                    }

        }
        return false;
    }
    public static void SetDiolog(Context context, String message) {

        mAlertDialog=new AlertDialog.Builder(context).create();
        mAlertDialog.setMessage(message);

        mAlertDialog.setButton("OK",new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        mAlertDialog.show();
    }
    public static Drawable tintImage(Activity act, int imagetochange, int residColor)
    {
        Drawable image = act.getResources().getDrawable(imagetochange);
        int tint = act.getResources().getColor(residColor);
        ColorFilter filter = new PorterDuffColorFilter(tint, PorterDuff.Mode.SRC_ATOP);
        image.setColorFilter(filter);

        return image;
    }
}
