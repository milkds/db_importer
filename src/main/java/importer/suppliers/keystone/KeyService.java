package importer.suppliers.keystone;

import importer.suppliers.keystone.entities.KeyItem;
import org.hibernate.Session;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class KeyService {
    public static Set<KeyItem> getAllItems(Session keySession) {
        List<KeyItem> allItemsList = KeyDAO.getAllItems(keySession);
        return new HashSet<>(allItemsList);
    }
}
