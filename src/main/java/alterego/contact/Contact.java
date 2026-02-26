package alterego.contact;

import alterego.task.TaskList;

import java.util.ArrayList;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Contact {
    private final String personName;
    private String relationship;

    public Contact(String personName, String relationship) {
        this.personName = personName;
        this.relationship = relationship;
    }

    public String getName() {
        return personName;
    }

    public String getRelationship() {
        return relationship;
    }

    public void setRelationship(String relationship) {
        this.relationship = relationship;
    }

    @Override
    public String toString() {
        return personName + " (" + relationship.toString() + ")";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Contact other = (Contact) obj;
        return Objects.equals(personName, other.personName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(personName);
    }
}
