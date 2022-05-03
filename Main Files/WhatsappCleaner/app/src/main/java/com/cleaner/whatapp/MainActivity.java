package com.cleaner.whatapp;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_EXTERNAL_STORAGE_RESULT = 0;

    TextView mTextViewAudioSize;
    TextView mTextViewVideoSize;
    TextView mTextViewImageSize;
    TextView mTextViewProfileSize;
    TextView mTextViewVoiceSize;
    TextView mTextViewWallpaperSize;
    TextView mTextViewDatabaseSize, mTextViewdocSize ;

    ArrayList<WhatsppImages> mListImages;
    ArrayList<WhatsppVideos> mListVideos;
    ArrayList<WhatsppImages> mListProfile;
    ArrayList<WhatsppImages> mListVoice;
    ArrayList<WhatsppImages> mListAudio;
    ArrayList<WhatsppImages> mListWallpaper;
    ArrayList<WhatsppImages> mListDatabase;
    ArrayList<WhatsppImages> mListDocuments;

    String imagepath = "";
    String databasePath = "";
    String videoPath = "";
    String audioPath = "";
    String voicePath = "";
    String profilePath = "";
    String wallpaperPath = "";
    String docPath = "";
    String myAppName;

    boolean doubleBackToExitPressedOnce = false;

    CardView imageCard, audioCard, videoCard, dpCard, wallpaperCard, voiceNotesCard, docCard, callCard, databaseCard;

    ProgressDialog mDialog;
    static boolean firstTime = false;

    Drawer result;
    AccountHeader headerResult;
    Toolbar mToolbar;
    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        new DrawerBuilder().withActivity(this).build();
        myAppName = getResources().getString(R.string.app_name);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        REQUEST_EXTERNAL_STORAGE_RESULT);
            }
        }

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQUEST_EXTERNAL_STORAGE_RESULT);
            }
        }
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        if (mToolbar != null) {
            mToolbar.setTitle(myAppName);
            setSupportActionBar(mToolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
        }
        headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.drawable.navigation_cover)
                .withSelectionListEnabledForSingleProfile(false)
                .withAlternativeProfileHeaderSwitching(false)
                .withCompactStyle(false)
                .withDividerBelowHeader(false)
                .withProfileImagesVisible(false)
                .addProfiles(new ProfileDrawerItem().withName(getResources().getString(R.string.app_name)).withEmail(getResources()
                        .getString(R.string.developer_email)))
                .build();
        result = new DrawerBuilder()
                .withActivity(this)
                .withSelectedItem(-1)
                .withFullscreen(true)
                .withAccountHeader(headerResult)
                .withActionBarDrawerToggle(true)
                .withCloseOnClick(true)
                .withMultiSelect(false)
                .withTranslucentStatusBar(true)
                .withToolbar(mToolbar)
                .addDrawerItems(
                        new PrimaryDrawerItem().withSelectable(false).withName(R.string.app_name),
                        new PrimaryDrawerItem().withSelectable(false).withName("Home").withIcon(R.drawable.ic_home_black_24dp).withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                            @Override
                            public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                                Toast.makeText(getApplicationContext(),"Home",Toast.LENGTH_SHORT).show();
                                return false;
                            }
                        }),
                        new PrimaryDrawerItem().withSelectable(false).withName("Recommend to Friends").withIcon(R.drawable.ic_share_black_24dp).withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                            @Override
                            public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                                final String shareappPackageName = getPackageName();
                                Intent sendIntent = new Intent();
                                sendIntent.setAction(Intent.ACTION_SEND);
                                sendIntent.putExtra(Intent.EXTRA_TEXT, "Check out All in one Interview Questions App at: https://play.google.com/store/apps/details?id=" + shareappPackageName);
                                sendIntent.setType("text/plain");
                                startActivity(sendIntent);
                                return false;
                            }
                        }),
                        new PrimaryDrawerItem().withSelectable(false).withName("Rate Us").withIcon(R.drawable.ic_thumb_up_black_24dp).withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                            @Override
                            public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                                final String appPackageName = getPackageName();
                                try {
                                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                                } catch (android.content.ActivityNotFoundException anfe) {
                                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                                }
                                return false;
                            }
                        }),
                        new PrimaryDrawerItem().withSelectable(false).withName("Exit").withIcon(R.drawable.ic_exit_to_app_black_24dp).withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                            @Override
                            public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                                onBackPressed();
                                return false;
                            }
                        })
                ).
                withSavedInstance(savedInstanceState)
                .build();

        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().addTestDevice("C69095F3C24675F5F8C9B5031B0E6EEB")
                .build();
        mAdView.loadAd(adRequest);

        mTextViewImageSize = (TextView) findViewById(R.id.whatsapp_images_sub_title_text);
        mTextViewAudioSize = (TextView) findViewById(R.id.whatsapp_audio_sub_title_text);
        mTextViewProfileSize = (TextView) findViewById(R.id.whatsapp_dp_sub_title_text);
        mTextViewVoiceSize = (TextView) findViewById(R.id.whatsapp_voice_sub_title_text);
        mTextViewVideoSize = (TextView) findViewById(R.id.whatsapp_video_sub_title_text);
        mTextViewWallpaperSize = (TextView) findViewById(R.id.whatsapp_wallpaper_sub_title_text);
        mTextViewDatabaseSize = (TextView) findViewById(R.id.whatsapp_databases_sub_title_text);
        mTextViewdocSize = (TextView)findViewById(R.id.whatsapp_documents_sub_title_text);

        imageCard = (CardView) findViewById(R.id.android_image);
        audioCard = (CardView) findViewById(R.id.whatsapp_audio);
        videoCard = (CardView) findViewById(R.id.whatsapp_video);
        dpCard = (CardView) findViewById(R.id.android_profile);
        wallpaperCard = (CardView) findViewById(R.id.whatsapp_wallpaper);
        voiceNotesCard = (CardView) findViewById(R.id.whatsapp_voice_notes);
        docCard = (CardView) findViewById(R.id.android_documents);
        databaseCard = (CardView) findViewById(R.id.whatsapp_databases);

        databasePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/WhatsApp/Databases";
        wallpaperPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/WhatsApp/Media/WallPaper";
        imagepath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/WhatsApp/Media/WhatsApp Images";
        voicePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/WhatsApp/Media/WhatsApp Voice Notes";
        audioPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/WhatsApp/Media/WhatsApp Audio";
        videoPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/WhatsApp/Media/WhatsApp Video";
        profilePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/WhatsApp/Media/WhatsApp Profile Photos";
        docPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/WhatsApp/Media/WhatsApp Documents";

        if (firstTime) {
            maintainCalulation();
            Log.e("first", "first if " + firstTime);
        } else {
            firstTime = true;
            Log.e("first", "first else " + firstTime);
            new UploadFeed().execute();
        }
        imageCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    File oldFolder = new File(Environment.getExternalStorageDirectory()+"/WhatsApp/Media/WhatsApp Images/","Sent");
                    File newFolder = new File(Environment.getExternalStorageDirectory()+"/WhatsApp/Media/","Sent");
                    boolean success = oldFolder.renameTo(newFolder);
                Intent intent = new Intent(MainActivity.this, GalleryActivity.class);
                startActivity(intent);
            }
        });
        dpCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, GalleryDpActivity.class);
                startActivity(intent);
            }
        });
        videoCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File oldFoldervideo = new File(Environment.getExternalStorageDirectory()+"/WhatsApp/Media/WhatsApp Video/","Sent");
                File newFoldervideo = new File(Environment.getExternalStorageDirectory()+"/WhatsApp/Media/","Sent2");
                boolean successvideo = oldFoldervideo.renameTo(newFoldervideo);
                Intent intent = new Intent(MainActivity.this, GalleryVideoActivity.class);
                startActivity(intent);
            }
        });
        audioCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File oldFolderaudio = new File(Environment.getExternalStorageDirectory()+"/WhatsApp/Media/WhatsApp Audio/","Sent");
                File newFolderaudio = new File(Environment.getExternalStorageDirectory()+"/WhatsApp/Media/","Sent3");
                boolean successaudio = oldFolderaudio.renameTo(newFolderaudio);
                Intent intent = new Intent(MainActivity.this, GalleryAudioActivity.class);
                startActivity(intent);
            }
        });
        wallpaperCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

                builder.setTitle("Confirm");
                builder.setMessage("Are you sure you want to delete Wallpapers?");

                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing but close the dialog
                        String path= Environment.getExternalStorageDirectory()+"/WhatsApp/Media/WallPaper";
                        File file = new File(path);
                        try{
                            if(file.exists()){
                                deleteRecursive(file);
                            }
                        }catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                        new UploadFeed().execute();
                        dialog.dismiss();
                    }
                });

                builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        // Do nothing
                        dialog.dismiss();
                    }
                });

                AlertDialog alert = builder.create();
                alert.show();
            }
        });
        voiceNotesCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

                builder.setTitle("Confirm");
                builder.setMessage("Are you sure you want to delete Voice Notes?");

                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing but close the dialog
                        String path= Environment.getExternalStorageDirectory()+"/WhatsApp/Media/WhatsApp Voice Notes";
                        File file = new File(path);
                        try{
                            if(file.exists()){
                                deleteRecursive(file);
                            }
                        }catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                        new UploadFeed().execute();
                        dialog.dismiss();
                    }
                });

                builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        // Do nothing
                        dialog.dismiss();
                    }
                });

                AlertDialog alert = builder.create();
                alert.show();

            }
        });
        docCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

                builder.setTitle("Confirm");
                builder.setMessage("Are you sure you want to delete Documents?");

                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing but close the dialog
                        String path= Environment.getExternalStorageDirectory()+"/WhatsApp/Media/WhatsApp Documents";
                        File file = new File(path);
                        try{
                            if(file.exists()){
                                deleteRecursive(file);
                            }
                        }catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                        new UploadFeed().execute();
                        dialog.dismiss();
                    }
                });

                builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        // Do nothing
                        dialog.dismiss();
                    }
                });

                AlertDialog alert = builder.create();
                alert.show();

            }
        });
        databaseCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

                builder.setTitle("Confirm");
                builder.setMessage("Are you sure you want to delete Databases?");

                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing but close the dialog
                        String path= Environment.getExternalStorageDirectory()+"/WhatsApp/Databases";
                        File file = new File(path);
                        try{
                            if(file.exists()){
                                deleteRecursive(file);
                            }
                        }catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                        new UploadFeed().execute();
                        dialog.dismiss();
                    }
                });

                builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        // Do nothing
                        dialog.dismiss();
                    }
                });

                AlertDialog alert = builder.create();
                alert.show();

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.refresh_us, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        TAGS.imageCounter = 0;
        TAGS.imageSize = 0;
        new UploadFeed().execute();
        Toast.makeText(this, "Refreshing..", Toast.LENGTH_SHORT).show();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_EXTERNAL_STORAGE_RESULT: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    new UploadFeed().execute();

                    mTextViewAudioSize.setText("Files: " + TAGS.audioCounter + " / " + TAGS.audioSize / 1024 + " MB");
                    mTextViewImageSize.setText("Files: " + TAGS.imageCounter + " / " + TAGS.imageSize / 1024 + " MB");
                    mTextViewProfileSize.setText("Files: " + TAGS.profileCounter + " / " + TAGS.profileSize / 1024 + " MB");
                    mTextViewVideoSize.setText("Files: " + TAGS.videoCounter + " / " + TAGS.videoSize / 1024 + " MB");
                    mTextViewVoiceSize.setText("Files: " + TAGS.voiceCounter + " / " + TAGS.voiceSize / 1024 + " MB");
                    mTextViewWallpaperSize.setText("Files: " + TAGS.wallpaperCounter + " / " + TAGS.wallpaperSize / 1024 + " MB");
                    mTextViewDatabaseSize.setText("Files: " + TAGS.databaseCounter + " / " + TAGS.databaseSize / 1024 + " MB");
                    mTextViewdocSize.setText("Files: " + TAGS.docCounter + " / " + TAGS.docSize / 1024 + " MB");
                    if (TAGS.audioSize < 1024) {
                        mTextViewAudioSize.setText("Files: " + TAGS.audioCounter + " / " + TAGS.audioSize + " KB");
                    }
                    if (TAGS.videoSize < 1024) {
                        mTextViewVideoSize.setText("Files: " + TAGS.videoCounter + " / " + TAGS.videoSize + " KB");
                    }
                    if (TAGS.imageSize < 1024) {
                        mTextViewImageSize.setText("Files: " + TAGS.imageCounter + " / " + TAGS.imageSize + " KB");
                    }
                    if (TAGS.profileSize < 1024) {
                        mTextViewProfileSize.setText("Files: " + TAGS.profileCounter + " / " + TAGS.profileSize + " KB");
                    }
                    if (TAGS.voiceSize < 1024) {
                        mTextViewVoiceSize.setText("Files: " + TAGS.voiceCounter + " / " + TAGS.voiceSize + " KB");
                    }
                    if (TAGS.wallpaperSize < 1024) {
                        mTextViewWallpaperSize.setText("Files: " + TAGS.wallpaperCounter + " / " + TAGS.wallpaperSize + " KB");
                    }
                    if (TAGS.databaseSize < 1024) {
                        mTextViewDatabaseSize.setText("Files: " + TAGS.databaseCounter + " / " + TAGS.databaseSize + " KB");
                    }
                    if (TAGS.docSize < 1024) {
                        mTextViewdocSize.setText("Files: " + TAGS.docCounter + " / " + TAGS.docSize + " KB");
                    }
                } else {
                }
                return;
            }
        }
    }
    void deleteRecursive(File fileOrDirectory) {
        if (fileOrDirectory.isDirectory())
            for (File child : fileOrDirectory.listFiles())
                deleteRecursive(child);

        fileOrDirectory.delete();
    }
    private void maintainCalulation() {
        mTextViewAudioSize.setText("Files: " + TAGS.audioCounter + " / " + TAGS.audioSize / 1024 + " MB");
        mTextViewImageSize.setText("Files: " + TAGS.imageCounter + " / " + TAGS.imageSize / 1024 + " MB");
        mTextViewProfileSize.setText("Files: " + TAGS.profileCounter + " / " + TAGS.profileSize / 1024 + " MB");
        mTextViewVideoSize.setText("Files: " + TAGS.videoCounter + " / " + TAGS.videoSize / 1024 + " MB");
        mTextViewVoiceSize.setText("Files: " + TAGS.voiceCounter + " / " + TAGS.voiceSize / 1024 + " MB");
        mTextViewWallpaperSize.setText("Files: " + TAGS.wallpaperCounter + " / " + TAGS.wallpaperSize / 1024 + " MB");
        mTextViewDatabaseSize.setText("Files: " + TAGS.databaseCounter + " / " + TAGS.databaseSize / 1024 + " MB");
        mTextViewdocSize.setText("Files: " + TAGS.docCounter + " / " + TAGS.docSize / 1024 + " MB");
        if (TAGS.audioSize < 1024) {
            mTextViewAudioSize.setText("Files: " + TAGS.audioCounter + " / " + TAGS.audioSize + " KB");
        }
        if (TAGS.videoSize < 1024) {
            mTextViewVideoSize.setText("Files: " + TAGS.videoCounter + " / " + TAGS.videoSize + " KB");
        }
        if (TAGS.imageSize < 1024) {
            mTextViewImageSize.setText("Files: " + TAGS.imageCounter + " / " + TAGS.imageSize + " KB");
        }
        if (TAGS.profileSize < 1024) {
            mTextViewProfileSize.setText("Files: " + TAGS.profileCounter + " / " + TAGS.profileSize + " KB");
        }
        if (TAGS.voiceSize < 1024) {
            mTextViewVoiceSize.setText("Files: " + TAGS.voiceCounter + " / " + TAGS.voiceSize + " KB");
        }
        if (TAGS.wallpaperSize < 1024) {
            mTextViewWallpaperSize.setText("Files: " + TAGS.wallpaperCounter + " / " + TAGS.wallpaperSize + " KB");
        }
        if (TAGS.databaseSize < 1024) {
            mTextViewDatabaseSize.setText("Files: " + TAGS.databaseCounter + " / " + TAGS.databaseSize + " KB");
        }
        if (TAGS.docSize < 1024) {
            mTextViewdocSize.setText("Files: " + TAGS.docCounter + " / " + TAGS.docSize + " KB");
        }
    }

    public class UploadFeed extends AsyncTask<Void, Void, Void> {
        String response = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            File oldFolder = new File(Environment.getExternalStorageDirectory()+"/WhatsApp/Media/WhatsApp Images/","Sent");
            File newFolder = new File(Environment.getExternalStorageDirectory()+"/WhatsApp/Media/","Sent");
            boolean success = oldFolder.renameTo(newFolder);
            File oldFoldervideo = new File(Environment.getExternalStorageDirectory()+"/WhatsApp/Media/WhatsApp Video/","Sent");
            File newFoldervideo = new File(Environment.getExternalStorageDirectory()+"/WhatsApp/Media/","Sent2");
            boolean successvideo = oldFoldervideo.renameTo(newFoldervideo);

            File oldFolderaudio = new File(Environment.getExternalStorageDirectory()+"/WhatsApp/Media/WhatsApp Audio/","Sent");
            File newFolderaudio = new File(Environment.getExternalStorageDirectory()+"/WhatsApp/Media/","Sent3");
            boolean successaudio = oldFolderaudio.renameTo(newFolderaudio);
            mDialog = Utils.SetProgressBar(mDialog, MainActivity.this);
            TAGS.clearTags();
        }

        @Override
        protected Void doInBackground(Void... params) {
            getWhatsAppVidoes(videoPath, true);
            getWhatsImages(imagepath, true);
            getWhatsVoice(voicePath, true);
            getWhatsAudio(audioPath, true);
            getWhatsProfileImages(profilePath);
            getWhatsAppWallpapers(wallpaperPath);
            getdatebasefile(databasePath);
            getDocfile(docPath);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            mDialog.dismiss();
            mTextViewAudioSize.setText("Files: " + TAGS.audioCounter + " / " + TAGS.audioSize / 1024 + " MB");
            mTextViewImageSize.setText("Files: " + TAGS.imageCounter + " / " + TAGS.imageSize / 1024 + " MB");
            mTextViewProfileSize.setText("Files: " + TAGS.profileCounter + " / " + TAGS.profileSize / 1024 + " MB");
            mTextViewVideoSize.setText("Files: " + TAGS.videoCounter + " / " + TAGS.videoSize / 1024 + " MB");
            mTextViewVoiceSize.setText("Files: " + TAGS.voiceCounter + " / " + TAGS.voiceSize / 1024 + " MB");
            mTextViewWallpaperSize.setText("Files: " + TAGS.wallpaperCounter + " / " + TAGS.wallpaperSize / 1024 + " MB");
            mTextViewDatabaseSize.setText("Files: " + TAGS.databaseCounter + " / " + TAGS.databaseSize / 1024 + " MB");
            mTextViewdocSize.setText("Files: " + TAGS.docCounter + " / " + TAGS.docSize / 1024 + " MB");
            if (TAGS.audioSize < 1024) {
                mTextViewAudioSize.setText("Files: " + TAGS.audioCounter + " / " + TAGS.audioSize + " KB");
            }
            if (TAGS.videoSize < 1024) {
                mTextViewVideoSize.setText("Files: " + TAGS.videoCounter + " / " + TAGS.videoSize + " KB");
            }
            if (TAGS.imageSize < 1024) {
                mTextViewImageSize.setText("Files: " + TAGS.imageCounter + " / " + TAGS.imageSize + " KB");
            }
            if (TAGS.profileSize < 1024) {
                mTextViewProfileSize.setText("Files: " + TAGS.profileCounter + " / " + TAGS.profileSize + " KB");
            }
            if (TAGS.voiceSize < 1024) {
                mTextViewVoiceSize.setText("Files: " + TAGS.voiceCounter + " / " + TAGS.voiceSize + " KB");
            }
            if (TAGS.wallpaperSize < 1024) {
                mTextViewWallpaperSize.setText("Files: " + TAGS.wallpaperCounter + " / " + TAGS.wallpaperSize + " KB");
            }
            if (TAGS.databaseSize < 1024) {
                mTextViewDatabaseSize.setText("Files: " + TAGS.databaseCounter + " / " + TAGS.databaseSize + " KB");
            }
            if (TAGS.docSize < 1024) {
                mTextViewdocSize.setText("Files: " + TAGS.docCounter + " / " + TAGS.docSize + " KB");
            }
        }
    }

    public void getWhatsImages(String path, boolean first) {
        File files = new File(path);
        FileFilter filter = new FileFilter() {
            private final List<String> exts = Arrays.asList("jpeg", "jpg", "png", "bmp", "gif");

            @Override
            public boolean accept(File pathname) {
                String ext;
                String path = pathname.getPath();
                ext = path.substring(path.lastIndexOf(".") + 1);
                return exts.contains(ext);
            }
        };

        final File[] filesFound = files.listFiles(filter);
        mListImages = new ArrayList<WhatsppImages>();
        if (filesFound != null && filesFound.length > 0) {
            for (File file : filesFound) {
                WhatsppImages ld = new WhatsppImages();
                ld.setName(file.getName());
                ld.setPath(file.getAbsolutePath());
                ld.setSize(file.length() / 1024);
                TAGS.imageSize += file.length() / 1024;
                TAGS.imageCounter++;
                mListImages.add(ld);
            }
        }
        if (first) {
            TAGS.mListImages = mListImages;
        } else {
            TAGS.mListImages.addAll(mListImages);
        }
    }

    public void getWhatsAppVidoes(String path, boolean first) {
        File files = new File(path);
        FileFilter filter = new FileFilter() {
            private final List<String> exts = Arrays.asList("3gp", "mp4", "mkv");

            @Override
            public boolean accept(File pathname) {
                String ext;
                String path = pathname.getPath();
                ext = path.substring(path.lastIndexOf(".") + 1);
                return exts.contains(ext);
            }
        };
        final File[] filesFound = files.listFiles(filter);
        mListVideos = new ArrayList<WhatsppVideos>();
        if (filesFound != null && filesFound.length > 0) {
            for (File file : filesFound) {
                WhatsppVideos vd = new WhatsppVideos();
                vd.setPath(file.getAbsolutePath());
                vd.setSize(file.length() / 1024);
                TAGS.videoSize += file.length() / 1024;
                vd.setBitmap(ThumbnailUtils.createVideoThumbnail(file.getAbsolutePath(), MediaStore.Video.Thumbnails.MICRO_KIND));
                mListVideos.add(vd);
                TAGS.videoCounter++;
            }
        }
        if (first) {
            TAGS.mListVideos = mListVideos;
        } else {
            TAGS.mListVideos.addAll(mListVideos);
        }
        Log.e("size", "video size: " + TAGS.videoSize);
    }

    public void getWhatsProfileImages(String path) {

        File files = new File(path);

        FileFilter filter = new FileFilter() {

            private final List<String> exts = Arrays.asList("jpeg", "jpg", "png", "bmp", "gif");
            //	        private final List<String> exts = Arrays.asList("3gp", "mp4");

            @Override
            public boolean accept(File pathname) {
                String ext;
                String path = pathname.getPath();
                ext = path.substring(path.lastIndexOf(".") + 1);
                return exts.contains(ext);
            }
        };

        final File[] filesFound = files.listFiles(filter);
        mListProfile = new ArrayList<WhatsppImages>();
        if (filesFound != null && filesFound.length > 0) {
            for (File file : filesFound) {
                WhatsppImages ld = new WhatsppImages();
                ld.setPath(file.getAbsolutePath());
                ld.setSize(file.length() / 1024);
                TAGS.profileSize += file.length() / 1024;
                mListProfile.add(ld);
                TAGS.profileCounter++;
            }
        }
        TAGS.mListProfileImages = mListProfile;
    }
    public void getWhatsAppWallpapers(String path) {

        File files = new File(path);

        FileFilter filter = new FileFilter() {

            private final List<String> exts = Arrays.asList("jpeg", "jpg", "png", "bmp", "gif");
            //	        private final List<String> exts = Arrays.asList("3gp", "mp4");

            @Override
            public boolean accept(File pathname)
            {
                String ext;
                String path = pathname.getPath();
                ext = path.substring(path.lastIndexOf(".") + 1);
                return exts.contains(ext);
            }
        };

        final File [] filesFound = files.listFiles(filter);
        mListWallpaper = new ArrayList<WhatsppImages>();
        if (filesFound != null && filesFound.length > 0) {
            for (File file : filesFound)
            {
                WhatsppImages ld=new WhatsppImages();
                ld.setPath(file.getAbsolutePath());
                ld.setSize(file.length()/1024);
                TAGS.wallpaperSize+=file.length()/1024;
                mListWallpaper.add(ld);
                TAGS.wallpaperCounter++;
            }
        }
        TAGS.mListWallpaper=mListWallpaper;
    }
    public void getWhatsVoice(String path,boolean first) {

        //		mDialog=Utils.SetProgressBar(mDialog, MainActivity.this);
        File files = new File(path);

        FileFilter filter = new FileFilter() {

            private final List<String> exts = Arrays.asList("aac", "m4a", "amr");
            //	        private final List<String> exts = Arrays.asList("3gp", "mp4");

            @Override
            public boolean accept(File pathname)
            {
                String ext;
                String path = pathname.getPath();
                ext = path.substring(path.lastIndexOf(".") + 1);
                return exts.contains(ext);
            }
        };

        final File [] filesFound = files.listFiles(filter);
        mListVoice = new ArrayList<WhatsppImages>();
        if (filesFound != null && filesFound.length > 0) {
            for (File file : filesFound)
            {
                WhatsppImages ld=new WhatsppImages();
                ld.setPath(file.getAbsolutePath());
                ld.setSize(file.length()/1024);
                TAGS.voiceSize+=file.length()/1024;
                mListVoice.add(ld);
                TAGS.voiceCounter++;
            }
        }
        if(first)
        {
            TAGS.mListVoice=mListVoice;
        }
        else
        {
            TAGS.mListVoice.addAll(mListVoice);
        }
    }
    public void getdatebasefile(String path)
    {
        File files = new File(path);

        FileFilter filter = new FileFilter() {

            private final List<String> exts = Arrays.asList("crypt12");
            //	        private final List<String> exts = Arrays.asList("3gp", "mp4");

            @Override
            public boolean accept(File pathname)
            {
                String ext;
                String path = pathname.getPath();
                ext = path.substring(path.lastIndexOf(".") + 1);
                return exts.contains(ext);
            }
        };

        final File [] filesFound = files.listFiles(filter);
        mListDatabase = new ArrayList<WhatsppImages>();
        if (filesFound != null && filesFound.length > 0) {
            for (File file : filesFound)
            {
                WhatsppImages ld=new WhatsppImages();
                ld.setPath(file.getAbsolutePath());
                ld.setSize(file.length()/1024);
                mListDatabase.add(ld);
                TAGS.databaseSize+=file.length()/1024;
                TAGS.databaseCounter++;
            }
        }
    }
    public void getDocfile(String path)
    {
        File files = new File(path);

        FileFilter filter = new FileFilter() {

            private final List<String> exts = Arrays.asList("docx","pdf","doc");
            //	        private final List<String> exts = Arrays.asList("3gp", "mp4");

            @Override
            public boolean accept(File pathname)
            {
                String ext;
                String path = pathname.getPath();
                ext = path.substring(path.lastIndexOf(".") + 1);
                return exts.contains(ext);
            }
        };

        final File [] filesFound = files.listFiles(filter);
        mListDocuments = new ArrayList<WhatsppImages>();
        if (filesFound != null && filesFound.length > 0) {
            for (File file : filesFound)
            {
                WhatsppImages ld=new WhatsppImages();
                ld.setPath(file.getAbsolutePath());
                ld.setSize(file.length()/1024);
                mListDocuments.add(ld);
                TAGS.docSize+=file.length()/1024;
                TAGS.docCounter++;
            }
        }
    }
    public void getWhatsAudio(String path,boolean first) {

        //		mDialog=Utils.SetProgressBar(mDialog, MainActivity.this);
        File files = new File(path);

        FileFilter filter = new FileFilter() {

            private final List<String> exts = Arrays.asList("aac", "m4a", "amr", "mp3");
            //	        private final List<String> exts = Arrays.asList("3gp", "mp4");

            @Override
            public boolean accept(File pathname)
            {
                String ext;
                String path = pathname.getPath();
                ext = path.substring(path.lastIndexOf(".") + 1);
                return exts.contains(ext);
            }
        };

        final File [] filesFound = files.listFiles(filter);
        mListAudio = new ArrayList<WhatsppImages>();
        if (filesFound != null && filesFound.length > 0) {
            for (File file : filesFound)
            {
                WhatsppImages ld=new WhatsppImages();
                ld.setPath(file.getAbsolutePath());
                ld.setSize(file.length()/1024);
                TAGS.audioSize+=file.length()/1024;
                mListAudio.add(ld);
                TAGS.audioCounter++;
            }
        }
        if(first)
        {
            TAGS.mListAudio=mListAudio;
        }
        else
        {
            TAGS.mListAudio.addAll(mListAudio);
        }

    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }
        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }
}

