import java.util.Random;

class AnimalThread extends Thread {
    private String animalName;
    private int distanceCovered;
    private static String winner = null; // Для фиксации победителя

    public AnimalThread(String animalName, int priority) {
        this.animalName = animalName;
        this.distanceCovered = 0;
        this.setPriority(priority);
    }

    @Override
    public void run() {
        Random random = new Random();
        while (distanceCovered < 100) {
            distanceCovered += 1; // Животное преодолевает 1 метр за цикл
            System.out.println(animalName + " пробежал(а) " + distanceCovered + " метров.");

            // Проверка на победу
            if (distanceCovered >= 100 && winner == null) {
                winner = animalName; // Устанавливаем победителя
                System.out.println(animalName + " победил(а)!");
            }

            try {
                Thread.sleep(random.nextInt(200)); // Пауза, чтобы симулировать время на прохождение дистанции
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public int getDistanceCovered() {
        return distanceCovered;
    }
}
 class RabbitAndTurtle {
    public static void main(String[] args) {
        AnimalThread rabbit = new AnimalThread("Кролик", Thread.MIN_PRIORITY); // низкий приоритет
        AnimalThread turtle = new AnimalThread("Черепаха", Thread.MAX_PRIORITY); // высокий приоритет

        // Запуск потоков
        rabbit.start();
        turtle.start();

        // Переменные для отслеживания, кто обгоняет
        boolean rabbitLeading = false;
        boolean turtleLeading = false;

        // В процессе гонки проверяем, кто лидирует
        while (rabbit.isAlive() && turtle.isAlive()) {
            int rabbitDistance = rabbit.getDistanceCovered();
            int turtleDistance = turtle.getDistanceCovered();

            if (rabbitDistance > turtleDistance && !rabbitLeading) {
                // Кролик обогнал черепаху
                System.out.println("Кролик обогнал черепаху! Меняем приоритеты.");
                rabbit.setPriority(Thread.MIN_PRIORITY);  // Снижаем приоритет кролику
                turtle.setPriority(Thread.MAX_PRIORITY);  // Увеличиваем приоритет черепахе
                rabbitLeading = true;  // Обновляем состояние
                turtleLeading = false;
            } else if (turtleDistance > rabbitDistance && !turtleLeading) {
                // Черепаха обогнала кролика
                System.out.println("Черепаха обогнала кролика! Меняем приоритеты.");
                turtle.setPriority(Thread.MIN_PRIORITY);  // Снижаем приоритет черепахе
                rabbit.setPriority(Thread.MAX_PRIORITY);  // Увеличиваем приоритет кролику
                turtleLeading = true;  // Обновляем состояние
                rabbitLeading = false;
            }

            // Пауза перед следующей проверкой
            try {
                Thread.sleep(200); // Пауза между проверками
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // Ждём завершения потоков
        try {
            rabbit.join();
            turtle.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Гонка завершена!");
    }
}
