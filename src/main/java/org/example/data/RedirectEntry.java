package org.example.data;

public class RedirectEntry {
    String urlSource;

    String urlDestination;


    public RedirectEntry(String urlSource, String urlDestination) {
        this.urlSource = urlSource;
        this.urlDestination = urlDestination;
    }

    public void setUrlSource(String urlSource) {
        this.urlSource = urlSource;
    }

    public void setUrlDestination(String urlDestination) {
        this.urlDestination = urlDestination;
    }

    public String getUrlSource() {
        return urlSource;
    }

    public String getUrlDestination() {
        return urlDestination;
    }
}
