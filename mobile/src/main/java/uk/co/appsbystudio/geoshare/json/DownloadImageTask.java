package uk.co.appsbystudio.geoshare.json;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import de.hdodenhof.circleimageview.CircleImageView;
import uk.co.appsbystudio.geoshare.R;

public class DownloadImageTask extends AsyncTask<String, Void, Bitmap>{

    private final CircleImageView viewById;
    private final ImageView imageViewById;
    private final Context context;

    public DownloadImageTask(CircleImageView viewById, ImageView imageViewById, Context context) {
        this.viewById = viewById;
        this.imageViewById = imageViewById;
        this.context = context;
    }

    @Override
    protected Bitmap doInBackground(String... params) {
        String url = params[0].replace(" ", "%20");
        Bitmap image_bitmap = null;

        try {
            URL urlString = new URL(url);
            HttpURLConnection httpURLConnection = (HttpURLConnection) urlString.openConnection();
            httpURLConnection.setRequestProperty("If-Modified-Since", "Mon, 30 May 2016 00:15:25 GMT");

            int statusCode = httpURLConnection.getResponseCode();

            if (statusCode == 200) {
                try {
                    InputStream inputStream = httpURLConnection.getInputStream();
                    image_bitmap = BitmapFactory.decodeStream(inputStream);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return image_bitmap;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        Bitmap default_image = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_profile_picture);

        if (viewById != null) {
            if (bitmap != null) {
                viewById.setImageBitmap(bitmap);
            } else {
                viewById.setImageBitmap(default_image);
            }
        } else {
            if (bitmap != null) {
                imageViewById.setImageBitmap(bitmap);
            } else {
                imageViewById.setImageBitmap(default_image);
            }
        }

    }
}