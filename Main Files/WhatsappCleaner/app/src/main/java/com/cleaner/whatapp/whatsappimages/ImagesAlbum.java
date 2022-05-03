package com.cleaner.whatapp.whatsappimages;
public class ImagesAlbum {
    public static final String A1 = "e8kkc7";
    protected String albumImages, imagename;
    private boolean isSelected;
    public String getImagename() {
        return imagename;
    }
    public void setImagename(String imagename) {
        this.imagename = imagename;
    }
    public boolean isSelected() {
        return isSelected;
    }
    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }
    public String getAlbumImages() {
        return albumImages;
    }
    public void setAlbumImages(String albumImages) {
        this.albumImages = albumImages;
    }
}

