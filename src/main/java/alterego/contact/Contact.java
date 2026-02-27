package alterego.contact;

import java.util.Objects;

/**
 * Represents a contact with a name and relationship type.
 */
public class Contact {
    private final String personName;
    private String relationship;

    /**
     * Constructs a new contact with the specified name and relationship.
     * @param personName Name of the contact
     * @param relationship Relationship to the contact (e.g., "friend", "family")
     */
    public Contact(String personName, String relationship) {
        this.personName = personName;
        this.relationship = relationship.toLowerCase();
    }

    /**
     * Returns the name of this contact.
     * @return Contact name
     */
    public String getName() {
        return personName;
    }

    /**
     * Returns the relationship type of this contact.
     * @return Relationship string
     */
    public String getRelationship() {
        return relationship;
    }

    /**
     * Returns a string representation of this contact.
     * Format: name (relationship)
     * @return Formatted contact string
     */
    @Override
    public String toString() {
        return personName + " (" + relationship.toString() + ")";
    }

    /**
     * Compares the equality of this contact with other object.
     * @param obj Object to compare with
     * @return true if contacts have the same name, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Contact other = (Contact) obj;
        return Objects.equals(personName.toLowerCase(), other.personName.toLowerCase());
    }

    /**
     * Returns a hash code based on the contact name.
     * @return Hash code value
     */
    @Override
    public int hashCode() {
        return Objects.hash(personName.toLowerCase());
    }
}
