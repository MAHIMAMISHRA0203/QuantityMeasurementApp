
class Feet {

    private final double value;

    public Feet(double value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object object) {

        if (this == object)
            return true;

        if (object == null)
            return false;

        if (getClass() != object.getClass())
            return false;

        Feet otherFeet = (Feet) object;

        return Double.compare(this.value, otherFeet.value) == 0;
    }
}

class Inches {

    private final double value;

    public Inches(double value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object object) {

        if (this == object)
            return true;

        if (object == null)
            return false;

        if (getClass() != object.getClass())
            return false;

        Inches otherInches = (Inches) object;

        return Double.compare(this.value, otherInches.value) == 0;
    }
}

public class App {

    static void checkFeetEquality() {

        Feet feet1 = new Feet(1.0);
        Feet feet2 = new Feet(1.0);

        System.out.println("Feet equality: " + feet1.equals(feet2));
    }

    static void checkInchEquality() {

        Inches inch1 = new Inches(1.0);
        Inches inch2 = new Inches(1.0);

        System.out.println("Inch equality: " + inch1.equals(inch2));
    }

    public static void main(String[] args) {

        checkFeetEquality();
        checkInchEquality();
    }
}