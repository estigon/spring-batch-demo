package com.estigon.batchdemo.procesor;

import com.estigon.batchdemo.model.Person;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

public class PersonItemProcesor implements ItemProcessor<Person, Person> { // <tipoObjetoDeEntrada, tipoObjetoDeSalida>

    private static final Logger LOG = LoggerFactory.getLogger(PersonItemProcesor.class);

    @Override
    public Person process(Person item) throws Exception {
        String name = item.getFirstName().toUpperCase();
        String lastName = item.getLastName().toUpperCase();

        Person person = new Person(name,lastName, item.getCellphone());

        LOG.info("transformando {} a {}", item, person);

        return person;
    }
}
