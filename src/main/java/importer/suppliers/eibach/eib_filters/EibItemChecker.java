package importer.suppliers.eibach.eib_filters;

import importer.suppliers.eibach.eib_entities.EibItem;
import org.hibernate.Session;

interface EibItemChecker {
   void init(Session eibSession);
   boolean check( EibItem eibItem);
 }
