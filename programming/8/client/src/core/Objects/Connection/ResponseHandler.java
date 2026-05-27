package core.Objects.Connection;


import Commons.Collection.Product;
import Commons.CustomPackage;
import core.Objects.CommandsControllers.Commands.CollectionUpdated;
import gui.Objects.Frames.MainFrame;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.BlockingQueue;

public class ResponseHandler {
    private MainFrame mainFrame;

    public ResponseHandler(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
    }

    public void setMainFrame(MainFrame mainFrame){
        this.mainFrame=mainFrame;
    }

    public void handleServerPackages(
            CustomPackage[] packages,
            BlockingQueue<CustomPackage[]> serverAnswers) throws InterruptedException {
        List<CustomPackage> ordinaryAnswers = new ArrayList<>();

        for (CustomPackage pack : packages) {
            if (pack == null) {
                continue;
            }

            if (isCollectionUpdatedPackage(pack)) {
                updateTableFromPackage(pack);
            } else {
                ordinaryAnswers.add(pack);
            }
        }

        if (!ordinaryAnswers.isEmpty()) {
            serverAnswers.put(ordinaryAnswers.toArray(new CustomPackage[0]));
        }
    }

    private boolean isCollectionUpdatedPackage(CustomPackage pack) {
        return pack.getCommand().equals(new CollectionUpdated().getName());
    }

    private void updateTableFromPackage(CustomPackage pack) {
        Product[] products = extractProducts(pack.getObject());

        mainFrame.setActualProducts(products);
    }

    private Product[] extractProducts(Object object) {
        if (object == null) {
            return new Product[0];
        }

        if (object instanceof Object[] rawProducts) {
            return Arrays.copyOf(rawProducts, rawProducts.length, Product[].class);
        }

        return new Product[0];
    }
}
