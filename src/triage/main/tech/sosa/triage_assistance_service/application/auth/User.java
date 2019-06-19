package tech.sosa.triage_assistance_service.application.auth;

public class User {

    public final String id;
    public final Role role;

    public User(String id, Role role) {
        this.id = id;
        this.role = role;
    }
}
