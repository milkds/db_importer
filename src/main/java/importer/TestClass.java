package importer;

import importer.dao.ItemDAO;
import importer.entities.*;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class TestClass {

    public static void TestItemSave(){
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        try {
            transaction = session.getTransaction();
            transaction.begin();
            ProductionItem item = getNewItem();
            ItemDAO.saveItem(item, session);
            transaction.commit();
            session.close();
        } catch (Exception e) {
            e.printStackTrace();
            if (transaction != null) {
                transaction.rollback();
            }
        }
        HibernateUtil.shutdown();
    }

    private static ProductionItem getNewItem() {
        ProductionItem item  = new ProductionItem();
        item.setItemType("Shock");
        item.setItemManufacturer("Bilstein");
        item.setItemPartNo("24-777777");

        ItemAttribute itemAtt = new ItemAttribute();
        itemAtt.setItemAttName("Color");
        itemAtt.setItemAttValue("Yellow");
        item.getItemAttributes().add(itemAtt);

        ProductionFitment fitment = new ProductionFitment();
        FitmentAttribute fitAtt = new FitmentAttribute();
        fitAtt.setFitmentAttName("Position");
        fitAtt.setFitmentAttValue("Forward");
        fitment.getFitmentAttributes().add(fitAtt);

        ProductionCar car = new ProductionCar();
        car.setYearStart(2015);
        car.setYearFinish(2019);
        car.setMake("Ford");
        car.setModel("F-150");
        car.setSubModel("Laramie");
        car.setDrive("4WD");

        CarAttribute carAttribute = new CarAttribute();
        carAttribute.setCarAttName("Engine");
        carAttribute.setCarAttValue("4.0L");
        car.getAttributes().add(carAttribute);

        fitment.setCar(car);
        item.getProductionFitments().add(fitment);

        return item;
    }

    public static void testSplit(){
        String spl = "https://productdesk.cart.bilsteinus.com//media/products/bilstein/F4-SE5-C765-H0_1.jpg\n" +
                " https://productdesk.cart.bilsteinus.com//media/products/bilstein/F4-SE5-C765-H0_2.jpg\n" +
                " https://productdesk.cart.bilsteinus.com//media/products/bilstein/F4-SE5-C765-H0_3.jpg\n" +
                " https://productdesk.cart.bilsteinus.com//media/products/bilstein/F4-SE5-C765-H0_4.jpg";
        String split[] = spl.split("\n");
        System.out.println(split.length);
        System.out.println(split[0].length());
    }
}
