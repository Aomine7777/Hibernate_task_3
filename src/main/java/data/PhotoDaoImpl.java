package data;

import data.dao.PhotoDao;
import data.entity.Photo;
import data.entity.PhotoComment;
import exception.CustomException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.List;


public class PhotoDaoImpl implements PhotoDao {
    private EntityManagerFactory entityManagerFactory;

    public PhotoDaoImpl(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    static final Logger LOGGER = LoggerFactory.getLogger(PhotoDaoImpl.class);

    @Override
    public void save(Photo photo) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = null;
        try {
            transaction = entityManager.getTransaction();
            transaction.begin();
            entityManager.persist(photo);
            transaction.commit();
            LOGGER.info("Saved photo with id: {}", photo.getId());
        } catch (CustomException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            LOGGER.error("Error saving photo: {}", e.getMessage());
            throw new CustomException("Error saving photo", e);
        } finally {
            entityManager.close();
        }
    }

    @Override
    public Photo findById(long id) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            return entityManager.find(Photo.class, id);
        } catch (CustomException e) {
            LOGGER.error("Error finding photo by id: {}", id, e);
            throw new CustomException("Error finding by id: " + id, e);
        } finally {
            entityManager.close();
        }
    }

    @Override
    public List<Photo> findAll() {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            String jpql = "SELECT p FROM Photo p";
            TypedQuery<Photo> query = entityManager.createQuery(jpql, Photo.class);
            return query.getResultList();
        } catch (CustomException e) {
            LOGGER.error("Error finding all photo by id: {}", e);
            throw new CustomException("Error finding by all photo: ", e);
        } finally {
            entityManager.close();
        }
    }

    @Override
    public void remove(Photo photo) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            entityManager.remove(entityManager.contains(photo) ? photo : entityManager.merge(photo));
            transaction.commit();
        } catch (CustomException e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            entityManager.close();
        }
    }

    @Override
    public void addComment(long photoId, String commentText) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            Photo photo = entityManager.find(Photo.class, photoId);
            if (photo != null) {
                PhotoComment comment = new PhotoComment();
                comment.setText(commentText);
                comment.setCreated(LocalDateTime.now().toString());
                photo.addComment(comment);
                entityManager.persist(comment);
            }
            transaction.commit();
        } catch (CustomException e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            entityManager.close();
        }
    }

    @Override
    public void removeComment(long photoId, long commentId) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            Photo photo = entityManager.find(Photo.class, photoId);
            if (photo != null) {
                PhotoComment comment = entityManager.find(PhotoComment.class, commentId);
                if (comment != null) {
                    photo.removeComment(comment);
                    entityManager.remove(comment);
                }
            }
            transaction.commit();
        } catch (CustomException e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            entityManager.close();
        }
    }

}
