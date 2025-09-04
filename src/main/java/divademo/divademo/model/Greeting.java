package divademo.divademo.model;

public class Greeting {
       private int id;
       private String message;

        public Greeting(int id, String message) {
            this.id = id;
            this.message = message;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getId() {
            return this.id;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getMessage() {
            return this.message;
        }
}
