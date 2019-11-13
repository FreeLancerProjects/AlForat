package com.creative.share.apps.alforat.general_ui_method;

import android.net.Uri;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.databinding.BindingAdapter;

import com.creative.share.apps.alforat.R;
import com.creative.share.apps.alforat.tags.Tags;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class GeneralMethod {

    @BindingAdapter("error")
    public static void errorValidation(View view, String error) {
        if (view instanceof EditText) {
            EditText ed = (EditText) view;
            ed.setError(error);
        } else if (view instanceof TextView) {
            TextView tv = (TextView) view;
            tv.setError(error);


        }
    }

    @BindingAdapter("endPoint")
    public static void imageEndPoint(View view, String endPoint)
    {
        if (view instanceof CircleImageView)
        {
            CircleImageView circleImageView = (CircleImageView) view;
            Picasso.with(circleImageView.getContext()).load(Uri.parse(Tags.IMAGE_URL+endPoint)).fit().into(circleImageView);

        }else if (view instanceof RoundedImageView)
        {
            RoundedImageView roundedImageView = (RoundedImageView) view;

            Picasso.with(roundedImageView.getContext()).load(Uri.parse(Tags.IMAGE_URL+endPoint)).placeholder(R.drawable.logo).fit().into(roundedImageView);

        }else if (view instanceof ImageView)
        {
            ImageView imageView = (ImageView) view;

            Picasso.with(imageView.getContext()).load(Uri.parse(Tags.IMAGE_URL+endPoint)).placeholder(R.drawable.logo).fit().into(imageView);

        }
    }

    @BindingAdapter({"title","value"})
    public static void displayAmount(TextView textView,String title, String value)
    {
        if (value.isEmpty())
        {
            textView.setText(title);
        }else
            {
                String val = String.format(Locale.ENGLISH,"%.2f",Double.parseDouble(value));
                textView.setText(title+" ("+val+")");
            }
    }

    @BindingAdapter("url")
    public static void imageUrl(RoundedImageView imageView, String url)
    {
        Picasso.with(imageView.getContext()).load(Uri.parse(url)).fit().into(imageView);
    }




    @BindingAdapter("date")
    public static void date (TextView textView, String date)
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy",Locale.ENGLISH);
        String d = dateFormat.format(new Date(Long.parseLong(date)*1000));
        textView.setText(d);
    }

    @BindingAdapter("bill_date")
    public static void billDate (TextView textView, String date)
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm aa",Locale.ENGLISH);
        String d = dateFormat.format(new Date(Long.parseLong(date)*1000));
        textView.setText(d);
    }







}
