import java.sql.SQLException;

class Parent {
    // Throws SQLException
    void doSomething() throws SQLException {
        // Some code that may throw an SQLException
        System.out.println("Hello");
    }
}

class Child extends Parent {
    // This is not allowed, it would result in a compilation error
    // Changing from SQLException to NumberFormatException
    // void doSomething() throws NumberFormatException {
    // }
    // This is allowed, as it is compatible with the overridden method
    void doSomething() throws SQLException {
        // Overridden implementation
        System.out.println("Hii");
    }
    
}
class Main{
    public static void main(String[] args) {
        try{
        Parent p= new Child();
        p.doSomething();
        }
        catch(Exception e){
System.out.println("Exception");
        }
    }
}