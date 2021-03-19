package main;

import config.AppConfig;
import controller.exit.ExitController;
import controller.exit.ExitControllerImpl;
import entity.Field;
import entity.FieldElement;
import model.Model;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import view.EndOfGameFrame;
import view.GameFrame;

import javax.swing.*;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Main {

    public static void main(String[] args) throws Throwable {

        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
//        ModelDao modelDao = context.getBean("fileSystemModelDao", FileSystemModelDao.class);
//        Model model = modelDao.getByDimension(FieldDimension.FOUR_AND_FOUR);
//        FieldShiftController controller = new FieldShiftControllerImpl(model);
//        printResult(model);
//        controller.shift(Direction.DOWN);
////        Thread.sleep(2000);
//        printResult(model);
//        model.replaceState(new Model(FieldDimension.FIVE_AND_FIVE));
//        printResult(model);
//        controller.cancelShifts();
//
//        controller.shift(Direction.DOWN);
////        Thread.sleep(2000);
//        printResult(model);
//        controller.shift(Direction.DOWN);
////        Thread.sleep(2000);
//        printResult(model);
//
//        System.exit(0);
        GameFrame gameFrame = context.getBean("gameFrame", GameFrame.class);
        EndOfGameFrame endOfGameFrame = context.getBean("endOfGameFrame", EndOfGameFrame.class);
        Model model = context.getBean("model", Model.class);
        model.subscribe(gameFrame);
        model.subscribe(endOfGameFrame);

        SwingUtilities.invokeLater(() -> gameFrame.setVisible(true));

    }

    private static void printResult(Model model) {
        StringBuilder sb = new StringBuilder(100);
        sb.append(model.getScores()).append('\n');
        Field f = model.getField();
        for(int i = 0; i < f.getFieldDimension().getHeight(); i++){
            List<FieldElement> row = f.getRow(i);
            for (FieldElement fieldElement : row) {
                sb.append(fieldElement.getValue()).append('\t');
            }
            sb.append('\n');
        }
        System.out.println(sb);
    }

}
