package com.polkadotninja.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.widget.ImageView;

public class AndroidTools {
  public static void setPic(Context activity, Uri currentPhotoUri, ImageView imageView) {
    setPic(getRealPathFromURI(activity,  currentPhotoUri), imageView);
  }
  
  public static void setPic(String currentPhotoPath, ImageView imageView) {
    // Get the dimensions of the View
    int targetW = imageView.getWidth();
    int targetH = imageView.getHeight();

    // Get the dimensions of the bitmap
    BitmapFactory.Options bmOptions = new BitmapFactory.Options();
    bmOptions.inJustDecodeBounds = true;
    BitmapFactory.decodeFile(currentPhotoPath, bmOptions);
    int photoW = bmOptions.outWidth;
    int photoH = bmOptions.outHeight;

    // Determine how much to scale down the image
    int scaleFactor = Math.min(photoW/targetW, photoH/targetH);

    // Decode the image file into a Bitmap sized to fill the View
    bmOptions.inJustDecodeBounds = false;
    bmOptions.inSampleSize = scaleFactor;
    bmOptions.inPurgeable = true;

    Bitmap bitmap = BitmapFactory.decodeFile(currentPhotoPath, bmOptions);
    imageView.setImageBitmap(bitmap);
  }
  
  public static Bitmap getBitmapFromCameraData(Intent data, Activity context){
    Uri selectedImage = data.getData();
    String[] filePathColumn = { MediaStore.Images.Media.DATA };
    Cursor cursor = context.getContentResolver().query(selectedImage,filePathColumn, null, null, null);
    cursor.moveToFirst();
    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
    String picturePath = cursor.getString(columnIndex);
    cursor.close();
    return BitmapFactory.decodeFile(picturePath);
  }
  
  public static String getRealPathFromURI(Context context, Uri contentUri) {
    Cursor cursor = null;
    try { 
      String[] proj = { MediaStore.Images.Media.DATA };
      cursor = context.getContentResolver().query(contentUri,  proj, null, null, null);
      int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
      cursor.moveToFirst();
      return cursor.getString(column_index);
    } finally {
      if (cursor != null) {
        cursor.close();
      }
    }
  }
}
