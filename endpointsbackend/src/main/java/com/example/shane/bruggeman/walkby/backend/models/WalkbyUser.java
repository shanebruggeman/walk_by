package com.example.shane.bruggeman.walkby.backend.models;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

import java.util.ArrayList;
import java.util.List;

@Entity
public class WalkbyUser {

    @Id Long id;

    private String username;

    private String password;

    private String macAddress;

    private List<String> encounteredMacAddresses = new ArrayList<String>();


    public List<String> getEncounteredMacAddresses() {
        return encounteredMacAddresses;
    }

    public void setEncounteredMacAddresses(List<String> encounteredMacAddresses) {
        this.encounteredMacAddresses = encounteredMacAddresses;
    }

    public boolean addMacAddress(String macAddress) {
        return this.encounteredMacAddresses.add(macAddress);
    }

    public Long getId() {
        return id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String walkbyusername) {
        this.username = walkbyusername;
    }
}
