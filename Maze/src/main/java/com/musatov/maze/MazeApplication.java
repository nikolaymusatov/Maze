package com.musatov.maze;

import com.musatov.maze.model.Model;
import com.musatov.maze.view.MainWindowController;
import com.musatov.maze.presenter.Presenter;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MazeApplication extends Application {
    @Override
    public void start(Stage mainStage) throws Exception {
        FXMLLoader mainLoader =
                new FXMLLoader(MazeApplication.class.getResource("/mainwindow.fxml"));
        Scene mainScene = new Scene(mainLoader.load());
        MainWindowController mainWindowController = mainLoader.getController();
        Presenter presenter = new Presenter(new Model(), mainWindowController);
        mainWindowController.setPresenter(presenter);
        mainWindowController.setStage(mainStage);
        mainStage.setResizable(false);
        mainStage.setTitle("Maze");
        mainStage.setScene(mainScene);
        mainStage.show();
    }
    
    @Override
    public void stop() throws Exception {
        super.stop();
    }
    
    public static void main(String[] args) {
        launch();
    }
}