package summitmail.utils;

import org.bson.BsonReader;
import org.bson.BsonString;
import org.bson.BsonWriter;
import org.bson.Document;
import org.bson.codecs.*;
import org.bson.types.ObjectId;
import summitmail.models.Customer;

public class CustomerCodec implements CollectibleCodec<Customer> {

    private final Codec<Document> documentCodec;

    public CustomerCodec() {
        super();
        this.documentCodec = new DocumentCodec();
    }

    public void encode(BsonWriter bsonWriter, Customer customer, EncoderContext encoderContext) {
        Document customerDoc = new Document();
        String customerId = customer.getId();
        String name = customer.getName();
        String country = customer.getCountry();
        String website = customer.getWebsite();
        String email = customer.getEmail();

        if (null != customerId) {
            customerDoc.put("_id", new ObjectId(customerId));
        }

        if (null != name) {
            customerDoc.put("name", name);
        }

        if (null != country) {
            customerDoc.put("country", country);
        }

        if (null != website) {
            customerDoc.put("website", website);
        }

        if (null != email) {
            customerDoc.put("email", email);
        }

        documentCodec.encode(bsonWriter, customerDoc, encoderContext);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Customer decode(BsonReader bsonReader, DecoderContext decoderContext) {
        Document customerDoc = documentCodec.decode(bsonReader, decoderContext);
        Customer customer = new Customer();
        customer.setId(customerDoc.getObjectId("_id").toHexString());
        customer.setName(customerDoc.getString("name"));
        customer.setCountry(customerDoc.getString("country"));
        customer.setWebsite(customerDoc.getString("website"));
        customer.setEmail(customerDoc.getString("email"));
        return customer;
    }

    @Override
    public Class<Customer> getEncoderClass() {
        return Customer.class;
    }

    @Override
    public Customer generateIdIfAbsentFromDocument(Customer customer) {
        return !documentHasId(customer) ? customer.withNewId() : customer;
    }

    @Override
    public boolean documentHasId(Customer customer) {
        return null != customer.getId();
    }

    @Override
    public BsonString getDocumentId(Customer customer) {
        if (!documentHasId(customer)) {
            throw new IllegalStateException("This document does not have an _id");
        }

        return new BsonString(customer.getId());
    }
}
