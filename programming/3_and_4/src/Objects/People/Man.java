package Objects.People;

public abstract class Man {
    private String name;

    public Man(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;

        Man r = (Man) obj;
        return name.equals(r.name);
    }

    @Override
    public int hashCode() {
        int result = name.length();
        return result;
    }
}
