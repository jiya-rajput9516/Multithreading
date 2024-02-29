 class Singleton {

    // The single instance of the class
    private static Singleton instance;

    // Private constructor to prevent instantiation from outside the class
    private Singleton() {
        // Initialization code (if needed)
    }

    // Public method to provide the global point of access to the instance
    public static Singleton getInstance() {
        if (instance == null) {
            // If the instance is not yet created, create it
            instance = new Singleton();
        }
        return instance;
    }

    // Other methods and attributes can be added as needed
}
