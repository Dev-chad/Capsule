package kr.co.teamnova.chad.capsule;

import android.Manifest;
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
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by Chad on 2017-02-15.
 */

public class AddContentActivity extends AppCompatActivity {
    private static final String TAG = "AddContentActivity";

    private final int PICK_FROM_ALBUM = 0;
    private final int GPS = 1;

    private LocationManager locationManager;

    private ImageView imageContent;
    private EditText editContentDetails;
    private TextView textLocation;
    private ImageButton btnLocation;
    private ImageButton btnCancel;
    private ImageButton ibtnImageCancel;

    private Content editContent;
    private Bitmap contentImage = null;

    private boolean useLocation = false;
    private boolean isEditMode = false;

    private User loginUser;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_content);

        loginUser = getIntent().getParcelableExtra("login_user");

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        editContentDetails = (EditText) findViewById(R.id.edit_content_details);
        textLocation = (TextView) findViewById(R.id.text_location);

        imageContent = (ImageView) findViewById(R.id.image_content);
        imageContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                    requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, PICK_FROM_ALBUM);
                } else {
                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                    intent.putExtra("return-data", true);
                    startActivityForResult(intent, PICK_FROM_ALBUM);
                }
            }
        });

        btnLocation = (ImageButton) findViewById(R.id.ibtn_location);
        btnLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(AddContentActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, GPS);
                } else {
                    useLocation = false;
                    textLocation.setText("수신중...");
                    btnCancel.setVisibility(View.GONE);
                    getLocation(LocationManager.GPS_PROVIDER);
                }
            }
        });

        btnCancel = (ImageButton) findViewById(R.id.ibtn_cancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                useLocation = false;
                textLocation.setText(R.string.str_location);
                btnCancel.setVisibility(View.GONE);
            }
        });

        editContent = getIntent().getParcelableExtra("edit_content");
        if (editContent != null) {
            isEditMode = true;
            setTitle("수정");
            if (editContent.getContentImage() != null) {
                imageContent.setImageURI(editContent.getContentImage());
                ibtnImageCancel.setVisibility(View.VISIBLE);
                try {
                    contentImage = MediaStore.Images.Media.getBitmap(getContentResolver(), editContent.getContentImage());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (editContent.getLocation().length() > 0) {
                useLocation = true;
                textLocation.setText(editContent.getLocation());
                btnCancel.setVisibility(View.VISIBLE);
            }
            editContentDetails.setText(editContent.getContentDesc());
        }

        ibtnImageCancel = (ImageButton) findViewById(R.id.ibtn_image_cancel);
        ibtnImageCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageContent.setImageBitmap(null);
                contentImage = null;
                ibtnImageCancel.setVisibility(View.GONE);
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PICK_FROM_ALBUM: {
                    if (data.getData() != null) {
                        try {
                            contentImage = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        imageContent.setImageBitmap(contentImage);
                        ibtnImageCancel.setVisibility(View.VISIBLE);
                    }
                    break;
                }
            }
        }
    }

    public void getLocation(String mode) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 100, 1, mLocationListener);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 100, 1, mLocationListener);
        }
    }

    private final LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            double longitude = location.getLongitude();
            double latitude = location.getLatitude();

            if (ActivityCompat.checkSelfPermission(AddContentActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                locationManager.removeUpdates(this);
            }

            String address = getAddress(AddContentActivity.this, latitude, longitude);
            String simpleAddress = "";
            if (address != null) {
                if (textLocation.getText().toString().equals("수신중...")) {
                    useLocation = true;
                    String[] splitAddress = address.split(" ");
                    for (int i = 1; i < splitAddress.length - 1; i++) {
                        simpleAddress += (splitAddress[i] + " ");
                    }
                    textLocation.setText(simpleAddress);
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

    public static String getByteStringForm(String origin, String separator) {
        byte[] array = origin.getBytes();
        String strByteArray = String.valueOf(array[0]);

        for (int i = 1; i < array.length; i++) {
            strByteArray += (separator + array[i]);
        }

        return strByteArray;
    }

    public static String getStringFromByteString(String target, String regex) {
        String[] strArray = target.split(regex);

        byte[] byteArray = new byte[strArray.length];

        for (int i = 0; i < strArray.length; i++) {
            byteArray[i] = Byte.valueOf(strArray[i]);
        }

        return new String(byteArray);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()){
            case R.id.btn_upload:{
                String strNewContent;
                String strImageUri;
                String desc;
                String strLocation;
                String strWeather;
                long currentTime;

                SharedPreferences spContent = getSharedPreferences("contents", MODE_PRIVATE);
                SharedPreferences.Editor spContentEditor = spContent.edit();

                if (isEditMode) {
                    currentTime = editContent.getDateToMillisecond();

                } else {
                    SharedPreferences spAccount = getSharedPreferences("account", MODE_PRIVATE);
                    SharedPreferences.Editor spAccountEditor = spAccount.edit();

                    String[] strUserData = spAccount.getString(loginUser.getEmail(), "").split(",");

                    currentTime = System.currentTimeMillis();

                    int currentNumOfContent = loginUser.getNumOfContent();
                    loginUser.setNumOfContent(++currentNumOfContent);
                    strUserData[Const.INDEX_NUM_OF_CONTENT] = String.valueOf(currentNumOfContent);

                    String strEditUserData = strUserData[0];
                    for (int i = 1; i <= 7; i++) {
                        strEditUserData += (',' + strUserData[i]);
                    }

                    spAccountEditor.putString(loginUser.getEmail(), strEditUserData);
                    spAccountEditor.apply();
                }

                if (contentImage != null) {
                    File imageContentFile = new File(getFilesDir() + "/contents/" + loginUser.getEmail() + '/' + currentTime + ".jpg");
                    try {
                        imageContentFile.createNewFile();
                        BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(imageContentFile));
                        contentImage.compress(Bitmap.CompressFormat.JPEG, 100, out);

                        out.flush();
                        out.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    strImageUri = Uri.fromFile(imageContentFile).toString();
                } else {
                    strImageUri = " ";
                }

                desc = editContentDetails.getText().toString();
                if (desc.length() > 0) {
                    desc = getByteStringForm(desc, "+");
                } else {
                    desc = " ";
                }

                if (useLocation) {
                    strLocation = textLocation.getText().toString();
                } else {
                    strLocation = " ";
                }

                strNewContent =
                        strImageUri + "::"
                                + desc + "::"
                                + strLocation + "::"
                                + " :: ::"
                                + currentTime + ":: ";

                String strUserContent = spContent.getString(loginUser.getEmail(), "");

                if (isEditMode) {
                    String strEditContent = "";
                    String[] strContentSet = strUserContent.split(",");

                    for (int i = 0; i < strContentSet.length; i++) {
                        String[] strContentDetail = strContentSet[i].split("::");
                        if (strContentDetail[Const.CONTENT_TIME].equals(String.valueOf(currentTime))) {
                            Log.d(TAG, "Find");
                            if (strEditContent.length() == 0) {
                                strEditContent = strNewContent;
                            } else {
                                strEditContent += ("," + strNewContent);
                            }
                        } else {
                            if (strEditContent.length() == 0) {
                                strEditContent = strContentSet[i];
                            } else {
                                strEditContent += ("," + strContentSet[i]);
                            }
                        }

                    }
                    strUserContent = strEditContent;
                } else {
                    if (strUserContent.length() == 0) {
                        strUserContent = strNewContent;
                    } else {
                        strUserContent += (',' + strNewContent);
                    }
                }

                spContentEditor.putString(loginUser.getEmail(), strUserContent);
                spContentEditor.apply();

                Intent intent = new Intent();
                intent.putExtra("updated_login_user", loginUser);
                if(isEditMode){
                    intent.putExtra("position", getIntent().getIntExtra("position", -1));
                }
                setResult(RESULT_OK, intent);
                finish();

                return true;
            }
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp()
    {
        finish();
        return super.onSupportNavigateUp();
    }
}
