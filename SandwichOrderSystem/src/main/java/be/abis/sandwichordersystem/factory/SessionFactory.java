package be.abis.sandwichordersystem.factory;

import be.abis.sandwichordersystem.model.Session;

import java.util.List;

public interface SessionFactory {

    List<Session> createSessions();
}
