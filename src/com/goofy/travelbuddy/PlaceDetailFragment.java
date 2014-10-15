package com.goofy.travelbuddy;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import com.goofy.models.Photo;
import com.goofy.travelbuddy.dao.PhotosDataSource;

import android.app.Fragment;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.os.Bundle;
import android.util.FloatMath;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class PlaceDetailFragment extends Fragment  implements OnTouchListener {

	List<Photo> photos;
	String placeTitle;
	ArrayList<String> visitors;
	Context fragmentContext;
	View fragmentView;
	int placeId = 0;
	private PhotosDataSource photosDatasource;
	
	 private static final String TAG = "Touch";
	    @SuppressWarnings("unused")
	    private static final float MIN_ZOOM = 1f,MAX_ZOOM = 1f;

	    // These matrices will be used to scale points of the image
	    Matrix matrix = new Matrix();
	    Matrix savedMatrix = new Matrix();

	    // The 3 states (events) which the user is trying to perform
	    static final int NONE = 0;
	    static final int DRAG = 1;
	    static final int ZOOM = 2;
	    int mode = NONE;

	    // these PointF objects are used to record the point(s) the user is touching
	    PointF start = new PointF();
	    PointF mid = new PointF();
	    float oldDist = 1f;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		if(savedInstanceState != null){
		    placeId = savedInstanceState.getInt("PLACEID", 0);
		    placeTitle = savedInstanceState.getString("PLACE_TITLE");
		    visitors = savedInstanceState.getStringArrayList("VISITORS");
		}
		
		View view = inflater.inflate(R.layout.place_details_fragment,
				container, false);
		this.photos = new ArrayList<Photo>();
		this.fragmentView = view;
		this.fragmentContext = view.getContext();
		
		// TODO Add real photos
		addFakePhotos();
		//photosDatasource = new PhotosDataSource(fragmentContext);
		// photosDatasource.open();
		//photos = photosDatasource.getPhotosByPlaceId(placeId);
		// photosDatasource.close();
		TextView placeDetailsTitle = (TextView) view.findViewById(R.id.placedetailstitle);
		placeDetailsTitle.setText(placeTitle);
		
		
		// The Gallery
		Gallery gallery = (Gallery) view.findViewById(R.id.placedetailsgallery);
		gallery.setAdapter(new GalleryPhotosAdapter(this.fragmentContext,
				this.photos));
		gallery.setOnItemClickListener(new OnItemClickListener() {
			
			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {
				// display the images selected
				ImageView imageView = (ImageView) fragmentView
						.findViewById(R.id.selectedgalleryphoto);
				TextView photoText = (TextView) fragmentView.findViewById(R.id.imagetitletext);
				
				byte[] image = photos.get(position).getImage();
				imageView.setImageBitmap(BitmapFactory.decodeByteArray(image,
						0, image.length));
				
				photoText.setText(photos.get(position).getName());
			}
		});

		ImageView imageView = (ImageView) fragmentView
				.findViewById(R.id.selectedgalleryphoto);
		imageView.setOnTouchListener(this);
		return fragmentView;
	}

	private void addFakePhotos() {
		this.placeTitle = "Some place";
	    this.photos.add( new Photo(0, "Faky", getResourceBitmap(R.drawable.ic_launcher), "0", 0));
	    this.photos.add( new Photo(1, "Faky delete", getResourceBitmap(android.R.drawable.ic_menu_delete), "0", 0));
	    this.photos.add( new Photo(2, "Faky camea", getResourceBitmap(android.R.drawable.ic_menu_camera), "0", 0));
	    this.photos.add( new Photo(3, "Faky call", getResourceBitmap(android.R.drawable.sym_call_missed), "0", 0));
	    this.photos.add( new Photo(4, "Faky star", getResourceBitmap(android.R.drawable.btn_star_big_on), "0", 0));
	}

	private byte[] getResourceBitmap(int drawableId) {
		Bitmap bitmap = BitmapFactory.decodeResource(getResources(), drawableId);
		
		
	    ByteArrayOutputStream stream = new ByteArrayOutputStream();
	    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
	    
	    
	    byte[] bitmapdata = stream.toByteArray();
		return bitmapdata;
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		ImageView view = (ImageView) v;
        view.setScaleType(ImageView.ScaleType.MATRIX);
        float scale;

        dumpEvent(event);
        // Handle touch events here...

        switch (event.getAction() & MotionEvent.ACTION_MASK) 
        {
            case MotionEvent.ACTION_DOWN:   // first finger down only
                                                savedMatrix.set(matrix);
                                                start.set(event.getX(), event.getY());
                                                Log.d(TAG, "mode=DRAG"); // write to LogCat
                                                mode = DRAG;
                                                break;

            case MotionEvent.ACTION_UP: // first finger lifted

            case MotionEvent.ACTION_POINTER_UP: // second finger lifted

                                                mode = NONE;
                                                Log.d(TAG, "mode=NONE");
                                                break;

            case MotionEvent.ACTION_POINTER_DOWN: // first and second finger down

                                                oldDist = spacing(event);
                                                Log.d(TAG, "oldDist=" + oldDist);
                                                if (oldDist > 5f) {
                                                    savedMatrix.set(matrix);
                                                    midPoint(mid, event);
                                                    mode = ZOOM;
                                                    Log.d(TAG, "mode=ZOOM");
                                                }
                                                break;

            case MotionEvent.ACTION_MOVE:

                                                if (mode == DRAG) 
                                                { 
                                                    matrix.set(savedMatrix);
                                                    matrix.postTranslate(event.getX() - start.x, event.getY() - start.y); // create the transformation in the matrix  of points
                                                } 
                                                else if (mode == ZOOM) 
                                                { 
                                                    // pinch zooming
                                                    float newDist = spacing(event);
                                                    Log.d(TAG, "newDist=" + newDist);
                                                    if (newDist > 5f) 
                                                    {
                                                        matrix.set(savedMatrix);
                                                        scale = newDist / oldDist; // setting the scaling of the
                                                                                    // matrix...if scale > 1 means
                                                                                    // zoom in...if scale < 1 means
                                                                                    // zoom out
                                                        matrix.postScale(scale, scale, mid.x, mid.y);
                                                    }
                                                }
                                                break;
        }

        view.setImageMatrix(matrix); // display the transformation on screen

        return true; // indicate event was handled
    }
	  /*
     * --------------------------------------------------------------------------
     * Method: spacing Parameters: MotionEvent Returns: float Description:
     * checks the spacing between the two fingers on touch
     * ----------------------------------------------------
     */

    private float spacing(MotionEvent event) 
    {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return FloatMath.sqrt(x * x + y * y);
    }

    /*
     * --------------------------------------------------------------------------
     * Method: midPoint Parameters: PointF object, MotionEvent Returns: void
     * Description: calculates the midpoint between the two fingers
     * ------------------------------------------------------------
     */

    private void midPoint(PointF point, MotionEvent event) 
    {
        float x = event.getX(0) + event.getX(1);
        float y = event.getY(0) + event.getY(1);
        point.set(x / 2, y / 2);
    }

    /** Show an event in the LogCat view, for debugging */
    private void dumpEvent(MotionEvent event) 
    {
        String names[] = { "DOWN", "UP", "MOVE", "CANCEL", "OUTSIDE","POINTER_DOWN", "POINTER_UP", "7?", "8?", "9?" };
        StringBuilder sb = new StringBuilder();
        int action = event.getAction();
        int actionCode = action & MotionEvent.ACTION_MASK;
        sb.append("event ACTION_").append(names[actionCode]);

        if (actionCode == MotionEvent.ACTION_POINTER_DOWN || actionCode == MotionEvent.ACTION_POINTER_UP) 
        {
            sb.append("(pid ").append(action >> MotionEvent.ACTION_POINTER_ID_SHIFT);
            sb.append(")");
        }

        sb.append("[");
        for (int i = 0; i < event.getPointerCount(); i++) 
        {
            sb.append("#").append(i);
            sb.append("(pid ").append(event.getPointerId(i));
            sb.append(")=").append((int) event.getX(i));
            sb.append(",").append((int) event.getY(i));
            if (i + 1 < event.getPointerCount())
                sb.append(";");
        }

        sb.append("]");
        Log.d("Touch Events ---------", sb.toString());
    }

}
