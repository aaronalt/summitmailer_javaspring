package summitmail.daos;

import summitmail.models.*;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@SuppressWarnings("unchecked")
public class CustomerDocumentMapper {

    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static Logger log = LoggerFactory.getLogger(CustomerDocumentMapper.class.getName());

    public static Customer mapToCustomer(Bson bson) {

        Customer customer = new Customer();
        Document document = (Document) bson;
        try {
            customer.setId(document.getObjectId("_id").toHexString());
            customer.setName(MessageFormat.format("{0}", document.get("name")));
            customer.setCountry(MessageFormat.format("{0}", document.get("country")));
            customer.setWebsite(MessageFormat.format("{0}", document.get("website")));
            customer.setEmail(MessageFormat.format("{0}", document.get("email")));
        } catch (Exception e) {
            log.warn("Unable to map document `{}` to `Customer` object: {} ", document, e.getMessage());
            log.warn("Skipping document");
        }
        return customer;
    }

    private static Date parseDate(Object stringDate) {
        if (stringDate == null) {
            return null;
        }
        try {
            if (stringDate instanceof String) {
                return sdf.parse((String) stringDate);
            }
            if (stringDate instanceof Date) {
                return (Date) stringDate;
            }
        } catch (ParseException ex) {
            log.error("Error parsing `{}` string into Date object: {}", stringDate, ex.getMessage());
        }
        return null;
    }

    public static Integer parseInt(Object o) {
        if (o instanceof String) {
            if ("".equals(o)) {
                return 0;
            }
            return Integer.valueOf((String) o);
        }
        return ((Number) o).intValue();
    }

    private static Double parseDouble(Object rating) {
        if (rating instanceof String) {
            if ("".equals(rating)) {
                return (double) 0;
            }
            return Double.parseDouble((String) rating);
        }
        return ((Number) rating).doubleValue();
    }
}
