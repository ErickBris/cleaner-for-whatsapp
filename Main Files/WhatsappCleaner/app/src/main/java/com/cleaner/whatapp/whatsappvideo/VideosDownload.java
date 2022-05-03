package com.cleaner.whatapp.whatsappvideo;

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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.cleaner.whatapp.GalleryVideoActivity;
import com.cleaner.whatapp.R;
import com.cleaner.whatapp.ShowVideo;
import com.cleaner.whatapp.StaticVariables;
import com.cleaner.whatapp.whatsappimages.ImagesAlbum;

import org.apache.commons.io.comparator.LastModifiedFileComparator;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

public class VideosDownload extends Fragment implements
        VideosAdapter.ViewHolder.ClickListener {
    String myAppName, path, org_path;
    View videos;
    RecyclerView mRecyclerView_allvideo;
    VideosAdapter mAdapter_video;
    int videoselectedpos, video_count, flag;
    public ArrayList<String> mShareVideos = new ArrayList<String>();
    ArrayList<String> FilePathStrings = new ArrayList<>();
    ArrayList<String> FileNameStrings = new ArrayList<>();
    private File[] listFile;
    File file;
    private int video_pos;
    public Boolean longclick = false;
    RelativeLayout lyout_novideo;
    class data_path {
        String video_path;
        int video_no;
        public data_path(String org_path, int image_no) {
            this.video_path = org_path;
            this.video_no = image_no;
        }
    }
    ArrayList<data_path> datalist;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        videos = inflater.inflate(R.layout.video_download, container, false);
        return videos;
    }
    private void main() {
        myAppName = getResources().getString(R.string.app_name);
        mRecyclerView_allvideo = (RecyclerView) videos.findViewById(R.id.my_recycler_view_allvideos);
        lyout_novideo = (RelativeLayout) videos.findViewById(R.id.lyout_novideo);
        datalist = new ArrayList<data_path>();
        mRecyclerView_allvideo.setHasFixedSize(true);
        ViewTreeObserver observer = mRecyclerView_allvideo.getViewTreeObserver();
        observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @SuppressLint("NewApi")
            @Override
            public void onGlobalLayout() {
                mRecyclerView_allvideo.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                int viewWidth = mRecyclerView_allvideo.getMeasuredWidth();
                float cardViewWidth = getResources().getDimension(
                        R.dimen.column_width_category);
                int newSpanCount = (int) Math.floor(viewWidth / cardViewWidth);
                if (newSpanCount == 0) {
                    mRecyclerView_allvideo.setLayoutManager(new GridLayoutManager(getActivity(), 3));
                } else {
                    mRecyclerView_allvideo.setLayoutManager(new GridLayoutManager(getActivity(), newSpanCount));
                }
            }
        });
        getallvideos();
        mAdapter_video = new VideosAdapter(getActivity(), getAlbumvideos(), this);
        mRecyclerView_allvideo.setAdapter(mAdapter_video);
    }
    private void getallvideos() {
        FilePathStrings.clear();
        FileNameStrings.clear();
        mShareVideos.clear();
        video_count = 0;
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            Toast.makeText(getActivity(), "Error! No SDCARD Found!", Toast.LENGTH_LONG).show();
        } else {
            file = new File(Environment.getExternalStorageDirectory(), StaticVariables.rootDir + "/" + StaticVariables.videoDir + "/");
            file.mkdirs();
        }
        if (file.isDirectory()) {
            listFile = file.listFiles();
            Arrays.sort(listFile, LastModifiedFileComparator.LASTMODIFIED_REVERSE);
            for (int i = 0; i < listFile.length; i++) {
                if (!listFile[i].getName().contains(".jpg")) {
                    FilePathStrings.add(listFile[i].getAbsolutePath());
                    FileNameStrings.add(listFile[i].getName());
                }
            }
        }
        if (FilePathStrings.size() == 0) {
            lyout_novideo.setVisibility(View.VISIBLE);
            mRecyclerView_allvideo.setVisibility(View.GONE);
        } else {
            lyout_novideo.setVisibility(View.GONE);
            mRecyclerView_allvideo.setVisibility(View.VISIBLE);
        }
    }
    private ArrayList<ImagesAlbum> getAlbumvideos() {
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
            videoselectedpos = position;
            if (mAdapter_video.isSelected(position)) {
                flag = 0;
                path = mShareVideos.get(video_count - 1);
                Uri uriPath = Uri.parse(mAdapter_video.getAlbumImagesList().get(position).getAlbumImages());
                org_path = uriPath.getPath();
                setimage(path, flag);
            } else {
                toggleSelection(position);
            }
        } else {
            Intent i = new Intent(getActivity(), ShowVideo.class);
            i.putExtra("pos", position);
            startActivity(i);
        }
    }
    @Override
    public boolean onItemLongClicked(int position) {
        videoselectedpos = position;
        if (mAdapter_video.isSelected(position)) {
            flag = 0;
            path = mShareVideos.get(video_count - 1);
            Uri uriPath = Uri.parse(mAdapter_video.getAlbumImagesList()
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
        int count = mAdapter_video.getSelectedItemCount();
        Uri uriPath = Uri.parse(mAdapter_video.getAlbumImagesList().get(position).getAlbumImages());
        org_path = uriPath.getPath();
        flag = 1;
        File imageFile = new File(org_path);
        Uri uri = getImageContentUri(imageFile);
        Uri destination = Uri.fromFile(new File(getActivity().getCacheDir(), "cropped"));
        int suc = 0, po = 0;
        for (int i = 0; i < mShareVideos.size(); i++) {
            if (mShareVideos.get(i).equalsIgnoreCase("null")) {
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
            int id = cursor.getInt(cursor
                    .getColumnIndex(MediaStore.MediaColumns._ID));
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
        getallvideos();
        mAdapter_video = new VideosAdapter(getActivity(), getAlbumvideos(), this);
        mRecyclerView_allvideo.setAdapter(mAdapter_video);
        GalleryVideoActivity.delete.setVisible(false);
        GalleryVideoActivity.share.setVisible(false);
        mShareVideos.clear();
        longclick = false;
    }
    private void setimage(String imagepath2, int i) {
        // TODO Auto-generated method stub
        mAdapter_video.toggleSelection(videoselectedpos);
        if (i == 1) {
            int flag = 0, pos = 0, cnt = 0;
            for (int j = 0; j < mShareVideos.size(); j++) {
                if (mShareVideos.get(j).equalsIgnoreCase("null")) {
                    pos = j;
                    flag = 1;
                    break;
                } else {
                    flag = 0;
                }
            }
            if (flag == 1) {
                mShareVideos.remove("null");
                cnt = pos + 1;
                video_count = video_count + 1;
                mShareVideos.add(pos, path);
            } else {
                mShareVideos.add(imagepath2);
                video_count = video_count + 1;
                cnt = video_count;
            }
            datalist.add(new data_path(org_path, cnt));
        } else {
            if (i == 0) {
                for (int j = 0; j < datalist.size(); j++) {
                    if (org_path.equalsIgnoreCase(datalist.get(j).video_path)) {
                        video_pos = datalist.get(j).video_no;
                        imagepath2 = mShareVideos.get(video_pos - 1);
                        datalist.remove(j);
                        break;
                    }
                }
                video_count = video_count - 1;
                mShareVideos.remove(imagepath2);
                mShareVideos.add(video_pos - 1, "null");
            }
        }
        if (video_count == 0) {
            GalleryVideoActivity.delete.setVisible(false);
            GalleryVideoActivity.share.setVisible(false);
            mShareVideos.clear();
            longclick = false;
        } else {
            GalleryVideoActivity.delete.setVisible(true);
            GalleryVideoActivity.share.setVisible(true);
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
    public void refressArrayVideoList() {
        getallvideos();
    }
    public ArrayList<String> getVideoArray() {
        return FilePathStrings;
    }
}
