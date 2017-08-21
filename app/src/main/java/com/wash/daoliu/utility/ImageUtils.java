package com.wash.daoliu.utility;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.provider.MediaStore;
import java.io.File;
import java.io.FileNotFoundException;

public class ImageUtils {
	private static ImageUtils mImageUtils = null;
//	private static int sWidth = 0;
	private ImageUtils(){

	}
	public static ImageUtils getInstance(){
		if(mImageUtils==null){
			mImageUtils = new ImageUtils();
		}
		return mImageUtils;
	}

	public static Bitmap resizeImage(Bitmap bitmap, int w, int h)   
	{    
		Bitmap BitmapOrg = bitmap;    
		int width = BitmapOrg.getWidth();    
		int height = BitmapOrg.getHeight();    
		int newWidth = w;    
		int newHeight = h;    

		float scaleWidth = ((float) newWidth) / width;    
		float scaleHeight = ((float) newHeight) / height;    

		Matrix matrix = new Matrix();    
		matrix.postScale(scaleWidth, scaleHeight);    
		// if you want to rotate the Bitmap     
		// matrix.postRotate(45);     
		Bitmap resizedBitmap = Bitmap.createBitmap(BitmapOrg, 0, 0, width,    
				height, matrix, true);    
		return resizedBitmap;    
	}  



	// 保存图片到相册
	public static void saveImageToGallery(Context context, File file ){
	// 把文件插入到系统图库
		try{
			MediaStore.Images.Media.insertImage(context.getContentResolver(), file.getAbsolutePath(), file.getName(), null);
		}catch(FileNotFoundException e){
			e.printStackTrace();
		}
		// 通知图库更新
		context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" +file.getAbsolutePath() )));

	}

}
