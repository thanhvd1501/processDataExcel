package org.example.data;

public class ColumnTsv {
    private String email;

    private String title;

    private String file;

    public String getEmail() {
        return email;
    }

    public String getTitle() {
        return title;
    }

    public String getFile() {
        return file;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public ColumnTsv(String email, String title, String file) {
        this.email = email;
        this.title = title;
        this.file = file;
    }
}
