package kr.co.teamnova.chad.capsule;

import android.Manifest;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.LOCATION_SERVICE;
import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Chad on 2017-01-18.
 */

public class AddFragment extends Fragment {
    private final int PICK_FROM_ALBUM = 0;
    private final int GPS = 1;

    private static final String TAG = "AddFragment";

    private LocationManager locationManager;

    private OnClickAddListener mCallback;
    private EditText editContentDetails;
    private ImageView imageContent;
    private Bitmap contentImage;
    private TextView textLocation;
    private ImageButton btnLocation;
    private ImageButton btnCancel;

    private boolean useLocation = false;


    public interface OnClickAddListener {
        public void AddClickEvent();
    }

    public AddFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add, container, false);

        locationManager = (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);

        contentImage = null;
        final User loginUser = getArguments().getParcelable("login_user");
        textLocation = (TextView) view.findViewById(R.id.text_location);
        btnCancel = (ImageButton) view.findViewById(R.id.ibtn_cancel);
        editContentDetails = (EditText) view.findViewById(R.id.edit_content_details);
        imageContent = (ImageView) view.findViewById(R.id.image_content);
        imageContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                    requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, PICK_FROM_ALBUM);
                } else {
                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                    intent.putExtra("return-data", true);
                    startActivityForResult(intent, PICK_FROM_ALBUM);
                }
            }
        });
        Button btnUpload = (Button) view.findViewById(R.id.btn_add);
        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int numOfContent;
                SharedPreferences userData = getActivity().getSharedPreferences(loginUser.getEmail(), MODE_PRIVATE);
                SharedPreferences.Editor userDataEditor = userData.edit();

                if (!userData.contains("num_of_content")) {
                    userDataEditor.putInt("num_of_content", 0);
                    userDataEditor.apply();
                }

                numOfContent = userData.getInt("num_of_content", -1);
                numOfContent++;

                String contentDetail = editContentDetails.getText().toString();

                File userContentsDir = new File("/data/data/" + getActivity().getPackageName() + "/User/" + loginUser.getEmail() + "/Contents");
                if (!userContentsDir.exists()) {
                    Log.e(TAG, "userContentDir does not exist");
                }

                long currentTime = System.currentTimeMillis();
                File savefile = new File(userContentsDir.getPath() + "/" + currentTime + ".txt");

                try {
                    FileOutputStream fos = new FileOutputStream(savefile);
                    fos.write(contentDetail.getBytes());
                    fos.close();

                } catch (IOException e) {
                    Log.e("error", e.toString());
                }

                if (contentImage != null) {
                    File copyFile = new File(userContentsDir.getPath() + "/" + currentTime + ".jpg");
                    try {
                        copyFile.createNewFile();
                        BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(copyFile));
                        contentImage.compress(Bitmap.CompressFormat.JPEG, 100, out);

                        getActivity().sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(copyFile)));
                        out.flush();
                        out.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                userDataEditor.putInt("num_of_content", numOfContent);
                userDataEditor.apply();

                mCallback.AddClickEvent();
            }
        });
        btnLocation = (ImageButton) view.findViewById(R.id.ibtn_location);
        btnLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, GPS);
                } else {
                    useLocation = false;
                    textLocation.setText("수신중...");
                    btnCancel.setVisibility(View.GONE);
                    getLocation(LocationManager.GPS_PROVIDER);
                }
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textLocation.setText(R.string.str_location);
                btnCancel.setVisibility(View.GONE);
            }
        });

        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            mCallback = (OnClickAddListener) context;

        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "must implement OnArticleSelectedListener");
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != RESULT_OK) {
            contentImage = null;
            imageContent.setImageBitmap(null);
            return;
        }

        switch (requestCode) {
            case PICK_FROM_ALBUM: {
                if (data.getData() != null) {
                    try {
                        contentImage = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), data.getData());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    imageContent.setImageBitmap(contentImage);
                }
                break;
            }
        }

    }

    public void getLocation(String mode) {
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } else {
            if (mode.equals(LocationManager.GPS_PROVIDER)) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 10, mLocationListener);
                GPSTimer task = new GPSTimer();

                Timer timer = new Timer();
                timer.schedule(task, 2000);

            } else if (mode.equals(LocationManager.NETWORK_PROVIDER)) {
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 10, mLocationListener);
            }
        }
    }

    private final LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            double longitude = location.getLongitude();
            double latitude = location.getLatitude();
            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                locationManager.removeUpdates(this);
            }
            String address = getAddress(getContext(), latitude, longitude);
            if (address != null) {
                if (textLocation.getText().toString().equals("수신중...")) {
                    useLocation = true;
                    textLocation.setText(address);
                    btnCancel.setVisibility(View.VISIBLE);
                }
            }
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PICK_FROM_ALBUM:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                    intent.putExtra("return-data", true);
                    startActivityForResult(intent, PICK_FROM_ALBUM);
                } else {
                    Toast.makeText(getActivity().getApplicationContext(), "권한 거부로 인해 갤러리에 접근을 실패하였습니다.", Toast.LENGTH_LONG).show();
                }
                break;
            case GPS: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getLocation(LocationManager.GPS_PROVIDER);
                } else {
                    Toast.makeText(getActivity().getApplicationContext(), "권한 거부로 인해 위치 검색에 실패하였습니다.", Toast.LENGTH_LONG).show();
                }
                break;
            }
        }
    }

    public static String getAddress(Context mContext, double lat, double lng) {
        String nowAddress = null;
        Geocoder geocoder = new Geocoder(mContext, Locale.KOREA);
        List<Address> address;
        try {
            if (geocoder != null) {
                address = geocoder.getFromLocation(lat, lng, 1);

                if (address != null && address.size() > 0) {
                    String currentLocationAddress = address.get(0).getAddressLine(0).toString();
                    nowAddress = currentLocationAddress;
                }
            }

        } catch (IOException e) {
            Log.e(TAG, e.toString());
        }
        return nowAddress;
    }


    public class GPSTimer extends TimerTask {
        public void run() {
            try {
                getLocation(LocationManager.NETWORK_PROVIDER);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
