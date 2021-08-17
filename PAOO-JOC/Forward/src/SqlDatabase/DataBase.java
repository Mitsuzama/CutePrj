package SqlDatabase;



public class DataBase {

    public static class Rezultat{
        int nrCrt;
        int timp;

        public Rezultat(int nrCrt, int timp){
            this.nrCrt = nrCrt;
            this.timp = timp;
        }
    }

    static {
        try{
            Class.forName("org.sqlite.JDBC");
        }
        catch(ClassNotFoundException e){
            throw new RuntimeException("Managerul nu a reusit sa incarce baza de date!");
        }
    }
}
