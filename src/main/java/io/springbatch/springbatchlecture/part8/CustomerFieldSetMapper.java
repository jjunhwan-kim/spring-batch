package io.springbatch.springbatchlecture.part8;

import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;

public class CustomerFieldSetMapper implements FieldSetMapper<Customer> {

    @Override
    public Customer mapFieldSet(FieldSet fieldSet) throws BindException {

        if (fieldSet == null) {
            return null;
        }

        return new Customer(
                fieldSet.readString(0),
                fieldSet.readInt(1),
                fieldSet.readString(2)
        );
    }
}
