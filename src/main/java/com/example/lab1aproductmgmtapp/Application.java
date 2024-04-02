package com.example.lab1aproductmgmtapp;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.opencsv.CSVWriter;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import model.Product;
import model.Products;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {


        SpringApplication.run(Application.class, args);

        List<Product> products = List.of(
                new Product(9189927460L, "Carrot", new Date(2023 - 1900, 2, 31), 89, 2.99),
                new Product(2927458265L, "Apple", new Date(2022 - 1900, 11, 9), 18, 1.09),
                new Product(3128874119L, "Banana", new Date(2023 - 1900, 0, 24), 124, 0.55)
        );

        // Call printProducts method
        printProducts(products);

        printProductsXML(products);


        printProductsCSV(products);

    }


    public static void printProducts(List<Product> products) {
        // Convert immutable list to mutable list
        List<Product> mutableProducts = new ArrayList<>(products);

        // Sort products by name
        mutableProducts.sort((p1, p2) -> p1.getName().compareTo(p2.getName()));

        // Print products in JSON format
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String jsonOutput = objectMapper.writeValueAsString(mutableProducts);
            System.out.println("Products in JSON format:");
            System.out.println(jsonOutput);
        } catch (Exception e) {
            System.out.println("Error occurred while converting products to JSON: " + e.getMessage());
        }
    }



    public static void printProductsXML(List<Product> products) {
        // Convert immutable list to mutable list
        List<Product> mutableProducts = new ArrayList<>(products);

        // Sort products by name
        mutableProducts.sort((p1, p2) -> p1.getName().compareTo(p2.getName()));

        // Print products in XML format
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(Product.class);
            Marshaller marshaller = jaxbContext.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            Products wrapper = new Products();
            wrapper.setProductList(mutableProducts);

            StringWriter writer = new StringWriter();
            marshaller.marshal(wrapper, writer);

            System.out.println("Products in XML format:");
            System.out.println(writer.toString());
        } catch (JAXBException e) {
            System.out.println("Error occurred while converting products to XML: " + e.getMessage());
        }
    }



    public static void printProductsCSV(List<Product> products) {
        // Convert immutable list to mutable list
        List<Product> mutableProducts = new ArrayList<>(products);

        // Sort products by name
        mutableProducts.sort((p1, p2) -> p1.getName().compareTo(p2.getName()));

        // Print products in CSV format to console without surrounding quotes
        try (StringWriter stringWriter = new StringWriter();
             CSVWriter writer = new CSVWriter(stringWriter, CSVWriter.DEFAULT_SEPARATOR, CSVWriter.NO_QUOTE_CHARACTER, CSVWriter.DEFAULT_ESCAPE_CHARACTER, CSVWriter.DEFAULT_LINE_END)) {

            String[] header = { "Product ID", "Name", "Date Supplied", "Quantity", "Unit Price" };
            writer.writeNext(header);

            for (Product product : mutableProducts) {
                String[] line = { String.valueOf(product.getProductId()), product.getName(),
                        product.getDateSupplied().toString(), String.valueOf(product.getQuantity()),
                        String.valueOf(product.getUnitPrice()) };
                writer.writeNext(line);
            }

            String csvOutput = stringWriter.toString();
            System.out.println("Products in CSV format:");
            System.out.println(csvOutput);
        } catch (IOException e) {
            System.out.println("Error occurred while writing products to CSV: " + e.getMessage());
        }
    }

}
