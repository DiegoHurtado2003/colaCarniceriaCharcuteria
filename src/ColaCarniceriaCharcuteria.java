import java.time.Duration;
import java.util.concurrent.Semaphore;


public class ColaCarniceriaCharcuteria implements Runnable {
    public static Semaphore semaphoreCarniceria = new Semaphore(4);
    public static Semaphore semaphoreCharcuteria = new Semaphore(2);
    private boolean esAtendidoCarniceria = false;
    private boolean esAtendidoCharcuteria = false;


    public void carniceria() {
        try {
            int random = (int) (Math.random() * 10 + 10);
            semaphoreCarniceria.acquire();  // Cerramos el semaforo
            System.out.println("El " + Thread.currentThread().getName() + " está  pidiendo en la carniceria");
            Thread.sleep(Duration.ofSeconds(random)); //Se tardan entre 10 y 20 segundos en ser atendidos
            System.out.println("El " + Thread.currentThread().getName() + " ha terminado en la carniceria");
            semaphoreCarniceria.release(); // Abrimos el semaforo
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void charcuteria() {
        try {
            int random = (int) (Math.random() * 10 + 10);
            semaphoreCharcuteria.acquire();  // Cerramos el semaforo
            System.out.println("El " + Thread.currentThread().getName() + " está  pidiendo en la charcuteria");
            Thread.sleep(Duration.ofSeconds(random)); //Se tardan entre 10 y 20 segundos en ser atendidos
            System.out.println("El " + Thread.currentThread().getName() + " ha terminado en la charcuteria");
            semaphoreCharcuteria.release(); // Abrimos el semaforo
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        // Mientras no haya sido atendido por la charcutería y la carniceria el hilo sigue funcionando
        // hasta que haya sido atendido por ambos y si ha sido atendido por uno le atiende el otro
        while (esAtendidoCarniceria == false || esAtendidoCharcuteria == false) {
            if (semaphoreCarniceria.availablePermits() > 0 && esAtendidoCarniceria == false) {
                carniceria();
                esAtendidoCarniceria = true;
            }
            if (semaphoreCharcuteria.availablePermits() > 0 && esAtendidoCharcuteria == false) {
                charcuteria();
                esAtendidoCharcuteria = true;
            }
        }
    }

    public static void main(String[] args) {
        for (int i = 1; i <= 10; i++) {
            Thread hilo = new Thread(new ColaCarniceriaCharcuteria());
            hilo.setName("Hilo" + i);
            hilo.start();
        }
    }
}
