package service;

import data.PhotoDaoImpl;
import data.entity.Photo;
import exception.CustomException;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

public class PhotoService extends PhotoDaoImpl {
    static final Logger LOGGER = LoggerFactory.getLogger(PhotoService.class);
    static SessionFactory factory = new Configuration().configure().buildSessionFactory();
    PhotoDaoImpl photoDaoImpl = new PhotoDaoImpl(factory);
    Scanner scanner = new Scanner(System.in);

    public PhotoService(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public void createPhoto() {
        System.out.println("Створення фотографії");
        System.out.println("Введіть URL фотографії");
        String photoUrl = scanner.nextLine();
        if (!isValidUrl(photoUrl)) {
            System.out.println("Невірний формат URL. Фотографія не була створена");
            return;
        }
        System.out.println("Введіть описання фотографії");
        String photoDescription = scanner.nextLine();
        if (photoDescription.isEmpty()) {
            System.out.println("Описання фотографії не може бути порожнім. Фотографія не була створена");
            return;
        }
        Photo newPhoto = new Photo();
        newPhoto.setUrl(photoUrl);
        newPhoto.setDescription(photoDescription);
        photoDaoImpl.save(newPhoto);
        System.out.println("Фотографія успішно додана");

    }

    public void printPhotoById() {
        try {
            System.out.println("Введіть ідентифікатор фото");
            int photoId = scanner.nextInt();

            if (photoId <= 0) {
                System.out.println("Введіть ідентифікатор більший за 0");
                return;
            }
            Photo photo = photoDaoImpl.findById(photoId);
            if (photo != null) {
                System.out.println("Знайдено фото: " + photoId);
            } else {
                System.out.println("Фото з ідентифікатором: " + photoId + " не знайдено");
            }

        } catch (InputMismatchException e) {
            System.out.println("Некоректний формат ідентифікатора студента!");
            scanner.nextLine();
        } catch (CustomException e) {
            LOGGER.error("Error occurred while printing photo by photo ID", e);
        }
    }

    public void printAllPhotos() {
        try {
            List<Photo> photos = photoDaoImpl.findAll();
            if (photos.isEmpty() || photos == null) {
                System.out.println("Список фотографій порожній");
            } else {
                System.out.println("Список усіх фотографій: ");
                for (Photo photo : photos) {
                    System.out.println(photo.toString());
                }
            }
        } catch (CustomException e) {
            LOGGER.error("Error occurred while printing all photos");
        }
    }

    public void removePhoto() {
        try {
            System.out.println("Введіть ідентифікатор фото для його видалення");
            int photoId = scanner.nextInt();
            if (photoId <= 0) {
                System.out.println("Введіть ідентифікатор більше 0");
            }
            Photo photo = photoDaoImpl.findById(photoId);
            if (photo == null) {
                System.out.println("Фото з ідентифікатором: " + photoId + " не знайдено");
            } else {
                photoDaoImpl.remove(photo);
                System.out.println("Фото з ідентифікатором: " + photoId + " успішно видалено");
            }

        } catch (CustomException e) {
            LOGGER.error("Error occurred while deleting photo by ID");
        }
    }

    public void addPhotoComment() {
        try {
            System.out.println("Введіть ідентифікатор фото якому бажаєте додати коментар");
            int photoId = scanner.nextInt();
            scanner.nextLine();
            System.out.println("Введіть текст коментаря який буде додано до фото");
            String text = scanner.nextLine();
            if (photoId <= 0) {
                System.out.println("Введіть ідентифікатор більше 0");
            }
            Photo photo = photoDaoImpl.findById(photoId);
            if (photo == null) {
                System.out.println("Фото з ідентифікатором: " + photoId + " не знайдено");
            } else {
                photoDaoImpl.addComment(photoId, text);
                System.out.println("Коментар (" + text + ") до фото з ідентифікатором: " + photoId + " успішно доданий");
            }
        } catch (CustomException e) {
            LOGGER.error("Error occurred while adding comment");
        }
    }

    public void removePhotoComment() {
        try {
            System.out.println("Введіть ідентифікатор фото якому бажаєте видаплити коментарі");
            int photoId = scanner.nextInt();
            scanner.nextLine();
            System.out.println("Введіть ідентифікатор коментаря який буде видалено");
            int commentId = scanner.nextInt();
            if (photoId <= 0) {
                System.out.println("Введіть ідентифікатор більше 0");
            }
            Photo photo = photoDaoImpl.findById(photoId);
            if (photo == null) {
                System.out.println("Фото з ідентифікатором: " + photoId + " не знайдено");
            } else {
                photoDaoImpl.removeComment(photoId, commentId);
                System.out.println("Коментар до фото з ідентифікатором: " + photoId + " успішно видалений");
            }
        } catch (CustomException e) {
            LOGGER.error("Error occurred while adding comment");
        }
    }

    private boolean isValidUrl(String url) {
        String regex = "(^http|https)://.*$";
        return Pattern.matches(regex, url);
    }
}
