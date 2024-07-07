package data.dao;

import data.entity.Photo;

import java.util.List;

public interface PhotoDao {
    void save(Photo photo);

    Photo findById(long id);

    List<Photo> findAll();

    void remove(Photo photo);

    void addComment(long photoId, String commentText);

    void removeComment(long photoId, long commentId);
}
