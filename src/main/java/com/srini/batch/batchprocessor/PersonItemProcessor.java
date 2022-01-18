package com.srini.batch.batchprocessor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

public class PersonItemProcessor implements ItemProcessor<LowerPerson, LowerPerson> {

    private final Logger LOGGER = LoggerFactory.getLogger(PersonItemProcessor.class);

    @Override
    public LowerPerson process(final LowerPerson lowerPerson) throws Exception {
        final String upperFName = lowerPerson.getLowerFirstName().toUpperCase();
        final String upperLName = lowerPerson.getLowerLastName().toUpperCase();
        LowerPerson upperPerson = new LowerPerson(upperFName, upperLName);
        LOGGER.info("Converting (" + lowerPerson + ") into (" + upperPerson + ")");
        return upperPerson;
    }
}
