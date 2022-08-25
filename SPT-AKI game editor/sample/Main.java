package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
            Scene scene = new Scene(root, 600, 400);
            primaryStage.setTitle("SPT-AKI item editor");
            primaryStage.setScene(scene);
            primaryStage.show();
        }catch (IOException v) {System.out.println("Ошибка в первоначальом запуске базового модуля: " + v.getMessage());
            System.exit(0);}
    }

    public static void main(String[] args) {
        try {
            launch(args);
        }catch (RuntimeException b) {System.out.println("Ошибка при исполнии классов: " + b.getMessage());
        System.exit(0);}
    }
}
