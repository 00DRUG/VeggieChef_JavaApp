package org.example;

import com.google.zxing.*;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class QRCodeScanner extends Application {

    public static String decodeQRCode(File qrCodeImage) throws IOException {
        Image fxImage = new Image(qrCodeImage.toURI().toString());
        BufferedImage bufferedImage = SwingFXUtils.fromFXImage(fxImage, null);
        LuminanceSource source = new BufferedImageLuminanceSource(bufferedImage);
        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
        try {
            Result result = new MultiFormatReader().decode(bitmap);
            return result.getText();
        } catch (NotFoundException e) {
            System.out.println("There is no QR code in the image");
            return null;
        }
    }

    @Override
    public void start(Stage primaryStage) {
        File file = new File("path/to/qr-code-image.png");
        String qrCodeText = null;
        try {
            qrCodeText = decodeQRCode(file);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Image fxImage = new Image(file.toURI().toString());
        ImageView imageView = new ImageView(fxImage);
        VBox root = new VBox();
        root.getChildren().add(imageView);

        if (qrCodeText != null) {
            System.out.println("QR Code text: " + qrCodeText);
        } else {
            System.out.println("No QR Code found in the image");
        }

        Scene scene = new Scene(root, 400, 400);
        primaryStage.setScene(scene);
        primaryStage.setTitle("QR Code Scanner");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
