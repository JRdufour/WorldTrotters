package ca.worldtrotter.stclair.worldtrotters;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.PlacePhotoMetadata;
import com.google.android.gms.location.places.PlacePhotoMetadataBuffer;
import com.google.android.gms.location.places.PlacePhotoMetadataResult;
import com.google.android.gms.location.places.PlacePhotoResult;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.GeoDataClient;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import ca.worldtrotter.stclair.worldtrotters.MainActivity;

/**
 * Created by Dufour on 2018-04-05.
 */

public class Helper {


    /**
     * This method takes in a place id and returns a place object from Google
     * @param id
     * @return
     */
//    public static Place findPlaceById(String id){
//
//        Place place;
//
//        if(TextUtils.isEmpty(id) || client == null || !client.isConnected()) {
//            return null;
//        }
//
//        Places.GeoDataApi.getPlaceById(client, id).setResultCallback(new ResultCallback<PlaceBuffer>() {
//            @Override
//            public void onResult(@NonNull PlaceBuffer places) {
//                if(places.getStatus().isSuccess()) {
//                    Place place = places.get(0);
//
//                }
//                // release the PlaceBuffer to prevent a memory leak
//                places.release();
//            }
//        });
//        return null;
//    }


    public static void addPlacePhoto(final Context context, final String placeId){
        final GoogleApiClient client = MainActivity.googleClient;

        Places.GeoDataApi.getPlacePhotos(client, placeId).setResultCallback(new ResultCallback<PlacePhotoMetadataResult>() {
            @Override
            public void onResult(@NonNull PlacePhotoMetadataResult placePhotoMetadataResult) {
                if(placePhotoMetadataResult.getStatus().isSuccess()){
                    PlacePhotoMetadataBuffer photoBuffer = placePhotoMetadataResult.getPhotoMetadata();

                    //Random r = new Random();
                    //PlacePhotoMetadata photoMetadata = photoBuffer.get(r.nextInt(photoBuffer.getCount()));
                    final PlacePhotoMetadata photoMetadata = photoBuffer.get(0);
                    Log.d("NUMBER OF PHTOTOS AVAILABLE IN ARRAY", photoBuffer.getCount() + "");
                    photoMetadata.getPhoto(client).setResultCallback(new ResultCallback<PlacePhotoResult>() {
                        @Override
                        public void onResult(@NonNull PlacePhotoResult placePhotoResult) {
                            //Add the photo to the phone

                            //grab the photo from Google
                            Bitmap photo = placePhotoResult.getBitmap();

                            //create a new image file
                            File photoFile = null;
                            try {
                                photoFile = createTempImageFile();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            if (photoFile.exists())
                                photoFile.delete();

                            try {
                                FileOutputStream out = new FileOutputStream(photoFile);
                                photo.compress(Bitmap.CompressFormat.JPEG, 70, out);
                                out.flush();
                                out.close();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            //get the resulting image path
                            String imagePath = photoFile.getAbsolutePath();
                            Image img = new Image(placeId, imagePath, photoMetadata.getAttributions().toString());
                            //add the image path to the database
                            DatabaseHandler db = new DatabaseHandler(context);
                            db.addImage(img);
                            db.close();
                        }
                    });
                    photoBuffer.release();
                    //photoBuffer.close();
                }
            }

        });


    }

    /**
     * This method will create a temporary file wih a unique name to store the photo on the user's device
     * @return
     * @throws IOException
     */
    public static File createTempImageFile() throws IOException {
        //make the file name
        String fileName = "worldtrotters_app_v1_" + System.currentTimeMillis() ;
        //grab the directory to save the image in
        File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File photo = File.createTempFile(fileName, ".jpg", dir);
        Log.d("FILE_NAME", photo.getAbsolutePath());
        return photo;
    }


    public static String formatDate(long l, String pattern){
        Date date = new Date(l);
        DateFormat df = new SimpleDateFormat(pattern);
        return df.format(date);
    }

    public static String formatDate(long l) {
        return formatDate(l, "MMMM d, yyyy");
    }

    public static long formatDate(int year, int month, int day){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, day);
        return calendar.getTimeInMillis();
    }

}















