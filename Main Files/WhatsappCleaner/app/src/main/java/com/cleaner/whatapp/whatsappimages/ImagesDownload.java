package com.cleaner.whatapp.whatsappimages;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.cleaner.whatapp.GalleryActivity;
import com.cleaner.whatapp.R;
import com.cleaner.whatapp.ShowImages;
import com.cleaner.whatapp.StaticVariables;

import org.apache.commons.io.comparator.LastModifiedFileComparator;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

public class ImagesDownload extends Fragment implements
        ImagesAdapter.ViewHolder.ClickListener {
    public static final String A5 = "52a5xz";
    String myAppName, path, org_path;
    View album;
    RecyclerView mRecyclerView_allimage;
    ImagesAdapter mAdapter_image;
    int imageselectedpos, image_count, flag;
    public ArrayList<String> mShareImages = new ArrayList<String>();
    ArrayList<String> FilePathStrings = new ArrayList<>();
    ArrayList<String> FileNameStrings = new ArrayList<>();
    private File[] listFile;
    File file;
    private int image_pos;
    public Boolean longclick = false;
    RelativeLayout lyout_noimage;
    class data_path {
        String image_path;
        int image_no;
        public data_path(String org_path, int image_no) {
            this.image_path = org_path;
            this.image_no = image_no;
        }
    }
    ArrayList<data_path> datalist;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        album = inflater.inflate(R.layout.img_download, container, false);
        return album;
    }
    private void main() {
        myAppName = getResources().getString(R.string.app_name);
        mRecyclerView_allimage = (RecyclerView) album.findViewById(R.id.my_recycler_view_allimage);
        lyout_noimage = (RelativeLayout) album.findViewById(R.id.lyout_noimage);
        datalist = new ArrayList<data_path>();
        mRecyclerView_allimage.setHasFixedSize(true);
        ViewTreeObserver observer = mRecyclerView_allimage.getViewTreeObserver();
        observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @SuppressLint("NewApi")
            @Override
            public void onGlobalLayout() {
                mRecyclerView_allimage.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                int viewWidth = mRecyclerView_allimage.getMeasuredWidth();
                float cardViewWidth = getResources().getDimension(
                        R.dimen.column_width_category);
                int newSpanCount = (int) Math.floor(viewWidth / cardViewWidth);
                Log.i("newSpanCount", "" + newSpanCount);
                if (newSpanCount == 0) {
                    mRecyclerView_allimage.setLayoutManager(new GridLayoutManager(getActivity(), 4));
                } else {
                    mRecyclerView_allimage.setLayoutManager(new GridLayoutManager(getActivity(), newSpanCount));
                }
            }
        });
        getallimages();
        mAdapter_image = new ImagesAdapter(getActivity(), getAlbumImages(), this);
        mRecyclerView_allimage.setAdapter(mAdapter_image);
    }
    private void getallimages() {
        FilePathStrings.clear();
        FileNameStrings.clear();
        mShareImages.clear();
        image_count = 0;
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            Toast.makeText(getActivity(), "Error! No SDCARD Found!", Toast.LENGTH_LONG).show();
        } else {
            file = new File(Environment.getExternalStorageDirectory(), StaticVariables.rootDir + "/" + StaticVariables.imgDir + "/");
            file.mkdirs();
        }
        if (file.isDirectory()) {
            listFile = file.listFiles();
            Arrays.sort(listFile, LastModifiedFileComparator.LASTMODIFIED_REVERSE);
            for (int i = 0; i < listFile.length; i++) {
                if (listFile[i].getName().contains(".jpg")) {
                    FilePathStrings.add(listFile[i].getAbsolutePath());
                    FileNameStrings.add(listFile[i].getName());
                }
            }
        }
        if (FilePathStrings.size() == 0) {
            lyout_noimage.setVisibility(View.VISIBLE);
            mRecyclerView_allimage.setVisibility(View.GONE);
        } else {
            lyout_noimage.setVisibility(View.GONE);
            mRecyclerView_allimage.setVisibility(View.VISIBLE);
        }
    }
    private ArrayList<ImagesAlbum> getAlbumImages() {
        ArrayList<ImagesAlbum> paths = new ArrayList<ImagesAlbum>();
        for (int i = 0; i < FileNameStrings.size(); i++) {
            ImagesAlbum albumImages = new ImagesAlbum();
            albumImages.setAlbumImages(FilePathStrings.get(i));
            albumImages.setImagename(FileNameStrings.get(i));
            paths.add(albumImages);
        }
        return paths;
    }
    @Override
    public void onItemClicked(int position) {
        // TODO Auto-generated method stub
        if (longclick) {
            imageselectedpos = position;
            if (mAdapter_image.isSelected(position)) {
                flag = 0;
                path = mShareImages.get(image_count - 1);
                Uri uriPath = Uri.parse(mAdapter_image.getAlbumImagesList().get(position).getAlbumImages());
                org_path = uriPath.getPath();
                setimage(path, flag);
            } else {
                toggleSelection(position);
            }
        } else {
            Intent i = new Intent(getActivity(), ShowImages.class);
            i.putExtra("pos", position);
            startActivity(i);
        }
    }
    @Override
    public boolean onItemLongClicked(int position) {
        imageselectedpos = position;
        if (mAdapter_image.isSelected(position)) {
            flag = 0;
            path = mShareImages.get(image_count - 1);
            Uri uriPath = Uri.parse(mAdapter_image.getAlbumImagesList()
                    .get(position).getAlbumImages());
            org_path = uriPath.getPath();
            setimage(path, flag);
        } else {
            toggleSelection(position);
        }
        longclick = true;
        return true;
    }
    private void toggleSelection(int position) {
        int count = mAdapter_image.getSelectedItemCount();
        Uri uriPath = Uri.parse(mAdapter_image.getAlbumImagesList().get(position).getAlbumImages());
        org_path = uriPath.getPath();
        flag = 1;
        File imageFile = new File(org_path);
        Uri uri = getImageContentUri(imageFile);
        Uri destination = Uri.fromFile(new File(getActivity().getCacheDir(), "cropped"));
        int suc = 0, po = 0;
        for (int i = 0; i < mShareImages.size(); i++) {
            if (mShareImages.get(i).equalsIgnoreCase("null")) {
                suc = 1;
                po = i;
                break;
            } else {
                suc = 0;
            }
        }
        setimage(org_path, 1);
    }
    private Uri getImageContentUri(File imageFile) {
        String filePath = imageFile.getAbsolutePath();
        Cursor cursor = getActivity().getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Images.Media._ID},
                MediaStore.Images.Media.DATA + "=? ",
                new String[]{filePath}, null);
        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor.getColumnIndex(MediaStore.MediaColumns._ID));
            Uri baseUri = Uri.parse("content://media/external/images/media");
            return Uri.withAppendedPath(baseUri, "" + id);
        } else {
            if (imageFile.exists()) {
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.DATA, filePath);
                return getActivity().getContentResolver().insert(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            } else {
                return null;
            }
        }
    }
    public void refresh() {
        getallimages();
        mAdapter_image = new ImagesAdapter(getActivity(), getAlbumImages(), this);
        mRecyclerView_allimage.setAdapter(mAdapter_image);
        GalleryActivity.delete.setVisible(false);
        GalleryActivity.share.setVisible(false);
        mShareImages.clear();
        longclick = false;
    }
    private void setimage(String imagepath2, int i) {
        // TODO Auto-generated method stub
        mAdapter_image.toggleSelection(imageselectedpos);
        if (i == 1) {
            int flag = 0, pos = 0, cnt = 0;
            for (int j = 0; j < mShareImages.size(); j++) {
                if (mShareImages.get(j).equalsIgnoreCase("null")) {
                    pos = j;
                    flag = 1;
                    break;
                } else {
                    flag = 0;
                }
            }
            if (flag == 1) {
                mShareImages.remove("null");
                cnt = pos + 1;
                image_count = image_count + 1;
                mShareImages.add(pos, path);
            } else {
                mShareImages.add(imagepath2);
                image_count = image_count + 1;
                cnt = image_count;
            }
            datalist.add(new data_path(org_path, cnt));
        } else {
            if (i == 0) {
                for (int j = 0; j < datalist.size(); j++) {
                    if (org_path.equalsIgnoreCase(datalist.get(j).image_path)) {
                        image_pos = datalist.get(j).image_no;
                        imagepath2 = mShareImages.get(image_pos - 1);
                        datalist.remove(j);
                        break;
                    }
                }
                image_count = image_count - 1;
                mShareImages.remove(imagepath2);
                mShareImages.add(image_pos - 1, "null");
            }
        }
        if (image_count == 0) {
            GalleryActivity.delete.setVisible(false);
            GalleryActivity.share.setVisible(false);
            mShareImages.clear();
            longclick = false;
        } else {
            GalleryActivity.delete.setVisible(true);
            GalleryActivity.share.setVisible(true);
        }
    }
    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
    }
    @Override
    public void onResume() {
        super.onResume();
        main();
    }
    public ArrayList<String> getDeleteImage() {
        return mShareImages;
    }
    public void refressArrayImageList() {
        getallimages();
    }
    public ArrayList<String> getImageArray() {
        FilePathStrings.clear();
        FileNameStrings.clear();
        mShareImages.clear();
        image_count = 0;
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            Toast.makeText(getActivity(), "Error! No SDCARD Found!", Toast.LENGTH_LONG).show();
        } else {
            file = new File(Environment.getExternalStorageDirectory(), StaticVariables.rootDir + "/" + StaticVariables.imgDir + "/");
        }
        if (file.isDirectory()) {
            listFile = file.listFiles();
            Arrays.sort(listFile, LastModifiedFileComparator.LASTMODIFIED_REVERSE);
            for (int i = 0; i < listFile.length; i++) {
                if (listFile[i].getName().contains(".jpg")) {
                    FilePathStrings.add(listFile[i].getAbsolutePath());
                    FileNameStrings.add(listFile[i].getName());
                }
            }
        }
        return FilePathStrings;
    }
}
