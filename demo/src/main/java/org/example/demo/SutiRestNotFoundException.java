package org.example.demo;

class SutiRestNotFoundException extends RuntimeException {
    SutiRestNotFoundException(Long id) {
        super("A süti nem található: " + id);
    }
}
