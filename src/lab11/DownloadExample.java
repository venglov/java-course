package lab11;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Locale;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

public class DownloadExample {

    // lista plików do pobrania
    static String[] toDownload = {
            "http://home.agh.edu.pl/pszwed/wyklad-c/01-jezyk-c-intro.pdf",
            "http://home.agh.edu.pl/~pszwed/wyklad-c/02-jezyk-c-podstawy-skladni.pdf",
            "http://home.agh.edu.pl/~pszwed/wyklad-c/03-jezyk-c-instrukcje.pdf",
            "http://home.agh.edu.pl/~pszwed/wyklad-c/04-jezyk-c-funkcje.pdf",
            "http://home.agh.edu.pl/~pszwed/wyklad-c/05-jezyk-c-deklaracje-typy.pdf",
//            "http://home.agh.edu.pl/~pszwed/wyklad-c/06-jezyk-c-wskazniki.pdf",
//            "http://home.agh.edu.pl/~pszwed/wyklad-c/07-jezyk-c-operatory.pdf",
//            "http://home.agh.edu.pl/~pszwed/wyklad-c/08-jezyk-c-lancuchy-znakow.pdf",
//            "http://home.agh.edu.pl/~pszwed/wyklad-c/09-jezyk-c-struktura-programow.pdf",
//            "http://home.agh.edu.pl/~pszwed/wyklad-c/10-jezyk-c-dynamiczna-alokacja-pamieci.pdf",
//            "http://home.agh.edu.pl/~pszwed/wyklad-c/11-jezyk-c-biblioteka-we-wy.pdf",
//            "http://home.agh.edu.pl/~pszwed/wyklad-c/preprocesor-make-funkcje-biblioteczne.pdf",
    };

    static Semaphore sem = new Semaphore(0);

    static class Downloader implements Runnable {
        private final String url;
        static AtomicInteger count = new AtomicInteger(0);

        Downloader(String url) {
            this.url = url;
        }

        public void run() {

            String fileName = url.substring(url.lastIndexOf('/') + 1, url.length());

            try (InputStream in = new URL(url).openStream(); FileOutputStream out = new FileOutputStream(fileName)) {
                for (int tmp = in.read(); tmp >= 0; tmp = in.read()) {
                    out.write(tmp);
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("Done:" + fileName);
            count.incrementAndGet();
            sem.release();
        }


        static void sequentialDownload() {
            double t1 = System.nanoTime() / 1e6;
            for (String url : toDownload) {
                new Downloader(url).run();
            }
            double t2 = System.nanoTime() / 1e6;
            System.out.printf(Locale.US, "t2-t1=%f\n", t2 - t1);
        }

        static void concurrentDownload() {
            double t1 = System.nanoTime() / 1e6;
            for (String url : toDownload) {
                new Thread(new Downloader(url)).start();
            }
            double t2 = System.nanoTime() / 1e6;
            System.out.printf(Locale.US, "t2-t1=%f\n", t2 - t1);
        }

        static void concurrentDownload2() {
            double t1 = System.nanoTime() / 1e6;
            for (String url : toDownload) {
                new Thread(new Downloader(url)).start();
            }

            while (count.get() != toDownload.length) {
                // wait...
                Thread.yield();
            }
            double t2 = System.nanoTime() / 1e6;
            System.out.printf(Locale.US, "t2-t1=%f\n", t2 - t1);
        }

        static void concurrentDownload3() {
            double t1 = System.nanoTime() / 1e6;
            for (String url : toDownload) {
                new Thread(new Downloader(url)).start();
            }

            try {
                sem.acquire(toDownload.length);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            double t2 = System.nanoTime() / 1e6;
            System.out.printf(Locale.US, "t2-t1=%f\n", t2 - t1);
        }

        public static void main(String[] args) {
            sequentialDownload();
            concurrentDownload();
            concurrentDownload2();
            concurrentDownload3();
        }
    }


}