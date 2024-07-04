
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import service.PhotoService;

import java.util.Scanner;

public class AppLauncher {
    static SessionFactory factory = new Configuration().configure().buildSessionFactory();
    static PhotoService photoService = new PhotoService(factory);
    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        while (true) {
            System.out.println("Меню");
            System.out.println("Обери 0, якщо бажаешь додати фотографію");
            System.out.println("Обери 1, якщо бажаешь знайти фотографію по id");
            System.out.println("Обери 2, якщо бажаешь вивести усі фотографії");
            System.out.println("Обери 3, якщо бажаешь видалити фотграфію");
            System.out.println("Обери 4, якщо бажаешь додати коментар");
            System.out.println("Обери 5, якщо бажаешь видалити коментар");


            int userChoice = scanner.nextInt();
            scanner.nextLine();

            switch (userChoice) {
                case 0 -> photoService.createPhoto();
                case 1 -> photoService.printPhotoById();
                case 2 -> photoService.printAllPhotos();
                case 3 -> photoService.removePhoto();
                case 4 -> photoService.addPhotoComment();
                case 5 -> photoService.removePhotoComment();
            }
        }
    }
}