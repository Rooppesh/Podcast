package com.android.podcast;

public class GenreEntity {
    String name;
    String id;
    String parent_id;

    public GenreEntity(String name, String id, String parent_id) {
        this.name = name;
        this.id = id;
        this.parent_id = parent_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getParent_id() {
        return parent_id;
    }

    public void setParent_id(String parent_id) {
        this.parent_id = parent_id;
    }
}
