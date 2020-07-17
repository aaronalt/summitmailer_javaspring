package summitmail.services;

import summitmail.daos.CustomerDao;
import summitmail.daos.CustomerDocumentMapper;
import summitmail.daos.IncorrectDaoOperation;
import summitmail.daos.UserDao;
import summitmail.models.Customer;
import summitmail.models.CustomerRegistry;
import summitmail.models.User;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Configuration
public class CustomerService {

    @Autowired
    private CustomerDao customerDao;

    @Autowired
    private UserDao userDao;

    public CustomerService() {
        super();
    }

    /**
     * Finds the Customer object that matches the `id` value.
     *
     * @return Customer object or null if no match applies.
     */
    public Customer createCustomer(CustomerRegistry register, Map<String, String> errors) {
        Customer customer = getCustomerFromRegistry(register);
        return createCustomer(customer, errors);
    }

    private Customer getCustomerFromRegistry(CustomerRegistry register) {
        Customer customer = new Customer();
        customer.setName(register.getName());
        customer.setCountry(register.getCountry());
        customer.setWebsite(register.getWebsite());
        customer.setEmail(register.getEmail());
        return customer;
    }

    private Customer createCustomer(Customer customer, Map<String, String> errors) {
        try {
            return customerDao.addCustomer(customer) ? customer : null;
        } catch (IncorrectDaoOperation ex) {
            errors.put("msg", ex.getMessage());
        }
        return null;
    }

    // public Customer loadCustomer(String email) { return customerDao.getCustomer(email); }

    /**
     * Lists all customers per page.
     *
     * @param resultsPerPage - number of results per page
     * @param page           - result set page
     * @return Map with list of results under `customers_list` key and total count under `customers_count`
     * key.
     */
    public Map<String, ?> getCustomers(int resultsPerPage, int page) {
        int skip = resultsPerPage * page;

        List<Customer> customers =
                new ArrayList<>(customerDao
                        .getCustomers(resultsPerPage, skip));
        Map<String, Object> result = new HashMap<>();
        result.put("customer_list", customers);
        if (page == 0) {
            result.put("customer_count", customerDao.getCustomersCount());
        }
        return result;
    }

    /**
     * Finds all countries that have been recorded
     *
     * @param countries - array of countries required to match
     * @return Map containing messages and customers objects that match the countries array
     */
    public Map<String, ?> getCustomersByCountry(String... countries) {

        Map<String, Object> results = new HashMap<>();
        results.put(
                "customer_list",
                new ArrayList<>(customerDao
                        .getCustomersByCountry(countries)));

        return results;
    }

    /**
     * Collects the list of customers that match the Text search for the provided filter.
     *
     * @param resultsPerPage - max number of results per page
     * @param page           - wanted page number
     * @param filter         - List of keywords to be matched
     * @return Map containing the customers array and total results matching filter criteria.
     *
    public Map<String, ?> getCustomersByText(int resultsPerPage, int page, ArrayList<String> filter) {
        int skip = resultsPerPage * page;
        String keywords = String.join(" ", filter);

        List<Customer> movieList =
                customerDao
                        .getCustomersByText(resultsPerPage, skip, keywords)
                        .stream()
                        .map(CustomerDocumentMapper::mapToCustomer)
                        .collect(Collectors.toList());
        Map<String, Object> result = new HashMap<>();
        result.put("customers_list", movieList);

        if (page == 0) {
            result.put("customers_count", customerDao.getTextSearchCount(keywords));
        }
        return result;
    }
    */

    /**
     * Finds all customers that match the expected cast members.
     *
     * @param resultsPerPage - max number of customers per page
     * @param page           - wanted page number
     * @param castFilter     - cast to be matched
     * @return Map containing the customers array and total results matching filter criteria.
     *
    public Map<String, ?> getCustomersByCast(
            int resultsPerPage, int page, ArrayList<String> castFilter) {
        int skip = page * resultsPerPage;

        String[] cast = castFilter.toArray(new String[0]);
        String sortKey = "tomatoes.viewer.numReviews";
        List<Customer> movieList =
                customerDao
                        .getCustomersByCast(sortKey, resultsPerPage, skip, cast)
                        .stream()
                        .map(CustomerDocumentMapper::mapToCustomer)
                        .collect(Collectors.toList());

        Map<String, Object> result = new HashMap<>();
        result.put("customers_list", movieList);

        if (page == 0) {
            result.put("customers_count", customerDao.getCastSearchCount(cast));
        }
        return result;
    }
    */

    /**
     * Counts all customers in the database.
     *
     * @return number of customers
     */
    public long getCustomersCount() {
        return customerDao.getCustomersCount();
    }

    /**
     * Collects the configured pool size and user connection status.
     *
     * @return Map of key value pairs reflecting the configured userInfo, pool_size and wtimeout
     * settings.
     */
    public Map<String, ?> getConfiguration() {

        return this.customerDao.getConfiguration();
    }
}
